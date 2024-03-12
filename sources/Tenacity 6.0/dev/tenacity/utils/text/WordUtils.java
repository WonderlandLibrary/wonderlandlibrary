// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text;

import java.util.HashSet;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class WordUtils
{
    public static String wrap(final String str, final int wrapLength) {
        return wrap(str, wrapLength, null, false);
    }
    
    public static String wrap(final String str, final int wrapLength, final String newLineStr, final boolean wrapLongWords) {
        return wrap(str, wrapLength, newLineStr, wrapLongWords, " ");
    }
    
    public static String wrap(final String str, int wrapLength, String newLineStr, final boolean wrapLongWords, String wrapOn) {
        if (str == null) {
            return null;
        }
        if (newLineStr == null) {
            newLineStr = System.lineSeparator();
        }
        if (wrapLength < 1) {
            wrapLength = 1;
        }
        if (StringUtils.isBlank((CharSequence)wrapOn)) {
            wrapOn = " ";
        }
        final Pattern patternToWrapOn = Pattern.compile(wrapOn);
        final int inputLineLength = str.length();
        int offset = 0;
        final StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);
        int matcherSize = -1;
        while (offset < inputLineLength) {
            int spaceToWrapAt = -1;
            Matcher matcher = patternToWrapOn.matcher(str.substring(offset, Math.min((int)Math.min(2147483647L, offset + wrapLength + 1L), inputLineLength)));
            if (matcher.find()) {
                if (matcher.start() == 0) {
                    matcherSize = matcher.end() - matcher.start();
                    if (matcherSize != 0) {
                        offset += matcher.end();
                        continue;
                    }
                    ++offset;
                }
                spaceToWrapAt = matcher.start() + offset;
            }
            if (inputLineLength - offset <= wrapLength) {
                break;
            }
            while (matcher.find()) {
                spaceToWrapAt = matcher.start() + offset;
            }
            if (spaceToWrapAt >= offset) {
                wrappedLine.append(str, offset, spaceToWrapAt);
                wrappedLine.append(newLineStr);
                offset = spaceToWrapAt + 1;
            }
            else if (wrapLongWords) {
                if (matcherSize == 0) {
                    --offset;
                }
                wrappedLine.append(str, offset, wrapLength + offset);
                wrappedLine.append(newLineStr);
                offset += wrapLength;
                matcherSize = -1;
            }
            else {
                matcher = patternToWrapOn.matcher(str.substring(offset + wrapLength));
                if (matcher.find()) {
                    matcherSize = matcher.end() - matcher.start();
                    spaceToWrapAt = matcher.start() + offset + wrapLength;
                }
                if (spaceToWrapAt >= 0) {
                    if (matcherSize == 0 && offset != 0) {
                        --offset;
                    }
                    wrappedLine.append(str, offset, spaceToWrapAt);
                    wrappedLine.append(newLineStr);
                    offset = spaceToWrapAt + 1;
                }
                else {
                    if (matcherSize == 0 && offset != 0) {
                        --offset;
                    }
                    wrappedLine.append(str, offset, str.length());
                    offset = inputLineLength;
                    matcherSize = -1;
                }
            }
        }
        if (matcherSize == 0 && offset < inputLineLength) {
            --offset;
        }
        wrappedLine.append(str, offset, str.length());
        return wrappedLine.toString();
    }
    
    public static String capitalize(final String str) {
        return capitalize(str, (char[])null);
    }
    
    public static String capitalize(final String str, final char... delimiters) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return str;
        }
        final Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        boolean capitalizeNext = true;
        int index = 0;
        while (index < strLen) {
            final int codePoint = str.codePointAt(index);
            if (delimiterSet.contains(codePoint)) {
                capitalizeNext = true;
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
            else if (capitalizeNext) {
                final int titleCaseCodePoint = Character.toTitleCase(codePoint);
                newCodePoints[outOffset++] = titleCaseCodePoint;
                index += Character.charCount(titleCaseCodePoint);
                capitalizeNext = false;
            }
            else {
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
        }
        return new String(newCodePoints, 0, outOffset);
    }
    
    public static String capitalizeFully(final String str) {
        return capitalizeFully(str, (char[])null);
    }
    
    public static String capitalizeFully(String str, final char... delimiters) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return str;
        }
        str = str.toLowerCase();
        return capitalize(str, delimiters);
    }
    
    public static String uncapitalize(final String str) {
        return uncapitalize(str, (char[])null);
    }
    
    public static String uncapitalize(final String str, final char... delimiters) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return str;
        }
        final Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        boolean uncapitalizeNext = true;
        int index = 0;
        while (index < strLen) {
            final int codePoint = str.codePointAt(index);
            if (delimiterSet.contains(codePoint)) {
                uncapitalizeNext = true;
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
            else if (uncapitalizeNext) {
                final int titleCaseCodePoint = Character.toLowerCase(codePoint);
                newCodePoints[outOffset++] = titleCaseCodePoint;
                index += Character.charCount(titleCaseCodePoint);
                uncapitalizeNext = false;
            }
            else {
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
        }
        return new String(newCodePoints, 0, outOffset);
    }
    
    public static String swapCase(final String str) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return str;
        }
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        boolean whitespace = true;
        int newCodePoint;
        for (int index = 0; index < strLen; index += Character.charCount(newCodePoint)) {
            final int oldCodepoint = str.codePointAt(index);
            if (Character.isUpperCase(oldCodepoint) || Character.isTitleCase(oldCodepoint)) {
                newCodePoint = Character.toLowerCase(oldCodepoint);
                whitespace = false;
            }
            else if (Character.isLowerCase(oldCodepoint)) {
                if (whitespace) {
                    newCodePoint = Character.toTitleCase(oldCodepoint);
                    whitespace = false;
                }
                else {
                    newCodePoint = Character.toUpperCase(oldCodepoint);
                }
            }
            else {
                whitespace = Character.isWhitespace(oldCodepoint);
                newCodePoint = oldCodepoint;
            }
            newCodePoints[outOffset++] = newCodePoint;
        }
        return new String(newCodePoints, 0, outOffset);
    }
    
    public static String initials(final String str) {
        return initials(str, (char[])null);
    }
    
    public static String initials(final String str, final char... delimiters) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return str;
        }
        if (delimiters != null && delimiters.length == 0) {
            return "";
        }
        final Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen / 2 + 1];
        int count = 0;
        boolean lastWasGap = true;
        int codePoint;
        for (int i = 0; i < strLen; i += Character.charCount(codePoint)) {
            codePoint = str.codePointAt(i);
            if (delimiterSet.contains(codePoint) || (delimiters == null && Character.isWhitespace(codePoint))) {
                lastWasGap = true;
            }
            else if (lastWasGap) {
                newCodePoints[count++] = codePoint;
                lastWasGap = false;
            }
        }
        return new String(newCodePoints, 0, count);
    }
    
    public static boolean containsAllWords(final CharSequence word, final CharSequence... words) {
        if (StringUtils.isEmpty(word) || ArrayUtils.isEmpty((Object[])words)) {
            return false;
        }
        for (final CharSequence w : words) {
            if (StringUtils.isBlank(w)) {
                return false;
            }
            final Pattern p = Pattern.compile(".*\\b" + (Object)w + "\\b.*");
            if (!p.matcher(word).matches()) {
                return false;
            }
        }
        return true;
    }
    
    @Deprecated
    public static boolean isDelimiter(final char ch, final char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        }
        for (final char delimiter : delimiters) {
            if (ch == delimiter) {
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public static boolean isDelimiter(final int codePoint, final char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(codePoint);
        }
        for (int index = 0; index < delimiters.length; ++index) {
            final int delimiterCodePoint = Character.codePointAt(delimiters, index);
            if (delimiterCodePoint == codePoint) {
                return true;
            }
        }
        return false;
    }
    
    public static String abbreviate(final String str, int lower, int upper, final String appendToEnd) {
        Validate.isTrue(upper >= -1, "upper value cannot be less than -1", new Object[0]);
        Validate.isTrue(upper >= lower || upper == -1, "upper value is less than lower value", new Object[0]);
        if (StringUtils.isEmpty((CharSequence)str)) {
            return str;
        }
        if (lower > str.length()) {
            lower = str.length();
        }
        if (upper == -1 || upper > str.length()) {
            upper = str.length();
        }
        final StringBuilder result = new StringBuilder();
        final int index = StringUtils.indexOf((CharSequence)str, (CharSequence)" ", lower);
        if (index == -1) {
            result.append(str, 0, upper);
            if (upper != str.length()) {
                result.append(StringUtils.defaultString(appendToEnd));
            }
        }
        else if (index > upper) {
            result.append(str, 0, upper);
            result.append(StringUtils.defaultString(appendToEnd));
        }
        else {
            result.append(str, 0, index);
            result.append(StringUtils.defaultString(appendToEnd));
        }
        return result.toString();
    }
    
    private static Set<Integer> generateDelimiterSet(final char[] delimiters) {
        final Set<Integer> delimiterHashSet = new HashSet<Integer>();
        if (delimiters == null || delimiters.length == 0) {
            if (delimiters == null) {
                delimiterHashSet.add(Character.codePointAt(new char[] { ' ' }, 0));
            }
            return delimiterHashSet;
        }
        for (int index = 0; index < delimiters.length; ++index) {
            delimiterHashSet.add(Character.codePointAt(delimiters, index));
        }
        return delimiterHashSet;
    }
}
