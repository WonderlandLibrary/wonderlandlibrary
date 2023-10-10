// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

import java.util.Iterator;
import java.util.Enumeration;
import java.util.Properties;

public class Property
{
    public static JSONObject toJSONObject(final Properties properties) throws JSONException {
        final JSONObject jo = new JSONObject();
        if (properties != null && !properties.isEmpty()) {
            final Enumeration<?> enumProperties = properties.propertyNames();
            while (enumProperties.hasMoreElements()) {
                final String name = (String)enumProperties.nextElement();
                jo.put(name, properties.getProperty(name));
            }
        }
        return jo;
    }
    
    public static Properties toProperties(final JSONObject jo) throws JSONException {
        final Properties properties = new Properties();
        if (jo != null) {
            final Iterator<String> keys = jo.keys();
            while (keys.hasNext()) {
                final String name = keys.next();
                properties.put(name, jo.getString(name));
            }
        }
        return properties;
    }
}
