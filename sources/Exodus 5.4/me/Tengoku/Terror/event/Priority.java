/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event;

public class Priority {
    public static final byte SECOND = 1;
    public static final byte FIFTH = 4;
    public static final byte[] VALUE_ARRAY;
    public static final byte FIRST = 0;
    public static final byte THIRD = 2;
    public static final byte FOURTH = 3;

    static {
        byte[] byArray = new byte[5];
        byArray[1] = 1;
        byArray[2] = 2;
        byArray[3] = 3;
        byArray[4] = 4;
        VALUE_ARRAY = byArray;
    }
}

