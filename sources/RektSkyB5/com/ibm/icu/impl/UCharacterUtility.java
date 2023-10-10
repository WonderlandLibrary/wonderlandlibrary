/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

public final class UCharacterUtility {
    private static final int NON_CHARACTER_SUFFIX_MIN_3_0_ = 65534;
    private static final int NON_CHARACTER_MIN_3_1_ = 64976;
    private static final int NON_CHARACTER_MAX_3_1_ = 65007;

    public static boolean isNonCharacter(int ch) {
        if ((ch & 0xFFFE) == 65534) {
            return true;
        }
        return ch >= 64976 && ch <= 65007;
    }

    static int toInt(char msc, char lsc) {
        return msc << 16 | lsc;
    }

    static int getNullTermByteSubString(StringBuffer str, byte[] array, int index) {
        byte b2 = 1;
        while (b2 != 0) {
            b2 = array[index];
            if (b2 != 0) {
                str.append((char)(b2 & 0xFF));
            }
            ++index;
        }
        return index;
    }

    static int compareNullTermByteSubString(String str, byte[] array, int strindex, int aindex) {
        byte b2 = 1;
        int length = str.length();
        while (b2 != 0) {
            b2 = array[aindex];
            ++aindex;
            if (b2 == 0) break;
            if (strindex == length || str.charAt(strindex) != (char)(b2 & 0xFF)) {
                return -1;
            }
            ++strindex;
        }
        return strindex;
    }

    static int skipNullTermByteSubString(byte[] array, int index, int skipcount) {
        for (int i2 = 0; i2 < skipcount; ++i2) {
            byte b2 = 1;
            while (b2 != 0) {
                b2 = array[index];
                ++index;
            }
        }
        return index;
    }

    static int skipByteSubString(byte[] array, int index, int length, byte skipend) {
        int result;
        for (result = 0; result < length; ++result) {
            byte b2 = array[index + result];
            if (b2 != skipend) continue;
            ++result;
            break;
        }
        return result;
    }

    private UCharacterUtility() {
    }
}

