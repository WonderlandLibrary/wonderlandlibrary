/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni.constants;

public interface OPSize {
    public static final int OPCODE = 1;
    public static final int RELADDR = 1;
    public static final int ABSADDR = 1;
    public static final int LENGTH = 1;
    public static final int MEMNUM = 1;
    public static final int STATE_CHECK_NUM = 1;
    public static final int REPEATNUM = 1;
    public static final int OPTION = 1;
    public static final int CODE_POINT = 1;
    public static final int POINTER = 1;
    public static final int INDEX = 1;
    public static final int ANYCHAR_STAR = 1;
    public static final int ANYCHAR_STAR_PEEK_NEXT = 2;
    public static final int JUMP = 2;
    public static final int PUSH = 2;
    public static final int POP = 1;
    public static final int PUSH_OR_JUMP_EXACT1 = 3;
    public static final int PUSH_IF_PEEK_NEXT = 3;
    public static final int REPEAT_INC = 2;
    public static final int REPEAT_INC_NG = 2;
    public static final int PUSH_POS = 1;
    public static final int PUSH_POS_NOT = 2;
    public static final int POP_POS = 1;
    public static final int FAIL_POS = 1;
    public static final int SET_OPTION = 2;
    public static final int SET_OPTION_PUSH = 2;
    public static final int FAIL = 1;
    public static final int MEMORY_START = 2;
    public static final int MEMORY_START_PUSH = 2;
    public static final int MEMORY_END_PUSH = 2;
    public static final int MEMORY_END_PUSH_REC = 2;
    public static final int MEMORY_END = 2;
    public static final int MEMORY_END_REC = 2;
    public static final int PUSH_STOP_BT = 1;
    public static final int POP_STOP_BT = 1;
    public static final int NULL_CHECK_START = 2;
    public static final int NULL_CHECK_END = 2;
    public static final int LOOK_BEHIND = 2;
    public static final int PUSH_LOOK_BEHIND_NOT = 3;
    public static final int FAIL_LOOK_BEHIND_NOT = 1;
    public static final int CALL = 2;
    public static final int RETURN = 1;
    public static final int STATE_CHECK = 2;
    public static final int STATE_CHECK_PUSH = 3;
    public static final int STATE_CHECK_PUSH_OR_JUMP = 3;
    public static final int STATE_CHECK_ANYCHAR_STAR = 2;
}

