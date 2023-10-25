package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.util.Strings;

public final class Transform {
  private static final String CDATA_START = "<![CDATA[";
  
  private static final String CDATA_END = "]]>";
  
  private static final String CDATA_PSEUDO_END = "]]&gt;";
  
  private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
  
  private static final int CDATA_END_LEN = "]]>".length();
  
  public static String escapeHtmlTags(String input) {
    if (Strings.isEmpty(input) || (input
      .indexOf('"') == -1 && input
      .indexOf('&') == -1 && input
      .indexOf('<') == -1 && input
      .indexOf('>') == -1))
      return input; 
    StringBuilder buf = new StringBuilder(input.length() + 6);
    int len = input.length();
    for (int i = 0; i < len; i++) {
      char ch = input.charAt(i);
      if (ch > '>') {
        buf.append(ch);
      } else {
        switch (ch) {
          case '<':
            buf.append("&lt;");
            break;
          case '>':
            buf.append("&gt;");
            break;
          case '&':
            buf.append("&amp;");
            break;
          case '"':
            buf.append("&quot;");
            break;
          default:
            buf.append(ch);
            break;
        } 
      } 
    } 
    return buf.toString();
  }
  
  public static void appendEscapingCData(StringBuilder buf, String str) {
    if (str != null) {
      int end = str.indexOf("]]>");
      if (end < 0) {
        buf.append(str);
      } else {
        int start = 0;
        while (end > -1) {
          buf.append(str.substring(start, end));
          buf.append("]]>]]&gt;<![CDATA[");
          start = end + CDATA_END_LEN;
          if (start < str.length()) {
            end = str.indexOf("]]>", start);
            continue;
          } 
          return;
        } 
        buf.append(str.substring(start));
      } 
    } 
  }
  
  public static String escapeJsonControlCharacters(String input) {
    if (Strings.isEmpty(input) || (input
      .indexOf('"') == -1 && input
      .indexOf('\\') == -1 && input
      .indexOf('/') == -1 && input
      .indexOf('\b') == -1 && input
      .indexOf('\f') == -1 && input
      .indexOf('\n') == -1 && input
      .indexOf('\r') == -1 && input
      .indexOf('\t') == -1))
      return input; 
    StringBuilder buf = new StringBuilder(input.length() + 6);
    int len = input.length();
    for (int i = 0; i < len; i++) {
      char ch = input.charAt(i);
      String escBs = "\\";
      switch (ch) {
        case '"':
          buf.append("\\");
          buf.append(ch);
          break;
        case '\\':
          buf.append("\\");
          buf.append(ch);
          break;
        case '/':
          buf.append("\\");
          buf.append(ch);
          break;
        case '\b':
          buf.append("\\");
          buf.append('b');
          break;
        case '\f':
          buf.append("\\");
          buf.append('f');
          break;
        case '\n':
          buf.append("\\");
          buf.append('n');
          break;
        case '\r':
          buf.append("\\");
          buf.append('r');
          break;
        case '\t':
          buf.append("\\");
          buf.append('t');
          break;
        default:
          buf.append(ch);
          break;
      } 
    } 
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\Transform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */