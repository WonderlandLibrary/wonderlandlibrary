/*
 * Decompiled with CFR 0.152.
 */
package org.json;

import org.json.JSONException;
import org.json.JSONTokener;

public class HTTPTokener
extends JSONTokener {
    public HTTPTokener(String string) {
        super(string);
    }

    public String nextToken() throws JSONException {
        char c;
        StringBuilder sb = new StringBuilder();
        while (Character.isWhitespace(c = this.next())) {
        }
        if (c == '\"' || c == '\'') {
            char q = c;
            while (true) {
                if ((c = this.next()) < ' ') {
                    throw this.syntaxError("Unterminated string.");
                }
                if (c == q) {
                    return sb.toString();
                }
                sb.append(c);
            }
        }
        while (c != '\u0000' && !Character.isWhitespace(c)) {
            sb.append(c);
            c = this.next();
        }
        return sb.toString();
    }
}

