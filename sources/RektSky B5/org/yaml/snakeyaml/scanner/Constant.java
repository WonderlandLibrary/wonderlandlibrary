/*
 * Decompiled with CFR 0.152.
 */
package org.yaml.snakeyaml.scanner;

import java.util.Arrays;

public final class Constant {
    private static final String ALPHA_S = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
    private static final String LINEBR_S = "\n\u0085\u2028\u2029";
    private static final String FULL_LINEBR_S = "\r\n\u0085\u2028\u2029";
    private static final String NULL_OR_LINEBR_S = "\u0000\r\n\u0085\u2028\u2029";
    private static final String NULL_BL_LINEBR_S = " \u0000\r\n\u0085\u2028\u2029";
    private static final String NULL_BL_T_LINEBR_S = "\t \u0000\r\n\u0085\u2028\u2029";
    private static final String NULL_BL_T_S = "\u0000 \t";
    private static final String URI_CHARS_S = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_-;/?:@&=+$,_.!~*'()[]%";
    public static final Constant LINEBR = new Constant("\n\u0085\u2028\u2029");
    public static final Constant FULL_LINEBR = new Constant("\r\n\u0085\u2028\u2029");
    public static final Constant NULL_OR_LINEBR = new Constant("\u0000\r\n\u0085\u2028\u2029");
    public static final Constant NULL_BL_LINEBR = new Constant(" \u0000\r\n\u0085\u2028\u2029");
    public static final Constant NULL_BL_T_LINEBR = new Constant("\t \u0000\r\n\u0085\u2028\u2029");
    public static final Constant NULL_BL_T = new Constant("\u0000 \t");
    public static final Constant URI_CHARS = new Constant("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_-;/?:@&=+$,_.!~*'()[]%");
    public static final Constant ALPHA = new Constant("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_");
    private String content;
    boolean[] contains = new boolean[128];
    boolean noASCII = false;

    private Constant(String content) {
        Arrays.fill(this.contains, false);
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < content.length(); ++i2) {
            int c2 = content.codePointAt(i2);
            if (c2 < 128) {
                this.contains[c2] = true;
                continue;
            }
            sb.appendCodePoint(c2);
        }
        if (sb.length() > 0) {
            this.noASCII = true;
            this.content = sb.toString();
        }
    }

    public boolean has(int c2) {
        return c2 < 128 ? this.contains[c2] : this.noASCII && this.content.indexOf(c2, 0) != -1;
    }

    public boolean hasNo(int c2) {
        return !this.has(c2);
    }

    public boolean has(int c2, String additional) {
        return this.has(c2) || additional.indexOf(c2, 0) != -1;
    }

    public boolean hasNo(int c2, String additional) {
        return !this.has(c2, additional);
    }
}

