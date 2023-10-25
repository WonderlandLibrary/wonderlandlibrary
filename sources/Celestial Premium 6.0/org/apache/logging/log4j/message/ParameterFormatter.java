/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.util.StringBuilders;

final class ParameterFormatter {
    static final String RECURSION_PREFIX = "[...";
    static final String RECURSION_SUFFIX = "...]";
    static final String ERROR_PREFIX = "[!!!";
    static final String ERROR_SEPARATOR = "=>";
    static final String ERROR_MSG_SEPARATOR = ":";
    static final String ERROR_SUFFIX = "!!!]";
    private static final char DELIM_START = '{';
    private static final char DELIM_STOP = '}';
    private static final char ESCAPE_CHAR = '\\';
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_REF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

    private ParameterFormatter() {
    }

    static int countArgumentPlaceholders(String messagePattern) {
        if (messagePattern == null) {
            return 0;
        }
        int length = messagePattern.length();
        int result = 0;
        boolean isEscaped = false;
        for (int i = 0; i < length - 1; ++i) {
            char curChar = messagePattern.charAt(i);
            if (curChar == '\\') {
                isEscaped = !isEscaped;
                continue;
            }
            if (curChar == '{') {
                if (!isEscaped && messagePattern.charAt(i + 1) == '}') {
                    ++result;
                    ++i;
                }
                isEscaped = false;
                continue;
            }
            isEscaped = false;
        }
        return result;
    }

    static int countArgumentPlaceholders2(String messagePattern, int[] indices) {
        if (messagePattern == null) {
            return 0;
        }
        int length = messagePattern.length();
        int result = 0;
        boolean isEscaped = false;
        for (int i = 0; i < length - 1; ++i) {
            char curChar = messagePattern.charAt(i);
            if (curChar == '\\') {
                isEscaped = !isEscaped;
                indices[0] = -1;
                ++result;
                continue;
            }
            if (curChar == '{') {
                if (!isEscaped && messagePattern.charAt(i + 1) == '}') {
                    indices[result] = i++;
                    ++result;
                }
                isEscaped = false;
                continue;
            }
            isEscaped = false;
        }
        return result;
    }

    static int countArgumentPlaceholders3(char[] messagePattern, int length, int[] indices) {
        int result = 0;
        boolean isEscaped = false;
        for (int i = 0; i < length - 1; ++i) {
            char curChar = messagePattern[i];
            if (curChar == '\\') {
                isEscaped = !isEscaped;
                continue;
            }
            if (curChar == '{') {
                if (!isEscaped && messagePattern[i + 1] == '}') {
                    indices[result] = i++;
                    ++result;
                }
                isEscaped = false;
                continue;
            }
            isEscaped = false;
        }
        return result;
    }

    static String format(String messagePattern, Object[] arguments) {
        StringBuilder result = new StringBuilder();
        int argCount = arguments == null ? 0 : arguments.length;
        ParameterFormatter.formatMessage(result, messagePattern, arguments, argCount);
        return result.toString();
    }

    static void formatMessage2(StringBuilder buffer, String messagePattern, Object[] arguments, int argCount, int[] indices) {
        if (messagePattern == null || arguments == null || argCount == 0) {
            buffer.append(messagePattern);
            return;
        }
        int previous = 0;
        for (int i = 0; i < argCount; ++i) {
            buffer.append(messagePattern, previous, indices[i]);
            previous = indices[i] + 2;
            ParameterFormatter.recursiveDeepToString(arguments[i], buffer);
        }
        buffer.append(messagePattern, previous, messagePattern.length());
    }

    static void formatMessage3(StringBuilder buffer, char[] messagePattern, int patternLength, Object[] arguments, int argCount, int[] indices) {
        if (messagePattern == null) {
            return;
        }
        if (arguments == null || argCount == 0) {
            buffer.append(messagePattern);
            return;
        }
        int previous = 0;
        for (int i = 0; i < argCount; ++i) {
            buffer.append(messagePattern, previous, indices[i]);
            previous = indices[i] + 2;
            ParameterFormatter.recursiveDeepToString(arguments[i], buffer);
        }
        buffer.append(messagePattern, previous, patternLength);
    }

    static void formatMessage(StringBuilder buffer, String messagePattern, Object[] arguments, int argCount) {
        int i;
        if (messagePattern == null || arguments == null || argCount == 0) {
            buffer.append(messagePattern);
            return;
        }
        int escapeCounter = 0;
        int currentArgument = 0;
        int len = messagePattern.length();
        for (i = 0; i < len - 1; ++i) {
            char curChar = messagePattern.charAt(i);
            if (curChar == '\\') {
                ++escapeCounter;
                continue;
            }
            if (ParameterFormatter.isDelimPair(curChar, messagePattern, i)) {
                ++i;
                ParameterFormatter.writeEscapedEscapeChars(escapeCounter, buffer);
                if (ParameterFormatter.isOdd(escapeCounter)) {
                    ParameterFormatter.writeDelimPair(buffer);
                } else {
                    ParameterFormatter.writeArgOrDelimPair(arguments, argCount, currentArgument, buffer);
                    ++currentArgument;
                }
            } else {
                ParameterFormatter.handleLiteralChar(buffer, escapeCounter, curChar);
            }
            escapeCounter = 0;
        }
        ParameterFormatter.handleRemainingCharIfAny(messagePattern, len, buffer, escapeCounter, i);
    }

    private static boolean isDelimPair(char curChar, String messagePattern, int curCharIndex) {
        return curChar == '{' && messagePattern.charAt(curCharIndex + 1) == '}';
    }

    private static void handleRemainingCharIfAny(String messagePattern, int len, StringBuilder buffer, int escapeCounter, int i) {
        if (i == len - 1) {
            char curChar = messagePattern.charAt(i);
            ParameterFormatter.handleLastChar(buffer, escapeCounter, curChar);
        }
    }

    private static void handleLastChar(StringBuilder buffer, int escapeCounter, char curChar) {
        if (curChar == '\\') {
            ParameterFormatter.writeUnescapedEscapeChars(escapeCounter + 1, buffer);
        } else {
            ParameterFormatter.handleLiteralChar(buffer, escapeCounter, curChar);
        }
    }

    private static void handleLiteralChar(StringBuilder buffer, int escapeCounter, char curChar) {
        ParameterFormatter.writeUnescapedEscapeChars(escapeCounter, buffer);
        buffer.append(curChar);
    }

    private static void writeDelimPair(StringBuilder buffer) {
        buffer.append('{');
        buffer.append('}');
    }

    private static boolean isOdd(int number) {
        return (number & 1) == 1;
    }

    private static void writeEscapedEscapeChars(int escapeCounter, StringBuilder buffer) {
        int escapedEscapes = escapeCounter >> 1;
        ParameterFormatter.writeUnescapedEscapeChars(escapedEscapes, buffer);
    }

    private static void writeUnescapedEscapeChars(int escapeCounter, StringBuilder buffer) {
        while (escapeCounter > 0) {
            buffer.append('\\');
            --escapeCounter;
        }
    }

    private static void writeArgOrDelimPair(Object[] arguments, int argCount, int currentArgument, StringBuilder buffer) {
        if (currentArgument < argCount) {
            ParameterFormatter.recursiveDeepToString(arguments[currentArgument], buffer);
        } else {
            ParameterFormatter.writeDelimPair(buffer);
        }
    }

    static String deepToString(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return (String)o;
        }
        if (o instanceof Integer) {
            return Integer.toString((Integer)o);
        }
        if (o instanceof Long) {
            return Long.toString((Long)o);
        }
        if (o instanceof Double) {
            return Double.toString((Double)o);
        }
        if (o instanceof Boolean) {
            return Boolean.toString((Boolean)o);
        }
        if (o instanceof Character) {
            return Character.toString(((Character)o).charValue());
        }
        if (o instanceof Short) {
            return Short.toString((Short)o);
        }
        if (o instanceof Float) {
            return Float.toString(((Float)o).floatValue());
        }
        if (o instanceof Byte) {
            return Byte.toString((Byte)o);
        }
        StringBuilder str = new StringBuilder();
        ParameterFormatter.recursiveDeepToString(o, str);
        return str.toString();
    }

    static void recursiveDeepToString(Object o, StringBuilder str) {
        ParameterFormatter.recursiveDeepToString(o, str, null);
    }

    private static void recursiveDeepToString(Object o, StringBuilder str, Set<Object> dejaVu) {
        if (ParameterFormatter.appendSpecialTypes(o, str)) {
            return;
        }
        if (ParameterFormatter.isMaybeRecursive(o)) {
            ParameterFormatter.appendPotentiallyRecursiveValue(o, str, dejaVu);
        } else {
            ParameterFormatter.tryObjectToString(o, str);
        }
    }

    private static boolean appendSpecialTypes(Object o, StringBuilder str) {
        return StringBuilders.appendSpecificTypes(str, o) || ParameterFormatter.appendDate(o, str);
    }

    private static boolean appendDate(Object o, StringBuilder str) {
        if (!(o instanceof Date)) {
            return false;
        }
        Date date = (Date)o;
        SimpleDateFormat format = SIMPLE_DATE_FORMAT_REF.get();
        str.append(format.format(date));
        return true;
    }

    private static boolean isMaybeRecursive(Object o) {
        return o.getClass().isArray() || o instanceof Map || o instanceof Collection;
    }

    private static void appendPotentiallyRecursiveValue(Object o, StringBuilder str, Set<Object> dejaVu) {
        Class<?> oClass = o.getClass();
        if (oClass.isArray()) {
            ParameterFormatter.appendArray(o, str, dejaVu, oClass);
        } else if (o instanceof Map) {
            ParameterFormatter.appendMap(o, str, dejaVu);
        } else if (o instanceof Collection) {
            ParameterFormatter.appendCollection(o, str, dejaVu);
        } else {
            throw new IllegalArgumentException("was expecting a container, found " + oClass);
        }
    }

    private static void appendArray(Object o, StringBuilder str, Set<Object> dejaVu, Class<?> oClass) {
        if (oClass == byte[].class) {
            str.append(Arrays.toString((byte[])o));
        } else if (oClass == short[].class) {
            str.append(Arrays.toString((short[])o));
        } else if (oClass == int[].class) {
            str.append(Arrays.toString((int[])o));
        } else if (oClass == long[].class) {
            str.append(Arrays.toString((long[])o));
        } else if (oClass == float[].class) {
            str.append(Arrays.toString((float[])o));
        } else if (oClass == double[].class) {
            str.append(Arrays.toString((double[])o));
        } else if (oClass == boolean[].class) {
            str.append(Arrays.toString((boolean[])o));
        } else if (oClass == char[].class) {
            str.append(Arrays.toString((char[])o));
        } else {
            boolean seen;
            Set<Object> effectiveDejaVu = ParameterFormatter.getOrCreateDejaVu(dejaVu);
            boolean bl = seen = !effectiveDejaVu.add(o);
            if (seen) {
                String id = ParameterFormatter.identityToString(o);
                str.append(RECURSION_PREFIX).append(id).append(RECURSION_SUFFIX);
            } else {
                Object[] oArray = (Object[])o;
                str.append('[');
                boolean first = true;
                for (Object current : oArray) {
                    if (first) {
                        first = false;
                    } else {
                        str.append(", ");
                    }
                    ParameterFormatter.recursiveDeepToString(current, str, ParameterFormatter.cloneDejaVu(effectiveDejaVu));
                }
                str.append(']');
            }
        }
    }

    private static void appendMap(Object o, StringBuilder str, Set<Object> dejaVu) {
        boolean seen;
        Set<Object> effectiveDejaVu = ParameterFormatter.getOrCreateDejaVu(dejaVu);
        boolean bl = seen = !effectiveDejaVu.add(o);
        if (seen) {
            String id = ParameterFormatter.identityToString(o);
            str.append(RECURSION_PREFIX).append(id).append(RECURSION_SUFFIX);
        } else {
            Map oMap = (Map)o;
            str.append('{');
            boolean isFirst = true;
            Iterator iterator = oMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry o1;
                Map.Entry current = o1 = iterator.next();
                if (isFirst) {
                    isFirst = false;
                } else {
                    str.append(", ");
                }
                Object key = current.getKey();
                Object value = current.getValue();
                ParameterFormatter.recursiveDeepToString(key, str, ParameterFormatter.cloneDejaVu(effectiveDejaVu));
                str.append('=');
                ParameterFormatter.recursiveDeepToString(value, str, ParameterFormatter.cloneDejaVu(effectiveDejaVu));
            }
            str.append('}');
        }
    }

    private static void appendCollection(Object o, StringBuilder str, Set<Object> dejaVu) {
        boolean seen;
        Set<Object> effectiveDejaVu = ParameterFormatter.getOrCreateDejaVu(dejaVu);
        boolean bl = seen = !effectiveDejaVu.add(o);
        if (seen) {
            String id = ParameterFormatter.identityToString(o);
            str.append(RECURSION_PREFIX).append(id).append(RECURSION_SUFFIX);
        } else {
            Collection oCol = (Collection)o;
            str.append('[');
            boolean isFirst = true;
            for (Object anOCol : oCol) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    str.append(", ");
                }
                ParameterFormatter.recursiveDeepToString(anOCol, str, ParameterFormatter.cloneDejaVu(effectiveDejaVu));
            }
            str.append(']');
        }
    }

    private static Set<Object> getOrCreateDejaVu(Set<Object> dejaVu) {
        return dejaVu == null ? ParameterFormatter.createDejaVu() : dejaVu;
    }

    private static Set<Object> createDejaVu() {
        return Collections.newSetFromMap(new IdentityHashMap());
    }

    private static Set<Object> cloneDejaVu(Set<Object> dejaVu) {
        Set<Object> clonedDejaVu = ParameterFormatter.createDejaVu();
        clonedDejaVu.addAll(dejaVu);
        return clonedDejaVu;
    }

    private static void tryObjectToString(Object o, StringBuilder str) {
        try {
            str.append(o.toString());
        }
        catch (Throwable t) {
            ParameterFormatter.handleErrorInObjectToString(o, str, t);
        }
    }

    private static void handleErrorInObjectToString(Object o, StringBuilder str, Throwable t) {
        str.append(ERROR_PREFIX);
        str.append(ParameterFormatter.identityToString(o));
        str.append(ERROR_SEPARATOR);
        String msg = t.getMessage();
        String className = t.getClass().getName();
        str.append(className);
        if (!className.equals(msg)) {
            str.append(ERROR_MSG_SEPARATOR);
            str.append(msg);
        }
        str.append(ERROR_SUFFIX);
    }

    static String identityToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(obj));
    }
}

