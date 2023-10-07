/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.constants.TokenType;

final class Token {
    TokenType type;
    boolean escaped;
    int backP;
    private int INT1;
    private int INT2;
    private int INT3;
    private int INT4;

    Token() {
    }

    int getC() {
        return this.INT1;
    }

    void setC(int c) {
        this.INT1 = c;
    }

    int getCode() {
        return this.INT1;
    }

    void setCode(int code) {
        this.INT1 = code;
    }

    int getAnchor() {
        return this.INT1;
    }

    void setAnchor(int anchor) {
        this.INT1 = anchor;
    }

    int getRepeatLower() {
        return this.INT1;
    }

    void setRepeatLower(int lower) {
        this.INT1 = lower;
    }

    int getRepeatUpper() {
        return this.INT2;
    }

    void setRepeatUpper(int upper) {
        this.INT2 = upper;
    }

    boolean getRepeatGreedy() {
        return this.INT3 != 0;
    }

    void setRepeatGreedy(boolean greedy) {
        this.INT3 = greedy ? 1 : 0;
    }

    boolean getRepeatPossessive() {
        return this.INT4 != 0;
    }

    void setRepeatPossessive(boolean possessive) {
        this.INT4 = possessive ? 1 : 0;
    }

    int getBackrefRef() {
        return this.INT2;
    }

    void setBackrefRef(int ref1) {
        this.INT2 = ref1;
    }

    int getPropCType() {
        return this.INT1;
    }

    void setPropCType(int ctype) {
        this.INT1 = ctype;
    }

    boolean getPropNot() {
        return this.INT2 != 0;
    }

    void setPropNot(boolean not) {
        this.INT2 = not ? 1 : 0;
    }
}

