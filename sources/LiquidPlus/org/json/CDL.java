/*
 * Decompiled with CFR 0.152.
 */
package org.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CDL {
    private static String getValue(JSONTokener x) throws JSONException {
        char c;
        while ((c = x.next()) == ' ' || c == '\t') {
        }
        switch (c) {
            case '\u0000': {
                return null;
            }
            case '\"': 
            case '\'': {
                char q = c;
                StringBuffer sb = new StringBuffer();
                while (true) {
                    char nextC;
                    if ((c = x.next()) == q && (nextC = x.next()) != '\"') {
                        if (nextC <= '\u0000') break;
                        x.back();
                        break;
                    }
                    if (c == '\u0000' || c == '\n' || c == '\r') {
                        throw x.syntaxError("Missing close quote '" + q + "'.");
                    }
                    sb.append(c);
                }
                return sb.toString();
            }
            case ',': {
                x.back();
                return "";
            }
        }
        x.back();
        return x.nextTo(',');
    }

    public static JSONArray rowToJSONArray(JSONTokener x) throws JSONException {
        JSONArray ja = new JSONArray();
        block0: while (true) {
            String value = CDL.getValue(x);
            char c = x.next();
            if (value == null || ja.length() == 0 && value.length() == 0 && c != ',') {
                return null;
            }
            ja.put(value);
            while (true) {
                if (c == ',') continue block0;
                if (c != ' ') {
                    if (c == '\n' || c == '\r' || c == '\u0000') {
                        return ja;
                    }
                    throw x.syntaxError("Bad character '" + c + "' (" + c + ").");
                }
                c = x.next();
            }
            break;
        }
    }

    public static JSONObject rowToJSONObject(JSONArray names, JSONTokener x) throws JSONException {
        JSONArray ja = CDL.rowToJSONArray(x);
        return ja != null ? ja.toJSONObject(names) : null;
    }

    public static String rowToString(JSONArray ja) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ja.length(); ++i) {
            Object object;
            if (i > 0) {
                sb.append(',');
            }
            if ((object = ja.opt(i)) == null) continue;
            String string = object.toString();
            if (string.length() > 0 && (string.indexOf(44) >= 0 || string.indexOf(10) >= 0 || string.indexOf(13) >= 0 || string.indexOf(0) >= 0 || string.charAt(0) == '\"')) {
                sb.append('\"');
                int length = string.length();
                for (int j = 0; j < length; ++j) {
                    char c = string.charAt(j);
                    if (c < ' ' || c == '\"') continue;
                    sb.append(c);
                }
                sb.append('\"');
                continue;
            }
            sb.append(string);
        }
        sb.append('\n');
        return sb.toString();
    }

    public static JSONArray toJSONArray(String string) throws JSONException {
        return CDL.toJSONArray(new JSONTokener(string));
    }

    public static JSONArray toJSONArray(JSONTokener x) throws JSONException {
        return CDL.toJSONArray(CDL.rowToJSONArray(x), x);
    }

    public static JSONArray toJSONArray(JSONArray names, String string) throws JSONException {
        return CDL.toJSONArray(names, new JSONTokener(string));
    }

    public static JSONArray toJSONArray(JSONArray names, JSONTokener x) throws JSONException {
        JSONObject jo;
        if (names == null || names.length() == 0) {
            return null;
        }
        JSONArray ja = new JSONArray();
        while ((jo = CDL.rowToJSONObject(names, x)) != null) {
            ja.put(jo);
        }
        if (ja.length() == 0) {
            return null;
        }
        return ja;
    }

    public static String toString(JSONArray ja) throws JSONException {
        JSONArray names;
        JSONObject jo = ja.optJSONObject(0);
        if (jo != null && (names = jo.names()) != null) {
            return CDL.rowToString(names) + CDL.toString(names, ja);
        }
        return null;
    }

    public static String toString(JSONArray names, JSONArray ja) throws JSONException {
        if (names == null || names.length() == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ja.length(); ++i) {
            JSONObject jo = ja.optJSONObject(i);
            if (jo == null) continue;
            sb.append(CDL.rowToString(jo.toJSONArray(names)));
        }
        return sb.toString();
    }
}

