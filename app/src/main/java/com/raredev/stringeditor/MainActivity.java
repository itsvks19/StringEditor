package com.raredev.stringeditor;

import android.os.*;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import com.raredev.stringeditor.databinding.ActivityMainBinding;
import com.raredev.stringeditor.fragment.*;

public class MainActivity extends AppCompatActivity {
  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    init();
  }

  private void init() {
    setSupportActionBar(binding.toolbar);

    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.container, EditorFragment.newInstance(), "editorFragment")
        .commit();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, 0, 0, "Visual Editor");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == 0) {
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.container, AttributesListFragment.newInstance(), "attributesListFragment")
        .addToBackStack(null)
        .commit();
    }
    return super.onOptionsItemSelected(item);
  }
}