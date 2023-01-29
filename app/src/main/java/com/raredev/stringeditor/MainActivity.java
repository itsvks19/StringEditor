package com.raredev.stringeditor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.itsaky.androidide.logsender.LogSender;
import com.raredev.stringeditor.adapter.StringsAdapter;
import com.raredev.stringeditor.callback.ItemMoveCallBack;
import com.raredev.stringeditor.databinding.ActivityMainBinding;
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
  
  public void dialogEditString(int pos) {
    AlertDialog dialog =
        new MaterialAlertDialogBuilder(this)
            .setView(R.layout.dialog_string)
            .setTitle("Edit String")
            .setPositiveButton("Create", null)
            .setNegativeButton("Cancel", null)
            .create();

    dialog.setOnShowListener(
        (d) -> {
          TextInputEditText name = dialog.findViewById(R.id.textinput_name);
          TextInputEditText value = dialog.findViewById(R.id.textinput_value);
          Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

          StringModel stringModel = listString.get(pos);
          name.setText(stringModel.getStringName());
          value.setText(stringModel.getStringValue());

          positive.setOnClickListener(
              (v) -> {
                String nameInput = name.getText().toString();
                String valueInput = value.getText().toString();

                if (!Validator.isValidStringName(nameInput, listString, name)) {
                  return;
                }
                if (!Validator.isValidStringValue(valueInput)) {
                  value.setError("Invalid value!");
                  return;
                }

                listString.set(pos, new StringModel(nameInput, valueInput));
                adapter.notifyDataSetChanged();
                dialog.cancel();
              });
        });
    dialog.show();
  }

  private void dialogNewString() {
    AlertDialog dialog =
        new MaterialAlertDialogBuilder(this)
            .setView(R.layout.dialog_string)
            .setTitle("Create String")
            .setPositiveButton("Create", null)
            .setNegativeButton("Cancel", null)
            .create();

    dialog.setOnShowListener(
        (d) -> {
          TextInputEditText name = dialog.findViewById(R.id.textinput_name);
          TextInputEditText value = dialog.findViewById(R.id.textinput_value);
          Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

          positive.setOnClickListener(
              (v) -> {
                String nameInput = name.getText().toString();
                String valueInput = value.getText().toString();

                if (!Validator.isValidStringName(nameInput, listString, name)) {
                  return;
                }
                if (!Validator.isValidStringValue(valueInput)) {
                  value.setError("Invalid value!");
                  return;
                }

                listString.add(new StringModel(nameInput, valueInput));
                adapter.notifyDataSetChanged();
                dialog.cancel();
              });
        });
    dialog.show();
  }
  
  private void dialogConfirmRemove(int pos) {
    new MaterialAlertDialogBuilder(this)
            .setTitle("Remove String")
            .setMessage("Are you sure you want to remove this String?")
            .setPositiveButton("Remove", (dlg, v) -> {
              listString.remove(pos);
              adapter.notifyDataSetChanged();
              ToastUtils.showShort("Removed");
            })
            .setNegativeButton("Cancel", null)
            .show();
  }
}
