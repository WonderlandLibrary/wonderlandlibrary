package org.json;

public class CookieList {
  public static JSONObject toJSONObject(String string) throws JSONException {
    JSONObject jo = new JSONObject();
    JSONTokener x = new JSONTokener(string);
    while (x.more()) {
      String name = Cookie.unescape(x.nextTo('='));
      x.next('=');
      jo.put(name, Cookie.unescape(x.nextTo(';')));
      x.next();
    } 
    return jo;
  }
  
  public static String toString(JSONObject jo) throws JSONException {
    boolean b = false;
    StringBuilder sb = new StringBuilder();
    for (String key : jo.keySet()) {
      Object value = jo.opt(key);
      if (!JSONObject.NULL.equals(value)) {
        if (b)
          sb.append(';'); 
        sb.append(Cookie.escape(key));
        sb.append("=");
        sb.append(Cookie.escape(value.toString()));
        b = true;
      } 
    } 
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\json\CookieList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */