/*
 * Decompiled with CFR 0.152.
 */
package org.json;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONTokener;
import org.json.XML;

public class XMLTokener
extends JSONTokener {
    public static final HashMap<String, Character> entity = new HashMap(8);

    public XMLTokener(String s) {
        super(s);
    }

    public String nextCDATA() throws JSONException {
        StringBuilder sb = new StringBuilder();
        while (this.more()) {
            char c = this.next();
            sb.append(c);
            int i = sb.length() - 3;
            if (i < 0 || sb.charAt(i) != ']' || sb.charAt(i + 1) != ']' || sb.charAt(i + 2) != '>') continue;
            sb.setLength(i);
            return sb.toString();
        }
        throw this.syntaxError("Unclosed CDATA");
    }

    public Object nextContent() throws JSONException {
        char c;
        while (Character.isWhitespace(c = this.next())) {
        }
        if (c == '\u0000') {
            return null;
        }
        if (c == '<') {
            return XML.LT;
        }
        StringBuilder sb = new StringBuilder();
        while (c != '\u0000') {
            if (c == '<') {
                this.back();
                return sb.toString().trim();
            }
            if (c == '&') {
                sb.append(this.nextEntity(c));
            } else {
                sb.append(c);
            }
            c = this.next();
        }
        return sb.toString().trim();
    }

    public Object nextEntity(char ampersand) throws JSONException {
        char c;
        StringBuilder sb = new StringBuilder();
        while (Character.isLetterOrDigit(c = this.next()) || c == '#') {
            sb.append(Character.toLowerCase(c));
        }
        if (c != ';') {
            throw this.syntaxError("Missing ';' in XML entity: &" + sb);
        }
        String string = sb.toString();
        return XMLTokener.unescapeEntity(string);
    }

    static String unescapeEntity(String e) {
        if (e == null || e.isEmpty()) {
            return "";
        }
        if (e.charAt(0) == '#') {
            int cp = e.charAt(1) == 'x' ? Integer.parseInt(e.substring(2), 16) : Integer.parseInt(e.substring(1));
            return new String(new int[]{cp}, 0, 1);
        }
        Character knownEntity = entity.get(e);
        if (knownEntity == null) {
            return '&' + e + ';';
        }
        return knownEntity.toString();
    }

    public Object nextMeta() throws JSONException {
        char c;
        while (Character.isWhitespace(c = this.next())) {
        }
        switch (c) {
            case '\u0000': {
                throw this.syntaxError("Misshaped meta tag");
            }
            case '<': {
                return XML.LT;
            }
            case '>': {
                return XML.GT;
            }
            case '/': {
                return XML.SLASH;
            }
            case '=': {
                return XML.EQ;
            }
            case '!': {
                return XML.BANG;
            }
            case '?': {
                return XML.QUEST;
            }
            case '\"': 
            case '\'': {
                char q = c;
                do {
                    if ((c = this.next()) != '\u0000') continue;
                    throw this.syntaxError("Unterminated string");
                } while (c != q);
                return Boolean.TRUE;
            }
        }
        while (!Character.isWhitespace(c = this.next())) {
            switch (c) {
                case '\u0000': 
                case '!': 
                case '\"': 
                case '\'': 
                case '/': 
                case '<': 
                case '=': 
                case '>': 
                case '?': {
                    this.back();
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.TRUE;
    }

    public Object nextToken() throws JSONException {
        char c;
        while (Character.isWhitespace(c = this.next())) {
        }
        switch (c) {
            case '\u0000': {
                throw this.syntaxError("Misshaped element");
            }
            case '<': {
                throw this.syntaxError("Misplaced '<'");
            }
            case '>': {
                return XML.GT;
            }
            case '/': {
                return XML.SLASH;
            }
            case '=': {
                return XML.EQ;
            }
            case '!': {
                return XML.BANG;
            }
            case '?': {
                return XML.QUEST;
            }
            case '\"': 
            case '\'': {
                char q = c;
                StringBuilder sb = new StringBuilder();
                while (true) {
                    if ((c = this.next()) == '\u0000') {
                        throw this.syntaxError("Unterminated string");
                    }
                    if (c == q) {
                        return sb.toString();
                    }
                    if (c == '&') {
                        sb.append(this.nextEntity(c));
                        continue;
                    }
                    sb.append(c);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        while (true) {
            sb.append(c);
            c = this.next();
            if (Character.isWhitespace(c)) {
                return sb.toString();
            }
            switch (c) {
                case '\u0000': {
                    return sb.toString();
                }
                case '!': 
                case '/': 
                case '=': 
                case '>': 
                case '?': 
                case '[': 
                case ']': {
                    this.back();
                    return sb.toString();
                }
                case '\"': 
                case '\'': 
                case '<': {
                    throw this.syntaxError("Bad character in a name");
                }
            }
        }
    }

    public boolean skipPast(String to) throws JSONException {
        char c;
        int i;
        int offset = 0;
        int length = to.length();
        char[] circle = new char[length];
        for (i = 0; i < length; ++i) {
            c = this.next();
            if (c == '\u0000') {
                return false;
            }
            circle[i] = c;
        }
        while (true) {
            int j = offset;
            boolean b = true;
            for (i = 0; i < length; ++i) {
                if (circle[j] != to.charAt(i)) {
                    b = false;
                    break;
                }
                if (++j < length) continue;
                j -= length;
            }
            if (b) {
                return true;
            }
            c = this.next();
            if (c == '\u0000') {
                return false;
            }
            circle[offset] = c;
            if (++offset < length) continue;
            offset -= length;
        }
    }

    static {
        entity.put("amp", XML.AMP);
        entity.put("apos", XML.APOS);
        entity.put("gt", XML.GT);
        entity.put("lt", XML.LT);
        entity.put("quot", XML.QUOT);
    }
}

