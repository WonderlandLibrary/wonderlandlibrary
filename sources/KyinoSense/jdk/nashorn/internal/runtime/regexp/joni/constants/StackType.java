/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni.constants;

public interface StackType {
    public static final int INVALID_STACK_INDEX = -1;
    public static final int ALT = 1;
    public static final int LOOK_BEHIND_NOT = 2;
    public static final int POS_NOT = 3;
    public static final int MEM_START = 256;
    public static final int MEM_END = 33280;
    public static final int REPEAT_INC = 768;
    public static final int STATE_CHECK_MARK = 4096;
    public static final int NULL_CHECK_START = 12288;
    public static final int NULL_CHECK_END = 20480;
    public static final int MEM_END_MARK = 33792;
    public static final int POS = 1280;
    public static final int STOP_BT = 1536;
    public static final int REPEAT = 1792;
    public static final int CALL_FRAME = 2048;
    public static final int RETURN = 2304;
    public static final int VOID = 2560;
    public static final int MASK_POP_USED = 255;
    public static final int MASK_TO_VOID_TARGET = 4351;
    public static final int MASK_MEM_END_OR_MARK = 32768;
}

