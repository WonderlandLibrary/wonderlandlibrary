/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.CollationSettings;

public final class CollationFastLatin {
    public static final int VERSION = 2;
    public static final int LATIN_MAX = 383;
    public static final int LATIN_LIMIT = 384;
    static final int LATIN_MAX_UTF8_LEAD = 197;
    static final int PUNCT_START = 8192;
    static final int PUNCT_LIMIT = 8256;
    static final int NUM_FAST_CHARS = 448;
    static final int SHORT_PRIMARY_MASK = 64512;
    static final int INDEX_MASK = 1023;
    static final int SECONDARY_MASK = 992;
    static final int CASE_MASK = 24;
    static final int LONG_PRIMARY_MASK = 65528;
    static final int TERTIARY_MASK = 7;
    static final int CASE_AND_TERTIARY_MASK = 31;
    static final int TWO_SHORT_PRIMARIES_MASK = -67044352;
    static final int TWO_LONG_PRIMARIES_MASK = -458760;
    static final int TWO_SECONDARIES_MASK = 0x3E003E0;
    static final int TWO_CASES_MASK = 0x180018;
    static final int TWO_TERTIARIES_MASK = 458759;
    static final int CONTRACTION = 1024;
    static final int EXPANSION = 2048;
    static final int MIN_LONG = 3072;
    static final int LONG_INC = 8;
    static final int MAX_LONG = 4088;
    static final int MIN_SHORT = 4096;
    static final int SHORT_INC = 1024;
    static final int MAX_SHORT = 64512;
    static final int MIN_SEC_BEFORE = 0;
    static final int SEC_INC = 32;
    static final int MAX_SEC_BEFORE = 128;
    static final int COMMON_SEC = 160;
    static final int MIN_SEC_AFTER = 192;
    static final int MAX_SEC_AFTER = 352;
    static final int MIN_SEC_HIGH = 384;
    static final int MAX_SEC_HIGH = 992;
    static final int SEC_OFFSET = 32;
    static final int COMMON_SEC_PLUS_OFFSET = 192;
    static final int TWO_SEC_OFFSETS = 0x200020;
    static final int TWO_COMMON_SEC_PLUS_OFFSET = 0xC000C0;
    static final int LOWER_CASE = 8;
    static final int TWO_LOWER_CASES = 524296;
    static final int COMMON_TER = 0;
    static final int MAX_TER_AFTER = 7;
    static final int TER_OFFSET = 32;
    static final int COMMON_TER_PLUS_OFFSET = 32;
    static final int TWO_TER_OFFSETS = 0x200020;
    static final int TWO_COMMON_TER_PLUS_OFFSET = 0x200020;
    static final int MERGE_WEIGHT = 3;
    static final int EOS = 2;
    static final int BAIL_OUT = 1;
    static final int CONTR_CHAR_MASK = 511;
    static final int CONTR_LENGTH_SHIFT = 9;
    public static final int BAIL_OUT_RESULT = -2;

    static int getCharIndex(char c2) {
        if (c2 <= '\u017f') {
            return c2;
        }
        if ('\u2000' <= c2 && c2 < '\u2040') {
            return c2 - 7808;
        }
        return -1;
    }

    public static int getOptions(CollationData data, CollationSettings settings, char[] primaries) {
        int c2;
        int miniVarTop;
        char[] header = data.fastLatinTableHeader;
        if (header == null) {
            return -1;
        }
        assert (header[0] >> 8 == 2);
        if (primaries.length != 384) {
            assert (false);
            return -1;
        }
        if ((settings.options & 0xC) == 0) {
            miniVarTop = 3071;
        } else {
            int headerLength = header[0] & 0xFF;
            int i2 = 1 + settings.getMaxVariable();
            if (i2 >= headerLength) {
                return -1;
            }
            miniVarTop = header[i2];
        }
        boolean digitsAreReordered = false;
        if (settings.hasReordering()) {
            long prevStart = 0L;
            long beforeDigitStart = 0L;
            long digitStart = 0L;
            long afterDigitStart = 0L;
            for (int group = 4096; group < 4104; ++group) {
                long start = data.getFirstPrimaryForGroup(group);
                start = settings.reorder(start);
                if (group == 4100) {
                    beforeDigitStart = prevStart;
                    digitStart = start;
                    continue;
                }
                if (start == 0L) continue;
                if (start < prevStart) {
                    return -1;
                }
                if (digitStart != 0L && afterDigitStart == 0L && prevStart == beforeDigitStart) {
                    afterDigitStart = start;
                }
                prevStart = start;
            }
            long latinStart = data.getFirstPrimaryForGroup(25);
            if ((latinStart = settings.reorder(latinStart)) < prevStart) {
                return -1;
            }
            if (afterDigitStart == 0L) {
                afterDigitStart = latinStart;
            }
            if (beforeDigitStart >= digitStart || digitStart >= afterDigitStart) {
                digitsAreReordered = true;
            }
        }
        char[] table = data.fastLatinTable;
        for (c2 = 0; c2 < 384; ++c2) {
            int p2 = table[c2];
            p2 = p2 >= 4096 ? (p2 &= 0xFC00) : (p2 > miniVarTop ? (p2 &= 0xFFF8) : 0);
            primaries[c2] = (char)p2;
        }
        if (digitsAreReordered || (settings.options & 2) != 0) {
            for (c2 = 48; c2 <= 57; ++c2) {
                primaries[c2] = '\u0000';
            }
        }
        return miniVarTop << 16 | settings.options;
    }

    public static int compareUTF16(char[] table, char[] primaries, int options, CharSequence left, CharSequence right, int startIndex) {
        char c2;
        int variableTop = options >> 16;
        options &= 0xFFFF;
        int leftIndex = startIndex;
        int rightIndex = startIndex;
        int leftPair = 0;
        int rightPair = 0;
        while (true) {
            block80: {
                block83: {
                    block82: {
                        block81: {
                            if (leftPair != 0) break block80;
                            if (leftIndex != left.length()) break block81;
                            leftPair = 2;
                            break block80;
                        }
                        if ((c2 = left.charAt(leftIndex++)) > '\u017f') break block82;
                        leftPair = primaries[c2];
                        if (leftPair != 0) break block80;
                        if (c2 <= '9' && c2 >= '0' && (options & 2) != 0) {
                            return -2;
                        }
                        leftPair = table[c2];
                        break block83;
                    }
                    leftPair = '\u2000' <= c2 && c2 < '\u2040' ? table[c2 - 8192 + 384] : CollationFastLatin.lookup(table, c2);
                }
                if (leftPair >= 4096) {
                    leftPair &= 0xFC00;
                } else if (leftPair > variableTop) {
                    leftPair &= 0xFFF8;
                } else {
                    long pairAndInc = CollationFastLatin.nextPair(table, c2, leftPair, left, leftIndex);
                    if (pairAndInc < 0L) {
                        ++leftIndex;
                        pairAndInc ^= 0xFFFFFFFFFFFFFFFFL;
                    }
                    if ((leftPair = (int)pairAndInc) == 1) {
                        return -2;
                    }
                    leftPair = CollationFastLatin.getPrimaries(variableTop, leftPair);
                    continue;
                }
            }
            while (rightPair == 0) {
                if (rightIndex == right.length()) {
                    rightPair = 2;
                    break;
                }
                if ((c2 = right.charAt(rightIndex++)) <= '\u017f') {
                    rightPair = primaries[c2];
                    if (rightPair != 0) break;
                    if (c2 <= '9' && c2 >= '0' && (options & 2) != 0) {
                        return -2;
                    }
                    rightPair = table[c2];
                } else {
                    rightPair = '\u2000' <= c2 && c2 < '\u2040' ? table[c2 - 8192 + 384] : CollationFastLatin.lookup(table, c2);
                }
                if (rightPair >= 4096) {
                    rightPair &= 0xFC00;
                    break;
                }
                if (rightPair > variableTop) {
                    rightPair &= 0xFFF8;
                    break;
                }
                long pairAndInc = CollationFastLatin.nextPair(table, c2, rightPair, right, rightIndex);
                if (pairAndInc < 0L) {
                    ++rightIndex;
                    pairAndInc ^= 0xFFFFFFFFFFFFFFFFL;
                }
                if ((rightPair = (int)pairAndInc) == 1) {
                    return -2;
                }
                rightPair = CollationFastLatin.getPrimaries(variableTop, rightPair);
            }
            if (leftPair == rightPair) {
                if (leftPair == 2) break;
                rightPair = 0;
                leftPair = 0;
                continue;
            }
            int leftPrimary = leftPair & 0xFFFF;
            int rightPrimary = rightPair & 0xFFFF;
            if (leftPrimary != rightPrimary) {
                return leftPrimary < rightPrimary ? -1 : 1;
            }
            if (leftPair == 2) break;
            leftPair >>>= 16;
            rightPair >>>= 16;
        }
        if (CollationSettings.getStrength(options) >= 1) {
            leftIndex = rightIndex = startIndex;
            rightPair = 0;
            leftPair = 0;
            while (true) {
                if (leftPair == 0) {
                    if (leftIndex == left.length()) {
                        leftPair = 2;
                    } else if ((leftPair = (c2 = left.charAt(leftIndex++)) <= '\u017f' ? table[c2] : ('\u2000' <= c2 && c2 < '\u2040' ? table[c2 - 8192 + 384] : CollationFastLatin.lookup(table, c2))) >= 4096) {
                        leftPair = CollationFastLatin.getSecondariesFromOneShortCE(leftPair);
                    } else if (leftPair > variableTop) {
                        leftPair = 192;
                    } else {
                        long pairAndInc = CollationFastLatin.nextPair(table, c2, leftPair, left, leftIndex);
                        if (pairAndInc < 0L) {
                            ++leftIndex;
                            pairAndInc ^= 0xFFFFFFFFFFFFFFFFL;
                        }
                        leftPair = CollationFastLatin.getSecondaries(variableTop, (int)pairAndInc);
                        continue;
                    }
                }
                while (rightPair == 0) {
                    if (rightIndex == right.length()) {
                        rightPair = 2;
                        break;
                    }
                    if ((rightPair = (c2 = right.charAt(rightIndex++)) <= '\u017f' ? table[c2] : ('\u2000' <= c2 && c2 < '\u2040' ? table[c2 - 8192 + 384] : CollationFastLatin.lookup(table, c2))) >= 4096) {
                        rightPair = CollationFastLatin.getSecondariesFromOneShortCE(rightPair);
                        break;
                    }
                    if (rightPair > variableTop) {
                        rightPair = 192;
                        break;
                    }
                    long pairAndInc = CollationFastLatin.nextPair(table, c2, rightPair, right, rightIndex);
                    if (pairAndInc < 0L) {
                        ++rightIndex;
                        pairAndInc ^= 0xFFFFFFFFFFFFFFFFL;
                    }
                    rightPair = CollationFastLatin.getSecondaries(variableTop, (int)pairAndInc);
                }
                if (leftPair == rightPair) {
                    if (leftPair == 2) break;
                    rightPair = 0;
                    leftPair = 0;
                    continue;
                }
                int leftSecondary = leftPair & 0xFFFF;
                int rightSecondary = rightPair & 0xFFFF;
                if (leftSecondary != rightSecondary) {
                    if ((options & 0x800) != 0) {
                        return -2;
                    }
                    return leftSecondary < rightSecondary ? -1 : 1;
                }
                if (leftPair == 2) break;
                leftPair >>>= 16;
                rightPair >>>= 16;
            }
        }
        if ((options & 0x400) != 0) {
            boolean strengthIsPrimary = CollationSettings.getStrength(options) == 0;
            leftIndex = rightIndex = startIndex;
            rightPair = 0;
            leftPair = 0;
            while (true) {
                char c3;
                if (leftPair == 0) {
                    if (leftIndex == left.length()) {
                        leftPair = 2;
                    } else {
                        int n2 = leftPair = (c3 = left.charAt(leftIndex++)) <= '\u017f' ? table[c3] : CollationFastLatin.lookup(table, c3);
                        if (leftPair < 3072) {
                            long pairAndInc = CollationFastLatin.nextPair(table, c3, leftPair, left, leftIndex);
                            if (pairAndInc < 0L) {
                                ++leftIndex;
                                pairAndInc ^= 0xFFFFFFFFFFFFFFFFL;
                            }
                            leftPair = (int)pairAndInc;
                        }
                        leftPair = CollationFastLatin.getCases(variableTop, strengthIsPrimary, leftPair);
                        continue;
                    }
                }
                while (rightPair == 0) {
                    if (rightIndex == right.length()) {
                        rightPair = 2;
                        break;
                    }
                    int n3 = rightPair = (c3 = right.charAt(rightIndex++)) <= '\u017f' ? table[c3] : CollationFastLatin.lookup(table, c3);
                    if (rightPair < 3072) {
                        long pairAndInc = CollationFastLatin.nextPair(table, c3, rightPair, right, rightIndex);
                        if (pairAndInc < 0L) {
                            ++rightIndex;
                            pairAndInc ^= 0xFFFFFFFFFFFFFFFFL;
                        }
                        rightPair = (int)pairAndInc;
                    }
                    rightPair = CollationFastLatin.getCases(variableTop, strengthIsPrimary, rightPair);
                }
                if (leftPair == rightPair) {
                    if (leftPair == 2) break;
                    rightPair = 0;
                    leftPair = 0;
                    continue;
                }
                int leftCase = leftPair & 0xFFFF;
                int rightCase = rightPair & 0xFFFF;
                if (leftCase != rightCase) {
                    if ((options & 0x100) == 0) {
                        return leftCase < rightCase ? -1 : 1;
                    }
                    return leftCase < rightCase ? 1 : -1;
                }
                if (leftPair == 2) break;
                leftPair >>>= 16;
                rightPair >>>= 16;
            }
        }
        if (CollationSettings.getStrength(options) <= 1) {
            return 0;
        }
        boolean withCaseBits = CollationSettings.isTertiaryWithCaseBits(options);
        leftIndex = rightIndex = startIndex;
        rightPair = 0;
        leftPair = 0;
        while (true) {
            char c4;
            if (leftPair == 0) {
                if (leftIndex == left.length()) {
                    leftPair = 2;
                } else {
                    int n4 = leftPair = (c4 = left.charAt(leftIndex++)) <= '\u017f' ? table[c4] : CollationFastLatin.lookup(table, c4);
                    if (leftPair < 3072) {
                        long pairAndInc = CollationFastLatin.nextPair(table, c4, leftPair, left, leftIndex);
                        if (pairAndInc < 0L) {
                            ++leftIndex;
                            pairAndInc ^= 0xFFFFFFFFFFFFFFFFL;
                        }
                        leftPair = (int)pairAndInc;
                    }
                    leftPair = CollationFastLatin.getTertiaries(variableTop, withCaseBits, leftPair);
                    continue;
                }
            }
            while (rightPair == 0) {
                if (rightIndex == right.length()) {
                    rightPair = 2;
                    break;
                }
                int n5 = rightPair = (c4 = right.charAt(rightIndex++)) <= '\u017f' ? table[c4] : CollationFastLatin.lookup(table, c4);
                if (rightPair < 3072) {
                    long pairAndInc = CollationFastLatin.nextPair(table, c4, rightPair, right, rightIndex);
                    if (pairAndInc < 0L) {
                        ++rightIndex;
                        pairAndInc ^= 0xFFFFFFFFFFFFFFFFL;
                    }
                    rightPair = (int)pairAndInc;
                }
                rightPair = CollationFastLatin.getTertiaries(variableTop, withCaseBits, rightPair);
            }
            if (leftPair == rightPair) {
                if (leftPair == 2) break;
                rightPair = 0;
                leftPair = 0;
                continue;
            }
            int leftTertiary = leftPair & 0xFFFF;
            int rightTertiary = rightPair & 0xFFFF;
            if (leftTertiary != rightTertiary) {
                if (CollationSettings.sortsTertiaryUpperCaseFirst(options)) {
                    if (leftTertiary > 3) {
                        leftTertiary ^= 0x18;
                    }
                    if (rightTertiary > 3) {
                        rightTertiary ^= 0x18;
                    }
                }
                return leftTertiary < rightTertiary ? -1 : 1;
            }
            if (leftPair == 2) break;
            leftPair >>>= 16;
            rightPair >>>= 16;
        }
        if (CollationSettings.getStrength(options) <= 2) {
            return 0;
        }
        leftIndex = rightIndex = startIndex;
        rightPair = 0;
        leftPair = 0;
        while (true) {
            char c5;
            if (leftPair == 0) {
                if (leftIndex == left.length()) {
                    leftPair = 2;
                } else {
                    int n6 = leftPair = (c5 = left.charAt(leftIndex++)) <= '\u017f' ? table[c5] : CollationFastLatin.lookup(table, c5);
                    if (leftPair < 3072) {
                        long pairAndInc = CollationFastLatin.nextPair(table, c5, leftPair, left, leftIndex);
                        if (pairAndInc < 0L) {
                            ++leftIndex;
                            pairAndInc ^= 0xFFFFFFFFFFFFFFFFL;
                        }
                        leftPair = (int)pairAndInc;
                    }
                    leftPair = CollationFastLatin.getQuaternaries(variableTop, leftPair);
                    continue;
                }
            }
            while (rightPair == 0) {
                if (rightIndex == right.length()) {
                    rightPair = 2;
                    break;
                }
                int n7 = rightPair = (c5 = right.charAt(rightIndex++)) <= '\u017f' ? table[c5] : CollationFastLatin.lookup(table, c5);
                if (rightPair < 3072) {
                    long pairAndInc = CollationFastLatin.nextPair(table, c5, rightPair, right, rightIndex);
                    if (pairAndInc < 0L) {
                        ++rightIndex;
                        pairAndInc ^= 0xFFFFFFFFFFFFFFFFL;
                    }
                    rightPair = (int)pairAndInc;
                }
                rightPair = CollationFastLatin.getQuaternaries(variableTop, rightPair);
            }
            if (leftPair == rightPair) {
                if (leftPair == 2) break;
                rightPair = 0;
                leftPair = 0;
                continue;
            }
            int leftQuaternary = leftPair & 0xFFFF;
            int rightQuaternary = rightPair & 0xFFFF;
            if (leftQuaternary != rightQuaternary) {
                return leftQuaternary < rightQuaternary ? -1 : 1;
            }
            if (leftPair == 2) break;
            leftPair >>>= 16;
            rightPair >>>= 16;
        }
        return 0;
    }

    private static int lookup(char[] table, int c2) {
        assert (c2 > 383);
        if (8192 <= c2 && c2 < 8256) {
            return table[c2 - 8192 + 384];
        }
        if (c2 == 65534) {
            return 3;
        }
        if (c2 == 65535) {
            return 64680;
        }
        return 1;
    }

    private static long nextPair(char[] table, int c2, int ce, CharSequence s16, int sIndex) {
        int length;
        if (ce >= 3072 || ce < 1024) {
            return ce;
        }
        if (ce >= 2048) {
            int index = 448 + (ce & 0x3FF);
            return (long)table[index + 1] << 16 | (long)table[index];
        }
        int index = 448 + (ce & 0x3FF);
        boolean inc = false;
        if (sIndex != s16.length()) {
            int x2;
            int c22;
            int nextIndex = sIndex;
            if ((c22 = s16.charAt(nextIndex++)) > 383) {
                if (8192 <= c22 && c22 < 8256) {
                    c22 = c22 - 8192 + 384;
                } else if (c22 == 65534 || c22 == 65535) {
                    c22 = -1;
                } else {
                    return 1L;
                }
            }
            int i2 = index;
            char head = table[i2];
            while ((x2 = (head = table[i2 += head >> 9]) & 0x1FF) < c22) {
            }
            if (x2 == c22) {
                index = i2;
                inc = true;
            }
        }
        if ((length = table[index] >> 9) == 1) {
            return 1L;
        }
        ce = table[index + 1];
        long result = length == 2 ? (long)ce : (long)table[index + 2] << 16 | (long)ce;
        return inc ? result ^ 0xFFFFFFFFFFFFFFFFL : result;
    }

    private static int getPrimaries(int variableTop, int pair) {
        int ce = pair & 0xFFFF;
        if (ce >= 4096) {
            return pair & 0xFC00FC00;
        }
        if (ce > variableTop) {
            return pair & 0xFFF8FFF8;
        }
        if (ce >= 3072) {
            return 0;
        }
        return pair;
    }

    private static int getSecondariesFromOneShortCE(int ce) {
        if ((ce &= 0x3E0) < 384) {
            return ce + 32;
        }
        return ce + 32 << 16 | 0xC0;
    }

    private static int getSecondaries(int variableTop, int pair) {
        if (pair <= 65535) {
            if (pair >= 4096) {
                pair = CollationFastLatin.getSecondariesFromOneShortCE(pair);
            } else if (pair > variableTop) {
                pair = 192;
            } else if (pair >= 3072) {
                pair = 0;
            }
        } else {
            int ce = pair & 0xFFFF;
            if (ce >= 4096) {
                pair = (pair & 0x3E003E0) + 0x200020;
            } else if (ce > variableTop) {
                pair = 0xC000C0;
            } else {
                assert (ce >= 3072);
                pair = 0;
            }
        }
        return pair;
    }

    private static int getCases(int variableTop, boolean strengthIsPrimary, int pair) {
        if (pair <= 65535) {
            if (pair >= 4096) {
                int ce = pair;
                pair &= 0x18;
                if (!strengthIsPrimary && (ce & 0x3E0) >= 384) {
                    pair |= 0x80000;
                }
            } else if (pair > variableTop) {
                pair = 8;
            } else if (pair >= 3072) {
                pair = 0;
            }
        } else {
            int ce = pair & 0xFFFF;
            if (ce >= 4096) {
                pair = strengthIsPrimary && (pair & 0xFC000000) == 0 ? (pair &= 0x18) : (pair &= 0x180018);
            } else if (ce > variableTop) {
                pair = 524296;
            } else {
                assert (ce >= 3072);
                pair = 0;
            }
        }
        return pair;
    }

    private static int getTertiaries(int variableTop, boolean withCaseBits, int pair) {
        if (pair <= 65535) {
            if (pair >= 4096) {
                int ce = pair;
                if (withCaseBits) {
                    pair = (pair & 0x1F) + 32;
                    if ((ce & 0x3E0) >= 384) {
                        pair |= 0x280000;
                    }
                } else {
                    pair = (pair & 7) + 32;
                    if ((ce & 0x3E0) >= 384) {
                        pair |= 0x200000;
                    }
                }
            } else if (pair > variableTop) {
                pair = (pair & 7) + 32;
                if (withCaseBits) {
                    pair |= 8;
                }
            } else if (pair >= 3072) {
                pair = 0;
            }
        } else {
            int ce = pair & 0xFFFF;
            if (ce >= 4096) {
                pair = withCaseBits ? (pair &= 0x1F001F) : (pair &= 0x70007);
                pair += 0x200020;
            } else if (ce > variableTop) {
                pair = (pair & 0x70007) + 0x200020;
                if (withCaseBits) {
                    pair |= 0x80008;
                }
            } else {
                assert (ce >= 3072);
                pair = 0;
            }
        }
        return pair;
    }

    private static int getQuaternaries(int variableTop, int pair) {
        if (pair <= 65535) {
            if (pair >= 4096) {
                pair = (pair & 0x3E0) >= 384 ? -67044352 : 64512;
            } else if (pair > variableTop) {
                pair = 64512;
            } else if (pair >= 3072) {
                pair &= 0xFFF8;
            }
        } else {
            int ce = pair & 0xFFFF;
            if (ce > variableTop) {
                pair = -67044352;
            } else {
                assert (ce >= 3072);
                pair &= 0xFFF8FFF8;
            }
        }
        return pair;
    }

    private CollationFastLatin() {
    }
}

