package com.raredev.stringeditor;

import android.content.Intent;
import android.os.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.logsender.LogSender;
import com.raredev.stringeditor.adapter.StringsAdapter;
import com.raredev.stringeditor.callback.DialogCallback;
import com.raredev.stringeditor.callback.ItemMoveCallBack;
import com.raredev.stringeditor.databinding.ActivityMainBinding;
import com.raredev.stringeditor.dialog.BaseStringDialog;
import com.raredev.stringeditor.utils.SourceUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity implements ItemMoveCallBack.ItemMoveListener {
  private List<StringModel> listString;
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

    listString = new ArrayList<>();
    adapter = new StringsAdapter(listString);

    ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemMoveCallBack(this));
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

  @Override
  public boolean onItemMove(RecyclerView.ViewHolder holder, int fromPosition, int toPosition) {
    Collections.swap(listString, fromPosition, toPosition);
    adapter.notifyItemMoved(fromPosition, toPosition);
    return true;
  }

  @Override
  public void onDragFinish() {
    adapter.notifyDataSetChanged();
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
    BaseStringDialog dialog = new BaseStringDialog(this, listString, pos,
      new DialogCallback() {
        @Override
        public void onPositiveButtonClicked(String name, String value) {
          listString.set(pos, new StringModel(name, value));
          adapter.notifyDataSetChanged();
        }
    });
    dialog.setTitle("Edit String");
    dialog.show();
  }

  private void dialogNewString() {
    BaseStringDialog dialog = new BaseStringDialog(this, listString, -1,
      new DialogCallback() {
        @Override
        public void onPositiveButtonClicked(String name, String value) {
          listString.add(new StringModel(name, value));
          adapter.notifyDataSetChanged();
        }
    });
    dialog.setTitle("Create String");
    dialog.show();
  }

  private void dialogConfirmRemove(int pos) {
    new MaterialAlertDialogBuilder(this)
        .setTitle("Remove String")
        .setMessage("Are you sure you want to remove this String?")
        .setPositiveButton(
            "Remove",
            (dlg, v) -> {
              listString.remove(pos);
              ToastUtils.showShort("Removed");
              adapter.notifyDataSetChanged();
            })
        .setNegativeButton("Cancel", null)
        .show();
  }
}