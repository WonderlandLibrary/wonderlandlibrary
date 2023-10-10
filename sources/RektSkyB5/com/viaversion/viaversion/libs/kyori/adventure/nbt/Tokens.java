/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.kyori.adventure.nbt;

final class Tokens {
    static final char COMPOUND_BEGIN = '{';
    static final char COMPOUND_END = '}';
    static final char COMPOUND_KEY_TERMINATOR = ':';
    static final char ARRAY_BEGIN = '[';
    static final char ARRAY_END = ']';
    static final char ARRAY_SIGNATURE_SEPARATOR = ';';
    static final char VALUE_SEPARATOR = ',';
    static final char SINGLE_QUOTE = '\'';
    static final char DOUBLE_QUOTE = '\"';
    static final char ESCAPE_MARKER = '\\';
    static final char TYPE_BYTE = 'b';
    static final char TYPE_SHORT = 's';
    static final char TYPE_INT = 'i';
    static final char TYPE_LONG = 'l';
    static final char TYPE_FLOAT = 'f';
    static final char TYPE_DOUBLE = 'd';
    static final String LITERAL_TRUE = "true";
    static final String LITERAL_FALSE = "false";
    static final String NEWLINE = System.getProperty("line.separator", "\n");
    static final char EOF = '\u0000';

    private Tokens() {
    }

    static boolean id(char c2) {
        return c2 >= 'a' && c2 <= 'z' || c2 >= 'A' && c2 <= 'Z' || c2 >= '0' && c2 <= '9' || c2 == '-' || c2 == '_' || c2 == '.' || c2 == '+';
    }

    static boolean numeric(char c2) {
        return c2 >= '0' && c2 <= '9' || c2 == '+' || c2 == '-' || c2 == 'e' || c2 == 'E' || c2 == '.';
    }
}

