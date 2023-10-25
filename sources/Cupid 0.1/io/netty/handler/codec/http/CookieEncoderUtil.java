package io.netty.handler.codec.http;

import io.netty.util.internal.InternalThreadLocalMap;

final class CookieEncoderUtil {
  static StringBuilder stringBuilder() {
    return InternalThreadLocalMap.get().stringBuilder();
  }
  
  static String stripTrailingSeparator(StringBuilder buf) {
    if (buf.length() > 0)
      buf.setLength(buf.length() - 2); 
    return buf.toString();
  }
  
  static void add(StringBuilder sb, String name, String val) {
    if (val == null) {
      addQuoted(sb, name, "");
      return;
    } 
    for (int i = 0; i < val.length(); i++) {
      char c = val.charAt(i);
      switch (c) {
        case '\t':
        case ' ':
        case '"':
        case '(':
        case ')':
        case ',':
        case '/':
        case ':':
        case ';':
        case '<':
        case '=':
        case '>':
        case '?':
        case '@':
        case '[':
        case '\\':
        case ']':
        case '{':
        case '}':
          addQuoted(sb, name, val);
          return;
      } 
    } 
    addUnquoted(sb, name, val);
  }
  
  static void addUnquoted(StringBuilder sb, String name, String val) {
    sb.append(name);
    sb.append('=');
    sb.append(val);
    sb.append(';');
    sb.append(' ');
  }
  
  static void addQuoted(StringBuilder sb, String name, String val) {
    if (val == null)
      val = ""; 
    sb.append(name);
    sb.append('=');
    sb.append('"');
    sb.append(val.replace("\\", "\\\\").replace("\"", "\\\""));
    sb.append('"');
    sb.append(';');
    sb.append(' ');
  }
  
  static void add(StringBuilder sb, String name, long val) {
    sb.append(name);
    sb.append('=');
    sb.append(val);
    sb.append(';');
    sb.append(' ');
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\CookieEncoderUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */