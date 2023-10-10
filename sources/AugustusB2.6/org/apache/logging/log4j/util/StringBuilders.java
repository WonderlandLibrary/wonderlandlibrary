// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.util.Map;

public final class StringBuilders
{
    private StringBuilders() {
    }
    
    public static StringBuilder appendDqValue(final StringBuilder sb, final Object value) {
        return sb.append('\"').append(value).append('\"');
    }
    
    public static StringBuilder appendKeyDqValue(final StringBuilder sb, final Map.Entry<String, String> entry) {
        return appendKeyDqValue(sb, entry.getKey(), entry.getValue());
    }
    
    public static StringBuilder appendKeyDqValue(final StringBuilder sb, final String key, final Object value) {
        return sb.append(key).append('=').append('\"').append(value).append('\"');
    }
    
    public static void appendValue(final StringBuilder stringBuilder, final Object obj) {
        if (!appendSpecificTypes(stringBuilder, obj)) {
            stringBuilder.append(obj);
        }
    }
    
    public static boolean appendSpecificTypes(final StringBuilder stringBuilder, final Object obj) {
        if (obj == null || obj instanceof String) {
            stringBuilder.append((String)obj);
        }
        else if (obj instanceof StringBuilderFormattable) {
            ((StringBuilderFormattable)obj).formatTo(stringBuilder);
        }
        else if (obj instanceof CharSequence) {
            stringBuilder.append((CharSequence)obj);
        }
        else if (obj instanceof Integer) {
            stringBuilder.append((int)obj);
        }
        else if (obj instanceof Long) {
            stringBuilder.append((long)obj);
        }
        else if (obj instanceof Double) {
            stringBuilder.append((double)obj);
        }
        else if (obj instanceof Boolean) {
            stringBuilder.append((boolean)obj);
        }
        else if (obj instanceof Character) {
            stringBuilder.append((char)obj);
        }
        else if (obj instanceof Short) {
            stringBuilder.append((short)obj);
        }
        else if (obj instanceof Float) {
            stringBuilder.append((float)obj);
        }
        else {
            if (!(obj instanceof Byte)) {
                return false;
            }
            stringBuilder.append((byte)obj);
        }
        return true;
    }
    
    public static boolean equals(final CharSequence left, final int leftOffset, final int leftLength, final CharSequence right, final int rightOffset, final int rightLength) {
        if (leftLength == rightLength) {
            for (int i = 0; i < rightLength; ++i) {
                if (left.charAt(i + leftOffset) != right.charAt(i + rightOffset)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public static boolean equalsIgnoreCase(final CharSequence left, final int leftOffset, final int leftLength, final CharSequence right, final int rightOffset, final int rightLength) {
        if (leftLength == rightLength) {
            for (int i = 0; i < rightLength; ++i) {
                if (Character.toLowerCase(left.charAt(i + leftOffset)) != Character.toLowerCase(right.charAt(i + rightOffset))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public static void trimToMaxSize(final StringBuilder stringBuilder, final int maxSize) {
        if (stringBuilder != null && stringBuilder.capacity() > maxSize) {
            stringBuilder.setLength(maxSize);
            stringBuilder.trimToSize();
        }
    }
    
    public static void escapeJson(final StringBuilder toAppendTo, final int start) {
        int escapeCount = 0;
        for (int i = start; i < toAppendTo.length(); ++i) {
            final char c = toAppendTo.charAt(i);
            switch (c) {
                case '\b':
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case '\"':
                case '\\': {
                    ++escapeCount;
                    break;
                }
                default: {
                    if (Character.isISOControl(c)) {
                        escapeCount += 5;
                        break;
                    }
                    break;
                }
            }
        }
        final int lastChar = toAppendTo.length() - 1;
        toAppendTo.setLength(toAppendTo.length() + escapeCount);
        for (int lastPos = toAppendTo.length() - 1, j = lastChar; lastPos > j; --j) {
            final char c2 = toAppendTo.charAt(j);
            switch (c2) {
                case '\b': {
                    lastPos = escapeAndDecrement(toAppendTo, lastPos, 'b');
                    break;
                }
                case '\t': {
                    lastPos = escapeAndDecrement(toAppendTo, lastPos, 't');
                    break;
                }
                case '\f': {
                    lastPos = escapeAndDecrement(toAppendTo, lastPos, 'f');
                    break;
                }
                case '\n': {
                    lastPos = escapeAndDecrement(toAppendTo, lastPos, 'n');
                    break;
                }
                case '\r': {
                    lastPos = escapeAndDecrement(toAppendTo, lastPos, 'r');
                    break;
                }
                case '\"':
                case '\\': {
                    lastPos = escapeAndDecrement(toAppendTo, lastPos, c2);
                    break;
                }
                default: {
                    if (Character.isISOControl(c2)) {
                        toAppendTo.setCharAt(lastPos--, Chars.getUpperCaseHex(c2 & '\u000f'));
                        toAppendTo.setCharAt(lastPos--, Chars.getUpperCaseHex((c2 & '\u00f0') >> 4));
                        toAppendTo.setCharAt(lastPos--, '0');
                        toAppendTo.setCharAt(lastPos--, '0');
                        toAppendTo.setCharAt(lastPos--, 'u');
                        toAppendTo.setCharAt(lastPos--, '\\');
                        break;
                    }
                    toAppendTo.setCharAt(lastPos, c2);
                    --lastPos;
                    break;
                }
            }
        }
    }
    
    private static int escapeAndDecrement(final StringBuilder toAppendTo, int lastPos, final char c) {
        toAppendTo.setCharAt(lastPos--, c);
        toAppendTo.setCharAt(lastPos--, '\\');
        return lastPos;
    }
    
    public static void escapeXml(final StringBuilder toAppendTo, final int start) {
        int escapeCount = 0;
        for (int i = start; i < toAppendTo.length(); ++i) {
            final char c = toAppendTo.charAt(i);
            switch (c) {
                case '&': {
                    escapeCount += 4;
                    break;
                }
                case '<':
                case '>': {
                    escapeCount += 3;
                    break;
                }
                case '\"':
                case '\'': {
                    escapeCount += 5;
                    break;
                }
            }
        }
        final int lastChar = toAppendTo.length() - 1;
        toAppendTo.setLength(toAppendTo.length() + escapeCount);
        for (int lastPos = toAppendTo.length() - 1, j = lastChar; lastPos > j; --j) {
            final char c2 = toAppendTo.charAt(j);
            switch (c2) {
                case '&': {
                    toAppendTo.setCharAt(lastPos--, ';');
                    toAppendTo.setCharAt(lastPos--, 'p');
                    toAppendTo.setCharAt(lastPos--, 'm');
                    toAppendTo.setCharAt(lastPos--, 'a');
                    toAppendTo.setCharAt(lastPos--, '&');
                    break;
                }
                case '<': {
                    toAppendTo.setCharAt(lastPos--, ';');
                    toAppendTo.setCharAt(lastPos--, 't');
                    toAppendTo.setCharAt(lastPos--, 'l');
                    toAppendTo.setCharAt(lastPos--, '&');
                    break;
                }
                case '>': {
                    toAppendTo.setCharAt(lastPos--, ';');
                    toAppendTo.setCharAt(lastPos--, 't');
                    toAppendTo.setCharAt(lastPos--, 'g');
                    toAppendTo.setCharAt(lastPos--, '&');
                    break;
                }
                case '\"': {
                    toAppendTo.setCharAt(lastPos--, ';');
                    toAppendTo.setCharAt(lastPos--, 't');
                    toAppendTo.setCharAt(lastPos--, 'o');
                    toAppendTo.setCharAt(lastPos--, 'u');
                    toAppendTo.setCharAt(lastPos--, 'q');
                    toAppendTo.setCharAt(lastPos--, '&');
                    break;
                }
                case '\'': {
                    toAppendTo.setCharAt(lastPos--, ';');
                    toAppendTo.setCharAt(lastPos--, 's');
                    toAppendTo.setCharAt(lastPos--, 'o');
                    toAppendTo.setCharAt(lastPos--, 'p');
                    toAppendTo.setCharAt(lastPos--, 'a');
                    toAppendTo.setCharAt(lastPos--, '&');
                    break;
                }
                default: {
                    toAppendTo.setCharAt(lastPos--, c2);
                    break;
                }
            }
        }
    }
}
