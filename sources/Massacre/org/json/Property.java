package org.json;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

public class Property {
   public static JSONObject toJSONObject(Properties properties) throws JSONException {
      JSONObject jo = new JSONObject();
      if (properties != null && !properties.isEmpty()) {
         Enumeration enumProperties = properties.propertyNames();

         while(enumProperties.hasMoreElements()) {
            String name = (String)enumProperties.nextElement();
            jo.put(name, (Object)properties.getProperty(name));
         }
      }

      return jo;
   }

   public static Properties toProperties(JSONObject jo) throws JSONException {
      Properties properties = new Properties();
      if (jo != null) {
         Iterator var2 = jo.keySet().iterator();

         while(var2.hasNext()) {
            String key = (String)var2.next();
            Object value = jo.opt(key);
            if (!JSONObject.NULL.equals(value)) {
               properties.put(key, value.toString());
            }
         }
      }

      return properties;
   }
}
