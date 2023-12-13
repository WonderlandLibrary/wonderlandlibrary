package org.json;

import java.util.Iterator;

public class CookieList {
   public static JSONObject toJSONObject(String string) throws JSONException {
      JSONObject jo = new JSONObject();
      JSONTokener x = new JSONTokener(string);

      while(x.more()) {
         String name = Cookie.unescape(x.nextTo('='));
         x.next('=');
         jo.put(name, (Object)Cookie.unescape(x.nextTo(';')));
         x.next();
      }

      return jo;
   }

   public static String toString(JSONObject jo) throws JSONException {
      boolean b = false;
      StringBuilder sb = new StringBuilder();
      Iterator var3 = jo.keySet().iterator();

      while(var3.hasNext()) {
         String key = (String)var3.next();
         Object value = jo.opt(key);
         if (!JSONObject.NULL.equals(value)) {
            if (b) {
               sb.append(';');
            }

            sb.append(Cookie.escape(key));
            sb.append("=");
            sb.append(Cookie.escape(value.toString()));
            b = true;
         }
      }

      return sb.toString();
   }
}
