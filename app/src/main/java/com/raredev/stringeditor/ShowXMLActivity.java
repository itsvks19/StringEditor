package com.raredev.stringeditor;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.raredev.stringeditor.databinding.ActivityShowxmlBinding;

public class ShowXMLActivity extends AppCompatActivity {
  private ActivityShowxmlBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityShowxmlBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    init();
  }
  
  private void init() {
    setSupportActionBar(binding.toolbar);
    
    binding.editor.setText(getIntent().getStringExtra("code"));
  }
}
