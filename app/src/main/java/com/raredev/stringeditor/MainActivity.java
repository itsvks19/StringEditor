package com.raredev.stringeditor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.*;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.itsaky.androidide.logsender.LogSender;
import com.raredev.stringeditor.adapter.StringsAdapter;
import com.raredev.stringeditor.utils.SourceUtils;
import com.raredev.stringeditor.databinding.ActivityMainBinding;
import java.util.ArrayList;
import java.util.List;

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
    
    binding.listString.setLayoutManager(new LinearLayoutManager(this));
    binding.listString.setAdapter(adapter);
    
    binding.fab.setOnClickListener((v) -> {
      dialogNewString();
    });

    StringModel defualtString = new StringModel();
    defualtString.setStringName("app_name");
    defualtString.setStringValue("String Editor");
    listString.add(defualtString);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
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

  private void dialogNewString() {
    AlertDialog dialog = new MaterialAlertDialogBuilder(this)
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

          positive.setOnClickListener((v) -> {
            StringModel string = new StringModel();
            string.setStringName(name.getText().toString());
            string.setStringValue(value.getText().toString());
            listString.add(string);
            dialog.cancel();
          });
        });
    dialog.show();
  }
}