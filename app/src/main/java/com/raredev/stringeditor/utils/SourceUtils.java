package com.raredev.stringeditor.utils;

import com.raredev.stringeditor.StringModel;
import java.util.List;

public class SourceUtils {
  public static String generateXml(List<StringModel> list) {
    StringBuilder sb = new StringBuilder();
    sb.append("<resources>\n");
    sb.append("\t<!-- this app is a test, arrays will be added soon -->\n");

    for (StringModel string : list) {
      sb.append("\t<string name=\"" + string.getStringName() + "\">" + string.getStringValue() + "</string>\n");
    }

    sb.append("</resources>");
    return sb.toString();
  }
}