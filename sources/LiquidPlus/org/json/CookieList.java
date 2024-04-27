/*
 * Decompiled with CFR 0.152.
 */
package org.json;

import java.util.Map;
import org.json.Cookie;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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
        for (Map.Entry<String, Object> entry : jo.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (JSONObject.NULL.equals(value)) continue;
            if (b) {
                sb.append(';');
            }
            sb.append(Cookie.escape(key));
            sb.append("=");
            sb.append(Cookie.escape(value.toString()));
            b = true;
        }
        return sb.toString();
    }
}

