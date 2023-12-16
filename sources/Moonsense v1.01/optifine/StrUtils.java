// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.StringTokenizer;
import java.util.ArrayList;

public class StrUtils
{
    public static boolean equalsMask(final String str, final String mask, final char wildChar, final char wildCharSingle) {
        if (mask == null || str == null) {
            return mask == str;
        }
        if (mask.indexOf(wildChar) < 0) {
            return (mask.indexOf(wildCharSingle) < 0) ? mask.equals(str) : equalsMaskSingle(str, mask, wildCharSingle);
        }
        final ArrayList tokens = new ArrayList();
        final String wildCharStr = new StringBuilder().append(wildChar).toString();
        if (mask.startsWith(wildCharStr)) {
            tokens.add("");
        }
        final StringTokenizer tok = new StringTokenizer(mask, wildCharStr);
        while (tok.hasMoreElements()) {
            tokens.add(tok.nextToken());
        }
        if (mask.endsWith(wildCharStr)) {
            tokens.add("");
        }
        final String startTok = tokens.get(0);
        if (!startsWithMaskSingle(str, startTok, wildCharSingle)) {
            return false;
        }
        final String endTok = tokens.get(tokens.size() - 1);
        if (!endsWithMaskSingle(str, endTok, wildCharSingle)) {
            return false;
        }
        int currPos = 0;
        for (int i = 0; i < tokens.size(); ++i) {
            final String token = tokens.get(i);
            if (token.length() > 0) {
                final int foundPos = indexOfMaskSingle(str, token, currPos, wildCharSingle);
                if (foundPos < 0) {
                    return false;
                }
                currPos = foundPos + token.length();
            }
        }
        return true;
    }
    
    private static boolean equalsMaskSingle(final String str, final String mask, final char wildCharSingle) {
        if (str == null || mask == null) {
            return str == mask;
        }
        if (str.length() != mask.length()) {
            return false;
        }
        for (int i = 0; i < mask.length(); ++i) {
            final char maskChar = mask.charAt(i);
            if (maskChar != wildCharSingle && str.charAt(i) != maskChar) {
                return false;
            }
        }
        return true;
    }
    
    private static int indexOfMaskSingle(final String str, final String mask, final int startPos, final char wildCharSingle) {
        if (str == null || mask == null) {
            return -1;
        }
        if (startPos < 0 || startPos > str.length()) {
            return -1;
        }
        if (str.length() < startPos + mask.length()) {
            return -1;
        }
        for (int i = startPos; i + mask.length() <= str.length(); ++i) {
            final String subStr = str.substring(i, i + mask.length());
            if (equalsMaskSingle(subStr, mask, wildCharSingle)) {
                return i;
            }
        }
        return -1;
    }
    
    private static boolean endsWithMaskSingle(final String str, final String mask, final char wildCharSingle) {
        if (str == null || mask == null) {
            return str == mask;
        }
        if (str.length() < mask.length()) {
            return false;
        }
        final String subStr = str.substring(str.length() - mask.length(), str.length());
        return equalsMaskSingle(subStr, mask, wildCharSingle);
    }
    
    private static boolean startsWithMaskSingle(final String str, final String mask, final char wildCharSingle) {
        if (str == null || mask == null) {
            return str == mask;
        }
        if (str.length() < mask.length()) {
            return false;
        }
        final String subStr = str.substring(0, mask.length());
        return equalsMaskSingle(subStr, mask, wildCharSingle);
    }
    
    public static boolean equalsMask(final String str, final String[] masks, final char wildChar) {
        for (int i = 0; i < masks.length; ++i) {
            final String mask = masks[i];
            if (equalsMask(str, mask, wildChar)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean equalsMask(final String str, final String mask, final char wildChar) {
        if (mask == null || str == null) {
            return mask == str;
        }
        if (mask.indexOf(wildChar) < 0) {
            return mask.equals(str);
        }
        final ArrayList tokens = new ArrayList();
        final String wildCharStr = new StringBuilder().append(wildChar).toString();
        if (mask.startsWith(wildCharStr)) {
            tokens.add("");
        }
        final StringTokenizer tok = new StringTokenizer(mask, wildCharStr);
        while (tok.hasMoreElements()) {
            tokens.add(tok.nextToken());
        }
        if (mask.endsWith(wildCharStr)) {
            tokens.add("");
        }
        final String startTok = tokens.get(0);
        if (!str.startsWith(startTok)) {
            return false;
        }
        final String endTok = tokens.get(tokens.size() - 1);
        if (!str.endsWith(endTok)) {
            return false;
        }
        int currPos = 0;
        for (int i = 0; i < tokens.size(); ++i) {
            final String token = tokens.get(i);
            if (token.length() > 0) {
                final int foundPos = str.indexOf(token, currPos);
                if (foundPos < 0) {
                    return false;
                }
                currPos = foundPos + token.length();
            }
        }
        return true;
    }
    
    public static String[] split(final String str, final String separators) {
        if (str == null || str.length() <= 0) {
            return new String[0];
        }
        if (separators == null) {
            return new String[] { str };
        }
        final ArrayList tokens = new ArrayList();
        int startPos = 0;
        for (int i = 0; i < str.length(); ++i) {
            final char ch = str.charAt(i);
            if (equals(ch, separators)) {
                tokens.add(str.substring(startPos, i));
                startPos = i + 1;
            }
        }
        tokens.add(str.substring(startPos, str.length()));
        return tokens.toArray(new String[tokens.size()]);
    }
    
    private static boolean equals(final char ch, final String matches) {
        for (int i = 0; i < matches.length(); ++i) {
            if (matches.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean equalsTrim(String a, String b) {
        if (a != null) {
            a = a.trim();
        }
        if (b != null) {
            b = b.trim();
        }
        return equals(a, b);
    }
    
    public static boolean isEmpty(final String string) {
        return string == null || string.trim().length() <= 0;
    }
    
    public static String stringInc(final String str) {
        int val = parseInt(str, -1);
        if (val == -1) {
            return "";
        }
        ++val;
        final String test = new StringBuilder().append(val).toString();
        return (test.length() > str.length()) ? "" : fillLeft(new StringBuilder().append(val).toString(), str.length(), '0');
    }
    
    public static int parseInt(final String s, final int defVal) {
        if (s == null) {
            return defVal;
        }
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException var3) {
            return defVal;
        }
    }
    
    public static boolean isFilled(final String string) {
        return !isEmpty(string);
    }
    
    public static String addIfNotContains(String target, final String source) {
        for (int i = 0; i < source.length(); ++i) {
            if (target.indexOf(source.charAt(i)) < 0) {
                target = String.valueOf(target) + source.charAt(i);
            }
        }
        return target;
    }
    
    public static String fillLeft(String s, final int len, final char fillChar) {
        if (s == null) {
            s = "";
        }
        if (s.length() >= len) {
            return s;
        }
        final StringBuffer buf = new StringBuffer(s);
        while (buf.length() < len) {
            buf.insert(0, fillChar);
        }
        return buf.toString();
    }
    
    public static String fillRight(String s, final int len, final char fillChar) {
        if (s == null) {
            s = "";
        }
        if (s.length() >= len) {
            return s;
        }
        final StringBuffer buf = new StringBuffer(s);
        while (buf.length() < len) {
            buf.append(fillChar);
        }
        return buf.toString();
    }
    
    public static boolean equals(final Object a, final Object b) {
        return a == b || (a != null && a.equals(b)) || (b != null && b.equals(a));
    }
    
    public static boolean startsWith(final String str, final String[] prefixes) {
        if (str == null) {
            return false;
        }
        if (prefixes == null) {
            return false;
        }
        for (int i = 0; i < prefixes.length; ++i) {
            final String prefix = prefixes[i];
            if (str.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean endsWith(final String str, final String[] suffixes) {
        if (str == null) {
            return false;
        }
        if (suffixes == null) {
            return false;
        }
        for (int i = 0; i < suffixes.length; ++i) {
            final String suffix = suffixes[i];
            if (str.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
    
    public static String removePrefix(String str, final String prefix) {
        if (str != null && prefix != null) {
            if (str.startsWith(prefix)) {
                str = str.substring(prefix.length());
            }
            return str;
        }
        return str;
    }
    
    public static String removeSuffix(String str, final String suffix) {
        if (str != null && suffix != null) {
            if (str.endsWith(suffix)) {
                str = str.substring(0, str.length() - suffix.length());
            }
            return str;
        }
        return str;
    }
    
    public static String replaceSuffix(String str, final String suffix, String suffixNew) {
        if (str != null && suffix != null) {
            if (suffixNew == null) {
                suffixNew = "";
            }
            if (str.endsWith(suffix)) {
                str = str.substring(0, str.length() - suffix.length());
            }
            return String.valueOf(str) + suffixNew;
        }
        return str;
    }
    
    public static int findPrefix(final String[] strs, final String prefix) {
        if (strs != null && prefix != null) {
            for (int i = 0; i < strs.length; ++i) {
                final String str = strs[i];
                if (str.startsWith(prefix)) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }
    
    public static int findSuffix(final String[] strs, final String suffix) {
        if (strs != null && suffix != null) {
            for (int i = 0; i < strs.length; ++i) {
                final String str = strs[i];
                if (str.endsWith(suffix)) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }
    
    public static String[] remove(final String[] strs, final int start, final int end) {
        if (strs == null) {
            return strs;
        }
        if (end <= 0 || start >= strs.length) {
            return strs;
        }
        if (start >= end) {
            return strs;
        }
        final ArrayList list = new ArrayList(strs.length);
        for (int strsNew = 0; strsNew < strs.length; ++strsNew) {
            final String str = strs[strsNew];
            if (strsNew < start || strsNew >= end) {
                list.add(str);
            }
        }
        final String[] var6 = list.toArray(new String[list.size()]);
        return var6;
    }
    
    public static String removeSuffix(String str, final String[] suffixes) {
        if (str != null && suffixes != null) {
            final int strLen = str.length();
            for (int i = 0; i < suffixes.length; ++i) {
                final String suffix = suffixes[i];
                str = removeSuffix(str, suffix);
                if (str.length() != strLen) {
                    break;
                }
            }
            return str;
        }
        return str;
    }
    
    public static String removePrefix(String str, final String[] prefixes) {
        if (str != null && prefixes != null) {
            final int strLen = str.length();
            for (int i = 0; i < prefixes.length; ++i) {
                final String prefix = prefixes[i];
                str = removePrefix(str, prefix);
                if (str.length() != strLen) {
                    break;
                }
            }
            return str;
        }
        return str;
    }
    
    public static String removePrefixSuffix(String str, final String[] prefixes, final String[] suffixes) {
        str = removePrefix(str, prefixes);
        str = removeSuffix(str, suffixes);
        return str;
    }
    
    public static String removePrefixSuffix(final String str, final String prefix, final String suffix) {
        return removePrefixSuffix(str, new String[] { prefix }, new String[] { suffix });
    }
    
    public static String getSegment(final String str, final String start, final String end) {
        if (str == null || start == null || end == null) {
            return null;
        }
        final int posStart = str.indexOf(start);
        if (posStart < 0) {
            return null;
        }
        final int posEnd = str.indexOf(end, posStart);
        return (posEnd < 0) ? null : str.substring(posStart, posEnd + end.length());
    }
}
