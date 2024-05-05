/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni.constants;

public interface NodeStatus {
    public static final int NST_MIN_FIXED = 1;
    public static final int NST_MAX_FIXED = 2;
    public static final int NST_CLEN_FIXED = 4;
    public static final int NST_MARK1 = 8;
    public static final int NST_MARK2 = 16;
    public static final int NST_MEM_BACKREFED = 32;
    public static final int NST_STOP_BT_SIMPLE_REPEAT = 64;
    public static final int NST_RECURSION = 128;
    public static final int NST_CALLED = 256;
    public static final int NST_ADDR_FIXED = 512;
    public static final int NST_NAMED_GROUP = 1024;
    public static final int NST_NAME_REF = 2048;
    public static final int NST_IN_REPEAT = 4096;
    public static final int NST_NEST_LEVEL = 8192;
    public static final int NST_BY_NUMBER = 16384;
}

