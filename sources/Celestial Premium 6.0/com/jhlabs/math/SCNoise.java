/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.math;

import com.jhlabs.math.Function1D;
import com.jhlabs.math.Function2D;
import com.jhlabs.math.Function3D;
import java.util.Random;

public class SCNoise
implements Function1D,
Function2D,
Function3D {
    private static Random randomGenerator = new Random();
    public short[] perm;
    private static final int TABSIZE = 256;
    private static final int TABMASK = 255;
    private static final int NIMPULSES = 3;
    private static float[] impulseTab;
    private static final int SAMPRATE = 100;
    private static final int NENTRIES = 401;
    private static float[] table;

    public SCNoise() {
        short[] arrs = new short[256];
        arrs[0] = 225;
        arrs[1] = 155;
        arrs[2] = 210;
        arrs[3] = 108;
        arrs[4] = 175;
        arrs[5] = 199;
        arrs[6] = 221;
        arrs[7] = 144;
        arrs[8] = 203;
        arrs[9] = 116;
        arrs[10] = 70;
        arrs[11] = 213;
        arrs[12] = 69;
        arrs[13] = 158;
        arrs[14] = 33;
        arrs[15] = 252;
        arrs[16] = 5;
        arrs[17] = 82;
        arrs[18] = 173;
        arrs[19] = 133;
        arrs[20] = 222;
        arrs[21] = 139;
        arrs[22] = 174;
        arrs[23] = 27;
        arrs[24] = 9;
        arrs[25] = 71;
        arrs[26] = 90;
        arrs[27] = 246;
        arrs[28] = 75;
        arrs[29] = 130;
        arrs[30] = 91;
        arrs[31] = 191;
        arrs[32] = 169;
        arrs[33] = 138;
        arrs[34] = 2;
        arrs[35] = 151;
        arrs[36] = 194;
        arrs[37] = 235;
        arrs[38] = 81;
        arrs[39] = 7;
        arrs[40] = 25;
        arrs[41] = 113;
        arrs[42] = 228;
        arrs[43] = 159;
        arrs[44] = 205;
        arrs[45] = 253;
        arrs[46] = 134;
        arrs[47] = 142;
        arrs[48] = 248;
        arrs[49] = 65;
        arrs[50] = 224;
        arrs[51] = 217;
        arrs[52] = 22;
        arrs[53] = 121;
        arrs[54] = 229;
        arrs[55] = 63;
        arrs[56] = 89;
        arrs[57] = 103;
        arrs[58] = 96;
        arrs[59] = 104;
        arrs[60] = 156;
        arrs[61] = 17;
        arrs[62] = 201;
        arrs[63] = 129;
        arrs[64] = 36;
        arrs[65] = 8;
        arrs[66] = 165;
        arrs[67] = 110;
        arrs[68] = 237;
        arrs[69] = 117;
        arrs[70] = 231;
        arrs[71] = 56;
        arrs[72] = 132;
        arrs[73] = 211;
        arrs[74] = 152;
        arrs[75] = 20;
        arrs[76] = 181;
        arrs[77] = 111;
        arrs[78] = 239;
        arrs[79] = 218;
        arrs[80] = 170;
        arrs[81] = 163;
        arrs[82] = 51;
        arrs[83] = 172;
        arrs[84] = 157;
        arrs[85] = 47;
        arrs[86] = 80;
        arrs[87] = 212;
        arrs[88] = 176;
        arrs[89] = 250;
        arrs[90] = 87;
        arrs[91] = 49;
        arrs[92] = 99;
        arrs[93] = 242;
        arrs[94] = 136;
        arrs[95] = 189;
        arrs[96] = 162;
        arrs[97] = 115;
        arrs[98] = 44;
        arrs[99] = 43;
        arrs[100] = 124;
        arrs[101] = 94;
        arrs[102] = 150;
        arrs[103] = 16;
        arrs[104] = 141;
        arrs[105] = 247;
        arrs[106] = 32;
        arrs[107] = 10;
        arrs[108] = 198;
        arrs[109] = 223;
        arrs[110] = 255;
        arrs[111] = 72;
        arrs[112] = 53;
        arrs[113] = 131;
        arrs[114] = 84;
        arrs[115] = 57;
        arrs[116] = 220;
        arrs[117] = 197;
        arrs[118] = 58;
        arrs[119] = 50;
        arrs[120] = 208;
        arrs[121] = 11;
        arrs[122] = 241;
        arrs[123] = 28;
        arrs[124] = 3;
        arrs[125] = 192;
        arrs[126] = 62;
        arrs[127] = 202;
        arrs[128] = 18;
        arrs[129] = 215;
        arrs[130] = 153;
        arrs[131] = 24;
        arrs[132] = 76;
        arrs[133] = 41;
        arrs[134] = 15;
        arrs[135] = 179;
        arrs[136] = 39;
        arrs[137] = 46;
        arrs[138] = 55;
        arrs[139] = 6;
        arrs[140] = 128;
        arrs[141] = 167;
        arrs[142] = 23;
        arrs[143] = 188;
        arrs[144] = 106;
        arrs[145] = 34;
        arrs[146] = 187;
        arrs[147] = 140;
        arrs[148] = 164;
        arrs[149] = 73;
        arrs[150] = 112;
        arrs[151] = 182;
        arrs[152] = 244;
        arrs[153] = 195;
        arrs[154] = 227;
        arrs[155] = 13;
        arrs[156] = 35;
        arrs[157] = 77;
        arrs[158] = 196;
        arrs[159] = 185;
        arrs[160] = 26;
        arrs[161] = 200;
        arrs[162] = 226;
        arrs[163] = 119;
        arrs[164] = 31;
        arrs[165] = 123;
        arrs[166] = 168;
        arrs[167] = 125;
        arrs[168] = 249;
        arrs[169] = 68;
        arrs[170] = 183;
        arrs[171] = 230;
        arrs[172] = 177;
        arrs[173] = 135;
        arrs[174] = 160;
        arrs[175] = 180;
        arrs[176] = 12;
        arrs[177] = 1;
        arrs[178] = 243;
        arrs[179] = 148;
        arrs[180] = 102;
        arrs[181] = 166;
        arrs[182] = 38;
        arrs[183] = 238;
        arrs[184] = 251;
        arrs[185] = 37;
        arrs[186] = 240;
        arrs[187] = 126;
        arrs[188] = 64;
        arrs[189] = 74;
        arrs[190] = 161;
        arrs[191] = 40;
        arrs[192] = 184;
        arrs[193] = 149;
        arrs[194] = 171;
        arrs[195] = 178;
        arrs[196] = 101;
        arrs[197] = 66;
        arrs[198] = 29;
        arrs[199] = 59;
        arrs[200] = 146;
        arrs[201] = 61;
        arrs[202] = 254;
        arrs[203] = 107;
        arrs[204] = 42;
        arrs[205] = 86;
        arrs[206] = 154;
        arrs[207] = 4;
        arrs[208] = 236;
        arrs[209] = 232;
        arrs[210] = 120;
        arrs[211] = 21;
        arrs[212] = 233;
        arrs[213] = 209;
        arrs[214] = 45;
        arrs[215] = 98;
        arrs[216] = 193;
        arrs[217] = 114;
        arrs[218] = 78;
        arrs[219] = 19;
        arrs[220] = 206;
        arrs[221] = 14;
        arrs[222] = 118;
        arrs[223] = 127;
        arrs[224] = 48;
        arrs[225] = 79;
        arrs[226] = 147;
        arrs[227] = 85;
        arrs[228] = 30;
        arrs[229] = 207;
        arrs[230] = 219;
        arrs[231] = 54;
        arrs[232] = 88;
        arrs[233] = 234;
        arrs[234] = 190;
        arrs[235] = 122;
        arrs[236] = 95;
        arrs[237] = 67;
        arrs[238] = 143;
        arrs[239] = 109;
        arrs[240] = 137;
        arrs[241] = 214;
        arrs[242] = 145;
        arrs[243] = 93;
        arrs[244] = 92;
        arrs[245] = 100;
        arrs[246] = 245;
        arrs[248] = 216;
        arrs[249] = 186;
        arrs[250] = 60;
        arrs[251] = 83;
        arrs[252] = 105;
        arrs[253] = 97;
        arrs[254] = 204;
        arrs[255] = 52;
        this.perm = arrs;
    }

    @Override
    public float evaluate(float x) {
        return this.evaluate(x, 0.1f);
    }

    @Override
    public float evaluate(float x, float y) {
        float sum = 0.0f;
        if (impulseTab == null) {
            impulseTab = SCNoise.impulseTabInit(665);
        }
        int ix = SCNoise.floor(x);
        float fx = x - (float)ix;
        int iy = SCNoise.floor(y);
        float fy = y - (float)iy;
        int m = 2;
        for (int i = -m; i <= m; ++i) {
            for (int j = -m; j <= m; ++j) {
                int h = this.perm[ix + i + this.perm[iy + j & 0xFF] & 0xFF];
                for (int n = 3; n > 0; --n) {
                    int h4 = h * 4;
                    float dx = fx - ((float)i + impulseTab[h4++]);
                    float dy = fy - ((float)j + impulseTab[h4++]);
                    float distsq = dx * dx + dy * dy;
                    sum += this.catrom2(distsq) * impulseTab[h4];
                    h = h + 1 & 0xFF;
                }
            }
        }
        return sum / 3.0f;
    }

    @Override
    public float evaluate(float x, float y, float z) {
        float sum = 0.0f;
        if (impulseTab == null) {
            impulseTab = SCNoise.impulseTabInit(665);
        }
        int ix = SCNoise.floor(x);
        float fx = x - (float)ix;
        int iy = SCNoise.floor(y);
        float fy = y - (float)iy;
        int iz = SCNoise.floor(z);
        float fz = z - (float)iz;
        int m = 2;
        for (int i = -m; i <= m; ++i) {
            for (int j = -m; j <= m; ++j) {
                for (int k = -m; k <= m; ++k) {
                    int h = this.perm[ix + i + this.perm[iy + j + this.perm[iz + k & 0xFF] & 0xFF] & 0xFF];
                    for (int n = 3; n > 0; --n) {
                        int h4 = h * 4;
                        float dx = fx - ((float)i + impulseTab[h4++]);
                        float dy = fy - ((float)j + impulseTab[h4++]);
                        float dz = fz - ((float)k + impulseTab[h4++]);
                        float distsq = dx * dx + dy * dy + dz * dz;
                        sum += this.catrom2(distsq) * impulseTab[h4];
                        h = h + 1 & 0xFF;
                    }
                }
            }
        }
        return sum / 3.0f;
    }

    public static int floor(float x) {
        int ix = (int)x;
        if (x < 0.0f && x != (float)ix) {
            return ix - 1;
        }
        return ix;
    }

    public float catrom2(float d) {
        int i;
        if (d >= 4.0f) {
            return 0.0f;
        }
        if (table == null) {
            table = new float[401];
            for (i = 0; i < 401; ++i) {
                float x = (float)i / 100.0f;
                SCNoise.table[i] = (x = (float)Math.sqrt(x)) < 1.0f ? 0.5f * (2.0f + x * x * (-5.0f + x * 3.0f)) : 0.5f * (4.0f + x * (-8.0f + x * (5.0f - x)));
            }
        }
        if ((i = SCNoise.floor(d = d * 100.0f + 0.5f)) >= 401) {
            return 0.0f;
        }
        return table[i];
    }

    static float[] impulseTabInit(int seed) {
        float[] impulseTab = new float[1024];
        randomGenerator = new Random(seed);
        for (int i = 0; i < 256; ++i) {
            impulseTab[i++] = randomGenerator.nextFloat();
            impulseTab[i++] = randomGenerator.nextFloat();
            impulseTab[i++] = randomGenerator.nextFloat();
            impulseTab[i++] = 1.0f - 2.0f * randomGenerator.nextFloat();
        }
        return impulseTab;
    }
}

