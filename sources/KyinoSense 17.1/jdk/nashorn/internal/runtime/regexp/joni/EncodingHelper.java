/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import java.util.Arrays;
import jdk.nashorn.internal.runtime.regexp.joni.ApplyCaseFold;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;

public final class EncodingHelper {
    static final int NEW_LINE = 10;
    static final int RETURN = 13;
    static final int LINE_SEPARATOR = 8232;
    static final int PARAGRAPH_SEPARATOR = 8233;
    static final char[] EMPTYCHARS = new char[0];
    static final int[][] codeRanges = new int[15][];

    public static int digitVal(int code) {
        return code - 48;
    }

    public static int odigitVal(int code) {
        return EncodingHelper.digitVal(code);
    }

    public static boolean isXDigit(int code) {
        return Character.isDigit(code) || code >= 97 && code <= 102 || code >= 65 && code <= 70;
    }

    public static int xdigitVal(int code) {
        if (Character.isDigit(code)) {
            return code - 48;
        }
        if (code >= 97 && code <= 102) {
            return code - 97 + 10;
        }
        return code - 65 + 10;
    }

    public static boolean isDigit(int code) {
        return code >= 48 && code <= 57;
    }

    public static boolean isWord(int code) {
        return (1 << Character.getType(code) & 0x8003FE) != 0;
    }

    public static boolean isNewLine(int code) {
        return code == 10 || code == 13 || code == 8232 || code == 8233;
    }

    public static boolean isNewLine(char[] chars, int p, int end) {
        return p < end && EncodingHelper.isNewLine(chars[p]);
    }

    public static int prevCharHead(int p, int s) {
        return s <= p ? -1 : s - 1;
    }

    public static int rightAdjustCharHeadWithPrev(int s, IntHolder prev) {
        if (prev != null) {
            prev.value = -1;
        }
        return s;
    }

    public static int stepBack(int p, int sp, int np) {
        int s;
        int n = np;
        for (s = sp; s != -1 && n-- > 0; --s) {
            if (s > p) continue;
            return -1;
        }
        return s;
    }

    public static int mbcodeStartPosition() {
        return 128;
    }

    public static char[] caseFoldCodesByString(int flag, char c) {
        char[] codes = EMPTYCHARS;
        char upper = EncodingHelper.toUpperCase(c);
        if (upper != EncodingHelper.toLowerCase(upper)) {
            char c2;
            int count = 0;
            char ch = '\u0000';
            do {
                char u;
                if ((u = EncodingHelper.toUpperCase(ch)) == upper && ch != c) {
                    codes = count == 0 ? new char[1] : Arrays.copyOf(codes, count + 1);
                    codes[count++] = ch;
                }
                c2 = ch;
                ch = (char)(ch + '\u0001');
            } while (c2 < '\uffff');
        }
        return codes;
    }

    public static void applyAllCaseFold(int flag, ApplyCaseFold fun, Object arg) {
        int upper;
        int c;
        for (c = 0; c < 65535; ++c) {
            if (!Character.isLowerCase(c) || (upper = EncodingHelper.toUpperCase(c)) == c) continue;
            ApplyCaseFold.apply(c, upper, arg);
        }
        for (c = 0; c < 65535; ++c) {
            if (!Character.isLowerCase(c) || (upper = EncodingHelper.toUpperCase(c)) == c) continue;
            ApplyCaseFold.apply(upper, c, arg);
        }
    }

    public static char toLowerCase(char c) {
        return (char)EncodingHelper.toLowerCase((int)c);
    }

    public static int toLowerCase(int c) {
        if (c < 128) {
            return 65 <= c && c <= 90 ? c + 32 : c;
        }
        int lower = Character.toLowerCase(c);
        return lower < 128 ? c : lower;
    }

    public static char toUpperCase(char c) {
        return (char)EncodingHelper.toUpperCase((int)c);
    }

    public static int toUpperCase(int c) {
        if (c < 128) {
            return 97 <= c && c <= 122 ? c + -32 : c;
        }
        int upper = Character.toUpperCase(c);
        return upper < 128 ? c : upper;
    }

    public static int[] ctypeCodeRange(int ctype, IntHolder sbOut) {
        sbOut.value = 256;
        int[] range = null;
        if (ctype < codeRanges.length && (range = codeRanges[ctype]) == null) {
            range = new int[16];
            int rangeCount = 0;
            int lastCode = -2;
            for (int code = 0; code <= 65535; ++code) {
                if (!EncodingHelper.isCodeCType(code, ctype)) continue;
                if (lastCode < code - 1) {
                    if (rangeCount * 2 + 2 >= range.length) {
                        range = Arrays.copyOf(range, range.length * 2);
                    }
                    range[rangeCount * 2 + 1] = code;
                    ++rangeCount;
                }
                range[rangeCount * 2] = lastCode = code;
            }
            if (rangeCount * 2 + 1 < range.length) {
                range = Arrays.copyOf(range, rangeCount * 2 + 1);
            }
            range[0] = rangeCount;
            EncodingHelper.codeRanges[ctype] = range;
        }
        return range;
    }

    public static boolean isInCodeRange(int[] p, int offset, int code) {
        int n;
        int low = 0;
        int high = n = p[offset];
        while (low < high) {
            int x = low + high >> 1;
            if (code > p[(x << 1) + 2 + offset]) {
                low = x + 1;
                continue;
            }
            high = x;
        }
        return low < n && code >= p[(low << 1) + 1 + offset];
    }

    public static boolean isCodeCType(int code, int ctype) {
        switch (ctype) {
            case 0: {
                return EncodingHelper.isNewLine(code);
            }
            case 1: {
                return (1 << Character.getType(code) & 0x1FE) != 0;
            }
            case 2: {
                return code == 9 || Character.getType(code) == 12;
            }
            case 3: {
                int type = Character.getType(code);
                return (1 << type & 0xD8000) != 0 || type == 0;
            }
            case 4: {
                return EncodingHelper.isDigit(code);
            }
            case 5: {
                switch (code) {
                    case 9: 
                    case 10: 
                    case 11: 
                    case 12: 
                    case 13: {
                        return false;
                    }
                }
                int type = Character.getType(code);
                return (1 << type & 0x8F000) == 0 && type != 0;
            }
            case 6: {
                return Character.isLowerCase(code);
            }
            case 7: {
                int type = Character.getType(code);
                return (1 << type & 0x88000) == 0 && type != 0;
            }
            case 8: {
                return (1 << Character.getType(code) & 0x61F00000) != 0;
            }
            case 9: {
                switch (code) {
                    case 9: 
                    case 10: 
                    case 11: 
                    case 12: 
                    case 13: {
                        return true;
                    }
                }
                return (1 << Character.getType(code) & 0x7000) != 0 || code == 65279;
            }
            case 10: {
                return Character.isUpperCase(code);
            }
            case 11: {
                return EncodingHelper.isXDigit(code);
            }
            case 12: {
                return (1 << Character.getType(code) & 0x8003FE) != 0;
            }
            case 13: {
                return (1 << Character.getType(code) & 0x3FE) != 0;
            }
            case 14: {
                return code < 128;
            }
        }
        throw new RuntimeException("illegal character type: " + ctype);
    }
}

