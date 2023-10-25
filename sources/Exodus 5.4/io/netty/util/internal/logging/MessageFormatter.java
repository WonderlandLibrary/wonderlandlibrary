/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal.logging;

import io.netty.util.internal.logging.FormattingTuple;
import java.util.HashMap;
import java.util.Map;

final class MessageFormatter {
    static final char DELIM_START = '{';
    private static final char ESCAPE_CHAR = '\\';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";

    private static void longArrayAppend(StringBuffer stringBuffer, long[] lArray) {
        stringBuffer.append('[');
        int n = lArray.length;
        for (int i = 0; i < n; ++i) {
            stringBuffer.append(lArray[i]);
            if (i == n - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append(']');
    }

    private static void booleanArrayAppend(StringBuffer stringBuffer, boolean[] blArray) {
        stringBuffer.append('[');
        int n = blArray.length;
        for (int i = 0; i < n; ++i) {
            stringBuffer.append(blArray[i]);
            if (i == n - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append(']');
    }

    static boolean isEscapedDelimeter(String string, int n) {
        if (n == 0) {
            return false;
        }
        return string.charAt(n - 1) == '\\';
    }

    private static void safeObjectAppend(StringBuffer stringBuffer, Object object) {
        try {
            String string = object.toString();
            stringBuffer.append(string);
        }
        catch (Throwable throwable) {
            System.err.println("SLF4J: Failed toString() invocation on an object of type [" + object.getClass().getName() + ']');
            throwable.printStackTrace();
            stringBuffer.append("[FAILED toString()]");
        }
    }

    private MessageFormatter() {
    }

    static FormattingTuple arrayFormat(String string, Object[] objectArray) {
        int n;
        Throwable throwable = MessageFormatter.getThrowableCandidate(objectArray);
        if (string == null) {
            return new FormattingTuple(null, objectArray, throwable);
        }
        if (objectArray == null) {
            return new FormattingTuple(string);
        }
        int n2 = 0;
        StringBuffer stringBuffer = new StringBuffer(string.length() + 50);
        for (n = 0; n < objectArray.length; ++n) {
            int n3 = string.indexOf(DELIM_STR, n2);
            if (n3 == -1) {
                if (n2 == 0) {
                    return new FormattingTuple(string, objectArray, throwable);
                }
                stringBuffer.append(string.substring(n2, string.length()));
                return new FormattingTuple(stringBuffer.toString(), objectArray, throwable);
            }
            if (MessageFormatter.isEscapedDelimeter(string, n3)) {
                if (!MessageFormatter.isDoubleEscaped(string, n3)) {
                    --n;
                    stringBuffer.append(string.substring(n2, n3 - 1));
                    stringBuffer.append('{');
                    n2 = n3 + 1;
                    continue;
                }
                stringBuffer.append(string.substring(n2, n3 - 1));
                MessageFormatter.deeplyAppendParameter(stringBuffer, objectArray[n], new HashMap<Object[], Void>());
                n2 = n3 + 2;
                continue;
            }
            stringBuffer.append(string.substring(n2, n3));
            MessageFormatter.deeplyAppendParameter(stringBuffer, objectArray[n], new HashMap<Object[], Void>());
            n2 = n3 + 2;
        }
        stringBuffer.append(string.substring(n2, string.length()));
        if (n < objectArray.length - 1) {
            return new FormattingTuple(stringBuffer.toString(), objectArray, throwable);
        }
        return new FormattingTuple(stringBuffer.toString(), objectArray, null);
    }

    private static void shortArrayAppend(StringBuffer stringBuffer, short[] sArray) {
        stringBuffer.append('[');
        int n = sArray.length;
        for (int i = 0; i < n; ++i) {
            stringBuffer.append(sArray[i]);
            if (i == n - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append(']');
    }

    private static void byteArrayAppend(StringBuffer stringBuffer, byte[] byArray) {
        stringBuffer.append('[');
        int n = byArray.length;
        for (int i = 0; i < n; ++i) {
            stringBuffer.append(byArray[i]);
            if (i == n - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append(']');
    }

    static Throwable getThrowableCandidate(Object[] objectArray) {
        if (objectArray == null || objectArray.length == 0) {
            return null;
        }
        Object object = objectArray[objectArray.length - 1];
        if (object instanceof Throwable) {
            return (Throwable)object;
        }
        return null;
    }

    static boolean isDoubleEscaped(String string, int n) {
        return n >= 2 && string.charAt(n - 2) == '\\';
    }

    static FormattingTuple format(String string, Object object) {
        return MessageFormatter.arrayFormat(string, new Object[]{object});
    }

    private static void deeplyAppendParameter(StringBuffer stringBuffer, Object object, Map<Object[], Void> map) {
        if (object == null) {
            stringBuffer.append("null");
            return;
        }
        if (!object.getClass().isArray()) {
            MessageFormatter.safeObjectAppend(stringBuffer, object);
        } else if (object instanceof boolean[]) {
            MessageFormatter.booleanArrayAppend(stringBuffer, (boolean[])object);
        } else if (object instanceof byte[]) {
            MessageFormatter.byteArrayAppend(stringBuffer, (byte[])object);
        } else if (object instanceof char[]) {
            MessageFormatter.charArrayAppend(stringBuffer, (char[])object);
        } else if (object instanceof short[]) {
            MessageFormatter.shortArrayAppend(stringBuffer, (short[])object);
        } else if (object instanceof int[]) {
            MessageFormatter.intArrayAppend(stringBuffer, (int[])object);
        } else if (object instanceof long[]) {
            MessageFormatter.longArrayAppend(stringBuffer, (long[])object);
        } else if (object instanceof float[]) {
            MessageFormatter.floatArrayAppend(stringBuffer, (float[])object);
        } else if (object instanceof double[]) {
            MessageFormatter.doubleArrayAppend(stringBuffer, (double[])object);
        } else {
            MessageFormatter.objectArrayAppend(stringBuffer, (Object[])object, map);
        }
    }

    static FormattingTuple format(String string, Object object, Object object2) {
        return MessageFormatter.arrayFormat(string, new Object[]{object, object2});
    }

    private static void doubleArrayAppend(StringBuffer stringBuffer, double[] dArray) {
        stringBuffer.append('[');
        int n = dArray.length;
        for (int i = 0; i < n; ++i) {
            stringBuffer.append(dArray[i]);
            if (i == n - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append(']');
    }

    private static void intArrayAppend(StringBuffer stringBuffer, int[] nArray) {
        stringBuffer.append('[');
        int n = nArray.length;
        for (int i = 0; i < n; ++i) {
            stringBuffer.append(nArray[i]);
            if (i == n - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append(']');
    }

    private static void objectArrayAppend(StringBuffer stringBuffer, Object[] objectArray, Map<Object[], Void> map) {
        stringBuffer.append('[');
        if (!map.containsKey(objectArray)) {
            map.put(objectArray, null);
            int n = objectArray.length;
            for (int i = 0; i < n; ++i) {
                MessageFormatter.deeplyAppendParameter(stringBuffer, objectArray[i], map);
                if (i == n - 1) continue;
                stringBuffer.append(", ");
            }
            map.remove(objectArray);
        } else {
            stringBuffer.append("...");
        }
        stringBuffer.append(']');
    }

    private static void floatArrayAppend(StringBuffer stringBuffer, float[] fArray) {
        stringBuffer.append('[');
        int n = fArray.length;
        for (int i = 0; i < n; ++i) {
            stringBuffer.append(fArray[i]);
            if (i == n - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append(']');
    }

    private static void charArrayAppend(StringBuffer stringBuffer, char[] cArray) {
        stringBuffer.append('[');
        int n = cArray.length;
        for (int i = 0; i < n; ++i) {
            stringBuffer.append(cArray[i]);
            if (i == n - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append(']');
    }
}

