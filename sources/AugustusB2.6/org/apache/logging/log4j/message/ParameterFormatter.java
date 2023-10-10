// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Date;
import org.apache.logging.log4j.util.StringBuilders;
import java.util.Set;
import java.text.SimpleDateFormat;

final class ParameterFormatter
{
    static final String RECURSION_PREFIX = "[...";
    static final String RECURSION_SUFFIX = "...]";
    static final String ERROR_PREFIX = "[!!!";
    static final String ERROR_SEPARATOR = "=>";
    static final String ERROR_MSG_SEPARATOR = ":";
    static final String ERROR_SUFFIX = "!!!]";
    private static final char DELIM_START = '{';
    private static final char DELIM_STOP = '}';
    private static final char ESCAPE_CHAR = '\\';
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_REF;
    
    private ParameterFormatter() {
    }
    
    static int countArgumentPlaceholders(final String messagePattern) {
        if (messagePattern == null) {
            return 0;
        }
        final int length = messagePattern.length();
        int result = 0;
        boolean isEscaped = false;
        for (int i = 0; i < length - 1; ++i) {
            final char curChar = messagePattern.charAt(i);
            if (curChar == '\\') {
                isEscaped = !isEscaped;
            }
            else if (curChar == '{') {
                if (!isEscaped && messagePattern.charAt(i + 1) == '}') {
                    ++result;
                    ++i;
                }
                isEscaped = false;
            }
            else {
                isEscaped = false;
            }
        }
        return result;
    }
    
    static int countArgumentPlaceholders2(final String messagePattern, final int[] indices) {
        if (messagePattern == null) {
            return 0;
        }
        final int length = messagePattern.length();
        int result = 0;
        boolean isEscaped = false;
        for (int i = 0; i < length - 1; ++i) {
            final char curChar = messagePattern.charAt(i);
            if (curChar == '\\') {
                isEscaped = !isEscaped;
                indices[0] = -1;
                ++result;
            }
            else if (curChar == '{') {
                if (!isEscaped && messagePattern.charAt(i + 1) == '}') {
                    indices[result] = i;
                    ++result;
                    ++i;
                }
                isEscaped = false;
            }
            else {
                isEscaped = false;
            }
        }
        return result;
    }
    
    static int countArgumentPlaceholders3(final char[] messagePattern, final int length, final int[] indices) {
        int result = 0;
        boolean isEscaped = false;
        for (int i = 0; i < length - 1; ++i) {
            final char curChar = messagePattern[i];
            if (curChar == '\\') {
                isEscaped = !isEscaped;
            }
            else if (curChar == '{') {
                if (!isEscaped && messagePattern[i + 1] == '}') {
                    indices[result] = i;
                    ++result;
                    ++i;
                }
                isEscaped = false;
            }
            else {
                isEscaped = false;
            }
        }
        return result;
    }
    
    static String format(final String messagePattern, final Object[] arguments) {
        final StringBuilder result = new StringBuilder();
        final int argCount = (arguments == null) ? 0 : arguments.length;
        formatMessage(result, messagePattern, arguments, argCount);
        return result.toString();
    }
    
    static void formatMessage2(final StringBuilder buffer, final String messagePattern, final Object[] arguments, final int argCount, final int[] indices) {
        if (messagePattern == null || arguments == null || argCount == 0) {
            buffer.append(messagePattern);
            return;
        }
        int previous = 0;
        for (int i = 0; i < argCount; ++i) {
            buffer.append(messagePattern, previous, indices[i]);
            previous = indices[i] + 2;
            recursiveDeepToString(arguments[i], buffer);
        }
        buffer.append(messagePattern, previous, messagePattern.length());
    }
    
    static void formatMessage3(final StringBuilder buffer, final char[] messagePattern, final int patternLength, final Object[] arguments, final int argCount, final int[] indices) {
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
            recursiveDeepToString(arguments[i], buffer);
        }
        buffer.append(messagePattern, previous, patternLength);
    }
    
    static void formatMessage(final StringBuilder buffer, final String messagePattern, final Object[] arguments, final int argCount) {
        if (messagePattern == null || arguments == null || argCount == 0) {
            buffer.append(messagePattern);
            return;
        }
        int escapeCounter = 0;
        int currentArgument = 0;
        int i;
        int len;
        for (i = 0, len = messagePattern.length(); i < len - 1; ++i) {
            final char curChar = messagePattern.charAt(i);
            if (curChar == '\\') {
                ++escapeCounter;
            }
            else {
                if (isDelimPair(curChar, messagePattern, i)) {
                    ++i;
                    writeEscapedEscapeChars(escapeCounter, buffer);
                    if (isOdd(escapeCounter)) {
                        writeDelimPair(buffer);
                    }
                    else {
                        writeArgOrDelimPair(arguments, argCount, currentArgument, buffer);
                        ++currentArgument;
                    }
                }
                else {
                    handleLiteralChar(buffer, escapeCounter, curChar);
                }
                escapeCounter = 0;
            }
        }
        handleRemainingCharIfAny(messagePattern, len, buffer, escapeCounter, i);
    }
    
    private static boolean isDelimPair(final char curChar, final String messagePattern, final int curCharIndex) {
        return curChar == '{' && messagePattern.charAt(curCharIndex + 1) == '}';
    }
    
    private static void handleRemainingCharIfAny(final String messagePattern, final int len, final StringBuilder buffer, final int escapeCounter, final int i) {
        if (i == len - 1) {
            final char curChar = messagePattern.charAt(i);
            handleLastChar(buffer, escapeCounter, curChar);
        }
    }
    
    private static void handleLastChar(final StringBuilder buffer, final int escapeCounter, final char curChar) {
        if (curChar == '\\') {
            writeUnescapedEscapeChars(escapeCounter + 1, buffer);
        }
        else {
            handleLiteralChar(buffer, escapeCounter, curChar);
        }
    }
    
    private static void handleLiteralChar(final StringBuilder buffer, final int escapeCounter, final char curChar) {
        writeUnescapedEscapeChars(escapeCounter, buffer);
        buffer.append(curChar);
    }
    
    private static void writeDelimPair(final StringBuilder buffer) {
        buffer.append('{');
        buffer.append('}');
    }
    
    private static boolean isOdd(final int number) {
        return (number & 0x1) == 0x1;
    }
    
    private static void writeEscapedEscapeChars(final int escapeCounter, final StringBuilder buffer) {
        final int escapedEscapes = escapeCounter >> 1;
        writeUnescapedEscapeChars(escapedEscapes, buffer);
    }
    
    private static void writeUnescapedEscapeChars(int escapeCounter, final StringBuilder buffer) {
        while (escapeCounter > 0) {
            buffer.append('\\');
            --escapeCounter;
        }
    }
    
    private static void writeArgOrDelimPair(final Object[] arguments, final int argCount, final int currentArgument, final StringBuilder buffer) {
        if (currentArgument < argCount) {
            recursiveDeepToString(arguments[currentArgument], buffer);
        }
        else {
            writeDelimPair(buffer);
        }
    }
    
    static String deepToString(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return (String)o;
        }
        if (o instanceof Integer) {
            return Integer.toString((int)o);
        }
        if (o instanceof Long) {
            return Long.toString((long)o);
        }
        if (o instanceof Double) {
            return Double.toString((double)o);
        }
        if (o instanceof Boolean) {
            return Boolean.toString((boolean)o);
        }
        if (o instanceof Character) {
            return Character.toString((char)o);
        }
        if (o instanceof Short) {
            return Short.toString((short)o);
        }
        if (o instanceof Float) {
            return Float.toString((float)o);
        }
        if (o instanceof Byte) {
            return Byte.toString((byte)o);
        }
        final StringBuilder str = new StringBuilder();
        recursiveDeepToString(o, str);
        return str.toString();
    }
    
    static void recursiveDeepToString(final Object o, final StringBuilder str) {
        recursiveDeepToString(o, str, null);
    }
    
    private static void recursiveDeepToString(final Object o, final StringBuilder str, final Set<Object> dejaVu) {
        if (appendSpecialTypes(o, str)) {
            return;
        }
        if (isMaybeRecursive(o)) {
            appendPotentiallyRecursiveValue(o, str, dejaVu);
        }
        else {
            tryObjectToString(o, str);
        }
    }
    
    private static boolean appendSpecialTypes(final Object o, final StringBuilder str) {
        return StringBuilders.appendSpecificTypes(str, o) || appendDate(o, str);
    }
    
    private static boolean appendDate(final Object o, final StringBuilder str) {
        if (!(o instanceof Date)) {
            return false;
        }
        final Date date = (Date)o;
        final SimpleDateFormat format = ParameterFormatter.SIMPLE_DATE_FORMAT_REF.get();
        str.append(format.format(date));
        return true;
    }
    
    private static boolean isMaybeRecursive(final Object o) {
        return o.getClass().isArray() || o instanceof Map || o instanceof Collection;
    }
    
    private static void appendPotentiallyRecursiveValue(final Object o, final StringBuilder str, final Set<Object> dejaVu) {
        final Class<?> oClass = o.getClass();
        if (oClass.isArray()) {
            appendArray(o, str, dejaVu, oClass);
        }
        else if (o instanceof Map) {
            appendMap(o, str, dejaVu);
        }
        else {
            if (!(o instanceof Collection)) {
                throw new IllegalArgumentException("was expecting a container, found " + oClass);
            }
            appendCollection(o, str, dejaVu);
        }
    }
    
    private static void appendArray(final Object o, final StringBuilder str, final Set<Object> dejaVu, final Class<?> oClass) {
        if (oClass == byte[].class) {
            str.append(Arrays.toString((byte[])o));
        }
        else if (oClass == short[].class) {
            str.append(Arrays.toString((short[])o));
        }
        else if (oClass == int[].class) {
            str.append(Arrays.toString((int[])o));
        }
        else if (oClass == long[].class) {
            str.append(Arrays.toString((long[])o));
        }
        else if (oClass == float[].class) {
            str.append(Arrays.toString((float[])o));
        }
        else if (oClass == double[].class) {
            str.append(Arrays.toString((double[])o));
        }
        else if (oClass == boolean[].class) {
            str.append(Arrays.toString((boolean[])o));
        }
        else if (oClass == char[].class) {
            str.append(Arrays.toString((char[])o));
        }
        else {
            final Set<Object> effectiveDejaVu = getOrCreateDejaVu(dejaVu);
            final boolean seen = !effectiveDejaVu.add(o);
            if (seen) {
                final String id = identityToString(o);
                str.append("[...").append(id).append("...]");
            }
            else {
                final Object[] oArray = (Object[])o;
                str.append('[');
                boolean first = true;
                for (final Object current : oArray) {
                    if (first) {
                        first = false;
                    }
                    else {
                        str.append(", ");
                    }
                    recursiveDeepToString(current, str, cloneDejaVu(effectiveDejaVu));
                }
                str.append(']');
            }
        }
    }
    
    private static void appendMap(final Object o, final StringBuilder str, final Set<Object> dejaVu) {
        final Set<Object> effectiveDejaVu = getOrCreateDejaVu(dejaVu);
        final boolean seen = !effectiveDejaVu.add(o);
        if (seen) {
            final String id = identityToString(o);
            str.append("[...").append(id).append("...]");
        }
        else {
            final Map<?, ?> oMap = (Map<?, ?>)o;
            str.append('{');
            boolean isFirst = true;
            for (final Object o2 : oMap.entrySet()) {
                final Map.Entry<?, ?> current = (Map.Entry<?, ?>)o2;
                if (isFirst) {
                    isFirst = false;
                }
                else {
                    str.append(", ");
                }
                final Object key = current.getKey();
                final Object value = current.getValue();
                recursiveDeepToString(key, str, cloneDejaVu(effectiveDejaVu));
                str.append('=');
                recursiveDeepToString(value, str, cloneDejaVu(effectiveDejaVu));
            }
            str.append('}');
        }
    }
    
    private static void appendCollection(final Object o, final StringBuilder str, final Set<Object> dejaVu) {
        final Set<Object> effectiveDejaVu = getOrCreateDejaVu(dejaVu);
        final boolean seen = !effectiveDejaVu.add(o);
        if (seen) {
            final String id = identityToString(o);
            str.append("[...").append(id).append("...]");
        }
        else {
            final Collection<?> oCol = (Collection<?>)o;
            str.append('[');
            boolean isFirst = true;
            for (final Object anOCol : oCol) {
                if (isFirst) {
                    isFirst = false;
                }
                else {
                    str.append(", ");
                }
                recursiveDeepToString(anOCol, str, cloneDejaVu(effectiveDejaVu));
            }
            str.append(']');
        }
    }
    
    private static Set<Object> getOrCreateDejaVu(final Set<Object> dejaVu) {
        return (dejaVu == null) ? createDejaVu() : dejaVu;
    }
    
    private static Set<Object> createDejaVu() {
        return Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
    }
    
    private static Set<Object> cloneDejaVu(final Set<Object> dejaVu) {
        final Set<Object> clonedDejaVu = createDejaVu();
        clonedDejaVu.addAll(dejaVu);
        return clonedDejaVu;
    }
    
    private static void tryObjectToString(final Object o, final StringBuilder str) {
        try {
            str.append(o.toString());
        }
        catch (Throwable t) {
            handleErrorInObjectToString(o, str, t);
        }
    }
    
    private static void handleErrorInObjectToString(final Object o, final StringBuilder str, final Throwable t) {
        str.append("[!!!");
        str.append(identityToString(o));
        str.append("=>");
        final String msg = t.getMessage();
        final String className = t.getClass().getName();
        str.append(className);
        if (!className.equals(msg)) {
            str.append(":");
            str.append(msg);
        }
        str.append("!!!]");
    }
    
    static String identityToString(final Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(obj));
    }
    
    static {
        SIMPLE_DATE_FORMAT_REF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
    }
}
