/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.parser;

import jdk.nashorn.internal.parser.TokenKind;
import jdk.nashorn.internal.parser.TokenType;

public final class TokenLookup {
    private static final TokenType[] table = new TokenType[95];
    private static final int tableBase = 32;
    private static final int tableLimit = 126;
    private static final int tableLength = 95;

    private TokenLookup() {
    }

    public static TokenType lookupKeyword(char[] content, int position, int length) {
        assert (table != null) : "Token lookup table is not initialized";
        char first = content[position];
        if ('a' <= first && first <= 'z') {
            int index = first - 32;
            for (TokenType tokenType = table[index]; tokenType != null; tokenType = tokenType.getNext()) {
                int tokenLength = tokenType.getLength();
                if (tokenLength == length) {
                    int i;
                    String name = tokenType.getName();
                    for (i = 0; i < length && content[position + i] == name.charAt(i); ++i) {
                    }
                    if (i != length) continue;
                    return tokenType;
                }
                if (tokenLength < length) break;
            }
        }
        return TokenType.IDENT;
    }

    public static TokenType lookupOperator(char ch0, char ch1, char ch2, char ch3) {
        assert (table != null) : "Token lookup table is not initialized";
        if (' ' < ch0 && ch0 <= '~' && ('a' > ch0 || ch0 > 'z')) {
            int index = ch0 - 32;
            block6: for (TokenType tokenType = table[index]; tokenType != null; tokenType = tokenType.getNext()) {
                String name = tokenType.getName();
                switch (name.length()) {
                    case 1: {
                        return tokenType;
                    }
                    case 2: {
                        if (name.charAt(1) != ch1) continue block6;
                        return tokenType;
                    }
                    case 3: {
                        if (name.charAt(1) != ch1 || name.charAt(2) != ch2) continue block6;
                        return tokenType;
                    }
                    case 4: {
                        if (name.charAt(1) != ch1 || name.charAt(2) != ch2 || name.charAt(3) != ch3) continue block6;
                        return tokenType;
                    }
                }
            }
        }
        return null;
    }

    static {
        for (TokenType tokenType : TokenType.getValues()) {
            TokenType next;
            String name = tokenType.getName();
            if (name == null || tokenType.getKind() == TokenKind.SPECIAL) continue;
            char first = name.charAt(0);
            int index = first - 32;
            assert (index < 95) : "Token name does not fit lookup table";
            int length = tokenType.getLength();
            TokenType prev = null;
            for (next = table[index]; next != null && next.getLength() > length; next = next.getNext()) {
                prev = next;
            }
            tokenType.setNext(next);
            if (prev == null) {
                TokenLookup.table[index] = tokenType;
                continue;
            }
            prev.setNext(tokenType);
        }
    }
}

