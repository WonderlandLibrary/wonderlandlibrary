package com.google.common.xml;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;

@Beta
@GwtCompatible
public class XmlEscapers {
  private static final char MIN_ASCII_CONTROL_CHAR = '\000';
  
  private static final char MAX_ASCII_CONTROL_CHAR = '\037';
  
  private static final Escaper XML_ESCAPER;
  
  private static final Escaper XML_CONTENT_ESCAPER;
  
  private static final Escaper XML_ATTRIBUTE_ESCAPER;
  
  public static Escaper xmlContentEscaper() {
    return XML_CONTENT_ESCAPER;
  }
  
  public static Escaper xmlAttributeEscaper() {
    return XML_ATTRIBUTE_ESCAPER;
  }
  
  static {
    Escapers.Builder builder = Escapers.builder();
    builder.setSafeRange(false, '￿');
    builder.setUnsafeReplacement("");
    for (char c = Character.MIN_VALUE; c <= '\037'; c = (char)(c + 1)) {
      if (c != '\t' && c != '\n' && c != '\r')
        builder.addEscape(c, ""); 
    } 
    builder.addEscape('&', "&amp;");
    builder.addEscape('<', "&lt;");
    builder.addEscape('>', "&gt;");
    XML_CONTENT_ESCAPER = builder.build();
    builder.addEscape('\'', "&apos;");
    builder.addEscape('"', "&quot;");
    XML_ESCAPER = builder.build();
    builder.addEscape('\t', "&#x9;");
    builder.addEscape('\n', "&#xA;");
    builder.addEscape('\r', "&#xD;");
    XML_ATTRIBUTE_ESCAPER = builder.build();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\xml\XmlEscapers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */