/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.StringPrepParseException;
import com.ibm.icu.text.UTF16;

public final class Punycode {
    private static final int BASE = 36;
    private static final int TMIN = 1;
    private static final int TMAX = 26;
    private static final int SKEW = 38;
    private static final int DAMP = 700;
    private static final int INITIAL_BIAS = 72;
    private static final int INITIAL_N = 128;
    private static final char HYPHEN = '-';
    private static final char DELIMITER = '-';
    private static final int ZERO = 48;
    private static final int SMALL_A = 97;
    private static final int SMALL_Z = 122;
    private static final int CAPITAL_A = 65;
    private static final int CAPITAL_Z = 90;
    static final int[] basicToDigit = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    private static int adaptBias(int delta, int length, boolean firstTime) {
        delta = firstTime ? (delta /= 700) : (delta /= 2);
        delta += delta / length;
        int count = 0;
        while (delta > 455) {
            delta /= 35;
            count += 36;
        }
        return count + 36 * delta / (delta + 38);
    }

    private static char asciiCaseMap(char b2, boolean uppercase) {
        if (uppercase) {
            if ('a' <= b2 && b2 <= 'z') {
                b2 = (char)(b2 - 32);
            }
        } else if ('A' <= b2 && b2 <= 'Z') {
            b2 = (char)(b2 + 32);
        }
        return b2;
    }

    private static char digitToBasic(int digit, boolean uppercase) {
        if (digit < 26) {
            if (uppercase) {
                return (char)(65 + digit);
            }
            return (char)(97 + digit);
        }
        return (char)(22 + digit);
    }

    public static StringBuilder encode(CharSequence src, boolean[] caseFlags) throws StringPrepParseException {
        int n2;
        int j2;
        int srcLength = src.length();
        int[] cpBuffer = new int[srcLength];
        StringBuilder dest = new StringBuilder(srcLength);
        int srcCPCount = 0;
        for (j2 = 0; j2 < srcLength; ++j2) {
            char c2;
            char c3 = src.charAt(j2);
            if (Punycode.isBasic(c3)) {
                cpBuffer[srcCPCount++] = 0;
                dest.append(caseFlags != null ? Punycode.asciiCaseMap(c3, caseFlags[j2]) : c3);
                continue;
            }
            n2 = (caseFlags != null && caseFlags[j2] ? 1 : 0) << 31;
            if (!UTF16.isSurrogate(c3)) {
                n2 |= c3;
            } else if (UTF16.isLeadSurrogate(c3) && j2 + 1 < srcLength && UTF16.isTrailSurrogate(c2 = src.charAt(j2 + 1))) {
                ++j2;
                n2 |= UCharacter.getCodePoint(c3, c2);
            } else {
                throw new StringPrepParseException("Illegal char found", 1);
            }
            cpBuffer[srcCPCount++] = n2;
        }
        int basicLength = dest.length();
        if (basicLength > 0) {
            dest.append('-');
        }
        n2 = 128;
        int delta = 0;
        int bias = 72;
        int handledCPCount = basicLength;
        while (handledCPCount < srcCPCount) {
            int q2;
            int m2 = Integer.MAX_VALUE;
            for (j2 = 0; j2 < srcCPCount; ++j2) {
                q2 = cpBuffer[j2] & Integer.MAX_VALUE;
                if (n2 > q2 || q2 >= m2) continue;
                m2 = q2;
            }
            if (m2 - n2 > (Integer.MAX_VALUE - delta) / (handledCPCount + 1)) {
                throw new IllegalStateException("Internal program error");
            }
            delta += (m2 - n2) * (handledCPCount + 1);
            n2 = m2;
            for (j2 = 0; j2 < srcCPCount; ++j2) {
                q2 = cpBuffer[j2] & Integer.MAX_VALUE;
                if (q2 < n2) {
                    ++delta;
                    continue;
                }
                if (q2 != n2) continue;
                q2 = delta;
                int k2 = 36;
                while (true) {
                    int t2;
                    if ((t2 = k2 - bias) < 1) {
                        t2 = 1;
                    } else if (k2 >= bias + 26) {
                        t2 = 26;
                    }
                    if (q2 < t2) break;
                    dest.append(Punycode.digitToBasic(t2 + (q2 - t2) % (36 - t2), false));
                    q2 = (q2 - t2) / (36 - t2);
                    k2 += 36;
                }
                dest.append(Punycode.digitToBasic(q2, cpBuffer[j2] < 0));
                bias = Punycode.adaptBias(delta, handledCPCount + 1, handledCPCount == basicLength);
                delta = 0;
                ++handledCPCount;
            }
            ++delta;
            ++n2;
        }
        return dest;
    }

    private static boolean isBasic(int ch) {
        return ch < 128;
    }

    private static boolean isBasicUpperCase(int ch) {
        return 65 <= ch && ch >= 90;
    }

    private static boolean isSurrogate(int ch) {
        return (ch & 0xFFFFF800) == 55296;
    }

    public static StringBuilder decode(CharSequence src, boolean[] caseFlags) throws StringPrepParseException {
        int in;
        int destCPCount;
        int srcLength = src.length();
        StringBuilder dest = new StringBuilder(src.length());
        int j2 = srcLength;
        while (j2 > 0 && src.charAt(--j2) != '-') {
        }
        int basicLength = destCPCount = j2;
        for (j2 = 0; j2 < basicLength; ++j2) {
            char b2 = src.charAt(j2);
            if (!Punycode.isBasic(b2)) {
                throw new StringPrepParseException("Illegal char found", 0);
            }
            dest.append(b2);
            if (caseFlags == null || j2 >= caseFlags.length) continue;
            caseFlags[j2] = Punycode.isBasicUpperCase(b2);
        }
        int n2 = 128;
        int i2 = 0;
        int bias = 72;
        int firstSupplementaryIndex = 1000000000;
        int n3 = in = basicLength > 0 ? basicLength + 1 : 0;
        while (in < srcLength) {
            int codeUnitIndex;
            int oldi = i2;
            int w2 = 1;
            int k2 = 36;
            while (true) {
                int digit;
                if (in >= srcLength) {
                    throw new StringPrepParseException("Illegal char found", 1);
                }
                if ((digit = basicToDigit[src.charAt(in++) & 0xFF]) < 0) {
                    throw new StringPrepParseException("Invalid char found", 0);
                }
                if (digit > (Integer.MAX_VALUE - i2) / w2) {
                    throw new StringPrepParseException("Illegal char found", 1);
                }
                i2 += digit * w2;
                int t2 = k2 - bias;
                if (t2 < 1) {
                    t2 = 1;
                } else if (k2 >= bias + 26) {
                    t2 = 26;
                }
                if (digit < t2) break;
                if (w2 > Integer.MAX_VALUE / (36 - t2)) {
                    throw new StringPrepParseException("Illegal char found", 1);
                }
                w2 *= 36 - t2;
                k2 += 36;
            }
            bias = Punycode.adaptBias(i2 - oldi, ++destCPCount, oldi == 0);
            if (i2 / destCPCount > Integer.MAX_VALUE - n2) {
                throw new StringPrepParseException("Illegal char found", 1);
            }
            n2 += i2 / destCPCount;
            i2 %= destCPCount;
            if (n2 > 0x10FFFF || Punycode.isSurrogate(n2)) {
                throw new StringPrepParseException("Illegal char found", 1);
            }
            int cpLength = Character.charCount(n2);
            if (i2 <= firstSupplementaryIndex) {
                codeUnitIndex = i2;
                firstSupplementaryIndex = cpLength > 1 ? codeUnitIndex : ++firstSupplementaryIndex;
            } else {
                codeUnitIndex = dest.offsetByCodePoints(firstSupplementaryIndex, i2 - firstSupplementaryIndex);
            }
            if (caseFlags != null && dest.length() + cpLength <= caseFlags.length) {
                if (codeUnitIndex < dest.length()) {
                    System.arraycopy(caseFlags, codeUnitIndex, caseFlags, codeUnitIndex + cpLength, dest.length() - codeUnitIndex);
                }
                caseFlags[codeUnitIndex] = Punycode.isBasicUpperCase(src.charAt(in - 1));
                if (cpLength == 2) {
                    caseFlags[codeUnitIndex + 1] = false;
                }
            }
            if (cpLength == 1) {
                dest.insert(codeUnitIndex, (char)n2);
            } else {
                dest.insert(codeUnitIndex, UTF16.getLeadSurrogate(n2));
                dest.insert(codeUnitIndex + 1, UTF16.getTrailSurrogate(n2));
            }
            ++i2;
        }
        return dest;
    }
}

