/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal.logging;

import io.netty.util.internal.logging.FormattingTuple;
import java.util.HashMap;
import java.util.Map;

final class MessageFormatter {
    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    static FormattingTuple format(String messagePattern, Object arg) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg});
    }

    static FormattingTuple format(String messagePattern, Object argA, Object argB) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{argA, argB});
    }

    static Throwable getThrowableCandidate(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }
        Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable)lastEntry;
        }
        return null;
    }

    static FormattingTuple arrayFormat(String messagePattern, Object[] argArray) {
        int L;
        Throwable throwableCandidate = MessageFormatter.getThrowableCandidate(argArray);
        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwableCandidate);
        }
        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }
        int i2 = 0;
        StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);
        for (L = 0; L < argArray.length; ++L) {
            int j2 = messagePattern.indexOf(DELIM_STR, i2);
            if (j2 == -1) {
                if (i2 == 0) {
                    return new FormattingTuple(messagePattern, argArray, throwableCandidate);
                }
                sbuf.append(messagePattern.substring(i2, messagePattern.length()));
                return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
            }
            if (MessageFormatter.isEscapedDelimeter(messagePattern, j2)) {
                if (!MessageFormatter.isDoubleEscaped(messagePattern, j2)) {
                    --L;
                    sbuf.append(messagePattern.substring(i2, j2 - 1));
                    sbuf.append('{');
                    i2 = j2 + 1;
                    continue;
                }
                sbuf.append(messagePattern.substring(i2, j2 - 1));
                MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Void>());
                i2 = j2 + 2;
                continue;
            }
            sbuf.append(messagePattern.substring(i2, j2));
            MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Void>());
            i2 = j2 + 2;
        }
        sbuf.append(messagePattern.substring(i2, messagePattern.length()));
        if (L < argArray.length - 1) {
            return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
        }
        return new FormattingTuple(sbuf.toString(), argArray, null);
    }

    static boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        }
        return messagePattern.charAt(delimeterStartIndex - 1) == '\\';
    }

    static boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        return delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\';
    }

    private static void deeplyAppendParameter(StringBuffer sbuf, Object o2, Map<Object[], Void> seenMap) {
        if (o2 == null) {
            sbuf.append("null");
            return;
        }
        if (!o2.getClass().isArray()) {
            MessageFormatter.safeObjectAppend(sbuf, o2);
        } else if (o2 instanceof boolean[]) {
            MessageFormatter.booleanArrayAppend(sbuf, (boolean[])o2);
        } else if (o2 instanceof byte[]) {
            MessageFormatter.byteArrayAppend(sbuf, (byte[])o2);
        } else if (o2 instanceof char[]) {
            MessageFormatter.charArrayAppend(sbuf, (char[])o2);
        } else if (o2 instanceof short[]) {
            MessageFormatter.shortArrayAppend(sbuf, (short[])o2);
        } else if (o2 instanceof int[]) {
            MessageFormatter.intArrayAppend(sbuf, (int[])o2);
        } else if (o2 instanceof long[]) {
            MessageFormatter.longArrayAppend(sbuf, (long[])o2);
        } else if (o2 instanceof float[]) {
            MessageFormatter.floatArrayAppend(sbuf, (float[])o2);
        } else if (o2 instanceof double[]) {
            MessageFormatter.doubleArrayAppend(sbuf, (double[])o2);
        } else {
            MessageFormatter.objectArrayAppend(sbuf, (Object[])o2, seenMap);
        }
    }

    private static void safeObjectAppend(StringBuffer sbuf, Object o2) {
        try {
            String oAsString = o2.toString();
            sbuf.append(oAsString);
        }
        catch (Throwable t2) {
            System.err.println("SLF4J: Failed toString() invocation on an object of type [" + o2.getClass().getName() + ']');
            t2.printStackTrace();
            sbuf.append("[FAILED toString()]");
        }
    }

    private static void objectArrayAppend(StringBuffer sbuf, Object[] a2, Map<Object[], Void> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a2)) {
            seenMap.put(a2, null);
            int len = a2.length;
            for (int i2 = 0; i2 < len; ++i2) {
                MessageFormatter.deeplyAppendParameter(sbuf, a2[i2], seenMap);
                if (i2 == len - 1) continue;
                sbuf.append(", ");
            }
            seenMap.remove(a2);
        } else {
            sbuf.append("...");
        }
        sbuf.append(']');
    }

    private static void booleanArrayAppend(StringBuffer sbuf, boolean[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; ++i2) {
            sbuf.append(a2[i2]);
            if (i2 == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void byteArrayAppend(StringBuffer sbuf, byte[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; ++i2) {
            sbuf.append(a2[i2]);
            if (i2 == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void charArrayAppend(StringBuffer sbuf, char[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; ++i2) {
            sbuf.append(a2[i2]);
            if (i2 == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void shortArrayAppend(StringBuffer sbuf, short[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; ++i2) {
            sbuf.append(a2[i2]);
            if (i2 == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void intArrayAppend(StringBuffer sbuf, int[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; ++i2) {
            sbuf.append(a2[i2]);
            if (i2 == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void longArrayAppend(StringBuffer sbuf, long[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; ++i2) {
            sbuf.append(a2[i2]);
            if (i2 == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void floatArrayAppend(StringBuffer sbuf, float[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; ++i2) {
            sbuf.append(a2[i2]);
            if (i2 == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void doubleArrayAppend(StringBuffer sbuf, double[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; ++i2) {
            sbuf.append(a2[i2]);
            if (i2 == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private MessageFormatter() {
    }
}

