package com.raredev.stringeditor.utils;

import android.util.Xml;
import com.raredev.stringeditor.model.Attribute;
import java.io.StringReader;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class XmlGeneratorUtils {
  private static XmlGeneratorUtils instance;
  private String code;
  
  public static XmlGeneratorUtils getInstance() {
    if (instance == null) {
      instance = new XmlGeneratorUtils();
    }
    return instance;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  public String getGeneratedCode() {
    return code;
  }

  public void generateXml(List<Attribute> list) {
    StringBuilder sb = new StringBuilder();
    sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    sb.append("<resources>\n");

    for (Attribute attr : list) {
      sb.append("\t<" + attr.getTag() + " name=\"" + attr.getName() + "\">" + attr.getValue() + "</" + attr.getTag() + ">\n");
    }
    sb.append("</resources>");
    
    code = sb.toString();
  }

  public void xmlToList(String xmlText, List<Attribute> list) {
    if (xmlText != null && !xmlText.isEmpty()) {
      try {
        list.clear();
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(new StringReader(xmlText));
        int event = xmlPullParser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
          if (event == XmlPullParser.START_TAG) {
            if (xmlPullParser.getName().equals("string")) {

              String name = xmlPullParser.getAttributeValue(null, "name");
              String value = xmlPullParser.nextText();
              list.add(new Attribute(xmlPullParser.getName(), name, value));
            }
          }
          event = xmlPullParser.next();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
