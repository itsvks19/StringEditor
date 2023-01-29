package com.raredev.stringeditor.utils;
import com.google.android.material.textfield.TextInputEditText;
import com.raredev.stringeditor.StringModel;
import java.util.List;

public class Validator {

  public static boolean isValidStringName(
      String nameInput, List<StringModel> listString, TextInputEditText name) {
    String pattern = "^[a-zA-Z]+[a-zA-Z0-9_]*$";

    if (nameInput.matches(pattern) && !nameInput.isEmpty()) {
      name.setError("Invalid name!");
      return true;
    }

    for (StringModel model : listString) {
      if (model.getStringName().equals(nameInput)) {
        name.setError("String exists!");
        return false;
      }
    }
    return false;
  }

  public static boolean isValidStringValue(String value) {
    if (!value.isEmpty()) {
      return true;
    }
    return false;
  }
}
