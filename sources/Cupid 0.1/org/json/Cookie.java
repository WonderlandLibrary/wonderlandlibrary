package org.json;

import java.util.Locale;

public class Cookie {
  public static String escape(String string) {
    String s = string.trim();
    int length = s.length();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      char c = s.charAt(i);
      if (c < ' ' || c == '+' || c == '%' || c == '=' || c == ';') {
        sb.append('%');
        sb.append(Character.forDigit((char)(c >>> 4 & 0xF), 16));
        sb.append(Character.forDigit((char)(c & 0xF), 16));
      } else {
        sb.append(c);
      } 
    } 
    return sb.toString();
  }
  
  public static JSONObject toJSONObject(String string) {
    JSONObject jo = new JSONObject();
    JSONTokener x = new JSONTokener(string);
    String name = unescape(x.nextTo('=').trim());
    if ("".equals(name))
      throw new JSONException("Cookies must have a 'name'"); 
    jo.put("name", name);
    x.next('=');
    jo.put("value", unescape(x.nextTo(';')).trim());
    x.next();
    while (x.more()) {
      Object value;
      name = unescape(x.nextTo("=;")).trim().toLowerCase(Locale.ROOT);
      if ("name".equalsIgnoreCase(name))
        throw new JSONException("Illegal attribute name: 'name'"); 
      if ("value".equalsIgnoreCase(name))
        throw new JSONException("Illegal attribute name: 'value'"); 
      if (x.next() != '=') {
        value = Boolean.TRUE;
      } else {
        value = unescape(x.nextTo(';')).trim();
        x.next();
      } 
      if (!"".equals(name) && !"".equals(value))
        jo.put(name, value); 
    } 
    return jo;
  }
  
  public static String toString(JSONObject jo) throws JSONException {
    StringBuilder sb = new StringBuilder();
    String name = null;
    Object value = null;
    for (String key : jo.keySet()) {
      if ("name".equalsIgnoreCase(key))
        name = jo.getString(key).trim(); 
      if ("value".equalsIgnoreCase(key))
        value = jo.getString(key).trim(); 
      if (name != null && value != null)
        break; 
    } 
    if (name == null || "".equals(name.trim()))
      throw new JSONException("Cookie does not have a name"); 
    if (value == null)
      value = ""; 
    sb.append(escape(name));
    sb.append("=");
    sb.append(escape((String)value));
    for (String key : jo.keySet()) {
      if ("name".equalsIgnoreCase(key) || "value"
        .equalsIgnoreCase(key))
        continue; 
      value = jo.opt(key);
      if (value instanceof Boolean) {
        if (Boolean.TRUE.equals(value))
          sb.append(';').append(escape(key)); 
        continue;
      } 
      sb.append(';')
        .append(escape(key))
        .append('=')
        .append(escape(value.toString()));
    } 
    return sb.toString();
  }
  
  public static String unescape(String string) {
    int length = string.length();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      char c = string.charAt(i);
      if (c == '+') {
        c = ' ';
      } else if (c == '%' && i + 2 < length) {
        int d = JSONTokener.dehexchar(string.charAt(i + 1));
        int e = JSONTokener.dehexchar(string.charAt(i + 2));
        if (d >= 0 && e >= 0) {
          c = (char)(d * 16 + e);
          i += 2;
        } 
      } 
      sb.append(c);
    } 
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\json\Cookie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */