package com.raredev.stringeditor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.itsaky.androidide.logsender.LogSender;
import com.raredev.stringeditor.adapter.StringsAdapter;
import com.raredev.stringeditor.callback.ItemMoveCallBack;
import com.raredev.stringeditor.databinding.ActivityMainBinding;
import com.raredev.stringeditor.databinding.DialogStringBinding;
import com.raredev.stringeditor.utils.SourceUtils;
import com.raredev.stringeditor.utils.Validator;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity {
  private List<StringModel> listString = new ArrayList<>();
  private StringsAdapter adapter;

  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    init();
  }

  private void init() {
    LogSender.startLogging(this);
    setSupportActionBar(binding.toolbar);

    adapter = new StringsAdapter(listString);

    ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemMoveCallBack(adapter));
    touchHelper.attachToRecyclerView(binding.listString);
    adapter.setTouchHelper(touchHelper);

    binding.listString.setLayoutManager(new LinearLayoutManager(this));
    binding.listString.setAdapter(adapter);

    binding.fab.setOnClickListener(
        (v) -> {
          dialogNewString();
        });

    adapter.setStringListener(
        new StringsAdapter.StringListener() {
          @Override
          public void onStringClick(View v, int pos) {}

          @Override
          public void onStringLongClick(View v, int pos) {
            showPopupMenu(v, pos);
          }
        });

    listString.add(new StringModel("app_name", "String Editor"));
  }

  private void showPopupMenu(View v, int pos) {
    PopupMenu menu = new PopupMenu(this, v);
    menu.getMenu().add(0, 0, 0, "Edit");
    menu.getMenu().add(0, 1, 1, "Remove");
    menu.setOnMenuItemClickListener(
        (item) -> {
          final var title = item.getTitle();
          if (title == "Edit") {
            dialogEditString(pos);
          } else if (title == "Remove") {
            dialogConfirmRemove(pos);
          }
          return true;
        });
    menu.show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, 0, 0, "View source");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    // Start activity Show XML
    Intent it = new Intent();
    it.setClass(this, ShowXMLActivity.class);
    it.putExtra("code", SourceUtils.generateXml(listString));
    startActivity(it);

    return super.onOptionsItemSelected(item);
  }
  
  private void dialogEditString(int pos) {
    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

    DialogStringBinding dialogBinding = DialogStringBinding.inflate(getLayoutInflater());

    builder.setView(dialogBinding.getRoot());
    builder.setTitle("Create String");

    builder.setPositiveButton("Create", (dlg, i) -> {
      listString.set(pos, new StringModel(dialogBinding.textinputName.getText().toString(), dialogBinding.textinputValue.getText().toString()));
      adapter.notifyDataSetChanged();
    });
    builder.setNegativeButton("Cancel", null);
    
    final AlertDialog alertDialog = builder.create();
    alertDialog.show();
    TextWatcher textWatcher =
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}

          @Override
          public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
            checkErrors(
                dialogBinding.textInputLayoutName,
                dialogBinding.textInputLayoutValue,
                dialogBinding.textinputName.getText().toString(),
                dialogBinding.textinputValue.getText().toString(),
                alertDialog,
                pos);
          }

          @Override
          public void afterTextChanged(Editable p1) {
            
          }
        };
    dialogBinding.textinputName.setText(listString.get(pos).getStringName());
    dialogBinding.textinputValue.setText(listString.get(pos).getStringValue());
    dialogBinding.textinputName.addTextChangedListener(textWatcher);
    dialogBinding.textinputValue.addTextChangedListener(textWatcher);
    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
  }

  private void dialogNewString() {
    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

    DialogStringBinding dialogBinding = DialogStringBinding.inflate(getLayoutInflater());

    builder.setView(dialogBinding.getRoot());
    builder.setTitle("Create String");

    builder.setPositiveButton("Create", (dlg, i) -> {
      listString.add(new StringModel(dialogBinding.textinputName.getText().toString(), dialogBinding.textinputValue.getText().toString()));
      adapter.notifyDataSetChanged();
    });
    builder.setNegativeButton("Cancel", null);
    
    final AlertDialog alertDialog = builder.create();
    alertDialog.show();
    TextWatcher textWatcher =
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}

          @Override
          public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
            checkErrors(
                dialogBinding.textInputLayoutName,
                dialogBinding.textInputLayoutValue,
                dialogBinding.textinputName.getText().toString(),
                dialogBinding.textinputValue.getText().toString(),
                alertDialog,
                -1);
          }

          @Override
          public void afterTextChanged(Editable p1) {
            
          }
        };

    dialogBinding.textinputName.addTextChangedListener(textWatcher);
    dialogBinding.textinputValue.addTextChangedListener(textWatcher);
    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
  }

  private void dialogConfirmRemove(int pos) {
    new MaterialAlertDialogBuilder(this)
        .setTitle("Remove String")
        .setMessage("Are you sure you want to remove this String?")
        .setPositiveButton(
            "Remove",
            (dlg, v) -> {
              listString.remove(pos);
              adapter.notifyDataSetChanged();
              ToastUtils.showShort("Removed");
            })
        .setNegativeButton("Cancel", null)
        .show();
  }

  private void checkErrors(TextInputLayout nameLayout, TextInputLayout valueLayout, String name, String value, AlertDialog dialog, int pos) {
    if (!Validator.isValidStringName(name, listString, pos) || !Validator.isValidStringValue(value)) {
      if (!Validator.isValidStringName(name, listString, pos)) {
        nameLayout.setErrorEnabled(true);
        nameLayout.setError("Invalid name!");
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        return;
      }
      if (!Validator.isValidStringValue(value)) {
        valueLayout.setErrorEnabled(true);
        valueLayout.setError("Invalid value!");
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        return;
      }
      return;
    }
    nameLayout.setErrorEnabled(false);
    nameLayout.setError("");
    valueLayout.setErrorEnabled(false);
    valueLayout.setError("");
    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
  }
}
