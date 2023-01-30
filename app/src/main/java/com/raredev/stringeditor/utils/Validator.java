package com.raredev.stringeditor.utils;

import com.google.android.material.textfield.TextInputEditText;
import com.raredev.stringeditor.StringModel;
import java.util.List;

public class Validator {

  public static boolean isValidStringName(
      String nameInput, String currentName, List<StringModel> listString) {
    String pattern = "^[a-zA-Z]+[a-zA-Z0-9_]*$";

    if (!nameInput.matches(pattern) || nameInput.isEmpty()) {
      return false;
    }

    for (StringModel model : listString) {
      if (nameInput.equals(model.getStringName()) && !currentName.equals(model.getStringName())) {
        return false;
      }
    }
    return true;
  }

  public static boolean isValidStringValue(String value) {
    if (value.isEmpty()) {
      return false;
    }
    return true;
  }
}
