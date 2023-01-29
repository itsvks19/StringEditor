package com.raredev.stringeditor;

import android.os.Parcel;
import android.os.Parcelable;

public class StringModel {
  private String string_name;
  private String string_value;
  
  public StringModel() {}
  public StringModel(String name, String value) {
    setStringName(name);
    setStringValue(value);
  }

  public void setStringName(String string_name) {
    this.string_name = string_name;
  }

  public String getStringName() {
    return this.string_name;
  }

  public void setStringValue(String string_value) {
    this.string_value = string_value;
  }

  public String getStringValue() {
    return this.string_value;
  }
}