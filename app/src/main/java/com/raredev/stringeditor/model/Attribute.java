package com.raredev.stringeditor.model;

public class Attribute {
  private String tag;
  private String name;
  private String value;

  public Attribute(String tag, String name, String value) {
    this.tag = tag;
    this.name = name;
    this.value = value;
  }
  
  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}