/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

public final class Collation {
    public static final int SENTINEL_CP = -1;
    public static final int LESS = -1;
    public static final int EQUAL = 0;
    public static final int GREATER = 1;
    public static final int TERMINATOR_BYTE = 0;
    public static final int LEVEL_SEPARATOR_BYTE = 1;
    static final int BEFORE_WEIGHT16 = 256;
    public static final int MERGE_SEPARATOR_BYTE = 2;
    public static final long MERGE_SEPARATOR_PRIMARY = 0x2000000L;
    static final int MERGE_SEPARATOR_CE32 = 0x2000505;
    public static final int PRIMARY_COMPRESSION_LOW_BYTE = 3;
    public static final int PRIMARY_COMPRESSION_HIGH_BYTE = 255;
    static final int COMMON_BYTE = 5;
    public static final int COMMON_WEIGHT16 = 1280;
    static final int COMMON_SECONDARY_CE = 0x5000000;
    static final int COMMON_TERTIARY_CE = 1280;
    public static final int COMMON_SEC_AND_TER_CE = 0x5000500;
    static final int SECONDARY_MASK = -65536;
    public static final int CASE_MASK = 49152;
    static final int SECONDARY_AND_CASE_MASK = -16384;
    public static final int ONLY_TERTIARY_MASK = 16191;
    static final int ONLY_SEC_TER_MASK = -49345;
    static final int CASE_AND_TERTIARY_MASK = 65343;
    public static final int QUATERNARY_MASK = 192;
    public static final int CASE_AND_QUATERNARY_MASK = 49344;
    static final int UNASSIGNED_IMPLICIT_BYTE = 254;
    static final long FIRST_UNASSIGNED_PRIMARY = 4261675520L;
    static final int TRAIL_WEIGHT_BYTE = 255;
    static final long FIRST_TRAILING_PRIMARY = 0xFF020200L;
    public static final long MAX_PRIMARY = 0xFFFF0000L;
    static final int MAX_REGULAR_CE32 = -64251;
    public static final long FFFD_PRIMARY = 0xFFFD0000L;
    static final int FFFD_CE32 = -195323;
    static final int SPECIAL_CE32_LOW_BYTE = 192;
    static final int FALLBACK_CE32 = 192;
    static final int LONG_PRIMARY_CE32_LOW_BYTE = 193;
    static final int UNASSIGNED_CE32 = -1;
    static final int NO_CE32 = 1;
    static final long NO_CE_PRIMARY = 1L;
    static final int NO_CE_WEIGHT16 = 256;
    public static final long NO_CE = 0x101000100L;
    public static final int NO_LEVEL = 0;
    public static final int PRIMARY_LEVEL = 1;
    public static final int SECONDARY_LEVEL = 2;
    public static final int CASE_LEVEL = 3;
    public static final int TERTIARY_LEVEL = 4;
    public static final int QUATERNARY_LEVEL = 5;
    public static final int IDENTICAL_LEVEL = 6;
    public static final int ZERO_LEVEL = 7;
    static final int NO_LEVEL_FLAG = 1;
    static final int PRIMARY_LEVEL_FLAG = 2;
    static final int SECONDARY_LEVEL_FLAG = 4;
    static final int CASE_LEVEL_FLAG = 8;
    static final int TERTIARY_LEVEL_FLAG = 16;
    static final int QUATERNARY_LEVEL_FLAG = 32;
    static final int IDENTICAL_LEVEL_FLAG = 64;
    static final int ZERO_LEVEL_FLAG = 128;
    static final int FALLBACK_TAG = 0;
    static final int LONG_PRIMARY_TAG = 1;
    static final int LONG_SECONDARY_TAG = 2;
    static final int RESERVED_TAG_3 = 3;
    static final int LATIN_EXPANSION_TAG = 4;
    static final int EXPANSION32_TAG = 5;
    static final int EXPANSION_TAG = 6;
    static final int BUILDER_DATA_TAG = 7;
    static final int PREFIX_TAG = 8;
    static final int CONTRACTION_TAG = 9;
    static final int DIGIT_TAG = 10;
    static final int U0000_TAG = 11;
    static final int HANGUL_TAG = 12;
    static final int LEAD_SURROGATE_TAG = 13;
    static final int OFFSET_TAG = 14;
    static final int IMPLICIT_TAG = 15;
    static final int MAX_EXPANSION_LENGTH = 31;
    static final int MAX_INDEX = 524287;
    static final int CONTRACT_SINGLE_CP_NO_MATCH = 256;
    static final int CONTRACT_NEXT_CCC = 512;
    static final int CONTRACT_TRAILING_CCC = 1024;
    static final int HANGUL_NO_SPECIAL_JAMO = 256;
    static final int LEAD_ALL_UNASSIGNED = 0;
    static final int LEAD_ALL_FALLBACK = 256;
    static final int LEAD_MIXED = 512;
    static final int LEAD_TYPE_MASK = 768;

    static boolean isAssignedCE32(int ce32) {
        return ce32 != 192 && ce32 != -1;
    }

    static int makeLongPrimaryCE32(long p2) {
        return (int)(p2 | 0xC1L);
    }

    static long primaryFromLongPrimaryCE32(int ce32) {
        return (long)ce32 & 0xFFFFFF00L;
    }

    static long ceFromLongPrimaryCE32(int ce32) {
        return (long)(ce32 & 0xFFFFFF00) << 32 | 0x5000500L;
    }

    static int makeLongSecondaryCE32(int lower32) {
        return lower32 | 0xC0 | 2;
    }

    static long ceFromLongSecondaryCE32(int ce32) {
        return (long)ce32 & 0xFFFFFF00L;
    }

    static int makeCE32FromTagIndexAndLength(int tag, int index, int length) {
        return index << 13 | length << 8 | 0xC0 | tag;
    }

    static int makeCE32FromTagAndIndex(int tag, int index) {
        return index << 13 | 0xC0 | tag;
    }

    static boolean isSpecialCE32(int ce32) {
        return (ce32 & 0xFF) >= 192;
    }

    static int tagFromCE32(int ce32) {
        return ce32 & 0xF;
    }

    static boolean hasCE32Tag(int ce32, int tag) {
        return Collation.isSpecialCE32(ce32) && Collation.tagFromCE32(ce32) == tag;
    }

    static boolean isLongPrimaryCE32(int ce32) {
        return Collation.hasCE32Tag(ce32, 1);
    }

    static boolean isSimpleOrLongCE32(int ce32) {
        return !Collation.isSpecialCE32(ce32) || Collation.tagFromCE32(ce32) == 1 || Collation.tagFromCE32(ce32) == 2;
    }

    static boolean isSelfContainedCE32(int ce32) {
        return !Collation.isSpecialCE32(ce32) || Collation.tagFromCE32(ce32) == 1 || Collation.tagFromCE32(ce32) == 2 || Collation.tagFromCE32(ce32) == 4;
    }

    static boolean isPrefixCE32(int ce32) {
        return Collation.hasCE32Tag(ce32, 8);
    }

    static boolean isContractionCE32(int ce32) {
        return Collation.hasCE32Tag(ce32, 9);
    }

    static boolean ce32HasContext(int ce32) {
        return Collation.isSpecialCE32(ce32) && (Collation.tagFromCE32(ce32) == 8 || Collation.tagFromCE32(ce32) == 9);
    }

    static long latinCE0FromCE32(int ce32) {
        return (long)(ce32 & 0xFF000000) << 32 | 0x5000000L | (long)((ce32 & 0xFF0000) >> 8);
    }

    static long latinCE1FromCE32(int ce32) {
        return ((long)ce32 & 0xFF00L) << 16 | 0x500L;
    }

    static int indexFromCE32(int ce32) {
        return ce32 >>> 13;
    }

    static int lengthFromCE32(int ce32) {
        return ce32 >> 8 & 0x1F;
    }

    static char digitFromCE32(int ce32) {
        return (char)(ce32 >> 8 & 0xF);
    }

    static long ceFromSimpleCE32(int ce32) {
        assert ((ce32 & 0xFF) < 192);
        return (long)(ce32 & 0xFFFF0000) << 32 | (long)(ce32 & 0xFF00) << 16 | (long)((ce32 & 0xFF) << 8);
    }

    static long ceFromCE32(int ce32) {
        int tertiary = ce32 & 0xFF;
        if (tertiary < 192) {
            return (long)(ce32 & 0xFFFF0000) << 32 | (long)(ce32 & 0xFF00) << 16 | (long)(tertiary << 8);
        }
        ce32 -= tertiary;
        if ((tertiary & 0xF) == 1) {
            return (long)ce32 << 32 | 0x5000500L;
        }
        assert ((tertiary & 0xF) == 2);
        return (long)ce32 & 0xFFFFFFFFL;
    }

    public static long makeCE(long p2) {
        return p2 << 32 | 0x5000500L;
    }

    static long makeCE(long p2, int s2, int t2, int q2) {
        return p2 << 32 | (long)s2 << 16 | (long)t2 | (long)(q2 << 6);
    }

    public static long incTwoBytePrimaryByOffset(long basePrimary, boolean isCompressible, int offset) {
        long primary;
        if (isCompressible) {
            primary = (offset += ((int)(basePrimary >> 16) & 0xFF) - 4) % 251 + 4 << 16;
            offset /= 251;
        } else {
            primary = (offset += ((int)(basePrimary >> 16) & 0xFF) - 2) % 254 + 2 << 16;
            offset /= 254;
        }
        return primary | (basePrimary & 0xFF000000L) + ((long)offset << 24);
    }

    public static long incThreeBytePrimaryByOffset(long basePrimary, boolean isCompressible, int offset) {
        long primary = (offset += ((int)(basePrimary >> 8) & 0xFF) - 2) % 254 + 2 << 8;
        offset /= 254;
        if (isCompressible) {
            primary |= (long)((offset += ((int)(basePrimary >> 16) & 0xFF) - 4) % 251 + 4 << 16);
            offset /= 251;
        } else {
            primary |= (long)((offset += ((int)(basePrimary >> 16) & 0xFF) - 2) % 254 + 2 << 16);
            offset /= 254;
        }
        return primary | (basePrimary & 0xFF000000L) + ((long)offset << 24);
    }

    static long decTwoBytePrimaryByOneStep(long basePrimary, boolean isCompressible, int step) {
        assert (0 < step && step <= 127);
        int byte2 = ((int)(basePrimary >> 16) & 0xFF) - step;
        if (isCompressible) {
            if (byte2 < 4) {
                byte2 += 251;
                basePrimary -= 0x1000000L;
            }
        } else if (byte2 < 2) {
            byte2 += 254;
            basePrimary -= 0x1000000L;
        }
        return basePrimary & 0xFF000000L | (long)(byte2 << 16);
    }

    static long decThreeBytePrimaryByOneStep(long basePrimary, boolean isCompressible, int step) {
        assert (0 < step && step <= 127);
        int byte3 = ((int)(basePrimary >> 8) & 0xFF) - step;
        if (byte3 >= 2) {
            return basePrimary & 0xFFFF0000L | (long)(byte3 << 8);
        }
        byte3 += 254;
        int byte2 = ((int)(basePrimary >> 16) & 0xFF) - 1;
        if (isCompressible) {
            if (byte2 < 4) {
                byte2 = 254;
                basePrimary -= 0x1000000L;
            }
        } else if (byte2 < 2) {
            byte2 = 255;
            basePrimary -= 0x1000000L;
        }
        return basePrimary & 0xFF000000L | (long)(byte2 << 16) | (long)(byte3 << 8);
    }

    static long getThreeBytePrimaryForOffsetData(int c2, long dataCE) {
        long p2 = dataCE >>> 32;
        int lower32 = (int)dataCE;
        int offset = (c2 - (lower32 >> 8)) * (lower32 & 0x7F);
        boolean isCompressible = (lower32 & 0x80) != 0;
        return Collation.incThreeBytePrimaryByOffset(p2, isCompressible, offset);
    }

    static long unassignedPrimaryFromCodePoint(int c2) {
        long primary = 2 + ++c2 % 18 * 14;
        primary |= (long)(2 + (c2 /= 18) % 254 << 8);
        return (primary |= (long)(4 + (c2 /= 254) % 251 << 16)) | 0xFE000000L;
    }

    static long unassignedCEFromCodePoint(int c2) {
        return Collation.makeCE(Collation.unassignedPrimaryFromCodePoint(c2));
    }
}

