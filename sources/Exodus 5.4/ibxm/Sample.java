/*
 * Decompiled with CFR 0.152.
 */
package ibxm;

public class Sample {
    public int fine_tune;
    private int loop_start;
    private static final int POINT_SHIFT = 4;
    private int loop_length;
    public String name = "";
    public int volume;
    public int c2_rate = 8363;
    public boolean set_panning;
    public int relative_note;
    private short[] sample_data;
    private static final int INTERP_SHIFT = 11;
    private static final int OVERLAP = 8;
    private static final int INTERP_BITMASK = 2047;
    private static final short[] sinc_table;
    private static final int POINTS = 16;
    public int panning;

    public void set_sample_data(short[] sArray, int n, int n2, boolean bl) {
        if (n < 0) {
            n = 0;
        }
        if (n >= sArray.length) {
            n = sArray.length - 1;
        }
        if (n + n2 > sArray.length) {
            n2 = sArray.length - n;
        }
        if (n2 <= 1) {
            this.sample_data = new short[8 + sArray.length + 24];
            System.arraycopy(sArray, 0, this.sample_data, 8, sArray.length);
            int n3 = 0;
            while (n3 < 8) {
                short s = this.sample_data[8 + sArray.length - 1];
                this.sample_data[8 + sArray.length + n3] = s = (short)(s * (8 - n3) / 8);
                ++n3;
            }
            n = 8 + sArray.length + 8;
            n2 = 1;
        } else {
            short s;
            int n4;
            if (bl) {
                this.sample_data = new short[8 + n + n2 * 2 + 16];
                System.arraycopy(sArray, 0, this.sample_data, 8, n + n2);
                n4 = 0;
                while (n4 < n2) {
                    this.sample_data[8 + n + n2 + n4] = s = sArray[n + n2 - n4 - 1];
                    ++n4;
                }
                n += 8;
                n2 *= 2;
            } else {
                this.sample_data = new short[8 + n + n2 + 16];
                System.arraycopy(sArray, 0, this.sample_data, 8, n + n2);
                n += 8;
            }
            n4 = 0;
            while (n4 < 16) {
                this.sample_data[n + n2 + n4] = s = this.sample_data[n + n4];
                ++n4;
            }
        }
        this.loop_start = n;
        this.loop_length = n2;
    }

    public Sample() {
        this.set_sample_data(new short[0], 0, 0, false);
    }

    static {
        short[] sArray = new short[272];
        sArray[1] = -7;
        sArray[2] = 27;
        sArray[3] = -71;
        sArray[4] = 142;
        sArray[5] = -227;
        sArray[6] = 299;
        sArray[7] = 32439;
        sArray[8] = 299;
        sArray[9] = -227;
        sArray[10] = 142;
        sArray[11] = -71;
        sArray[12] = 27;
        sArray[13] = -7;
        sArray[18] = -5;
        sArray[19] = 36;
        sArray[20] = -142;
        sArray[21] = 450;
        sArray[22] = -1439;
        sArray[23] = 32224;
        sArray[24] = 2302;
        sArray[25] = -974;
        sArray[26] = 455;
        sArray[27] = -190;
        sArray[28] = 64;
        sArray[29] = -15;
        sArray[30] = 2;
        sArray[33] = 6;
        sArray[34] = -33;
        sArray[35] = 128;
        sArray[36] = -391;
        sArray[37] = 1042;
        sArray[38] = -2894;
        sArray[39] = 31584;
        sArray[40] = 4540;
        sArray[41] = -1765;
        sArray[42] = 786;
        sArray[43] = -318;
        sArray[44] = 105;
        sArray[45] = -25;
        sArray[46] = 3;
        sArray[49] = 10;
        sArray[50] = -55;
        sArray[51] = 204;
        sArray[52] = -597;
        sArray[53] = 1533;
        sArray[54] = -4056;
        sArray[55] = 30535;
        sArray[56] = 6977;
        sArray[57] = -2573;
        sArray[58] = 1121;
        sArray[59] = -449;
        sArray[60] = 148;
        sArray[61] = -36;
        sArray[62] = 5;
        sArray[64] = -1;
        sArray[65] = 13;
        sArray[66] = -71;
        sArray[67] = 261;
        sArray[68] = -757;
        sArray[69] = 1916;
        sArray[70] = -4922;
        sArray[71] = 29105;
        sArray[72] = 9568;
        sArray[73] = -3366;
        sArray[74] = 1448;
        sArray[75] = -578;
        sArray[76] = 191;
        sArray[77] = -47;
        sArray[78] = 7;
        sArray[80] = -1;
        sArray[81] = 15;
        sArray[82] = -81;
        sArray[83] = 300;
        sArray[84] = -870;
        sArray[85] = 2185;
        sArray[86] = -5498;
        sArray[87] = 27328;
        sArray[88] = 12263;
        sArray[89] = -4109;
        sArray[90] = 1749;
        sArray[91] = -698;
        sArray[92] = 232;
        sArray[93] = -58;
        sArray[94] = 9;
        sArray[96] = -1;
        sArray[97] = 15;
        sArray[98] = -86;
        sArray[99] = 322;
        sArray[100] = -936;
        sArray[101] = 2343;
        sArray[102] = -5800;
        sArray[103] = 25249;
        sArray[104] = 15006;
        sArray[105] = -4765;
        sArray[106] = 2011;
        sArray[107] = -802;
        sArray[108] = 269;
        sArray[109] = -68;
        sArray[110] = 10;
        sArray[112] = -1;
        sArray[113] = 15;
        sArray[114] = -87;
        sArray[115] = 328;
        sArray[116] = -957;
        sArray[117] = 2394;
        sArray[118] = -5849;
        sArray[119] = 22920;
        sArray[120] = 17738;
        sArray[121] = -5298;
        sArray[122] = 2215;
        sArray[123] = -885;
        sArray[124] = 299;
        sArray[125] = -77;
        sArray[126] = 12;
        sArray[129] = 14;
        sArray[130] = -83;
        sArray[131] = 319;
        sArray[132] = -938;
        sArray[133] = 2347;
        sArray[134] = -5671;
        sArray[135] = 20396;
        sArray[136] = 20396;
        sArray[137] = -5671;
        sArray[138] = 2347;
        sArray[139] = -938;
        sArray[140] = 319;
        sArray[141] = -83;
        sArray[142] = 14;
        sArray[145] = 12;
        sArray[146] = -77;
        sArray[147] = 299;
        sArray[148] = -885;
        sArray[149] = 2215;
        sArray[150] = -5298;
        sArray[151] = 17738;
        sArray[152] = 22920;
        sArray[153] = -5849;
        sArray[154] = 2394;
        sArray[155] = -957;
        sArray[156] = 328;
        sArray[157] = -87;
        sArray[158] = 15;
        sArray[159] = -1;
        sArray[161] = 10;
        sArray[162] = -68;
        sArray[163] = 269;
        sArray[164] = -802;
        sArray[165] = 2011;
        sArray[166] = -4765;
        sArray[167] = 15006;
        sArray[168] = 25249;
        sArray[169] = -5800;
        sArray[170] = 2343;
        sArray[171] = -936;
        sArray[172] = 322;
        sArray[173] = -86;
        sArray[174] = 15;
        sArray[175] = -1;
        sArray[177] = 9;
        sArray[178] = -58;
        sArray[179] = 232;
        sArray[180] = -698;
        sArray[181] = 1749;
        sArray[182] = -4109;
        sArray[183] = 12263;
        sArray[184] = 27328;
        sArray[185] = -5498;
        sArray[186] = 2185;
        sArray[187] = -870;
        sArray[188] = 300;
        sArray[189] = -81;
        sArray[190] = 15;
        sArray[191] = -1;
        sArray[193] = 7;
        sArray[194] = -47;
        sArray[195] = 191;
        sArray[196] = -578;
        sArray[197] = 1448;
        sArray[198] = -3366;
        sArray[199] = 9568;
        sArray[200] = 29105;
        sArray[201] = -4922;
        sArray[202] = 1916;
        sArray[203] = -757;
        sArray[204] = 261;
        sArray[205] = -71;
        sArray[206] = 13;
        sArray[207] = -1;
        sArray[209] = 5;
        sArray[210] = -36;
        sArray[211] = 148;
        sArray[212] = -449;
        sArray[213] = 1121;
        sArray[214] = -2573;
        sArray[215] = 6977;
        sArray[216] = 30535;
        sArray[217] = -4056;
        sArray[218] = 1533;
        sArray[219] = -597;
        sArray[220] = 204;
        sArray[221] = -55;
        sArray[222] = 10;
        sArray[225] = 3;
        sArray[226] = -25;
        sArray[227] = 105;
        sArray[228] = -318;
        sArray[229] = 786;
        sArray[230] = -1765;
        sArray[231] = 4540;
        sArray[232] = 31584;
        sArray[233] = -2894;
        sArray[234] = 1042;
        sArray[235] = -391;
        sArray[236] = 128;
        sArray[237] = -33;
        sArray[238] = 6;
        sArray[241] = 2;
        sArray[242] = -15;
        sArray[243] = 64;
        sArray[244] = -190;
        sArray[245] = 455;
        sArray[246] = -974;
        sArray[247] = 2302;
        sArray[248] = 32224;
        sArray[249] = -1439;
        sArray[250] = 450;
        sArray[251] = -142;
        sArray[252] = 36;
        sArray[253] = -5;
        sArray[258] = -7;
        sArray[259] = 27;
        sArray[260] = -71;
        sArray[261] = 142;
        sArray[262] = -227;
        sArray[263] = 299;
        sArray[264] = 32439;
        sArray[265] = 299;
        sArray[266] = -227;
        sArray[267] = 142;
        sArray[268] = -71;
        sArray[269] = 27;
        sArray[270] = -7;
        sinc_table = sArray;
    }

    public void resample_nearest(int n, int n2, int n3, int n4, int n5, int[] nArray, int n6, int n7) {
        n += 8;
        int n8 = this.loop_start + this.loop_length - 1;
        int n9 = n6 << 1;
        int n10 = n6 + n7 - 1 << 1;
        while (n9 <= n10) {
            if (n > n8) {
                if (this.loop_length <= 1) break;
                n = this.loop_start + (n - this.loop_start) % this.loop_length;
            }
            int n11 = n9;
            nArray[n11] = nArray[n11] + (this.sample_data[n] * n4 >> 15);
            int n12 = n9 + 1;
            nArray[n12] = nArray[n12] + (this.sample_data[n] * n5 >> 15);
            n9 += 2;
            n += (n2 += n3) >> 15;
            n2 &= Short.MAX_VALUE;
        }
    }

    public void resample_linear(int n, int n2, int n3, int n4, int n5, int[] nArray, int n6, int n7) {
        n += 8;
        int n8 = this.loop_start + this.loop_length - 1;
        int n9 = n6 << 1;
        int n10 = n6 + n7 - 1 << 1;
        while (n9 <= n10) {
            if (n > n8) {
                if (this.loop_length <= 1) break;
                n = this.loop_start + (n - this.loop_start) % this.loop_length;
            }
            int n11 = this.sample_data[n];
            n11 += (this.sample_data[n + 1] - n11) * n2 >> 15;
            int n12 = n9;
            nArray[n12] = nArray[n12] + (n11 * n4 >> 15);
            int n13 = n9 + 1;
            nArray[n13] = nArray[n13] + (n11 * n5 >> 15);
            n9 += 2;
            n += (n2 += n3) >> 15;
            n2 &= Short.MAX_VALUE;
        }
    }

    public void resample_sinc(int n, int n2, int n3, int n4, int n5, int[] nArray, int n6, int n7) {
        int n8 = this.loop_start + this.loop_length - 1;
        int n9 = n6 << 1;
        int n10 = n6 + n7 - 1 << 1;
        while (n9 <= n10) {
            if (n > n8) {
                if (this.loop_length <= 1) break;
                n = this.loop_start + (n - this.loop_start) % this.loop_length;
            }
            int n11 = n2 >> 11 << 4;
            int n12 = sinc_table[n11 + 0] * this.sample_data[n + 0] >> 15;
            n12 += sinc_table[n11 + 1] * this.sample_data[n + 1] >> 15;
            n12 += sinc_table[n11 + 2] * this.sample_data[n + 2] >> 15;
            n12 += sinc_table[n11 + 3] * this.sample_data[n + 3] >> 15;
            n12 += sinc_table[n11 + 4] * this.sample_data[n + 4] >> 15;
            n12 += sinc_table[n11 + 5] * this.sample_data[n + 5] >> 15;
            n12 += sinc_table[n11 + 6] * this.sample_data[n + 6] >> 15;
            n12 += sinc_table[n11 + 7] * this.sample_data[n + 7] >> 15;
            n12 += sinc_table[n11 + 8] * this.sample_data[n + 8] >> 15;
            n12 += sinc_table[n11 + 9] * this.sample_data[n + 9] >> 15;
            n12 += sinc_table[n11 + 10] * this.sample_data[n + 10] >> 15;
            n12 += sinc_table[n11 + 11] * this.sample_data[n + 11] >> 15;
            n12 += sinc_table[n11 + 12] * this.sample_data[n + 12] >> 15;
            n12 += sinc_table[n11 + 13] * this.sample_data[n + 13] >> 15;
            n12 += sinc_table[n11 + 14] * this.sample_data[n + 14] >> 15;
            n12 += sinc_table[n11 + 15] * this.sample_data[n + 15] >> 15;
            int n13 = sinc_table[n11 + 16] * this.sample_data[n + 0] >> 15;
            n13 += sinc_table[n11 + 17] * this.sample_data[n + 1] >> 15;
            n13 += sinc_table[n11 + 18] * this.sample_data[n + 2] >> 15;
            n13 += sinc_table[n11 + 19] * this.sample_data[n + 3] >> 15;
            n13 += sinc_table[n11 + 20] * this.sample_data[n + 4] >> 15;
            n13 += sinc_table[n11 + 21] * this.sample_data[n + 5] >> 15;
            n13 += sinc_table[n11 + 22] * this.sample_data[n + 6] >> 15;
            n13 += sinc_table[n11 + 23] * this.sample_data[n + 7] >> 15;
            n13 += sinc_table[n11 + 24] * this.sample_data[n + 8] >> 15;
            n13 += sinc_table[n11 + 25] * this.sample_data[n + 9] >> 15;
            n13 += sinc_table[n11 + 26] * this.sample_data[n + 10] >> 15;
            n13 += sinc_table[n11 + 27] * this.sample_data[n + 11] >> 15;
            n13 += sinc_table[n11 + 28] * this.sample_data[n + 12] >> 15;
            n13 += sinc_table[n11 + 29] * this.sample_data[n + 13] >> 15;
            n13 += sinc_table[n11 + 30] * this.sample_data[n + 14] >> 15;
            int n14 = n12 + (((n13 += sinc_table[n11 + 31] * this.sample_data[n + 15] >> 15) - n12) * (n2 & 0x7FF) >> 11);
            int n15 = n9;
            nArray[n15] = nArray[n15] + (n14 * n4 >> 15);
            int n16 = n9 + 1;
            nArray[n16] = nArray[n16] + (n14 * n5 >> 15);
            n9 += 2;
            n += (n2 += n3) >> 15;
            n2 &= Short.MAX_VALUE;
        }
    }

    public boolean has_finished(int n) {
        boolean bl = false;
        if (this.loop_length <= 1 && n > this.loop_start) {
            bl = true;
        }
        return bl;
    }
}

