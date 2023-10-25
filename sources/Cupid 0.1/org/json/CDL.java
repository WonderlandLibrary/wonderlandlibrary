package org.json;

public class CDL {
  private static String getValue(JSONTokener x) throws JSONException {
    char c, q;
    StringBuilder sb;
    do {
      c = x.next();
    } while (c == ' ' || c == '\t');
    switch (c) {
      case '\000':
        return null;
      case '"':
      case '\'':
        q = c;
        sb = new StringBuilder();
        while (true) {
          c = x.next();
          if (c == q) {
            char nextC = x.next();
            if (nextC != '"') {
              if (nextC > '\000')
                x.back(); 
              break;
            } 
          } 
          if (c == '\000' || c == '\n' || c == '\r')
            throw x.syntaxError("Missing close quote '" + q + "'."); 
          sb.append(c);
        } 
        return sb.toString();
      case ',':
        x.back();
        return "";
    } 
    x.back();
    return x.nextTo(',');
  }
  
  public static JSONArray rowToJSONArray(JSONTokener x) throws JSONException {
    JSONArray ja = new JSONArray();
    while (true) {
      String value = getValue(x);
      char c = x.next();
      if (value == null || (ja
        .length() == 0 && value.length() == 0 && c != ','))
        return null; 
      ja.put(value);
      while (c != ',') {
        if (c != ' ') {
          if (c == '\n' || c == '\r' || c == '\000')
            return ja; 
          throw x.syntaxError("Bad character '" + c + "' (" + c + ").");
        } 
        c = x.next();
      } 
    } 
  }
  
  public static JSONObject rowToJSONObject(JSONArray names, JSONTokener x) throws JSONException {
    JSONArray ja = rowToJSONArray(x);
    return (ja != null) ? ja.toJSONObject(names) : null;
  }
  
  public static String rowToString(JSONArray ja) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < ja.length(); i++) {
      if (i > 0)
        sb.append(','); 
      Object object = ja.opt(i);
      if (object != null) {
        String string = object.toString();
        if (string.length() > 0 && (string.indexOf(',') >= 0 || string
          .indexOf('\n') >= 0 || string.indexOf('\r') >= 0 || string
          .indexOf(false) >= 0 || string.charAt(0) == '"')) {
          sb.append('"');
          int length = string.length();
          for (int j = 0; j < length; j++) {
            char c = string.charAt(j);
            if (c >= ' ' && c != '"')
              sb.append(c); 
          } 
          sb.append('"');
        } else {
          sb.append(string);
        } 
      } 
    } 
    sb.append('\n');
    return sb.toString();
  }
  
  public static JSONArray toJSONArray(String string) throws JSONException {
    return toJSONArray(new JSONTokener(string));
  }
  
  public static JSONArray toJSONArray(JSONTokener x) throws JSONException {
    return toJSONArray(rowToJSONArray(x), x);
  }
  
  public static JSONArray toJSONArray(JSONArray names, String string) throws JSONException {
    return toJSONArray(names, new JSONTokener(string));
  }
  
  public static JSONArray toJSONArray(JSONArray names, JSONTokener x) throws JSONException {
    if (names == null || names.length() == 0)
      return null; 
    JSONArray ja = new JSONArray();
    while (true) {
      JSONObject jo = rowToJSONObject(names, x);
      if (jo == null)
        break; 
      ja.put(jo);
    } 
    if (ja.length() == 0)
      return null; 
    return ja;
  }
  
  public static String toString(JSONArray ja) throws JSONException {
    JSONObject jo = ja.optJSONObject(0);
    if (jo != null) {
      JSONArray names = jo.names();
      if (names != null)
        return rowToString(names) + toString(names, ja); 
    } 
    return null;
  }
  
  public static String toString(JSONArray names, JSONArray ja) throws JSONException {
    if (names == null || names.length() == 0)
      return null; 
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < ja.length(); i++) {
      JSONObject jo = ja.optJSONObject(i);
      if (jo != null)
        sb.append(rowToString(jo.toJSONArray(names))); 
    } 
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\json\CDL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */