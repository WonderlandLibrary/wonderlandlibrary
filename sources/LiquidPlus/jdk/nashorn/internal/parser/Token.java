/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.parser;

import jdk.nashorn.internal.parser.TokenKind;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.runtime.Source;

public class Token {
    private Token() {
    }

    public static long toDesc(TokenType type, int position, int length) {
        return (long)position << 32 | (long)length << 8 | (long)type.ordinal();
    }

    public static int descPosition(long token) {
        return (int)(token >>> 32);
    }

    public static long withDelimiter(long token) {
        TokenType tokenType = Token.descType(token);
        switch (tokenType) {
            case STRING: 
            case ESCSTRING: 
            case EXECSTRING: {
                int start = Token.descPosition(token) - 1;
                int len = Token.descLength(token) + 2;
                return Token.toDesc(tokenType, start, len);
            }
        }
        return token;
    }

    public static int descLength(long token) {
        return (int)token >>> 8;
    }

    public static TokenType descType(long token) {
        return TokenType.getValues()[(int)token & 0xFF];
    }

    public static long recast(long token, TokenType newType) {
        return token & 0xFFFFFFFFFFFFFF00L | (long)newType.ordinal();
    }

    public static String toString(Source source, long token, boolean verbose) {
        TokenType type = Token.descType(token);
        String result = source != null && type.getKind() == TokenKind.LITERAL ? source.getString(token) : type.getNameOrType();
        if (verbose) {
            int position = Token.descPosition(token);
            int length = Token.descLength(token);
            result = result + " (" + position + ", " + length + ")";
        }
        return result;
    }

    public static String toString(Source source, long token) {
        return Token.toString(source, token, false);
    }

    public static String toString(long token) {
        return Token.toString(null, token, false);
    }

    public static int hashCode(long token) {
        return (int)(token ^ token >>> 32);
    }
}

