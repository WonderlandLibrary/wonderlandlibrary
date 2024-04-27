/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni.exception;

public interface ErrorMessages {
    public static final String ERR_INVALID_CODE_POINT_VALUE = "invalid code point value";
    public static final String ERR_TOO_BIG_WIDE_CHAR_VALUE = "too big wide-char value";
    public static final String ERR_TOO_LONG_WIDE_CHAR_VALUE = "too long wide-char value";
    public static final String ERR_PARSER_BUG = "internal parser error (bug)";
    public static final String ERR_UNDEFINED_BYTECODE = "undefined bytecode (bug)";
    public static final String ERR_UNEXPECTED_BYTECODE = "unexpected bytecode (bug)";
    public static final String ERR_END_PATTERN_AT_LEFT_BRACE = "end pattern at left brace";
    public static final String ERR_END_PATTERN_AT_LEFT_BRACKET = "end pattern at left bracket";
    public static final String ERR_EMPTY_CHAR_CLASS = "empty char-class";
    public static final String ERR_PREMATURE_END_OF_CHAR_CLASS = "premature end of char-class";
    public static final String ERR_END_PATTERN_AT_ESCAPE = "end pattern at escape";
    public static final String ERR_END_PATTERN_AT_META = "end pattern at meta";
    public static final String ERR_END_PATTERN_AT_CONTROL = "end pattern at control";
    public static final String ERR_META_CODE_SYNTAX = "invalid meta-code syntax";
    public static final String ERR_CONTROL_CODE_SYNTAX = "invalid control-code syntax";
    public static final String ERR_CHAR_CLASS_VALUE_AT_END_OF_RANGE = "char-class value at end of range";
    public static final String ERR_UNMATCHED_RANGE_SPECIFIER_IN_CHAR_CLASS = "unmatched range specifier in char-class";
    public static final String ERR_TARGET_OF_REPEAT_OPERATOR_NOT_SPECIFIED = "target of repeat operator is not specified";
    public static final String ERR_TARGET_OF_REPEAT_OPERATOR_INVALID = "target of repeat operator is invalid";
    public static final String ERR_UNMATCHED_CLOSE_PARENTHESIS = "unmatched close parenthesis";
    public static final String ERR_END_PATTERN_WITH_UNMATCHED_PARENTHESIS = "end pattern with unmatched parenthesis";
    public static final String ERR_END_PATTERN_IN_GROUP = "end pattern in group";
    public static final String ERR_UNDEFINED_GROUP_OPTION = "undefined group option";
    public static final String ERR_INVALID_POSIX_BRACKET_TYPE = "invalid POSIX bracket type";
    public static final String ERR_INVALID_LOOK_BEHIND_PATTERN = "invalid pattern in look-behind";
    public static final String ERR_INVALID_REPEAT_RANGE_PATTERN = "invalid repeat range {lower,upper}";
    public static final String ERR_TOO_BIG_NUMBER = "too big number";
    public static final String ERR_TOO_BIG_NUMBER_FOR_REPEAT_RANGE = "too big number for repeat range";
    public static final String ERR_UPPER_SMALLER_THAN_LOWER_IN_REPEAT_RANGE = "upper is smaller than lower in repeat range";
    public static final String ERR_EMPTY_RANGE_IN_CHAR_CLASS = "empty range in char class";
    public static final String ERR_TOO_MANY_MULTI_BYTE_RANGES = "too many multibyte code ranges are specified";
    public static final String ERR_TOO_SHORT_MULTI_BYTE_STRING = "too short multibyte code string";
    public static final String ERR_INVALID_BACKREF = "invalid backref number";
    public static final String ERR_NUMBERED_BACKREF_OR_CALL_NOT_ALLOWED = "numbered backref/call is not allowed. (use name)";
    public static final String ERR_EMPTY_GROUP_NAME = "group name is empty";
    public static final String ERR_INVALID_GROUP_NAME = "invalid group name <%n>";
    public static final String ERR_INVALID_CHAR_IN_GROUP_NAME = "invalid char in group number <%n>";
    public static final String ERR_GROUP_NUMBER_OVER_FOR_CAPTURE_HISTORY = "group number is too big for capture history";
    public static final String ERR_INVALID_COMBINATION_OF_OPTIONS = "invalid combination of options";
}

