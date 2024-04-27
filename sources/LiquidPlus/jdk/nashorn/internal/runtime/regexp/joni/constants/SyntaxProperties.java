/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni.constants;

public interface SyntaxProperties {
    public static final int OP_VARIABLE_META_CHARACTERS = 1;
    public static final int OP_DOT_ANYCHAR = 2;
    public static final int OP_ASTERISK_ZERO_INF = 4;
    public static final int OP_ESC_ASTERISK_ZERO_INF = 8;
    public static final int OP_PLUS_ONE_INF = 16;
    public static final int OP_ESC_PLUS_ONE_INF = 32;
    public static final int OP_QMARK_ZERO_ONE = 64;
    public static final int OP_ESC_QMARK_ZERO_ONE = 128;
    public static final int OP_BRACE_INTERVAL = 256;
    public static final int OP_ESC_BRACE_INTERVAL = 512;
    public static final int OP_VBAR_ALT = 1024;
    public static final int OP_ESC_VBAR_ALT = 2048;
    public static final int OP_LPAREN_SUBEXP = 4096;
    public static final int OP_ESC_LPAREN_SUBEXP = 8192;
    public static final int OP_ESC_AZ_BUF_ANCHOR = 16384;
    public static final int OP_ESC_CAPITAL_G_BEGIN_ANCHOR = 32768;
    public static final int OP_DECIMAL_BACKREF = 65536;
    public static final int OP_BRACKET_CC = 131072;
    public static final int OP_ESC_W_WORD = 262144;
    public static final int OP_ESC_LTGT_WORD_BEGIN_END = 524288;
    public static final int OP_ESC_B_WORD_BOUND = 0x100000;
    public static final int OP_ESC_S_WHITE_SPACE = 0x200000;
    public static final int OP_ESC_D_DIGIT = 0x400000;
    public static final int OP_LINE_ANCHOR = 0x800000;
    public static final int OP_POSIX_BRACKET = 0x1000000;
    public static final int OP_QMARK_NON_GREEDY = 0x2000000;
    public static final int OP_ESC_CONTROL_CHARS = 0x4000000;
    public static final int OP_ESC_C_CONTROL = 0x8000000;
    public static final int OP_ESC_OCTAL3 = 0x10000000;
    public static final int OP_ESC_X_HEX2 = 0x20000000;
    public static final int OP_ESC_X_BRACE_HEX8 = 0x40000000;
    public static final int OP2_ESC_CAPITAL_Q_QUOTE = 1;
    public static final int OP2_QMARK_GROUP_EFFECT = 2;
    public static final int OP2_OPTION_PERL = 4;
    public static final int OP2_OPTION_RUBY = 8;
    public static final int OP2_PLUS_POSSESSIVE_REPEAT = 16;
    public static final int OP2_PLUS_POSSESSIVE_INTERVAL = 32;
    public static final int OP2_CCLASS_SET_OP = 64;
    public static final int OP2_QMARK_LT_NAMED_GROUP = 128;
    public static final int OP2_ESC_K_NAMED_BACKREF = 256;
    public static final int OP2_ESC_G_SUBEXP_CALL = 512;
    public static final int OP2_ATMARK_CAPTURE_HISTORY = 1024;
    public static final int OP2_ESC_CAPITAL_C_BAR_CONTROL = 2048;
    public static final int OP2_ESC_CAPITAL_M_BAR_META = 4096;
    public static final int OP2_ESC_V_VTAB = 8192;
    public static final int OP2_ESC_U_HEX4 = 16384;
    public static final int OP2_ESC_GNU_BUF_ANCHOR = 32768;
    public static final int OP2_ESC_P_BRACE_CHAR_PROPERTY = 65536;
    public static final int OP2_ESC_P_BRACE_CIRCUMFLEX_NOT = 131072;
    public static final int OP2_ESC_H_XDIGIT = 524288;
    public static final int OP2_INEFFECTIVE_ESCAPE = 0x100000;
    public static final int CONTEXT_INDEP_ANCHORS = Integer.MIN_VALUE;
    public static final int CONTEXT_INDEP_REPEAT_OPS = 1;
    public static final int CONTEXT_INVALID_REPEAT_OPS = 2;
    public static final int ALLOW_UNMATCHED_CLOSE_SUBEXP = 4;
    public static final int ALLOW_INVALID_INTERVAL = 8;
    public static final int ALLOW_INTERVAL_LOW_ABBREV = 16;
    public static final int STRICT_CHECK_BACKREF = 32;
    public static final int DIFFERENT_LEN_ALT_LOOK_BEHIND = 64;
    public static final int CAPTURE_ONLY_NAMED_GROUP = 128;
    public static final int ALLOW_MULTIPLEX_DEFINITION_NAME = 256;
    public static final int FIXED_INTERVAL_IS_GREEDY_ONLY = 512;
    public static final int NOT_NEWLINE_IN_NEGATIVE_CC = 0x100000;
    public static final int BACKSLASH_ESCAPE_IN_CC = 0x200000;
    public static final int ALLOW_EMPTY_RANGE_IN_CC = 0x400000;
    public static final int ALLOW_DOUBLE_RANGE_OP_IN_CC = 0x800000;
    public static final int WARN_CC_OP_NOT_ESCAPED = 0x1000000;
    public static final int WARN_REDUNDANT_NESTED_REPEAT = 0x2000000;
    public static final int POSIX_COMMON_OP = 92471302;
    public static final int GNU_REGEX_OP = 33543510;
    public static final int GNU_REGEX_BV = -2136997877;
}

