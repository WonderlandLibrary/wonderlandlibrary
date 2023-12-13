/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

public final class SimpleFormatterImpl {
    private static final int ARG_NUM_LIMIT = 256;
    private static final char LEN1_CHAR = '\u0101';
    private static final char LEN2_CHAR = '\u0102';
    private static final char LEN3_CHAR = '\u0103';
    private static final char SEGMENT_LENGTH_ARGUMENT_CHAR = '\uffff';
    private static final int MAX_SEGMENT_LENGTH = 65279;
    private static final String[][] COMMON_PATTERNS = new String[][]{{"{0} {1}", "\u0002\u0000\u0101 \u0001"}, {"{0} ({1})", "\u0002\u0000\u0102 (\u0001\u0101)"}, {"{0}, {1}", "\u0002\u0000\u0102, \u0001"}, {"{0} \u2013 {1}", "\u0002\u0000\u0103 \u2013 \u0001"}};

    private SimpleFormatterImpl() {
    }

    public static String compileToStringMinMaxArguments(CharSequence pattern, StringBuilder sb, int min, int max) {
        int argCount;
        if (min <= 2 && 2 <= max) {
            for (String[] pair : COMMON_PATTERNS) {
                if (!pair[0].contentEquals(pattern)) continue;
                assert (pair[1].charAt(0) == '\u0002');
                return pair[1];
            }
        }
        int patternLength = pattern.length();
        sb.ensureCapacity(patternLength);
        sb.setLength(1);
        int textLength = 0;
        int maxArg = -1;
        boolean inQuote = false;
        int i2 = 0;
        while (i2 < patternLength) {
            int c2;
            if ((c2 = pattern.charAt(i2++)) == 39) {
                if (i2 < patternLength && (c2 = pattern.charAt(i2)) == 39) {
                    ++i2;
                } else {
                    if (inQuote) {
                        inQuote = false;
                        continue;
                    }
                    if (c2 == 123 || c2 == 125) {
                        ++i2;
                        inQuote = true;
                    } else {
                        c2 = 39;
                    }
                }
            } else if (!inQuote && c2 == 123) {
                int argNumber;
                if (textLength > 0) {
                    sb.setCharAt(sb.length() - textLength - 1, (char)(256 + textLength));
                    textLength = 0;
                }
                if (i2 + 1 < patternLength && 0 <= (argNumber = pattern.charAt(i2) - 48) && argNumber <= 9 && pattern.charAt(i2 + 1) == '}') {
                    i2 += 2;
                } else {
                    int argStart = i2 - 1;
                    argNumber = -1;
                    if (i2 < patternLength) {
                        char c3 = pattern.charAt(i2++);
                        c2 = c3;
                        if ('1' <= c3 && c2 <= 57) {
                            argNumber = c2 - 48;
                            while (i2 < patternLength) {
                                char c4 = pattern.charAt(i2++);
                                c2 = c4;
                                if ('0' <= c4 && c2 <= 57 && (argNumber = argNumber * 10 + (c2 - 48)) < 256) continue;
                            }
                        }
                    }
                    if (argNumber < 0 || c2 != 125) {
                        throw new IllegalArgumentException("Argument syntax error in pattern \"" + pattern + "\" at index " + argStart + ": " + pattern.subSequence(argStart, i2));
                    }
                }
                if (argNumber > maxArg) {
                    maxArg = argNumber;
                }
                sb.append((char)argNumber);
                continue;
            }
            if (textLength == 0) {
                sb.append('\uffff');
            }
            sb.append((char)c2);
            if (++textLength != 65279) continue;
            textLength = 0;
        }
        if (textLength > 0) {
            sb.setCharAt(sb.length() - textLength - 1, (char)(256 + textLength));
        }
        if ((argCount = maxArg + 1) < min) {
            throw new IllegalArgumentException("Fewer than minimum " + min + " arguments in pattern \"" + pattern + "\"");
        }
        if (argCount > max) {
            throw new IllegalArgumentException("More than maximum " + max + " arguments in pattern \"" + pattern + "\"");
        }
        sb.setCharAt(0, (char)argCount);
        return sb.toString();
    }

    public static int getArgumentLimit(String compiledPattern) {
        return compiledPattern.charAt(0);
    }

    public static String formatCompiledPattern(String compiledPattern, CharSequence ... values) {
        return SimpleFormatterImpl.formatAndAppend(compiledPattern, new StringBuilder(), null, values).toString();
    }

    public static String formatRawPattern(String pattern, int min, int max, CharSequence ... values) {
        StringBuilder sb = new StringBuilder();
        String compiledPattern = SimpleFormatterImpl.compileToStringMinMaxArguments(pattern, sb, min, max);
        sb.setLength(0);
        return SimpleFormatterImpl.formatAndAppend(compiledPattern, sb, null, values).toString();
    }

    public static StringBuilder formatAndAppend(String compiledPattern, StringBuilder appendTo, int[] offsets, CharSequence ... values) {
        int valuesLength;
        int n2 = valuesLength = values != null ? values.length : 0;
        if (valuesLength < SimpleFormatterImpl.getArgumentLimit(compiledPattern)) {
            throw new IllegalArgumentException("Too few values.");
        }
        return SimpleFormatterImpl.format(compiledPattern, values, appendTo, null, true, offsets);
    }

    public static StringBuilder formatAndReplace(String compiledPattern, StringBuilder result, int[] offsets, CharSequence ... values) {
        int valuesLength;
        int n2 = valuesLength = values != null ? values.length : 0;
        if (valuesLength < SimpleFormatterImpl.getArgumentLimit(compiledPattern)) {
            throw new IllegalArgumentException("Too few values.");
        }
        int firstArg = -1;
        String resultCopy = null;
        if (SimpleFormatterImpl.getArgumentLimit(compiledPattern) > 0) {
            int i2 = 1;
            while (i2 < compiledPattern.length()) {
                char n3;
                if ((n3 = compiledPattern.charAt(i2++)) < '\u0100') {
                    if (values[n3] != result) continue;
                    if (i2 == 2) {
                        firstArg = n3;
                        continue;
                    }
                    if (resultCopy != null) continue;
                    resultCopy = result.toString();
                    continue;
                }
                i2 += n3 - 256;
            }
        }
        if (firstArg < 0) {
            result.setLength(0);
        }
        return SimpleFormatterImpl.format(compiledPattern, values, result, resultCopy, false, offsets);
    }

    public static String getTextWithNoArguments(String compiledPattern) {
        int capacity = compiledPattern.length() - 1 - SimpleFormatterImpl.getArgumentLimit(compiledPattern);
        StringBuilder sb = new StringBuilder(capacity);
        int i2 = 1;
        while (i2 < compiledPattern.length()) {
            int segmentLength;
            if ((segmentLength = compiledPattern.charAt(i2++) - 256) <= 0) continue;
            int limit = i2 + segmentLength;
            sb.append(compiledPattern, i2, limit);
            i2 = limit;
        }
        return sb.toString();
    }

    private static StringBuilder format(String compiledPattern, CharSequence[] values, StringBuilder result, String resultCopy, boolean forbidResultAsValue, int[] offsets) {
        int i2;
        int offsetsLength;
        if (offsets == null) {
            offsetsLength = 0;
        } else {
            offsetsLength = offsets.length;
            for (i2 = 0; i2 < offsetsLength; ++i2) {
                offsets[i2] = -1;
            }
        }
        i2 = 1;
        while (i2 < compiledPattern.length()) {
            char n2;
            if ((n2 = compiledPattern.charAt(i2++)) < '\u0100') {
                CharSequence value = values[n2];
                if (value == result) {
                    if (forbidResultAsValue) {
                        throw new IllegalArgumentException("Value must not be same object as result");
                    }
                    if (i2 == 2) {
                        if (n2 >= offsetsLength) continue;
                        offsets[n2] = 0;
                        continue;
                    }
                    if (n2 < offsetsLength) {
                        offsets[n2] = result.length();
                    }
                    result.append(resultCopy);
                    continue;
                }
                if (n2 < offsetsLength) {
                    offsets[n2] = result.length();
                }
                result.append(value);
                continue;
            }
            int limit = i2 + (n2 - 256);
            result.append(compiledPattern, i2, limit);
            i2 = limit;
        }
        return result;
    }
}

