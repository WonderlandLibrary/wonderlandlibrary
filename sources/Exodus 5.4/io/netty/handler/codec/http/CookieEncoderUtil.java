/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.util.internal.InternalThreadLocalMap;

final class CookieEncoderUtil {
    static void addUnquoted(StringBuilder stringBuilder, String string, String string2) {
        stringBuilder.append(string);
        stringBuilder.append('=');
        stringBuilder.append(string2);
        stringBuilder.append(';');
        stringBuilder.append(' ');
    }

    static void addQuoted(StringBuilder stringBuilder, String string, String string2) {
        if (string2 == null) {
            string2 = "";
        }
        stringBuilder.append(string);
        stringBuilder.append('=');
        stringBuilder.append('\"');
        stringBuilder.append(string2.replace("\\", "\\\\").replace("\"", "\\\""));
        stringBuilder.append('\"');
        stringBuilder.append(';');
        stringBuilder.append(' ');
    }

    private CookieEncoderUtil() {
    }

    static void add(StringBuilder stringBuilder, String string, String string2) {
        if (string2 == null) {
            CookieEncoderUtil.addQuoted(stringBuilder, string, "");
            return;
        }
        for (int i = 0; i < string2.length(); ++i) {
            char c = string2.charAt(i);
            switch (c) {
                case '\t': 
                case ' ': 
                case '\"': 
                case '(': 
                case ')': 
                case ',': 
                case '/': 
                case ':': 
                case ';': 
                case '<': 
                case '=': 
                case '>': 
                case '?': 
                case '@': 
                case '[': 
                case '\\': 
                case ']': 
                case '{': 
                case '}': {
                    CookieEncoderUtil.addQuoted(stringBuilder, string, string2);
                    return;
                }
            }
        }
        CookieEncoderUtil.addUnquoted(stringBuilder, string, string2);
    }

    static String stripTrailingSeparator(StringBuilder stringBuilder) {
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }
        return stringBuilder.toString();
    }

    static StringBuilder stringBuilder() {
        return InternalThreadLocalMap.get().stringBuilder();
    }

    static void add(StringBuilder stringBuilder, String string, long l) {
        stringBuilder.append(string);
        stringBuilder.append('=');
        stringBuilder.append(l);
        stringBuilder.append(';');
        stringBuilder.append(' ');
    }
}

