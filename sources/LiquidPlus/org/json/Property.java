/*
 * Decompiled with CFR 0.152.
 */
package org.json;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import org.json.JSONException;
import org.json.JSONObject;

public class Property {
    public static JSONObject toJSONObject(Properties properties) throws JSONException {
        JSONObject jo = new JSONObject(properties == null ? 0 : properties.size());
        if (properties != null && !properties.isEmpty()) {
            Enumeration<?> enumProperties = properties.propertyNames();
            while (enumProperties.hasMoreElements()) {
                String name = (String)enumProperties.nextElement();
                jo.put(name, properties.getProperty(name));
            }
        }
        return jo;
    }

    public static Properties toProperties(JSONObject jo) throws JSONException {
        Properties properties = new Properties();
        if (jo != null) {
            for (Map.Entry<String, Object> entry : jo.entrySet()) {
                Object value = entry.getValue();
                if (JSONObject.NULL.equals(value)) continue;
                properties.put(entry.getKey(), value.toString());
            }
        }
        return properties;
    }
}

