/*
 * Decompiled with CFR 0.152.
 */
package org.json;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONWriter {
    private static final int maxdepth = 200;
    private boolean comma = false;
    protected char mode = (char)105;
    private final JSONObject[] stack = new JSONObject[200];
    private int top = 0;
    protected Appendable writer;

    public JSONWriter(Appendable w) {
        this.writer = w;
    }

    private JSONWriter append(String string) throws JSONException {
        if (string == null) {
            throw new JSONException("Null pointer");
        }
        if (this.mode == 'o' || this.mode == 'a') {
            try {
                if (this.comma && this.mode == 'a') {
                    this.writer.append(',');
                }
                this.writer.append(string);
            }
            catch (IOException e) {
                throw new JSONException(e);
            }
            if (this.mode == 'o') {
                this.mode = (char)107;
            }
            this.comma = true;
            return this;
        }
        throw new JSONException("Value out of sequence.");
    }

    public JSONWriter array() throws JSONException {
        if (this.mode == 'i' || this.mode == 'o' || this.mode == 'a') {
            this.push(null);
            this.append("[");
            this.comma = false;
            return this;
        }
        throw new JSONException("Misplaced array.");
    }

    private JSONWriter end(char m, char c) throws JSONException {
        if (this.mode != m) {
            throw new JSONException(m == 'a' ? "Misplaced endArray." : "Misplaced endObject.");
        }
        this.pop(m);
        try {
            this.writer.append(c);
        }
        catch (IOException e) {
            throw new JSONException(e);
        }
        this.comma = true;
        return this;
    }

    public JSONWriter endArray() throws JSONException {
        return this.end('a', ']');
    }

    public JSONWriter endObject() throws JSONException {
        return this.end('k', '}');
    }

    public JSONWriter key(String string) throws JSONException {
        if (string == null) {
            throw new JSONException("Null key.");
        }
        if (this.mode == 'k') {
            try {
                this.stack[this.top - 1].putOnce(string, Boolean.TRUE);
                if (this.comma) {
                    this.writer.append(',');
                }
                this.writer.append(JSONObject.quote(string));
                this.writer.append(':');
                this.comma = false;
                this.mode = (char)111;
                return this;
            }
            catch (IOException e) {
                throw new JSONException(e);
            }
        }
        throw new JSONException("Misplaced key.");
    }

    public JSONWriter object() throws JSONException {
        if (this.mode == 'i') {
            this.mode = (char)111;
        }
        if (this.mode == 'o' || this.mode == 'a') {
            this.append("{");
            this.push(new JSONObject());
            this.comma = false;
            return this;
        }
        throw new JSONException("Misplaced object.");
    }

    private void pop(char c) throws JSONException {
        char m;
        if (this.top <= 0) {
            throw new JSONException("Nesting error.");
        }
        char c2 = m = this.stack[this.top - 1] == null ? (char)'a' : 'k';
        if (m != c) {
            throw new JSONException("Nesting error.");
        }
        --this.top;
        this.mode = (char)(this.top == 0 ? 100 : (this.stack[this.top - 1] == null ? 97 : 107));
    }

    private void push(JSONObject jo) throws JSONException {
        if (this.top >= 200) {
            throw new JSONException("Nesting too deep.");
        }
        this.stack[this.top] = jo;
        this.mode = (char)(jo == null ? 97 : 107);
        ++this.top;
    }

    public JSONWriter value(boolean b) throws JSONException {
        return this.append(b ? "true" : "false");
    }

    public JSONWriter value(double d) throws JSONException {
        return this.value(new Double(d));
    }

    public JSONWriter value(long l) throws JSONException {
        return this.append(Long.toString(l));
    }

    public JSONWriter value(Object object) throws JSONException {
        return this.append(JSONObject.valueToString(object));
    }
}

