/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.util.Map;
import org.apache.logging.log4j.util.Chars;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public final class StringBuilders {
    private StringBuilders() {
    }

    public static StringBuilder appendDqValue(StringBuilder sb, Object value) {
        return sb.append('\"').append(value).append('\"');
    }

    public static StringBuilder appendKeyDqValue(StringBuilder sb, Map.Entry<String, String> entry) {
        return StringBuilders.appendKeyDqValue(sb, entry.getKey(), entry.getValue());
    }

    public static StringBuilder appendKeyDqValue(StringBuilder sb, String key, Object value) {
        return sb.append(key).append('=').append('\"').append(value).append('\"');
    }

    public static void appendValue(StringBuilder stringBuilder, Object obj) {
        if (!StringBuilders.appendSpecificTypes(stringBuilder, obj)) {
            stringBuilder.append(obj);
        }
    }

    public static boolean appendSpecificTypes(StringBuilder stringBuilder, Object obj) {
        if (obj == null || obj instanceof String) {
            stringBuilder.append((String)obj);
        } else if (obj instanceof StringBuilderFormattable) {
            ((StringBuilderFormattable)obj).formatTo(stringBuilder);
        } else if (obj instanceof CharSequence) {
            stringBuilder.append((CharSequence)obj);
        } else if (obj instanceof Integer) {
            stringBuilder.append((Integer)obj);
        } else if (obj instanceof Long) {
            stringBuilder.append((Long)obj);
        } else if (obj instanceof Double) {
            stringBuilder.append((Double)obj);
        } else if (obj instanceof Boolean) {
            stringBuilder.append((Boolean)obj);
        } else if (obj instanceof Character) {
            stringBuilder.append(((Character)obj).charValue());
        } else if (obj instanceof Short) {
            stringBuilder.append(((Short)obj).shortValue());
        } else if (obj instanceof Float) {
            stringBuilder.append(((Float)obj).floatValue());
        } else if (obj instanceof Byte) {
            stringBuilder.append(((Byte)obj).byteValue());
        } else {
            return false;
        }
        return true;
    }

    public static boolean equals(CharSequence left, int leftOffset, int leftLength, CharSequence right, int rightOffset, int rightLength) {
        if (leftLength == rightLength) {
            for (int i = 0; i < rightLength; ++i) {
                if (left.charAt(i + leftOffset) == right.charAt(i + rightOffset)) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean equalsIgnoreCase(CharSequence left, int leftOffset, int leftLength, CharSequence right, int rightOffset, int rightLength) {
        if (leftLength == rightLength) {
            for (int i = 0; i < rightLength; ++i) {
                if (Character.toLowerCase(left.charAt(i + leftOffset)) == Character.toLowerCase(right.charAt(i + rightOffset))) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    public static void trimToMaxSize(StringBuilder stringBuilder, int maxSize) {
        if (stringBuilder != null && stringBuilder.capacity() > maxSize) {
            stringBuilder.setLength(maxSize);
            stringBuilder.trimToSize();
        }
    }

    public static void escapeJson(StringBuilder toAppendTo, int start) {
        int escapeCount = 0;
        block11: for (int i = start; i < toAppendTo.length(); ++i) {
            char c = toAppendTo.charAt(i);
            switch (c) {
                case '\b': 
                case '\t': 
                case '\n': 
                case '\f': 
                case '\r': 
                case '\"': 
                case '\\': {
                    ++escapeCount;
                    continue block11;
                }
                default: {
                    if (!Character.isISOControl(c)) continue block11;
                    escapeCount += 5;
                }
            }
        }
        int lastChar = toAppendTo.length() - 1;
        toAppendTo.setLength(toAppendTo.length() + escapeCount);
        int lastPos = toAppendTo.length() - 1;
        block12: for (int i = lastChar; lastPos > i; --i) {
            char c = toAppendTo.charAt(i);
            switch (c) {
                case '\b': {
                    lastPos = StringBuilders.escapeAndDecrement(toAppendTo, lastPos, 'b');
                    continue block12;
                }
                case '\t': {
                    lastPos = StringBuilders.escapeAndDecrement(toAppendTo, lastPos, 't');
                    continue block12;
                }
                case '\f': {
                    lastPos = StringBuilders.escapeAndDecrement(toAppendTo, lastPos, 'f');
                    continue block12;
                }
                case '\n': {
                    lastPos = StringBuilders.escapeAndDecrement(toAppendTo, lastPos, 'n');
                    continue block12;
                }
                case '\r': {
                    lastPos = StringBuilders.escapeAndDecrement(toAppendTo, lastPos, 'r');
                    continue block12;
                }
                case '\"': 
                case '\\': {
                    lastPos = StringBuilders.escapeAndDecrement(toAppendTo, lastPos, c);
                    continue block12;
                }
                default: {
                    if (Character.isISOControl(c)) {
                        toAppendTo.setCharAt(lastPos--, Chars.getUpperCaseHex(c & 0xF));
                        toAppendTo.setCharAt(lastPos--, Chars.getUpperCaseHex((c & 0xF0) >> 4));
                        toAppendTo.setCharAt(lastPos--, '0');
                        toAppendTo.setCharAt(lastPos--, '0');
                        toAppendTo.setCharAt(lastPos--, 'u');
                        toAppendTo.setCharAt(lastPos--, '\\');
                        continue block12;
                    }
                    toAppendTo.setCharAt(lastPos, c);
                    --lastPos;
                }
            }
        }
    }

    private static int escapeAndDecrement(StringBuilder toAppendTo, int lastPos, char c) {
        toAppendTo.setCharAt(lastPos--, c);
        toAppendTo.setCharAt(lastPos--, '\\');
        return lastPos;
    }

    public static void escapeXml(StringBuilder toAppendTo, int start) {
        int escapeCount = 0;
        block12: for (int i = start; i < toAppendTo.length(); ++i) {
            char c = toAppendTo.charAt(i);
            switch (c) {
                case '&': {
                    escapeCount += 4;
                    continue block12;
                }
                case '<': 
                case '>': {
                    escapeCount += 3;
                    continue block12;
                }
                case '\"': 
                case '\'': {
                    escapeCount += 5;
                }
            }
        }
        int lastChar = toAppendTo.length() - 1;
        toAppendTo.setLength(toAppendTo.length() + escapeCount);
        int lastPos = toAppendTo.length() - 1;
        block13: for (int i = lastChar; lastPos > i; --i) {
            char c = toAppendTo.charAt(i);
            switch (c) {
                case '&': {
                    toAppendTo.setCharAt(lastPos--, ';');
                    toAppendTo.setCharAt(lastPos--, 'p');
                    toAppendTo.setCharAt(lastPos--, 'm');
                    toAppendTo.setCharAt(lastPos--, 'a');
                    toAppendTo.setCharAt(lastPos--, '&');
                    continue block13;
                }
                case '<': {
                    toAppendTo.setCharAt(lastPos--, ';');
                    toAppendTo.setCharAt(lastPos--, 't');
                    toAppendTo.setCharAt(lastPos--, 'l');
                    toAppendTo.setCharAt(lastPos--, '&');
                    continue block13;
                }
                case '>': {
                    toAppendTo.setCharAt(lastPos--, ';');
                    toAppendTo.setCharAt(lastPos--, 't');
                    toAppendTo.setCharAt(lastPos--, 'g');
                    toAppendTo.setCharAt(lastPos--, '&');
                    continue block13;
                }
                case '\"': {
                    toAppendTo.setCharAt(lastPos--, ';');
                    toAppendTo.setCharAt(lastPos--, 't');
                    toAppendTo.setCharAt(lastPos--, 'o');
                    toAppendTo.setCharAt(lastPos--, 'u');
                    toAppendTo.setCharAt(lastPos--, 'q');
                    toAppendTo.setCharAt(lastPos--, '&');
                    continue block13;
                }
                case '\'': {
                    toAppendTo.setCharAt(lastPos--, ';');
                    toAppendTo.setCharAt(lastPos--, 's');
                    toAppendTo.setCharAt(lastPos--, 'o');
                    toAppendTo.setCharAt(lastPos--, 'p');
                    toAppendTo.setCharAt(lastPos--, 'a');
                    toAppendTo.setCharAt(lastPos--, '&');
                    continue block13;
                }
                default: {
                    toAppendTo.setCharAt(lastPos--, c);
                }
            }
        }
    }
}

