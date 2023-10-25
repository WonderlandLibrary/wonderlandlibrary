/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.auth.util;

public class Base64 {
    private static final byte EQUALS_SIGN = 61;
    private static final byte WHITE_SPACE_ENC = -5;
    private static final byte EQUALS_SIGN_ENC = -1;
    private static final byte[] ALPHABET = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] DECODABET;

    static {
        byte[] byArray = new byte[256];
        byArray[0] = -9;
        byArray[1] = -9;
        byArray[2] = -9;
        byArray[3] = -9;
        byArray[4] = -9;
        byArray[5] = -9;
        byArray[6] = -9;
        byArray[7] = -9;
        byArray[8] = -9;
        byArray[9] = -5;
        byArray[10] = -5;
        byArray[11] = -9;
        byArray[12] = -9;
        byArray[13] = -5;
        byArray[14] = -9;
        byArray[15] = -9;
        byArray[16] = -9;
        byArray[17] = -9;
        byArray[18] = -9;
        byArray[19] = -9;
        byArray[20] = -9;
        byArray[21] = -9;
        byArray[22] = -9;
        byArray[23] = -9;
        byArray[24] = -9;
        byArray[25] = -9;
        byArray[26] = -9;
        byArray[27] = -9;
        byArray[28] = -9;
        byArray[29] = -9;
        byArray[30] = -9;
        byArray[31] = -9;
        byArray[32] = -5;
        byArray[33] = -9;
        byArray[34] = -9;
        byArray[35] = -9;
        byArray[36] = -9;
        byArray[37] = -9;
        byArray[38] = -9;
        byArray[39] = -9;
        byArray[40] = -9;
        byArray[41] = -9;
        byArray[42] = -9;
        byArray[43] = 62;
        byArray[44] = -9;
        byArray[45] = -9;
        byArray[46] = -9;
        byArray[47] = 63;
        byArray[48] = 52;
        byArray[49] = 53;
        byArray[50] = 54;
        byArray[51] = 55;
        byArray[52] = 56;
        byArray[53] = 57;
        byArray[54] = 58;
        byArray[55] = 59;
        byArray[56] = 60;
        byArray[57] = 61;
        byArray[58] = -9;
        byArray[59] = -9;
        byArray[60] = -9;
        byArray[61] = -1;
        byArray[62] = -9;
        byArray[63] = -9;
        byArray[64] = -9;
        byArray[66] = 1;
        byArray[67] = 2;
        byArray[68] = 3;
        byArray[69] = 4;
        byArray[70] = 5;
        byArray[71] = 6;
        byArray[72] = 7;
        byArray[73] = 8;
        byArray[74] = 9;
        byArray[75] = 10;
        byArray[76] = 11;
        byArray[77] = 12;
        byArray[78] = 13;
        byArray[79] = 14;
        byArray[80] = 15;
        byArray[81] = 16;
        byArray[82] = 17;
        byArray[83] = 18;
        byArray[84] = 19;
        byArray[85] = 20;
        byArray[86] = 21;
        byArray[87] = 22;
        byArray[88] = 23;
        byArray[89] = 24;
        byArray[90] = 25;
        byArray[91] = -9;
        byArray[92] = -9;
        byArray[93] = -9;
        byArray[94] = -9;
        byArray[95] = -9;
        byArray[96] = -9;
        byArray[97] = 26;
        byArray[98] = 27;
        byArray[99] = 28;
        byArray[100] = 29;
        byArray[101] = 30;
        byArray[102] = 31;
        byArray[103] = 32;
        byArray[104] = 33;
        byArray[105] = 34;
        byArray[106] = 35;
        byArray[107] = 36;
        byArray[108] = 37;
        byArray[109] = 38;
        byArray[110] = 39;
        byArray[111] = 40;
        byArray[112] = 41;
        byArray[113] = 42;
        byArray[114] = 43;
        byArray[115] = 44;
        byArray[116] = 45;
        byArray[117] = 46;
        byArray[118] = 47;
        byArray[119] = 48;
        byArray[120] = 49;
        byArray[121] = 50;
        byArray[122] = 51;
        byArray[123] = -9;
        byArray[124] = -9;
        byArray[125] = -9;
        byArray[126] = -9;
        byArray[127] = -9;
        byArray[128] = -9;
        byArray[129] = -9;
        byArray[130] = -9;
        byArray[131] = -9;
        byArray[132] = -9;
        byArray[133] = -9;
        byArray[134] = -9;
        byArray[135] = -9;
        byArray[136] = -9;
        byArray[137] = -9;
        byArray[138] = -9;
        byArray[139] = -9;
        byArray[140] = -9;
        byArray[141] = -9;
        byArray[142] = -9;
        byArray[143] = -9;
        byArray[144] = -9;
        byArray[145] = -9;
        byArray[146] = -9;
        byArray[147] = -9;
        byArray[148] = -9;
        byArray[149] = -9;
        byArray[150] = -9;
        byArray[151] = -9;
        byArray[152] = -9;
        byArray[153] = -9;
        byArray[154] = -9;
        byArray[155] = -9;
        byArray[156] = -9;
        byArray[157] = -9;
        byArray[158] = -9;
        byArray[159] = -9;
        byArray[160] = -9;
        byArray[161] = -9;
        byArray[162] = -9;
        byArray[163] = -9;
        byArray[164] = -9;
        byArray[165] = -9;
        byArray[166] = -9;
        byArray[167] = -9;
        byArray[168] = -9;
        byArray[169] = -9;
        byArray[170] = -9;
        byArray[171] = -9;
        byArray[172] = -9;
        byArray[173] = -9;
        byArray[174] = -9;
        byArray[175] = -9;
        byArray[176] = -9;
        byArray[177] = -9;
        byArray[178] = -9;
        byArray[179] = -9;
        byArray[180] = -9;
        byArray[181] = -9;
        byArray[182] = -9;
        byArray[183] = -9;
        byArray[184] = -9;
        byArray[185] = -9;
        byArray[186] = -9;
        byArray[187] = -9;
        byArray[188] = -9;
        byArray[189] = -9;
        byArray[190] = -9;
        byArray[191] = -9;
        byArray[192] = -9;
        byArray[193] = -9;
        byArray[194] = -9;
        byArray[195] = -9;
        byArray[196] = -9;
        byArray[197] = -9;
        byArray[198] = -9;
        byArray[199] = -9;
        byArray[200] = -9;
        byArray[201] = -9;
        byArray[202] = -9;
        byArray[203] = -9;
        byArray[204] = -9;
        byArray[205] = -9;
        byArray[206] = -9;
        byArray[207] = -9;
        byArray[208] = -9;
        byArray[209] = -9;
        byArray[210] = -9;
        byArray[211] = -9;
        byArray[212] = -9;
        byArray[213] = -9;
        byArray[214] = -9;
        byArray[215] = -9;
        byArray[216] = -9;
        byArray[217] = -9;
        byArray[218] = -9;
        byArray[219] = -9;
        byArray[220] = -9;
        byArray[221] = -9;
        byArray[222] = -9;
        byArray[223] = -9;
        byArray[224] = -9;
        byArray[225] = -9;
        byArray[226] = -9;
        byArray[227] = -9;
        byArray[228] = -9;
        byArray[229] = -9;
        byArray[230] = -9;
        byArray[231] = -9;
        byArray[232] = -9;
        byArray[233] = -9;
        byArray[234] = -9;
        byArray[235] = -9;
        byArray[236] = -9;
        byArray[237] = -9;
        byArray[238] = -9;
        byArray[239] = -9;
        byArray[240] = -9;
        byArray[241] = -9;
        byArray[242] = -9;
        byArray[243] = -9;
        byArray[244] = -9;
        byArray[245] = -9;
        byArray[246] = -9;
        byArray[247] = -9;
        byArray[248] = -9;
        byArray[249] = -9;
        byArray[250] = -9;
        byArray[251] = -9;
        byArray[252] = -9;
        byArray[253] = -9;
        byArray[254] = -9;
        byArray[255] = -9;
        DECODABET = byArray;
    }

    private Base64() {
    }

    public static byte[] encode(byte[] source) {
        if (source == null) {
            throw new NullPointerException("Cannot serialize a null array.");
        }
        byte[] outBuff = new byte[source.length / 3 * 4 + (source.length % 3 > 0 ? 4 : 0)];
        int d = 0;
        int e = 0;
        while (d < source.length - 2) {
            Base64.encode3to4(source, d, 3, outBuff, e);
            d += 3;
            e += 4;
        }
        if (d < source.length) {
            Base64.encode3to4(source, d, source.length - d, outBuff, e);
            e += 4;
        }
        if (e <= outBuff.length - 1) {
            byte[] finalOut = new byte[e];
            System.arraycopy(outBuff, 0, finalOut, 0, e);
            return finalOut;
        }
        return outBuff;
    }

    public static byte[] decode(byte[] source) {
        if (source == null) {
            throw new NullPointerException("Cannot decode null source array.");
        }
        byte[] outBuff = new byte[source.length * 3 / 4];
        byte[] b4 = new byte[4];
        int outBuffPosn = 0;
        int b4Posn = 0;
        int i = 0;
        while (i < source.length) {
            byte sbiDecode = DECODABET[source[i] & 0xFF];
            if (sbiDecode >= -5) {
                if (sbiDecode >= -1) {
                    b4[b4Posn++] = source[i];
                    if (b4Posn > 3) {
                        outBuffPosn += Base64.decode4to3(b4, 0, outBuff, outBuffPosn);
                        b4Posn = 0;
                        if (source[i] == 61) {
                            break;
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException(String.format("Bad Base64 input character decimal %d in array position %d", source[i] & 0xFF, i));
            }
            ++i;
        }
        byte[] out = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }

    private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset) {
        int inBuff = (numSigBytes > 0 ? source[srcOffset] << 24 >>> 8 : 0) | (numSigBytes > 1 ? source[srcOffset + 1] << 24 >>> 16 : 0) | (numSigBytes > 2 ? source[srcOffset + 2] << 24 >>> 24 : 0);
        switch (numSigBytes) {
            case 3: {
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
                destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
                destination[destOffset + 3] = ALPHABET[inBuff & 0x3F];
                return destination;
            }
            case 2: {
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
                destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
                destination[destOffset + 3] = 61;
                return destination;
            }
            case 1: {
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
                destination[destOffset + 2] = 61;
                destination[destOffset + 3] = 61;
                return destination;
            }
        }
        return destination;
    }

    private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset) {
        if (source == null) {
            throw new NullPointerException("Source array was null.");
        }
        if (destination == null) {
            throw new NullPointerException("Destination array was null.");
        }
        if (srcOffset < 0 || srcOffset + 3 >= source.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", source.length, srcOffset));
        }
        if (destOffset < 0 || destOffset + 2 >= destination.length) {
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", destination.length, destOffset));
        }
        if (source[srcOffset + 2] == 61) {
            int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12;
            destination[destOffset] = (byte)(outBuff >>> 16);
            return 1;
        }
        if (source[srcOffset + 3] == 61) {
            int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12 | (DECODABET[source[srcOffset + 2]] & 0xFF) << 6;
            destination[destOffset] = (byte)(outBuff >>> 16);
            destination[destOffset + 1] = (byte)(outBuff >>> 8);
            return 2;
        }
        int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12 | (DECODABET[source[srcOffset + 2]] & 0xFF) << 6 | DECODABET[source[srcOffset + 3]] & 0xFF;
        destination[destOffset] = (byte)(outBuff >> 16);
        destination[destOffset + 1] = (byte)(outBuff >> 8);
        destination[destOffset + 2] = (byte)outBuff;
        return 3;
    }
}

