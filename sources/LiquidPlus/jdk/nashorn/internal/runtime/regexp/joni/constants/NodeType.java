/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni.constants;

public interface NodeType {
    public static final int STR = 0;
    public static final int CCLASS = 1;
    public static final int CTYPE = 2;
    public static final int CANY = 3;
    public static final int BREF = 4;
    public static final int QTFR = 5;
    public static final int ENCLOSE = 6;
    public static final int ANCHOR = 7;
    public static final int LIST = 8;
    public static final int ALT = 9;
    public static final int CALL = 10;
    public static final int BIT_STR = 1;
    public static final int BIT_CCLASS = 2;
    public static final int BIT_CTYPE = 4;
    public static final int BIT_CANY = 8;
    public static final int BIT_BREF = 16;
    public static final int BIT_QTFR = 32;
    public static final int BIT_ENCLOSE = 64;
    public static final int BIT_ANCHOR = 128;
    public static final int BIT_LIST = 256;
    public static final int BIT_ALT = 512;
    public static final int BIT_CALL = 1024;
    public static final int ALLOWED_IN_LB = 2031;
    public static final int SIMPLE = 31;
}

