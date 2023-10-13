/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.http;

final class CookieEncoderUtil {
    static final ThreadLocal<StringBuilder> buffer = new ThreadLocal<StringBuilder>(){

        @Override
        public StringBuilder get() {
            StringBuilder buf = (StringBuilder)super.get();
            buf.setLength(0);
            return buf;
        }

        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder(512);
        }
    };

    static String stripTrailingSeparator(StringBuilder buf) {
        if (buf.length() > 0) {
            buf.setLength(buf.length() - 2);
        }
        return buf.toString();
    }

    static void add(StringBuilder sb, String name, String val) {
        if (val == null) {
            CookieEncoderUtil.addQuoted(sb, name, "");
            return;
        }
        for (int i = 0; i < val.length(); ++i) {
            char c = val.charAt(i);
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
                    CookieEncoderUtil.addQuoted(sb, name, val);
                    return;
                }
            }
        }
        CookieEncoderUtil.addUnquoted(sb, name, val);
    }

    static void addUnquoted(StringBuilder sb, String name, String val) {
        sb.append(name);
        sb.append('=');
        sb.append(val);
        sb.append(';');
        sb.append(' ');
    }

    static void addQuoted(StringBuilder sb, String name, String val) {
        if (val == null) {
            val = "";
        }
        sb.append(name);
        sb.append('=');
        sb.append('\"');
        sb.append(val.replace("\\", "\\\\").replace("\"", "\\\""));
        sb.append('\"');
        sb.append(';');
        sb.append(' ');
    }

    static void add(StringBuilder sb, String name, long val) {
        sb.append(name);
        sb.append('=');
        sb.append(val);
        sb.append(';');
        sb.append(' ');
    }

    private CookieEncoderUtil() {
    }
}

