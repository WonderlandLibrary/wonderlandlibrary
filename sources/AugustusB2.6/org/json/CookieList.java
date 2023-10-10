// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

import java.util.Iterator;

public class CookieList
{
    public static JSONObject toJSONObject(final String string) throws JSONException {
        final JSONObject jo = new JSONObject();
        final JSONTokener x = new JSONTokener(string);
        while (x.more()) {
            final String name = Cookie.unescape(x.nextTo('='));
            x.next('=');
            jo.put(name, Cookie.unescape(x.nextTo(';')));
            x.next();
        }
        return jo;
    }
    
    public static String toString(final JSONObject jo) throws JSONException {
        boolean b = false;
        final Iterator<String> keys = jo.keys();
        final StringBuilder sb = new StringBuilder();
        while (keys.hasNext()) {
            final String string = keys.next();
            if (!jo.isNull(string)) {
                if (b) {
                    sb.append(';');
                }
                sb.append(Cookie.escape(string));
                sb.append("=");
                sb.append(Cookie.escape(jo.getString(string)));
                b = true;
            }
        }
        return sb.toString();
    }
}
