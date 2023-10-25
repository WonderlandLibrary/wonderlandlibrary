/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css.parser;

public final class Token {
    public static final int EOF = -1;
    public static final int INVALID = 0;
    public static final int SKIP = 1;
    public static final Token EOF_TOKEN = new Token(-1, "EOF");
    public static final Token INVALID_TOKEN = new Token(0, "INVALID");
    public static final Token SKIP_TOKEN = new Token(1, "SKIP");
    private final String text;
    private int offset;
    private int line;
    private final int type;

    public Token(int n, String string, int n2, int n3) {
        this.type = n;
        this.text = string;
        this.line = n2;
        this.offset = n3;
    }

    public Token(int n, String string) {
        this(n, string, -1, -1);
    }

    Token(int n) {
        this(n, null);
    }

    private Token() {
        this(0, "INVALID");
    }

    public String getText() {
        return this.text;
    }

    public int getType() {
        return this.type;
    }

    public int getLine() {
        return this.line;
    }

    void setLine(int n) {
        this.line = n;
    }

    public int getOffset() {
        return this.offset;
    }

    void setOffset(int n) {
        this.offset = n;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[').append(this.line).append(',').append(this.offset).append(']').append(',').append(this.text).append(",<").append(this.type).append('>');
        return stringBuilder.toString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Token token = (Token)object;
        if (this.type != token.type) {
            return false;
        }
        return !(this.text == null ? token.text != null : !this.text.equals(token.text));
    }

    public int hashCode() {
        int n = 7;
        n = 67 * n + this.type;
        n = 67 * n + (this.text != null ? this.text.hashCode() : 0);
        return n;
    }
}

