package com.raredev.stringeditor.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.google.android.material.elevation.SurfaceColors;
import com.raredev.stringeditor.StringModel;
import java.util.List;

public class SourceUtils {
  public static String generateXml(List<StringModel> list) {
    StringBuilder sb = new StringBuilder();
    sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    sb.append("<resources>\n");

    for (StringModel string : list) {
      sb.append("\t<string name=\"" + string.getStringName() + "\">" + string.getStringValue() + "</string>\n");
    }

    sb.append("</resources>");
    return sb.toString();
  }
}
