// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text;

import org.apache.commons.lang3.ArrayUtils;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class CaseUtils
{
    public static String toCamelCase(String str, final boolean capitalizeFirstLetter, final char... delimiters) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return str;
        }
        str = str.toLowerCase();
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        final Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
        boolean capitalizeNext = false;
        if (capitalizeFirstLetter) {
            capitalizeNext = true;
        }
        int index = 0;
        while (index < strLen) {
            final int codePoint = str.codePointAt(index);
            if (delimiterSet.contains(codePoint)) {
                capitalizeNext = (outOffset != 0);
                index += Character.charCount(codePoint);
            }
            else if (capitalizeNext || (outOffset == 0 && capitalizeFirstLetter)) {
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
        if (outOffset != 0) {
            return new String(newCodePoints, 0, outOffset);
        }
        return str;
    }
    
    private static Set<Integer> generateDelimiterSet(final char[] delimiters) {
        final Set<Integer> delimiterHashSet = new HashSet<Integer>();
        delimiterHashSet.add(Character.codePointAt(new char[] { ' ' }, 0));
        if (ArrayUtils.isEmpty(delimiters)) {
            return delimiterHashSet;
        }
        for (int index = 0; index < delimiters.length; ++index) {
            delimiterHashSet.add(Character.codePointAt(delimiters, index));
        }
        return delimiterHashSet;
    }
}
