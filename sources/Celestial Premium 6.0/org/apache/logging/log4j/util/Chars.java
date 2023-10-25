/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

public final class Chars {
    public static final char CR = '\r';
    public static final char DQUOTE = '\"';
    public static final char EQ = '=';
    public static final char LF = '\n';
    public static final char NUL = '\u0000';
    public static final char QUOTE = '\'';
    public static final char SPACE = ' ';
    public static final char TAB = '\t';

    public static char getUpperCaseHex(int digit) {
        if (digit < 0 || digit >= 16) {
            return '\u0000';
        }
        return digit < 10 ? Chars.getNumericalDigit(digit) : Chars.getUpperCaseAlphaDigit(digit);
    }

    public static char getLowerCaseHex(int digit) {
        if (digit < 0 || digit >= 16) {
            return '\u0000';
        }
        return digit < 10 ? Chars.getNumericalDigit(digit) : Chars.getLowerCaseAlphaDigit(digit);
    }

    private static char getNumericalDigit(int digit) {
        return (char)(48 + digit);
    }

    private static char getUpperCaseAlphaDigit(int digit) {
        return (char)(65 + digit - 10);
    }

    private static char getLowerCaseAlphaDigit(int digit) {
        return (char)(97 + digit - 10);
    }

    private Chars() {
    }
}

