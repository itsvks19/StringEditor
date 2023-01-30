package com.raredev.stringeditor.utils;

import com.raredev.stringeditor.model.Attribute;
import java.util.List;

public class Validator {

  public static boolean isValidStringName(
      String nameInput, String currentName, List<Attribute> listString) {
    String pattern = "^[a-zA-Z]+[a-zA-Z0-9_]*$";

    if (!nameInput.matches(pattern) || nameInput.isEmpty()) {
      return false;
    }

    for (Attribute attr : listString) {
      if (nameInput.equals(attr.getName()) && !currentName.equals(attr.getName())) {
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
