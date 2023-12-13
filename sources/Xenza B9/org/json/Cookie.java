// 
// Decompiled by Procyon v0.6.0
// 

package org.json;

import java.util.Iterator;
import java.util.Locale;

public class Cookie
{
    public static String escape(final String string) {
        final String s = string.trim();
        final int length = s.length();
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            final char c = s.charAt(i);
            if (c < ' ' || c == '+' || c == '%' || c == '=' || c == ';') {
                sb.append('%');
                sb.append(Character.forDigit((char)(c >>> 4 & 0xF), 16));
                sb.append(Character.forDigit((char)(c & '\u000f'), 16));
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static JSONObject toJSONObject(final String string) {
        final JSONObject jo = new JSONObject();
        final JSONTokener x = new JSONTokener(string);
        String name = unescape(x.nextTo('=').trim());
        if ("".equals(name)) {
            throw new JSONException("Cookies must have a 'name'");
        }
        jo.put("name", name);
        x.next('=');
        jo.put("value", unescape(x.nextTo(';')).trim());
        x.next();
        while (x.more()) {
            name = unescape(x.nextTo("=;")).trim().toLowerCase(Locale.ROOT);
            if ("name".equalsIgnoreCase(name)) {
                throw new JSONException("Illegal attribute name: 'name'");
            }
            if ("value".equalsIgnoreCase(name)) {
                throw new JSONException("Illegal attribute name: 'value'");
            }
            Object value;
            if (x.next() != '=') {
                value = Boolean.TRUE;
            }
            else {
                value = unescape(x.nextTo(';')).trim();
                x.next();
            }
            if ("".equals(name) || "".equals(value)) {
                continue;
            }
            jo.put(name, value);
        }
        return jo;
    }
    
    public static String toString(final JSONObject jo) throws JSONException {
        final StringBuilder sb = new StringBuilder();
        String name = null;
        Object value = null;
        for (final String key : jo.keySet()) {
            if ("name".equalsIgnoreCase(key)) {
                name = jo.getString(key).trim();
            }
            if ("value".equalsIgnoreCase(key)) {
                value = jo.getString(key).trim();
            }
            if (name != null && value != null) {
                break;
            }
        }
        if (name == null || "".equals(name.trim())) {
            throw new JSONException("Cookie does not have a name");
        }
        if (value == null) {
            value = "";
        }
        sb.append(escape(name));
        sb.append("=");
        sb.append(escape((String)value));
        for (final String key : jo.keySet()) {
            if (!"name".equalsIgnoreCase(key)) {
                if ("value".equalsIgnoreCase(key)) {
                    continue;
                }
                value = jo.opt(key);
                if (value instanceof Boolean) {
                    if (!Boolean.TRUE.equals(value)) {
                        continue;
                    }
                    sb.append(';').append(escape(key));
                }
                else {
                    sb.append(';').append(escape(key)).append('=').append(escape(value.toString()));
                }
            }
        }
        return sb.toString();
    }
    
    public static String unescape(final String string) {
        final int length = string.length();
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            char c = string.charAt(i);
            if (c == '+') {
                c = ' ';
            }
            else if (c == '%' && i + 2 < length) {
                final int d = JSONTokener.dehexchar(string.charAt(i + 1));
                final int e = JSONTokener.dehexchar(string.charAt(i + 2));
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
