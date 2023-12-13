/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http;

public class HttpException
extends Exception {
    private static final int FIRST_VALID_CHAR = 32;
    private static final long serialVersionUID = -5437299376222011036L;

    static String clean(String message) {
        int i2;
        char[] chars = message.toCharArray();
        for (i2 = 0; i2 < chars.length && chars[i2] >= ' '; ++i2) {
        }
        if (i2 == chars.length) {
            return message;
        }
        StringBuilder builder = new StringBuilder(chars.length * 2);
        for (i2 = 0; i2 < chars.length; ++i2) {
            char ch = chars[i2];
            if (ch < ' ') {
                builder.append("[0x");
                String hexString = Integer.toHexString(i2);
                if (hexString.length() == 1) {
                    builder.append("0");
                }
                builder.append(hexString);
                builder.append("]");
                continue;
            }
            builder.append(ch);
        }
        return builder.toString();
    }

    public HttpException() {
    }

    public HttpException(String message) {
        super(HttpException.clean(message));
    }

    public HttpException(String message, Throwable cause) {
        super(HttpException.clean(message));
        this.initCause(cause);
    }
}

