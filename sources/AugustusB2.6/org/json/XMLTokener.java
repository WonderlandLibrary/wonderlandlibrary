// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

import java.util.HashMap;

public class XMLTokener extends JSONTokener
{
    public static final HashMap<String, Character> entity;
    
    public XMLTokener(final String s) {
        super(s);
    }
    
    public String nextCDATA() throws JSONException {
        final StringBuilder sb = new StringBuilder();
        int i;
        do {
            final char c = this.next();
            if (this.end()) {
                throw this.syntaxError("Unclosed CDATA");
            }
            sb.append(c);
            i = sb.length() - 3;
        } while (i < 0 || sb.charAt(i) != ']' || sb.charAt(i + 1) != ']' || sb.charAt(i + 2) != '>');
        sb.setLength(i);
        return sb.toString();
    }
    
    public Object nextContent() throws JSONException {
        char c;
        do {
            c = this.next();
        } while (Character.isWhitespace(c));
        if (c == '\0') {
            return null;
        }
        if (c == '<') {
            return XML.LT;
        }
        final StringBuilder sb = new StringBuilder();
        while (c != '<' && c != '\0') {
            if (c == '&') {
                sb.append(this.nextEntity(c));
            }
            else {
                sb.append(c);
            }
            c = this.next();
        }
        this.back();
        return sb.toString().trim();
    }
    
    public Object nextEntity(final char ampersand) throws JSONException {
        final StringBuilder sb = new StringBuilder();
        char c;
        while (true) {
            c = this.next();
            if (!Character.isLetterOrDigit(c) && c != '#') {
                break;
            }
            sb.append(Character.toLowerCase(c));
        }
        if (c == ';') {
            final String string = sb.toString();
            final Object object = XMLTokener.entity.get(string);
            return (object != null) ? object : (ampersand + string + ";");
        }
        throw this.syntaxError("Missing ';' in XML entity: &" + (Object)sb);
    }
    
    public Object nextMeta() throws JSONException {
        char c;
        do {
            c = this.next();
        } while (Character.isWhitespace(c));
        switch (c) {
            case '\0': {
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
                final char q = c;
                do {
                    c = this.next();
                    if (c == '\0') {
                        throw this.syntaxError("Unterminated string");
                    }
                } while (c != q);
                return Boolean.TRUE;
            }
            default: {
                while (true) {
                    c = this.next();
                    if (Character.isWhitespace(c)) {
                        return Boolean.TRUE;
                    }
                    switch (c) {
                        case '\0':
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
                        default: {
                            continue;
                        }
                    }
                }
                break;
            }
        }
    }
    
    public Object nextToken() throws JSONException {
        char c;
        do {
            c = this.next();
        } while (Character.isWhitespace(c));
        switch (c) {
            case '\0': {
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
                final char q = c;
                final StringBuilder sb = new StringBuilder();
                while (true) {
                    c = this.next();
                    if (c == '\0') {
                        throw this.syntaxError("Unterminated string");
                    }
                    if (c == q) {
                        return sb.toString();
                    }
                    if (c == '&') {
                        sb.append(this.nextEntity(c));
                    }
                    else {
                        sb.append(c);
                    }
                }
                break;
            }
            default: {
                final StringBuilder sb = new StringBuilder();
                while (true) {
                    sb.append(c);
                    c = this.next();
                    if (Character.isWhitespace(c)) {
                        return sb.toString();
                    }
                    switch (c) {
                        case '\0': {
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
                        default: {
                            continue;
                        }
                    }
                }
                break;
            }
        }
    }
    
    public boolean skipPast(final String to) throws JSONException {
        int offset = 0;
        final int length = to.length();
        final char[] circle = new char[length];
        for (int i = 0; i < length; ++i) {
            final char c = this.next();
            if (c == '\0') {
                return false;
            }
            circle[i] = c;
        }
        while (true) {
            int j = offset;
            boolean b = true;
            for (int i = 0; i < length; ++i) {
                if (circle[j] != to.charAt(i)) {
                    b = false;
                    break;
                }
                if (++j >= length) {
                    j -= length;
                }
            }
            if (b) {
                return true;
            }
            final char c = this.next();
            if (c == '\0') {
                return false;
            }
            circle[offset] = c;
            if (++offset < length) {
                continue;
            }
            offset -= length;
        }
    }
    
    static {
        (entity = new HashMap<String, Character>(8)).put("amp", XML.AMP);
        XMLTokener.entity.put("apos", XML.APOS);
        XMLTokener.entity.put("gt", XML.GT);
        XMLTokener.entity.put("lt", XML.LT);
        XMLTokener.entity.put("quot", XML.QUOT);
    }
}
