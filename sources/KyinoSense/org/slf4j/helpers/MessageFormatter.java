/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.helpers;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.Util;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class MessageFormatter {
    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    public static final FormattingTuple format(String messagePattern, Object arg) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg});
    }

    public static final FormattingTuple format(String messagePattern, Object arg1, Object arg2) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg1, arg2});
    }

    static final Throwable getThrowableCandidate(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }
        Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable)lastEntry;
        }
        return null;
    }

    public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray) {
        Throwable throwableCandidate = MessageFormatter.getThrowableCandidate(argArray);
        Object[] args2 = argArray;
        if (throwableCandidate != null) {
            args2 = MessageFormatter.trimmedCopy(argArray);
        }
        return MessageFormatter.arrayFormat(messagePattern, args2, throwableCandidate);
    }

    private static Object[] trimmedCopy(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
        int trimemdLen = argArray.length - 1;
        Object[] trimmed = new Object[trimemdLen];
        System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
        return trimmed;
    }

    public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray, Throwable throwable) {
        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwable);
        }
        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }
        int i = 0;
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        for (int L = 0; L < argArray.length; ++L) {
            int j = messagePattern.indexOf(DELIM_STR, i);
            if (j == -1) {
                if (i == 0) {
                    return new FormattingTuple(messagePattern, argArray, throwable);
                }
                sbuf.append(messagePattern, i, messagePattern.length());
                return new FormattingTuple(sbuf.toString(), argArray, throwable);
            }
            if (MessageFormatter.isEscapedDelimeter(messagePattern, j)) {
                if (!MessageFormatter.isDoubleEscaped(messagePattern, j)) {
                    --L;
                    sbuf.append(messagePattern, i, j - 1);
                    sbuf.append('{');
                    i = j + 1;
                    continue;
                }
                sbuf.append(messagePattern, i, j - 1);
                MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                i = j + 2;
                continue;
            }
            sbuf.append(messagePattern, i, j);
            MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
            i = j + 2;
        }
        sbuf.append(messagePattern, i, messagePattern.length());
        return new FormattingTuple(sbuf.toString(), argArray, throwable);
    }

    static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        return potentialEscape == '\\';
    }

    static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        return delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\';
    }

    private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
        if (o == null) {
            sbuf.append("null");
            return;
        }
        if (!o.getClass().isArray()) {
            MessageFormatter.safeObjectAppend(sbuf, o);
        } else if (o instanceof boolean[]) {
            MessageFormatter.booleanArrayAppend(sbuf, (boolean[])o);
        } else if (o instanceof byte[]) {
            MessageFormatter.byteArrayAppend(sbuf, (byte[])o);
        } else if (o instanceof char[]) {
            MessageFormatter.charArrayAppend(sbuf, (char[])o);
        } else if (o instanceof short[]) {
            MessageFormatter.shortArrayAppend(sbuf, (short[])o);
        } else if (o instanceof int[]) {
            MessageFormatter.intArrayAppend(sbuf, (int[])o);
        } else if (o instanceof long[]) {
            MessageFormatter.longArrayAppend(sbuf, (long[])o);
        } else if (o instanceof float[]) {
            MessageFormatter.floatArrayAppend(sbuf, (float[])o);
        } else if (o instanceof double[]) {
            MessageFormatter.doubleArrayAppend(sbuf, (double[])o);
        } else {
            MessageFormatter.objectArrayAppend(sbuf, (Object[])o, seenMap);
        }
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        }
        catch (Throwable t) {
            Util.report("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + "]", t);
            sbuf.append("[FAILED toString()]");
        }
    }

    private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            int len = a.length;
            for (int i = 0; i < len; ++i) {
                MessageFormatter.deeplyAppendParameter(sbuf, a[i], seenMap);
                if (i == len - 1) continue;
                sbuf.append(", ");
            }
            seenMap.remove(a);
        } else {
            sbuf.append("...");
        }
        sbuf.append(']');
    }

    private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void charArrayAppend(StringBuilder sbuf, char[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void intArrayAppend(StringBuilder sbuf, int[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void longArrayAppend(StringBuilder sbuf, long[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }
}

