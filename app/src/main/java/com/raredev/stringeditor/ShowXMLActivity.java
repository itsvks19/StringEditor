package com.raredev.stringeditor;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.raredev.stringeditor.databinding.ActivityShowxmlBinding;
import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme;
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage;
import io.github.rosemoe.sora.langs.textmate.registry.ThemeRegistry;

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
    getSupportActionBar().setTitle("Show XML");
    setupEditor();
    
    binding.fab.setOnClickListener((v) -> {
      ClipboardUtils.copyText(binding.editor.getText().toString());
      ToastUtils.showShort("Copied");
    });
  }

  private void setupEditor() {
    binding.editor.setEditable(false);
    updateEditorScheme();
    
    binding.editor.setText(getIntent().getStringExtra("code"));
    updateEditorLanguage();
  }

  private void updateEditorScheme() {
    try {
      var registry = ThemeRegistry.getInstance();
      if (isDarkMode()) {
        registry.setTheme("darcula");
      } else {
        registry.setTheme("quietlight");
      }

      binding.editor.setColorScheme(TextMateColorScheme.create(registry));
    } catch (Exception e) {
      e.printStackTrace();
    }
    binding.editor.invalidate();
  }

  private void updateEditorLanguage() {
    binding.editor.setEditorLanguage(TextMateLanguage.create("text.xml", false));
    binding.editor.invalidate();
  }

  private boolean isDarkMode() {
    int uiMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
    return uiMode == Configuration.UI_MODE_NIGHT_YES;
  }
}