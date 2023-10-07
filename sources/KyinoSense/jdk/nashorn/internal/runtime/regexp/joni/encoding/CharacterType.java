/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni.encoding;

public interface CharacterType {
    public static final int NEWLINE = 0;
    public static final int ALPHA = 1;
    public static final int BLANK = 2;
    public static final int CNTRL = 3;
    public static final int DIGIT = 4;
    public static final int GRAPH = 5;
    public static final int LOWER = 6;
    public static final int PRINT = 7;
    public static final int PUNCT = 8;
    public static final int SPACE = 9;
    public static final int UPPER = 10;
    public static final int XDIGIT = 11;
    public static final int WORD = 12;
    public static final int ALNUM = 13;
    public static final int ASCII = 14;
    public static final int SPECIAL_MASK = 256;
    public static final int S = 265;
    public static final int D = 260;
    public static final int W = 268;
    public static final int LETTER_MASK = 62;
    public static final int ALPHA_MASK = 510;
    public static final int ALNUM_MASK = 1022;
    public static final int WORD_MASK = 8389630;
    public static final int PUNCT_MASK = 1643118592;
    public static final int CNTRL_MASK = 884736;
    public static final int SPACE_MASK = 28672;
    public static final int GRAPH_MASK = 585728;
    public static final int PRINT_MASK = 557056;
}

