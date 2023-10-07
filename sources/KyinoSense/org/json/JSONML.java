/*
 * Decompiled with CFR 0.152.
 */
package org.json;

import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.json.XMLTokener;

public class JSONML {
    private static Object parse(XMLTokener x, boolean arrayForm, JSONArray ja, boolean keepStrings) throws JSONException {
        String closeTag = null;
        JSONArray newja = null;
        JSONObject newjo = null;
        String tagName = null;
        while (true) {
            if (!x.more()) {
                throw x.syntaxError("Bad XML");
            }
            Object token = x.nextContent();
            if (token == XML.LT) {
                token = x.nextToken();
                if (token instanceof Character) {
                    if (token == XML.SLASH) {
                        token = x.nextToken();
                        if (!(token instanceof String)) {
                            throw new JSONException("Expected a closing name instead of '" + token + "'.");
                        }
                        if (x.nextToken() != XML.GT) {
                            throw x.syntaxError("Misshaped close tag");
                        }
                        return token;
                    }
                    if (token == XML.BANG) {
                        char c = x.next();
                        if (c == '-') {
                            if (x.next() == '-') {
                                x.skipPast("-->");
                                continue;
                            }
                            x.back();
                            continue;
                        }
                        if (c == '[') {
                            token = x.nextToken();
                            if (token.equals("CDATA") && x.next() == '[') {
                                if (ja == null) continue;
                                ja.put(x.nextCDATA());
                                continue;
                            }
                            throw x.syntaxError("Expected 'CDATA['");
                        }
                        int i = 1;
                        do {
                            if ((token = x.nextMeta()) == null) {
                                throw x.syntaxError("Missing '>' after '<!'.");
                            }
                            if (token == XML.LT) {
                                ++i;
                                continue;
                            }
                            if (token != XML.GT) continue;
                            --i;
                        } while (i > 0);
                        continue;
                    }
                    if (token == XML.QUEST) {
                        x.skipPast("?>");
                        continue;
                    }
                    throw x.syntaxError("Misshaped tag");
                }
                if (!(token instanceof String)) {
                    throw x.syntaxError("Bad tagName '" + token + "'.");
                }
                tagName = (String)token;
                newja = new JSONArray();
                newjo = new JSONObject();
                if (arrayForm) {
                    newja.put(tagName);
                    if (ja != null) {
                        ja.put(newja);
                    }
                } else {
                    newjo.put("tagName", tagName);
                    if (ja != null) {
                        ja.put(newjo);
                    }
                }
                token = null;
                while (true) {
                    if (token == null) {
                        token = x.nextToken();
                    }
                    if (token == null) {
                        throw x.syntaxError("Misshaped tag");
                    }
                    if (!(token instanceof String)) break;
                    String attribute = (String)token;
                    if (!arrayForm && ("tagName".equals(attribute) || "childNode".equals(attribute))) {
                        throw x.syntaxError("Reserved attribute.");
                    }
                    token = x.nextToken();
                    if (token == XML.EQ) {
                        token = x.nextToken();
                        if (!(token instanceof String)) {
                            throw x.syntaxError("Missing value");
                        }
                        newjo.accumulate(attribute, keepStrings ? (String)token : XML.stringToValue((String)token));
                        token = null;
                        continue;
                    }
                    newjo.accumulate(attribute, "");
                }
                if (arrayForm && newjo.length() > 0) {
                    newja.put(newjo);
                }
                if (token == XML.SLASH) {
                    if (x.nextToken() != XML.GT) {
                        throw x.syntaxError("Misshaped tag");
                    }
                    if (ja != null) continue;
                    if (arrayForm) {
                        return newja;
                    }
                    return newjo;
                }
                if (token != XML.GT) {
                    throw x.syntaxError("Misshaped tag");
                }
                closeTag = (String)JSONML.parse(x, arrayForm, newja, keepStrings);
                if (closeTag == null) continue;
                if (!closeTag.equals(tagName)) {
                    throw x.syntaxError("Mismatched '" + tagName + "' and '" + closeTag + "'");
                }
                tagName = null;
                if (!arrayForm && newja.length() > 0) {
                    newjo.put("childNodes", newja);
                }
                if (ja != null) continue;
                if (arrayForm) {
                    return newja;
                }
                return newjo;
            }
            if (ja == null) continue;
            ja.put(token instanceof String ? (keepStrings ? XML.unescape((String)token) : XML.stringToValue((String)token)) : token);
        }
    }

    public static JSONArray toJSONArray(String string) throws JSONException {
        return (JSONArray)JSONML.parse(new XMLTokener(string), true, null, false);
    }

    public static JSONArray toJSONArray(String string, boolean keepStrings) throws JSONException {
        return (JSONArray)JSONML.parse(new XMLTokener(string), true, null, keepStrings);
    }

    public static JSONArray toJSONArray(XMLTokener x, boolean keepStrings) throws JSONException {
        return (JSONArray)JSONML.parse(x, true, null, keepStrings);
    }

    public static JSONArray toJSONArray(XMLTokener x) throws JSONException {
        return (JSONArray)JSONML.parse(x, true, null, false);
    }

    public static JSONObject toJSONObject(String string) throws JSONException {
        return (JSONObject)JSONML.parse(new XMLTokener(string), false, null, false);
    }

    public static JSONObject toJSONObject(String string, boolean keepStrings) throws JSONException {
        return (JSONObject)JSONML.parse(new XMLTokener(string), false, null, keepStrings);
    }

    public static JSONObject toJSONObject(XMLTokener x) throws JSONException {
        return (JSONObject)JSONML.parse(x, false, null, false);
    }

    public static JSONObject toJSONObject(XMLTokener x, boolean keepStrings) throws JSONException {
        return (JSONObject)JSONML.parse(x, false, null, keepStrings);
    }

    public static String toString(JSONArray ja) throws JSONException {
        int length;
        int i;
        StringBuilder sb = new StringBuilder();
        String tagName = ja.getString(0);
        XML.noSpace(tagName);
        tagName = XML.escape(tagName);
        sb.append('<');
        sb.append(tagName);
        Object object = ja.opt(1);
        if (object instanceof JSONObject) {
            i = 2;
            JSONObject jo = (JSONObject)object;
            for (Map.Entry<String, Object> entry : jo.entrySet()) {
                String key = entry.getKey();
                XML.noSpace(key);
                Object value = entry.getValue();
                if (value == null) continue;
                sb.append(' ');
                sb.append(XML.escape(key));
                sb.append('=');
                sb.append('\"');
                sb.append(XML.escape(value.toString()));
                sb.append('\"');
            }
        } else {
            i = 1;
        }
        if (i >= (length = ja.length())) {
            sb.append('/');
            sb.append('>');
        } else {
            sb.append('>');
            do {
                object = ja.get(i);
                ++i;
                if (object == null) continue;
                if (object instanceof String) {
                    sb.append(XML.escape(object.toString()));
                    continue;
                }
                if (object instanceof JSONObject) {
                    sb.append(JSONML.toString((JSONObject)object));
                    continue;
                }
                if (object instanceof JSONArray) {
                    sb.append(JSONML.toString((JSONArray)object));
                    continue;
                }
                sb.append(object.toString());
            } while (i < length);
            sb.append('<');
            sb.append('/');
            sb.append(tagName);
            sb.append('>');
        }
        return sb.toString();
    }

    public static String toString(JSONObject jo) throws JSONException {
        StringBuilder sb = new StringBuilder();
        String tagName = jo.optString("tagName");
        if (tagName == null) {
            return XML.escape(jo.toString());
        }
        XML.noSpace(tagName);
        tagName = XML.escape(tagName);
        sb.append('<');
        sb.append(tagName);
        for (Map.Entry<String, Object> entry : jo.entrySet()) {
            String key = entry.getKey();
            if ("tagName".equals(key) || "childNodes".equals(key)) continue;
            XML.noSpace(key);
            Object value = entry.getValue();
            if (value == null) continue;
            sb.append(' ');
            sb.append(XML.escape(key));
            sb.append('=');
            sb.append('\"');
            sb.append(XML.escape(value.toString()));
            sb.append('\"');
        }
        JSONArray ja = jo.optJSONArray("childNodes");
        if (ja == null) {
            sb.append('/');
            sb.append('>');
        } else {
            sb.append('>');
            int length = ja.length();
            for (int i = 0; i < length; ++i) {
                Object object = ja.get(i);
                if (object == null) continue;
                if (object instanceof String) {
                    sb.append(XML.escape(object.toString()));
                    continue;
                }
                if (object instanceof JSONObject) {
                    sb.append(JSONML.toString((JSONObject)object));
                    continue;
                }
                if (object instanceof JSONArray) {
                    sb.append(JSONML.toString((JSONArray)object));
                    continue;
                }
                sb.append(object.toString());
            }
            sb.append('<');
            sb.append('/');
            sb.append(tagName);
            sb.append('>');
        }
        return sb.toString();
    }
}

