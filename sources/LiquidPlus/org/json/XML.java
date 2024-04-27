/*
 * Decompiled with CFR 0.152.
 */
package org.json;

import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XMLTokener;

public class XML {
    public static final Character AMP = Character.valueOf('&');
    public static final Character APOS = Character.valueOf('\'');
    public static final Character BANG = Character.valueOf('!');
    public static final Character EQ = Character.valueOf('=');
    public static final Character GT = Character.valueOf('>');
    public static final Character LT = Character.valueOf('<');
    public static final Character QUEST = Character.valueOf('?');
    public static final Character QUOT = Character.valueOf('\"');
    public static final Character SLASH = Character.valueOf('/');

    private static Iterable<Integer> codePointIterator(final String string) {
        return new Iterable<Integer>(){

            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>(){
                    private int nextIndex = 0;
                    private int length;
                    {
                        this.length = string.length();
                    }

                    @Override
                    public boolean hasNext() {
                        return this.nextIndex < this.length;
                    }

                    @Override
                    public Integer next() {
                        int result = string.codePointAt(this.nextIndex);
                        this.nextIndex += Character.charCount(result);
                        return result;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static String escape(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        block7: for (int cp : XML.codePointIterator(string)) {
            switch (cp) {
                case 38: {
                    sb.append("&amp;");
                    continue block7;
                }
                case 60: {
                    sb.append("&lt;");
                    continue block7;
                }
                case 62: {
                    sb.append("&gt;");
                    continue block7;
                }
                case 34: {
                    sb.append("&quot;");
                    continue block7;
                }
                case 39: {
                    sb.append("&apos;");
                    continue block7;
                }
            }
            if (XML.mustEscape(cp)) {
                sb.append("&#x");
                sb.append(Integer.toHexString(cp));
                sb.append(';');
                continue;
            }
            sb.appendCodePoint(cp);
        }
        return sb.toString();
    }

    private static boolean mustEscape(int cp) {
        return Character.isISOControl(cp) && cp != 9 && cp != 10 && cp != 13 || (cp < 32 || cp > 55295) && (cp < 57344 || cp > 65533) && (cp < 65536 || cp > 0x10FFFF);
    }

    public static String unescape(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        int length = string.length();
        for (int i = 0; i < length; ++i) {
            char c = string.charAt(i);
            if (c == '&') {
                int semic = string.indexOf(59, i);
                if (semic > i) {
                    String entity = string.substring(i + 1, semic);
                    sb.append(XMLTokener.unescapeEntity(entity));
                    i += entity.length() + 1;
                    continue;
                }
                sb.append(c);
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static void noSpace(String string) throws JSONException {
        int length = string.length();
        if (length == 0) {
            throw new JSONException("Empty string.");
        }
        for (int i = 0; i < length; ++i) {
            if (!Character.isWhitespace(string.charAt(i))) continue;
            throw new JSONException("'" + string + "' contains a space character.");
        }
    }

    private static boolean parse(XMLTokener x, JSONObject context, String name, boolean keepStrings) throws JSONException {
        String string;
        JSONObject jsonobject = null;
        Object token = x.nextToken();
        if (token == BANG) {
            char c = x.next();
            if (c == '-') {
                if (x.next() == '-') {
                    x.skipPast("-->");
                    return false;
                }
                x.back();
            } else if (c == '[') {
                token = x.nextToken();
                if ("CDATA".equals(token) && x.next() == '[') {
                    String string2 = x.nextCDATA();
                    if (string2.length() > 0) {
                        context.accumulate("content", string2);
                    }
                    return false;
                }
                throw x.syntaxError("Expected 'CDATA['");
            }
            int i = 1;
            do {
                if ((token = x.nextMeta()) == null) {
                    throw x.syntaxError("Missing '>' after '<!'.");
                }
                if (token == LT) {
                    ++i;
                    continue;
                }
                if (token != GT) continue;
                --i;
            } while (i > 0);
            return false;
        }
        if (token == QUEST) {
            x.skipPast("?>");
            return false;
        }
        if (token == SLASH) {
            token = x.nextToken();
            if (name == null) {
                throw x.syntaxError("Mismatched close tag " + token);
            }
            if (!token.equals(name)) {
                throw x.syntaxError("Mismatched " + name + " and " + token);
            }
            if (x.nextToken() != GT) {
                throw x.syntaxError("Misshaped close tag");
            }
            return true;
        }
        if (token instanceof Character) {
            throw x.syntaxError("Misshaped tag");
        }
        String tagName = (String)token;
        token = null;
        jsonobject = new JSONObject();
        while (true) {
            if (token == null) {
                token = x.nextToken();
            }
            if (!(token instanceof String)) break;
            string = (String)token;
            token = x.nextToken();
            if (token == EQ) {
                token = x.nextToken();
                if (!(token instanceof String)) {
                    throw x.syntaxError("Missing value");
                }
                jsonobject.accumulate(string, keepStrings ? (String)token : XML.stringToValue((String)token));
                token = null;
                continue;
            }
            jsonobject.accumulate(string, "");
        }
        if (token == SLASH) {
            if (x.nextToken() != GT) {
                throw x.syntaxError("Misshaped tag");
            }
            if (jsonobject.length() > 0) {
                context.accumulate(tagName, jsonobject);
            } else {
                context.accumulate(tagName, "");
            }
            return false;
        }
        if (token == GT) {
            while (true) {
                if ((token = x.nextContent()) == null) {
                    if (tagName != null) {
                        throw x.syntaxError("Unclosed tag " + tagName);
                    }
                    return false;
                }
                if (token instanceof String) {
                    string = (String)token;
                    if (string.length() <= 0) continue;
                    jsonobject.accumulate("content", keepStrings ? string : XML.stringToValue(string));
                    continue;
                }
                if (token == LT && XML.parse(x, jsonobject, tagName, keepStrings)) break;
            }
            if (jsonobject.length() == 0) {
                context.accumulate(tagName, "");
            } else if (jsonobject.length() == 1 && jsonobject.opt("content") != null) {
                context.accumulate(tagName, jsonobject.opt("content"));
            } else {
                context.accumulate(tagName, jsonobject);
            }
            return false;
        }
        throw x.syntaxError("Misshaped tag");
    }

    public static Object stringToValue(String string) {
        return JSONObject.stringToValue(string);
    }

    public static JSONObject toJSONObject(String string) throws JSONException {
        return XML.toJSONObject(string, false);
    }

    public static JSONObject toJSONObject(String string, boolean keepStrings) throws JSONException {
        JSONObject jo = new JSONObject();
        XMLTokener x = new XMLTokener(string);
        while (x.more() && x.skipPast("<")) {
            XML.parse(x, jo, null, keepStrings);
        }
        return jo;
    }

    public static String toString(Object object) throws JSONException {
        return XML.toString(object, null);
    }

    public static String toString(Object object, String tagName) throws JSONException {
        String string;
        StringBuilder sb = new StringBuilder();
        if (object instanceof JSONObject) {
            if (tagName != null) {
                sb.append('<');
                sb.append(tagName);
                sb.append('>');
            }
            JSONObject jo = (JSONObject)object;
            for (Map.Entry<String, Object> entry : jo.entrySet()) {
                JSONArray ja;
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value == null) {
                    value = "";
                } else if (value.getClass().isArray()) {
                    value = new JSONArray(value);
                }
                if ("content".equals(key)) {
                    if (value instanceof JSONArray) {
                        ja = (JSONArray)value;
                        int i = 0;
                        for (Object val : ja) {
                            if (i > 0) {
                                sb.append('\n');
                            }
                            sb.append(XML.escape(val.toString()));
                            ++i;
                        }
                        continue;
                    }
                    sb.append(XML.escape(value.toString()));
                    continue;
                }
                if (value instanceof JSONArray) {
                    ja = (JSONArray)value;
                    for (Object val : ja) {
                        if (val instanceof JSONArray) {
                            sb.append('<');
                            sb.append(key);
                            sb.append('>');
                            sb.append(XML.toString(val));
                            sb.append("</");
                            sb.append(key);
                            sb.append('>');
                            continue;
                        }
                        sb.append(XML.toString(val, key));
                    }
                    continue;
                }
                if ("".equals(value)) {
                    sb.append('<');
                    sb.append(key);
                    sb.append("/>");
                    continue;
                }
                sb.append(XML.toString(value, key));
            }
            if (tagName != null) {
                sb.append("</");
                sb.append(tagName);
                sb.append('>');
            }
            return sb.toString();
        }
        if (object != null && (object instanceof JSONArray || object.getClass().isArray())) {
            JSONArray ja = object.getClass().isArray() ? new JSONArray(object) : (JSONArray)object;
            for (Object val : ja) {
                sb.append(XML.toString(val, tagName == null ? "array" : tagName));
            }
            return sb.toString();
        }
        String string2 = string = object == null ? "null" : XML.escape(object.toString());
        return tagName == null ? "\"" + string + "\"" : (string.length() == 0 ? "<" + tagName + "/>" : "<" + tagName + ">" + string + "</" + tagName + ">");
    }
}

