/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni.constants;

public interface AnchorType {
    public static final int BEGIN_BUF = 1;
    public static final int BEGIN_LINE = 2;
    public static final int BEGIN_POSITION = 4;
    public static final int END_BUF = 8;
    public static final int SEMI_END_BUF = 16;
    public static final int END_LINE = 32;
    public static final int WORD_BOUND = 64;
    public static final int NOT_WORD_BOUND = 128;
    public static final int WORD_BEGIN = 256;
    public static final int WORD_END = 512;
    public static final int PREC_READ = 1024;
    public static final int PREC_READ_NOT = 2048;
    public static final int LOOK_BEHIND = 4096;
    public static final int LOOK_BEHIND_NOT = 8192;
    public static final int ANYCHAR_STAR = 16384;
    public static final int ANYCHAR_STAR_ML = 32768;
    public static final int ANYCHAR_STAR_MASK = 49152;
    public static final int END_BUF_MASK = 24;
    public static final int ALLOWED_IN_LB = 4135;
    public static final int ALLOWED_IN_LB_NOT = 12327;
}

