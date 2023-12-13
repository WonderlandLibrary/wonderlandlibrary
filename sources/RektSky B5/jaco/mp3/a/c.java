/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.D;
import jaco.mp3.a.c_0;
import jaco.mp3.a.d_0;
import jaco.mp3.a.e_0;
import jaco.mp3.a.h;
import jaco.mp3.a.j;
import jaco.mp3.a.k;
import jaco.mp3.a.l;
import jaco.mp3.a.m;
import jaco.mp3.a.n;
import jaco.mp3.a.y;
import jaco.mp3.a.z;

final class c
implements k {
    private int[] a;
    private int b = 0;
    private int[] c;
    private float[][][] d;
    private float[][][] e;
    private float[] f;
    private float[][] g;
    private float[][] h;
    private int[] i;
    private z j;
    private D k;
    private y l;
    private y m;
    private c_0 n;
    private int o;
    private h p;
    private d_0 q;
    private l[] r;
    private l[] s;
    private int t;
    private int u;
    private int v;
    private int w;
    private int x;
    private int y;
    private int z;
    private float[] A = new float[32];
    private float[] B = new float[32];
    private final int[] C = new int[4];
    private int[] D = new int[1];
    private int[] E = new int[1];
    private int[] F = new int[1];
    private int[] G = new int[1];
    private int[] H = new int[576];
    private float[] I = new float[576];
    private float[] J = new float[18];
    private float[] K = new float[36];
    private int L = 0;
    private static final int[][] M;
    private static int[] N;
    private e_0[] O;
    private static float[] P;
    private static float[] Q;
    private static float[][] R;
    private static float[] S;
    private static int[][] T;
    private static final float[] U;
    private static final float[] V;
    private static float[][] W;
    private static int[][][] X;

    static {
        int[][] nArrayArray = new int[2][];
        int[] nArray = new int[16];
        nArray[4] = 3;
        nArray[5] = 1;
        nArray[6] = 1;
        nArray[7] = 1;
        nArray[8] = 2;
        nArray[9] = 2;
        nArray[10] = 2;
        nArray[11] = 3;
        nArray[12] = 3;
        nArray[13] = 3;
        nArray[14] = 4;
        nArray[15] = 4;
        nArrayArray[0] = nArray;
        int[] nArray2 = new int[16];
        nArray2[1] = 1;
        nArray2[2] = 2;
        nArray2[3] = 3;
        nArray2[5] = 1;
        nArray2[6] = 2;
        nArray2[7] = 3;
        nArray2[8] = 1;
        nArray2[9] = 2;
        nArray2[10] = 3;
        nArray2[11] = 1;
        nArray2[12] = 2;
        nArray2[13] = 3;
        nArray2[14] = 2;
        nArray2[15] = 3;
        nArrayArray[1] = nArray2;
        M = nArrayArray;
        int[] nArray3 = new int[22];
        nArray3[11] = 1;
        nArray3[12] = 1;
        nArray3[13] = 1;
        nArray3[14] = 1;
        nArray3[15] = 2;
        nArray3[16] = 2;
        nArray3[17] = 3;
        nArray3[18] = 3;
        nArray3[19] = 3;
        nArray3[20] = 2;
        N = nArray3;
        P = new float[]{1.0f, 0.70710677f, 0.5f, 0.35355338f, 0.25f, 0.17677669f, 0.125f, 0.088388346f, 0.0625f, 0.044194173f, 0.03125f, 0.022097087f, 0.015625f, 0.011048543f, 0.0078125f, 0.0055242716f, 0.00390625f, 0.0027621358f, 0.001953125f, 0.0013810679f, 9.765625E-4f, 6.9053395E-4f, 4.8828125E-4f, 3.4526698E-4f, 2.4414062E-4f, 1.7263349E-4f, 1.2207031E-4f, 8.6316744E-5f, 6.1035156E-5f, 4.3158372E-5f, 3.0517578E-5f, 2.1579186E-5f, 1.5258789E-5f, 1.0789593E-5f, 7.6293945E-6f, 5.3947965E-6f, 3.8146973E-6f, 2.6973983E-6f, 1.9073486E-6f, 1.3486991E-6f, 9.536743E-7f, 6.7434956E-7f, 4.7683716E-7f, 3.3717478E-7f, 2.3841858E-7f, 1.6858739E-7f, 1.1920929E-7f, 8.4293696E-8f, 5.9604645E-8f, 4.2146848E-8f, 2.9802322E-8f, 2.1073424E-8f, 1.4901161E-8f, 1.0536712E-8f, 7.4505806E-9f, 5.268356E-9f, 3.7252903E-9f, 2.634178E-9f, 1.8626451E-9f, 1.317089E-9f, 9.313226E-10f, 6.585445E-10f, 4.656613E-10f, 3.2927225E-10f};
        float[] fArray = new float[8192];
        int n2 = 0;
        while (n2 < 8192) {
            fArray[n2] = (float)Math.pow(n2, 1.3333333333333333);
            ++n2;
        }
        Q = fArray;
        R = new float[][]{{1.0f, 0.8408964f, 0.70710677f, 0.59460354f, 0.5f, 0.4204482f, 0.35355338f, 0.29730177f, 0.25f, 0.2102241f, 0.17677669f, 0.14865088f, 0.125f, 0.10511205f, 0.088388346f, 0.07432544f, 0.0625f, 0.052556027f, 0.044194173f, 0.03716272f, 0.03125f, 0.026278013f, 0.022097087f, 0.01858136f, 0.015625f, 0.013139007f, 0.011048543f, 0.00929068f, 0.0078125f, 0.0065695033f, 0.0055242716f, 0.00464534f}, {1.0f, 0.70710677f, 0.5f, 0.35355338f, 0.25f, 0.17677669f, 0.125f, 0.088388346f, 0.0625f, 0.044194173f, 0.03125f, 0.022097087f, 0.015625f, 0.011048543f, 0.0078125f, 0.0055242716f, 0.00390625f, 0.0027621358f, 0.001953125f, 0.0013810679f, 9.765625E-4f, 6.9053395E-4f, 4.8828125E-4f, 3.4526698E-4f, 2.4414062E-4f, 1.7263349E-4f, 1.2207031E-4f, 8.6316744E-5f, 6.1035156E-5f, 4.3158372E-5f, 3.0517578E-5f, 2.1579186E-5f}};
        S = new float[]{0.0f, 0.2679492f, 0.57735026f, 1.0f, 1.7320508f, 3.732051f, 9.9999998E10f, -3.732051f, -1.7320508f, -1.0f, -0.57735026f, -0.2679492f, 0.0f, 0.2679492f, 0.57735026f, 1.0f};
        U = new float[]{0.8574929f, 0.881742f, 0.94962865f, 0.9833146f, 0.9955178f, 0.9991606f, 0.9998992f, 0.99999315f};
        V = new float[]{-0.51449573f, -0.47173196f, -0.31337744f, -0.1819132f, -0.09457419f, -0.040965583f, -0.014198569f, -0.0036999746f};
        W = new float[][]{{-0.016141215f, -0.05360318f, -0.100707136f, -0.16280818f, -0.5f, -0.38388735f, -0.6206114f, -1.1659756f, -3.8720753f, -4.225629f, -1.519529f, -0.97416484f, -0.73744076f, -1.2071068f, -0.5163616f, -0.45426053f, -0.40715656f, -0.3696946f, -0.3387627f, -0.31242222f, -0.28939587f, -0.26880082f, -0.5f, -0.23251417f, -0.21596715f, -0.20004979f, -0.18449493f, -0.16905846f, -0.15350361f, -0.13758625f, -0.12103922f, -0.20710678f, -0.084752575f, -0.06415752f, -0.041131172f, -0.014790705f}, {-0.016141215f, -0.05360318f, -0.100707136f, -0.16280818f, -0.5f, -0.38388735f, -0.6206114f, -1.1659756f, -3.8720753f, -4.225629f, -1.519529f, -0.97416484f, -0.73744076f, -1.2071068f, -0.5163616f, -0.45426053f, -0.40715656f, -0.3696946f, -0.33908543f, -0.3151181f, -0.29642227f, -0.28184548f, -0.5411961f, -0.2621323f, -0.25387916f, -0.2329629f, -0.19852729f, -0.15233535f, -0.0964964f, -0.03342383f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, {-0.0483008f, -0.15715657f, -0.28325045f, -0.42953748f, -1.2071068f, -0.8242648f, -1.1451749f, -1.769529f, -4.5470223f, -3.489053f, -0.7329629f, -0.15076515f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.15076514f, -0.7329629f, -3.489053f, -4.5470223f, -1.769529f, -1.1451749f, -0.8313774f, -1.306563f, -0.54142016f, -0.46528974f, -0.4106699f, -0.3700468f, -0.3387627f, -0.31242222f, -0.28939587f, -0.26880082f, -0.5f, -0.23251417f, -0.21596715f, -0.20004979f, -0.18449493f, -0.16905846f, -0.15350361f, -0.13758625f, -0.12103922f, -0.20710678f, -0.084752575f, -0.06415752f, -0.041131172f, -0.014790705f}};
        int[][][] nArrayArray2 = new int[6][][];
        nArrayArray2[0] = new int[][]{{6, 5, 5, 5}, {9, 9, 9, 9}, {6, 9, 9, 9}};
        nArrayArray2[1] = new int[][]{{6, 5, 7, 3}, {9, 9, 12, 6}, {6, 9, 12, 6}};
        int[][] nArrayArray3 = new int[3][];
        int[] nArray4 = new int[4];
        nArray4[0] = 11;
        nArray4[1] = 10;
        nArrayArray3[0] = nArray4;
        int[] nArray5 = new int[4];
        nArray5[0] = 18;
        nArray5[1] = 18;
        nArrayArray3[1] = nArray5;
        int[] nArray6 = new int[4];
        nArray6[0] = 15;
        nArray6[1] = 18;
        nArrayArray3[2] = nArray6;
        nArrayArray2[2] = nArrayArray3;
        int[][] nArrayArray4 = new int[3][];
        int[] nArray7 = new int[4];
        nArray7[0] = 7;
        nArray7[1] = 7;
        nArray7[2] = 7;
        nArrayArray4[0] = nArray7;
        int[] nArray8 = new int[4];
        nArray8[0] = 12;
        nArray8[1] = 12;
        nArray8[2] = 12;
        nArrayArray4[1] = nArray8;
        int[] nArray9 = new int[4];
        nArray9[0] = 6;
        nArray9[1] = 15;
        nArray9[2] = 12;
        nArrayArray4[2] = nArray9;
        nArrayArray2[3] = nArrayArray4;
        nArrayArray2[4] = new int[][]{{6, 6, 6, 3}, {12, 9, 9, 6}, {6, 12, 9, 6}};
        int[][] nArrayArray5 = new int[3][];
        int[] nArray10 = new int[4];
        nArray10[0] = 8;
        nArray10[1] = 8;
        nArray10[2] = 5;
        nArrayArray5[0] = nArray10;
        int[] nArray11 = new int[4];
        nArray11[0] = 15;
        nArray11[1] = 12;
        nArray11[2] = 9;
        nArrayArray5[1] = nArray11;
        int[] nArray12 = new int[4];
        nArray12[0] = 6;
        nArray12[1] = 18;
        nArray12[2] = 9;
        nArrayArray5[2] = nArray12;
        nArrayArray2[5] = nArrayArray5;
        X = nArrayArray2;
    }

    public c(z z2, D d2, y y2, y y3, c_0 c_02, int n2) {
        jaco.mp3.a.m.a();
        this.c = new int[580];
        this.d = new float[2][32][18];
        this.e = new float[2][32][18];
        this.f = new float[576];
        this.g = new float[2][576];
        this.h = new float[2][576];
        this.i = new int[2];
        this.r = new l[2];
        this.r[0] = new l();
        this.r[1] = new l();
        this.s = this.r;
        this.O = new e_0[9];
        int[] nArray = new int[23];
        nArray[1] = 6;
        nArray[2] = 12;
        nArray[3] = 18;
        nArray[4] = 24;
        nArray[5] = 30;
        nArray[6] = 36;
        nArray[7] = 44;
        nArray[8] = 54;
        nArray[9] = 66;
        nArray[10] = 80;
        nArray[11] = 96;
        nArray[12] = 116;
        nArray[13] = 140;
        nArray[14] = 168;
        nArray[15] = 200;
        nArray[16] = 238;
        nArray[17] = 284;
        nArray[18] = 336;
        nArray[19] = 396;
        nArray[20] = 464;
        nArray[21] = 522;
        nArray[22] = 576;
        int[] nArray2 = nArray;
        int[] nArray3 = new int[14];
        nArray3[1] = 4;
        nArray3[2] = 8;
        nArray3[3] = 12;
        nArray3[4] = 18;
        nArray3[5] = 24;
        nArray3[6] = 32;
        nArray3[7] = 42;
        nArray3[8] = 56;
        nArray3[9] = 74;
        nArray3[10] = 100;
        nArray3[11] = 132;
        nArray3[12] = 174;
        nArray3[13] = 192;
        int[] nArray4 = nArray3;
        int[] nArray5 = new int[23];
        nArray5[1] = 6;
        nArray5[2] = 12;
        nArray5[3] = 18;
        nArray5[4] = 24;
        nArray5[5] = 30;
        nArray5[6] = 36;
        nArray5[7] = 44;
        nArray5[8] = 54;
        nArray5[9] = 66;
        nArray5[10] = 80;
        nArray5[11] = 96;
        nArray5[12] = 114;
        nArray5[13] = 136;
        nArray5[14] = 162;
        nArray5[15] = 194;
        nArray5[16] = 232;
        nArray5[17] = 278;
        nArray5[18] = 330;
        nArray5[19] = 394;
        nArray5[20] = 464;
        nArray5[21] = 540;
        nArray5[22] = 576;
        int[] nArray6 = nArray5;
        int[] nArray7 = new int[14];
        nArray7[1] = 4;
        nArray7[2] = 8;
        nArray7[3] = 12;
        nArray7[4] = 18;
        nArray7[5] = 26;
        nArray7[6] = 36;
        nArray7[7] = 48;
        nArray7[8] = 62;
        nArray7[9] = 80;
        nArray7[10] = 104;
        nArray7[11] = 136;
        nArray7[12] = 180;
        nArray7[13] = 192;
        int[] nArray8 = nArray7;
        int[] nArray9 = new int[23];
        nArray9[1] = 6;
        nArray9[2] = 12;
        nArray9[3] = 18;
        nArray9[4] = 24;
        nArray9[5] = 30;
        nArray9[6] = 36;
        nArray9[7] = 44;
        nArray9[8] = 54;
        nArray9[9] = 66;
        nArray9[10] = 80;
        nArray9[11] = 96;
        nArray9[12] = 116;
        nArray9[13] = 140;
        nArray9[14] = 168;
        nArray9[15] = 200;
        nArray9[16] = 238;
        nArray9[17] = 284;
        nArray9[18] = 336;
        nArray9[19] = 396;
        nArray9[20] = 464;
        nArray9[21] = 522;
        nArray9[22] = 576;
        int[] nArray10 = nArray9;
        int[] nArray11 = new int[14];
        nArray11[1] = 4;
        nArray11[2] = 8;
        nArray11[3] = 12;
        nArray11[4] = 18;
        nArray11[5] = 26;
        nArray11[6] = 36;
        nArray11[7] = 48;
        nArray11[8] = 62;
        nArray11[9] = 80;
        nArray11[10] = 104;
        nArray11[11] = 134;
        nArray11[12] = 174;
        nArray11[13] = 192;
        int[] nArray12 = nArray11;
        int[] nArray13 = new int[23];
        nArray13[1] = 4;
        nArray13[2] = 8;
        nArray13[3] = 12;
        nArray13[4] = 16;
        nArray13[5] = 20;
        nArray13[6] = 24;
        nArray13[7] = 30;
        nArray13[8] = 36;
        nArray13[9] = 44;
        nArray13[10] = 52;
        nArray13[11] = 62;
        nArray13[12] = 74;
        nArray13[13] = 90;
        nArray13[14] = 110;
        nArray13[15] = 134;
        nArray13[16] = 162;
        nArray13[17] = 196;
        nArray13[18] = 238;
        nArray13[19] = 288;
        nArray13[20] = 342;
        nArray13[21] = 418;
        nArray13[22] = 576;
        int[] nArray14 = nArray13;
        int[] nArray15 = new int[14];
        nArray15[1] = 4;
        nArray15[2] = 8;
        nArray15[3] = 12;
        nArray15[4] = 16;
        nArray15[5] = 22;
        nArray15[6] = 30;
        nArray15[7] = 40;
        nArray15[8] = 52;
        nArray15[9] = 66;
        nArray15[10] = 84;
        nArray15[11] = 106;
        nArray15[12] = 136;
        nArray15[13] = 192;
        int[] nArray16 = nArray15;
        int[] nArray17 = new int[23];
        nArray17[1] = 4;
        nArray17[2] = 8;
        nArray17[3] = 12;
        nArray17[4] = 16;
        nArray17[5] = 20;
        nArray17[6] = 24;
        nArray17[7] = 30;
        nArray17[8] = 36;
        nArray17[9] = 42;
        nArray17[10] = 50;
        nArray17[11] = 60;
        nArray17[12] = 72;
        nArray17[13] = 88;
        nArray17[14] = 106;
        nArray17[15] = 128;
        nArray17[16] = 156;
        nArray17[17] = 190;
        nArray17[18] = 230;
        nArray17[19] = 276;
        nArray17[20] = 330;
        nArray17[21] = 384;
        nArray17[22] = 576;
        int[] nArray18 = nArray17;
        int[] nArray19 = new int[14];
        nArray19[1] = 4;
        nArray19[2] = 8;
        nArray19[3] = 12;
        nArray19[4] = 16;
        nArray19[5] = 22;
        nArray19[6] = 28;
        nArray19[7] = 38;
        nArray19[8] = 50;
        nArray19[9] = 64;
        nArray19[10] = 80;
        nArray19[11] = 100;
        nArray19[12] = 126;
        nArray19[13] = 192;
        int[] nArray20 = nArray19;
        int[] nArray21 = new int[23];
        nArray21[1] = 4;
        nArray21[2] = 8;
        nArray21[3] = 12;
        nArray21[4] = 16;
        nArray21[5] = 20;
        nArray21[6] = 24;
        nArray21[7] = 30;
        nArray21[8] = 36;
        nArray21[9] = 44;
        nArray21[10] = 54;
        nArray21[11] = 66;
        nArray21[12] = 82;
        nArray21[13] = 102;
        nArray21[14] = 126;
        nArray21[15] = 156;
        nArray21[16] = 194;
        nArray21[17] = 240;
        nArray21[18] = 296;
        nArray21[19] = 364;
        nArray21[20] = 448;
        nArray21[21] = 550;
        nArray21[22] = 576;
        int[] nArray22 = nArray21;
        int[] nArray23 = new int[14];
        nArray23[1] = 4;
        nArray23[2] = 8;
        nArray23[3] = 12;
        nArray23[4] = 16;
        nArray23[5] = 22;
        nArray23[6] = 30;
        nArray23[7] = 42;
        nArray23[8] = 58;
        nArray23[9] = 78;
        nArray23[10] = 104;
        nArray23[11] = 138;
        nArray23[12] = 180;
        nArray23[13] = 192;
        int[] nArray24 = nArray23;
        int[] nArray25 = new int[23];
        nArray25[1] = 6;
        nArray25[2] = 12;
        nArray25[3] = 18;
        nArray25[4] = 24;
        nArray25[5] = 30;
        nArray25[6] = 36;
        nArray25[7] = 44;
        nArray25[8] = 54;
        nArray25[9] = 66;
        nArray25[10] = 80;
        nArray25[11] = 96;
        nArray25[12] = 116;
        nArray25[13] = 140;
        nArray25[14] = 168;
        nArray25[15] = 200;
        nArray25[16] = 238;
        nArray25[17] = 284;
        nArray25[18] = 336;
        nArray25[19] = 396;
        nArray25[20] = 464;
        nArray25[21] = 522;
        nArray25[22] = 576;
        int[] nArray26 = nArray25;
        int[] nArray27 = new int[14];
        nArray27[1] = 4;
        nArray27[2] = 8;
        nArray27[3] = 12;
        nArray27[4] = 18;
        nArray27[5] = 26;
        nArray27[6] = 36;
        nArray27[7] = 48;
        nArray27[8] = 62;
        nArray27[9] = 80;
        nArray27[10] = 104;
        nArray27[11] = 134;
        nArray27[12] = 174;
        nArray27[13] = 192;
        int[] nArray28 = nArray27;
        int[] nArray29 = new int[23];
        nArray29[1] = 6;
        nArray29[2] = 12;
        nArray29[3] = 18;
        nArray29[4] = 24;
        nArray29[5] = 30;
        nArray29[6] = 36;
        nArray29[7] = 44;
        nArray29[8] = 54;
        nArray29[9] = 66;
        nArray29[10] = 80;
        nArray29[11] = 96;
        nArray29[12] = 116;
        nArray29[13] = 140;
        nArray29[14] = 168;
        nArray29[15] = 200;
        nArray29[16] = 238;
        nArray29[17] = 284;
        nArray29[18] = 336;
        nArray29[19] = 396;
        nArray29[20] = 464;
        nArray29[21] = 522;
        nArray29[22] = 576;
        int[] nArray30 = nArray29;
        int[] nArray31 = new int[14];
        nArray31[1] = 4;
        nArray31[2] = 8;
        nArray31[3] = 12;
        nArray31[4] = 18;
        nArray31[5] = 26;
        nArray31[6] = 36;
        nArray31[7] = 48;
        nArray31[8] = 62;
        nArray31[9] = 80;
        nArray31[10] = 104;
        nArray31[11] = 134;
        nArray31[12] = 174;
        nArray31[13] = 192;
        int[] nArray32 = nArray31;
        int[] nArray33 = new int[23];
        nArray33[1] = 12;
        nArray33[2] = 24;
        nArray33[3] = 36;
        nArray33[4] = 48;
        nArray33[5] = 60;
        nArray33[6] = 72;
        nArray33[7] = 88;
        nArray33[8] = 108;
        nArray33[9] = 132;
        nArray33[10] = 160;
        nArray33[11] = 192;
        nArray33[12] = 232;
        nArray33[13] = 280;
        nArray33[14] = 336;
        nArray33[15] = 400;
        nArray33[16] = 476;
        nArray33[17] = 566;
        nArray33[18] = 568;
        nArray33[19] = 570;
        nArray33[20] = 572;
        nArray33[21] = 574;
        nArray33[22] = 576;
        int[] nArray34 = nArray33;
        int[] nArray35 = new int[14];
        nArray35[1] = 8;
        nArray35[2] = 16;
        nArray35[3] = 24;
        nArray35[4] = 36;
        nArray35[5] = 52;
        nArray35[6] = 72;
        nArray35[7] = 96;
        nArray35[8] = 124;
        nArray35[9] = 160;
        nArray35[10] = 162;
        nArray35[11] = 164;
        nArray35[12] = 166;
        nArray35[13] = 192;
        int[] nArray36 = nArray35;
        this.O[0] = new e_0(nArray2, nArray4);
        this.O[1] = new e_0(nArray6, nArray8);
        this.O[2] = new e_0(nArray10, nArray12);
        this.O[3] = new e_0(nArray14, nArray16);
        this.O[4] = new e_0(nArray18, nArray20);
        this.O[5] = new e_0(nArray22, nArray24);
        this.O[6] = new e_0(nArray26, nArray28);
        this.O[7] = new e_0(nArray30, nArray32);
        this.O[8] = new e_0(nArray34, nArray36);
        if (T == null) {
            T = new int[9][];
            int n3 = 0;
            while (n3 < 9) {
                jaco.mp3.a.c.T[n3] = jaco.mp3.a.c.a(this.O[n3].b);
                ++n3;
            }
        }
        int[] nArray37 = new int[5];
        nArray37[1] = 6;
        nArray37[2] = 11;
        nArray37[3] = 16;
        nArray37[4] = 21;
        int[] nArray38 = nArray37;
        int[] nArray39 = new int[3];
        nArray39[1] = 6;
        nArray39[2] = 12;
        nArray4 = nArray39;
        new j(this, nArray38, nArray4);
        this.a = new int[54];
        this.j = z2;
        this.k = d2;
        this.l = y2;
        this.m = y3;
        this.n = c_02;
        this.o = 0;
        this.u = 0;
        this.w = this.k.f() == 3 ? 1 : 2;
        this.t = this.k.a() == 1 ? 2 : 1;
        this.z = this.k.d() + (this.k.a() == 1 ? 3 : (this.k.a() == 2 ? 6 : 0));
        if (this.w == 2) {
            switch (this.o) {
                case 1: 
                case 3: {
                    this.y = 0;
                    this.x = 0;
                    break;
                }
                case 2: {
                    this.y = 1;
                    this.x = 1;
                    break;
                }
                default: {
                    this.x = 0;
                    this.y = 1;
                    break;
                }
            }
        } else {
            this.y = 0;
            this.x = 0;
        }
        int n4 = 0;
        while (n4 < 2) {
            int n5 = 0;
            while (n5 < 576) {
                this.g[n4][n5] = 0.0f;
                ++n5;
            }
            ++n4;
        }
        this.i[1] = 576;
        this.i[0] = 576;
        this.p = new h();
        this.q = new d_0();
    }

    @Override
    public final void a() {
        block112: {
            int n2;
            int n3;
            block111: {
                boolean bl;
                int n4;
                n3 = this.k.h();
                c c2 = this;
                if (c2.k.a() == 1) {
                    c2.q.a = c2.j.d(9);
                    if (c2.w == 1) {
                        c2.j.d(5);
                    } else {
                        c2.j.d(3);
                    }
                    n4 = 0;
                    while (n4 < c2.w) {
                        c2.q.b[n4].a[0] = c2.j.d(1);
                        c2.q.b[n4].a[1] = c2.j.d(1);
                        c2.q.b[n4].a[2] = c2.j.d(1);
                        c2.q.b[n4].a[3] = c2.j.d(1);
                        ++n4;
                    }
                    n2 = 0;
                    while (n2 < 2) {
                        n4 = 0;
                        while (n4 < c2.w) {
                            c2.q.b[n4].b[n2].a = c2.j.d(12);
                            c2.q.b[n4].b[n2].b = c2.j.d(9);
                            c2.q.b[n4].b[n2].c = c2.j.d(8);
                            c2.q.b[n4].b[n2].d = c2.j.d(4);
                            c2.q.b[n4].b[n2].e = c2.j.d(1);
                            if (c2.q.b[n4].b[n2].e != 0) {
                                c2.q.b[n4].b[n2].f = c2.j.d(2);
                                c2.q.b[n4].b[n2].g = c2.j.d(1);
                                c2.q.b[n4].b[n2].h[0] = c2.j.d(5);
                                c2.q.b[n4].b[n2].h[1] = c2.j.d(5);
                                c2.q.b[n4].b[n2].i[0] = c2.j.d(3);
                                c2.q.b[n4].b[n2].i[1] = c2.j.d(3);
                                c2.q.b[n4].b[n2].i[2] = c2.j.d(3);
                                if (c2.q.b[n4].b[n2].f == 0) {
                                    bl = false;
                                    break block111;
                                }
                                c2.q.b[n4].b[n2].j = c2.q.b[n4].b[n2].f == 2 && c2.q.b[n4].b[n2].g == 0 ? 8 : 7;
                                c2.q.b[n4].b[n2].k = 20 - c2.q.b[n4].b[n2].j;
                            } else {
                                c2.q.b[n4].b[n2].h[0] = c2.j.d(5);
                                c2.q.b[n4].b[n2].h[1] = c2.j.d(5);
                                c2.q.b[n4].b[n2].h[2] = c2.j.d(5);
                                c2.q.b[n4].b[n2].j = c2.j.d(4);
                                c2.q.b[n4].b[n2].k = c2.j.d(3);
                                c2.q.b[n4].b[n2].f = 0;
                            }
                            c2.q.b[n4].b[n2].l = c2.j.d(1);
                            c2.q.b[n4].b[n2].m = c2.j.d(1);
                            c2.q.b[n4].b[n2].n = c2.j.d(1);
                            ++n4;
                        }
                        ++n2;
                    }
                } else {
                    c2.q.a = c2.j.d(8);
                    if (c2.w == 1) {
                        c2.j.d(1);
                    } else {
                        c2.j.d(2);
                    }
                    n4 = 0;
                    while (n4 < c2.w) {
                        c2.q.b[n4].b[0].a = c2.j.d(12);
                        c2.q.b[n4].b[0].b = c2.j.d(9);
                        c2.q.b[n4].b[0].c = c2.j.d(8);
                        c2.q.b[n4].b[0].d = c2.j.d(9);
                        c2.q.b[n4].b[0].e = c2.j.d(1);
                        if (c2.q.b[n4].b[0].e != 0) {
                            c2.q.b[n4].b[0].f = c2.j.d(2);
                            c2.q.b[n4].b[0].g = c2.j.d(1);
                            c2.q.b[n4].b[0].h[0] = c2.j.d(5);
                            c2.q.b[n4].b[0].h[1] = c2.j.d(5);
                            c2.q.b[n4].b[0].i[0] = c2.j.d(3);
                            c2.q.b[n4].b[0].i[1] = c2.j.d(3);
                            c2.q.b[n4].b[0].i[2] = c2.j.d(3);
                            if (c2.q.b[n4].b[0].f == 0) {
                                bl = false;
                                break block111;
                            }
                            if (c2.q.b[n4].b[0].f == 2 && c2.q.b[n4].b[0].g == 0) {
                                c2.q.b[n4].b[0].j = 8;
                            } else {
                                c2.q.b[n4].b[0].j = 7;
                                c2.q.b[n4].b[0].k = 20 - c2.q.b[n4].b[0].j;
                            }
                        } else {
                            c2.q.b[n4].b[0].h[0] = c2.j.d(5);
                            c2.q.b[n4].b[0].h[1] = c2.j.d(5);
                            c2.q.b[n4].b[0].h[2] = c2.j.d(5);
                            c2.q.b[n4].b[0].j = c2.j.d(4);
                            c2.q.b[n4].b[0].k = c2.j.d(3);
                            c2.q.b[n4].b[0].f = 0;
                        }
                        c2.q.b[n4].b[0].m = c2.j.d(1);
                        c2.q.b[n4].b[0].n = c2.j.d(1);
                        ++n4;
                    }
                }
                bl = true;
            }
            int n5 = 0;
            while (n5 < n3) {
                this.p.b(this.j.d(8));
                ++n5;
            }
            int n6 = this.p.a() >>> 3;
            n5 = this.p.a() & 7;
            if (n5 != 0) {
                this.p.a(8 - n5);
                ++n6;
            }
            n5 = this.u - n6 - this.q.a;
            this.u += n3;
            if (n5 < 0) break block112;
            if (n6 > 4096) {
                this.u -= 4096;
                this.p.d(4096);
            }
            while (n5 > 0) {
                this.p.a(8);
                --n5;
            }
            n3 = 0;
            while (n3 < this.t) {
                float[][] fArray;
                int n7;
                int n8;
                int n9;
                int n10;
                int n11;
                int n12;
                int n13;
                n5 = 0;
                while (n5 < this.w) {
                    int n14;
                    int n15;
                    int n16;
                    c c3;
                    int n17;
                    this.v = this.p.a();
                    if (this.k.a() == 1) {
                        n2 = n3;
                        n17 = n5;
                        c3 = this;
                        n n18 = c3.q.b[n17].b[n2];
                        n16 = n18.d;
                        n13 = M[0][n16];
                        n12 = M[1][n16];
                        if (n18.e != 0 && n18.f == 2) {
                            if (n18.g != 0) {
                                n11 = 0;
                                while (n11 < 8) {
                                    c3.s[n17].a[n11] = c3.p.a(M[0][n18.d]);
                                    ++n11;
                                }
                                n11 = 3;
                                while (n11 < 6) {
                                    n15 = 0;
                                    while (n15 < 3) {
                                        c3.s[n17].b[n15][n11] = c3.p.a(M[0][n18.d]);
                                        ++n15;
                                    }
                                    ++n11;
                                }
                                n11 = 6;
                                while (n11 < 12) {
                                    n15 = 0;
                                    while (n15 < 3) {
                                        c3.s[n17].b[n15][n11] = c3.p.a(M[1][n18.d]);
                                        ++n15;
                                    }
                                    ++n11;
                                }
                                n15 = 0;
                                while (n15 < 3) {
                                    c3.s[n17].b[n15][12] = 0;
                                    ++n15;
                                }
                            } else {
                                c3.s[n17].b[0][0] = c3.p.a(n13);
                                c3.s[n17].b[1][0] = c3.p.a(n13);
                                c3.s[n17].b[2][0] = c3.p.a(n13);
                                c3.s[n17].b[0][1] = c3.p.a(n13);
                                c3.s[n17].b[1][1] = c3.p.a(n13);
                                c3.s[n17].b[2][1] = c3.p.a(n13);
                                c3.s[n17].b[0][2] = c3.p.a(n13);
                                c3.s[n17].b[1][2] = c3.p.a(n13);
                                c3.s[n17].b[2][2] = c3.p.a(n13);
                                c3.s[n17].b[0][3] = c3.p.a(n13);
                                c3.s[n17].b[1][3] = c3.p.a(n13);
                                c3.s[n17].b[2][3] = c3.p.a(n13);
                                c3.s[n17].b[0][4] = c3.p.a(n13);
                                c3.s[n17].b[1][4] = c3.p.a(n13);
                                c3.s[n17].b[2][4] = c3.p.a(n13);
                                c3.s[n17].b[0][5] = c3.p.a(n13);
                                c3.s[n17].b[1][5] = c3.p.a(n13);
                                c3.s[n17].b[2][5] = c3.p.a(n13);
                                c3.s[n17].b[0][6] = c3.p.a(n12);
                                c3.s[n17].b[1][6] = c3.p.a(n12);
                                c3.s[n17].b[2][6] = c3.p.a(n12);
                                c3.s[n17].b[0][7] = c3.p.a(n12);
                                c3.s[n17].b[1][7] = c3.p.a(n12);
                                c3.s[n17].b[2][7] = c3.p.a(n12);
                                c3.s[n17].b[0][8] = c3.p.a(n12);
                                c3.s[n17].b[1][8] = c3.p.a(n12);
                                c3.s[n17].b[2][8] = c3.p.a(n12);
                                c3.s[n17].b[0][9] = c3.p.a(n12);
                                c3.s[n17].b[1][9] = c3.p.a(n12);
                                c3.s[n17].b[2][9] = c3.p.a(n12);
                                c3.s[n17].b[0][10] = c3.p.a(n12);
                                c3.s[n17].b[1][10] = c3.p.a(n12);
                                c3.s[n17].b[2][10] = c3.p.a(n12);
                                c3.s[n17].b[0][11] = c3.p.a(n12);
                                c3.s[n17].b[1][11] = c3.p.a(n12);
                                c3.s[n17].b[2][11] = c3.p.a(n12);
                                c3.s[n17].b[0][12] = 0;
                                c3.s[n17].b[1][12] = 0;
                                c3.s[n17].b[2][12] = 0;
                            }
                        } else {
                            if (c3.q.b[n17].a[0] == 0 || n2 == 0) {
                                c3.s[n17].a[0] = c3.p.a(n13);
                                c3.s[n17].a[1] = c3.p.a(n13);
                                c3.s[n17].a[2] = c3.p.a(n13);
                                c3.s[n17].a[3] = c3.p.a(n13);
                                c3.s[n17].a[4] = c3.p.a(n13);
                                c3.s[n17].a[5] = c3.p.a(n13);
                            }
                            if (c3.q.b[n17].a[1] == 0 || n2 == 0) {
                                c3.s[n17].a[6] = c3.p.a(n13);
                                c3.s[n17].a[7] = c3.p.a(n13);
                                c3.s[n17].a[8] = c3.p.a(n13);
                                c3.s[n17].a[9] = c3.p.a(n13);
                                c3.s[n17].a[10] = c3.p.a(n13);
                            }
                            if (c3.q.b[n17].a[2] == 0 || n2 == 0) {
                                c3.s[n17].a[11] = c3.p.a(n12);
                                c3.s[n17].a[12] = c3.p.a(n12);
                                c3.s[n17].a[13] = c3.p.a(n12);
                                c3.s[n17].a[14] = c3.p.a(n12);
                                c3.s[n17].a[15] = c3.p.a(n12);
                            }
                            if (c3.q.b[n17].a[3] == 0 || n2 == 0) {
                                c3.s[n17].a[16] = c3.p.a(n12);
                                c3.s[n17].a[17] = c3.p.a(n12);
                                c3.s[n17].a[18] = c3.p.a(n12);
                                c3.s[n17].a[19] = c3.p.a(n12);
                                c3.s[n17].a[20] = c3.p.a(n12);
                            }
                            c3.s[n17].a[21] = 0;
                            c3.s[n17].a[22] = 0;
                        }
                    } else {
                        n2 = n3;
                        n17 = n5;
                        c3 = this;
                        n11 = 0;
                        n n19 = c3.q.b[n17].b[n2];
                        n14 = n2;
                        n12 = n17;
                        c c4 = c3;
                        int n20 = c4.k.i();
                        n10 = 0;
                        n n21 = c4.q.b[n12].b[n14];
                        int n22 = n21.d;
                        n9 = n21.f == 2 ? (n21.g == 0 ? 1 : (n21.g == 1 ? 2 : 0)) : 0;
                        if (n20 != 1 && n20 != 3 || n12 != 1) {
                            if (n22 < 400) {
                                c4.C[0] = (n22 >>> 4) / 5;
                                c4.C[1] = (n22 >>> 4) % 5;
                                c4.C[2] = (n22 & 0xF) >>> 2;
                                c4.C[3] = n22 & 3;
                                c4.q.b[n12].b[n14].l = 0;
                                n10 = 0;
                            } else if (n22 < 500) {
                                c4.C[0] = (n22 - 400 >>> 2) / 5;
                                c4.C[1] = (n22 - 400 >>> 2) % 5;
                                c4.C[2] = n22 - 400 & 3;
                                c4.C[3] = 0;
                                c4.q.b[n12].b[n14].l = 0;
                                n10 = 1;
                            } else if (n22 < 512) {
                                c4.C[0] = (n22 - 500) / 3;
                                c4.C[1] = (n22 - 500) % 3;
                                c4.C[2] = 0;
                                c4.C[3] = 0;
                                c4.q.b[n12].b[n14].l = 1;
                                n10 = 2;
                            }
                        }
                        if ((n20 == 1 || n20 == 3) && n12 == 1) {
                            n8 = n22 >>> 1;
                            if (n8 < 180) {
                                c4.C[0] = n8 / 36;
                                c4.C[1] = n8 % 36 / 6;
                                c4.C[2] = n8 % 36 % 6;
                                c4.C[3] = 0;
                                c4.q.b[n12].b[n14].l = 0;
                                n10 = 3;
                            } else if (n8 < 244) {
                                c4.C[0] = (n8 - 180 & 0x3F) >>> 4;
                                c4.C[1] = (n8 - 180 & 0xF) >>> 2;
                                c4.C[2] = n8 - 180 & 3;
                                c4.C[3] = 0;
                                c4.q.b[n12].b[n14].l = 0;
                                n10 = 4;
                            } else if (n8 < 255) {
                                c4.C[0] = (n8 - 244) / 3;
                                c4.C[1] = (n8 - 244) % 3;
                                c4.C[2] = 0;
                                c4.C[3] = 0;
                                c4.q.b[n12].b[n14].l = 0;
                                n10 = 5;
                            }
                        }
                        n2 = 0;
                        while (n2 < 45) {
                            c4.a[n2] = 0;
                            ++n2;
                        }
                        int n23 = 0;
                        n2 = 0;
                        while (n2 < 4) {
                            int n24 = 0;
                            while (n24 < X[n10][n9][n2]) {
                                c4.a[n23] = c4.C[n2] == 0 ? 0 : c4.p.a(c4.C[n2]);
                                ++n23;
                                ++n24;
                            }
                            ++n2;
                        }
                        if (n19.e != 0 && n19.f == 2) {
                            if (n19.g != 0) {
                                int n25 = 0;
                                while (n25 < 8) {
                                    c3.s[n17].a[n25] = c3.a[n11];
                                    ++n11;
                                    ++n25;
                                }
                                n25 = 3;
                                while (n25 < 12) {
                                    n7 = 0;
                                    while (n7 < 3) {
                                        c3.s[n17].b[n7][n25] = c3.a[n11];
                                        ++n11;
                                        ++n7;
                                    }
                                    ++n25;
                                }
                                n7 = 0;
                                while (n7 < 3) {
                                    c3.s[n17].b[n7][12] = 0;
                                    ++n7;
                                }
                            } else {
                                int n26 = 0;
                                while (n26 < 12) {
                                    n7 = 0;
                                    while (n7 < 3) {
                                        c3.s[n17].b[n7][n26] = c3.a[n11];
                                        ++n11;
                                        ++n7;
                                    }
                                    ++n26;
                                }
                                n7 = 0;
                                while (n7 < 3) {
                                    c3.s[n17].b[n7][12] = 0;
                                    ++n7;
                                }
                            }
                        } else {
                            int n27 = 0;
                            while (n27 < 21) {
                                c3.s[n17].a[n27] = c3.a[n11];
                                ++n11;
                                ++n27;
                            }
                            c3.s[n17].a[21] = 0;
                            c3.s[n17].a[22] = 0;
                        }
                    }
                    n2 = n3;
                    n17 = n5;
                    c3 = this;
                    c3.D[0] = 0;
                    c3.E[0] = 0;
                    c3.F[0] = 0;
                    c3.G[0] = 0;
                    n11 = c3.v + c3.q.b[n17].b[n2].a;
                    if (c3.q.b[n17].b[n2].e != 0 && c3.q.b[n17].b[n2].f == 2) {
                        n7 = c3.z == 8 ? 72 : 36;
                        n16 = 576;
                    } else {
                        n12 = c3.q.b[n17].b[n2].j + 1;
                        n14 = n12 + c3.q.b[n17].b[n2].k + 1;
                        if (n14 > c3.O[c3.z].a.length - 1) {
                            n14 = c3.O[c3.z].a.length - 1;
                        }
                        n7 = c3.O[c3.z].a[n12];
                        n16 = c3.O[c3.z].a[n14];
                    }
                    n13 = 0;
                    n8 = 0;
                    while (n8 < c3.q.b[n17].b[n2].b << 1) {
                        m m2 = n8 < n7 ? jaco.mp3.a.m.a[c3.q.b[n17].b[n2].h[0]] : (n8 < n16 ? jaco.mp3.a.m.a[c3.q.b[n17].b[n2].h[1]] : jaco.mp3.a.m.a[c3.q.b[n17].b[n2].h[2]]);
                        jaco.mp3.a.m.a(m2, c3.D, c3.E, c3.F, c3.G, c3.p);
                        c3.c[n13++] = c3.D[0];
                        c3.c[n13++] = c3.E[0];
                        c3.b = c3.b + c3.D[0] + c3.E[0];
                        n8 += 2;
                    }
                    m m3 = jaco.mp3.a.m.a[c3.q.b[n17].b[n2].n + 32];
                    n15 = c3.p.a();
                    while (n15 < n11 && n13 < 576) {
                        jaco.mp3.a.m.a(m3, c3.D, c3.E, c3.F, c3.G, c3.p);
                        c3.c[n13++] = c3.F[0];
                        c3.c[n13++] = c3.G[0];
                        c3.c[n13++] = c3.D[0];
                        c3.c[n13++] = c3.E[0];
                        c3.b = c3.b + c3.F[0] + c3.G[0] + c3.D[0] + c3.E[0];
                        n15 = c3.p.a();
                    }
                    if (n15 > n11) {
                        c3.p.c(n15 - n11);
                        n13 -= 4;
                    }
                    if ((n15 = c3.p.a()) < n11) {
                        c3.p.a(n11 - n15);
                    }
                    c3.i[n17] = n13 < 576 ? n13 : 576;
                    if (n13 < 0) {
                        n13 = 0;
                    }
                    while (n13 < 576) {
                        c3.c[n13] = 0;
                        ++n13;
                    }
                    n11 = n3;
                    n2 = n5;
                    float[][] fArray2 = this.d[n5];
                    c3 = this;
                    n n28 = c3.q.b[n2].b[n11];
                    n7 = 0;
                    n13 = 0;
                    n12 = 0;
                    n14 = 0;
                    fArray = fArray2;
                    if (n28.e != 0 && n28.f == 2 && n28.g == 0) {
                        n12 = c3.O[c3.z].b[1];
                        n16 = (n12 << 2) - n12;
                        n13 = 0;
                    } else {
                        n16 = c3.O[c3.z].a[1];
                    }
                    float f2 = (float)Math.pow(2.0, 0.25 * ((double)n28.c - 210.0));
                    n8 = 0;
                    while (n8 < c3.i[n2]) {
                        int n29;
                        n9 = n8 % 18;
                        n10 = (n8 - n9) / 18;
                        fArray[n10][n9] = c3.c[n8] == 0 ? 0.0f : ((n29 = c3.c[n8]) < Q.length ? (c3.c[n8] > 0 ? f2 * Q[n29] : (-n29 < Q.length ? -f2 * Q[-n29] : -f2 * (float)Math.pow(-n29, 1.3333333333333333))) : (c3.c[n8] > 0 ? f2 * (float)Math.pow(n29, 1.3333333333333333) : -f2 * (float)Math.pow(-n29, 1.3333333333333333)));
                        ++n8;
                    }
                    n8 = 0;
                    while (n8 < c3.i[n2]) {
                        int n30;
                        block113: {
                            block114: {
                                block115: {
                                    block116: {
                                        n9 = n8 % 18;
                                        n10 = (n8 - n9) / 18;
                                        if (n14 != n16) break block113;
                                        if (n28.e == 0 || n28.f != 2) break block114;
                                        if (n28.g == 0) break block115;
                                        if (n14 != c3.O[c3.z].a[8]) break block116;
                                        n16 = c3.O[c3.z].b[4];
                                        n16 = (n16 << 2) - n16;
                                        n7 = 3;
                                        n12 = c3.O[c3.z].b[4] - c3.O[c3.z].b[3];
                                        n13 = c3.O[c3.z].b[3];
                                        n13 = (n13 << 2) - n13;
                                        break block113;
                                    }
                                    if (n14 < c3.O[c3.z].a[8]) break block114;
                                    n16 = c3.O[c3.z].b[++n7 + 1];
                                    n16 = (n16 << 2) - n16;
                                    n13 = c3.O[c3.z].b[n7];
                                    n12 = c3.O[c3.z].b[n7 + 1] - n13;
                                    n13 = (n13 << 2) - n13;
                                    break block113;
                                }
                                n16 = c3.O[c3.z].b[++n7 + 1];
                                n16 = (n16 << 2) - n16;
                                n13 = c3.O[c3.z].b[n7];
                                n12 = c3.O[c3.z].b[n7 + 1] - n13;
                                n13 = (n13 << 2) - n13;
                                break block113;
                            }
                            n16 = c3.O[c3.z].a[++n7 + 1];
                        }
                        if (n28.e != 0 && (n28.f == 2 && n28.g == 0 || n28.f == 2 && n28.g != 0 && n8 >= 36)) {
                            int n31 = (n14 - n13) / n12;
                            n30 = c3.s[n2].b[n31][n7] << n28.m;
                            float[] fArray3 = fArray[n10];
                            int n32 = n9;
                            fArray3[n32] = fArray3[n32] * P[n30 += n28.i[n31] << 2];
                        } else {
                            n30 = c3.s[n2].a[n7];
                            if (n28.l != 0) {
                                n30 += N[n7];
                            }
                            float[] fArray4 = fArray[n10];
                            int n33 = n9;
                            fArray4[n33] = fArray4[n33] * P[n30 <<= n28.m];
                        }
                        ++n14;
                        ++n8;
                    }
                    n8 = c3.i[n2];
                    while (n8 < 576) {
                        n9 = n8 % 18;
                        n10 = (n8 - n9) / 18;
                        if (n9 < 0) {
                            n9 = 0;
                        }
                        if (n10 < 0) {
                            n10 = 0;
                        }
                        fArray[n10][n9] = 0.0f;
                        ++n8;
                    }
                    ++n5;
                }
                this.a(n3);
                if (this.o == 3 && this.w > 1) {
                    c c5 = this;
                    int n34 = 0;
                    while (n34 < 18) {
                        n2 = 0;
                        while (n2 < 18) {
                            c5.e[0][n34][n2] = (c5.e[0][n34][n2] + c5.e[1][n34][n2]) * 0.5f;
                            c5.e[0][n34][n2 + 1] = (c5.e[0][n34][n2 + 1] + c5.e[1][n34][n2 + 1]) * 0.5f;
                            c5.e[0][n34][n2 + 2] = (c5.e[0][n34][n2 + 2] + c5.e[1][n34][n2 + 2]) * 0.5f;
                            n2 += 3;
                        }
                        ++n34;
                    }
                }
                n5 = this.x;
                while (n5 <= this.y) {
                    int n35;
                    n11 = n3;
                    n2 = n5;
                    float[][] fArray5 = this.e[n5];
                    c c6 = this;
                    n n36 = c6.q.b[n2].b[n11];
                    fArray = fArray5;
                    if (n36.e != 0 && n36.f == 2) {
                        n13 = 0;
                        while (n13 < 576) {
                            c6.f[n13] = 0.0f;
                            ++n13;
                        }
                        if (n36.g != 0) {
                            n13 = 0;
                            while (n13 < 36) {
                                n9 = n13 % 18;
                                n10 = (n13 - n9) / 18;
                                c6.f[n13] = fArray[n10][n9];
                                ++n13;
                            }
                            n12 = 3;
                            while (n12 < 13) {
                                int n37 = c6.O[c6.z].b[n12];
                                int n38 = c6.O[c6.z].b[n12 + 1] - n37;
                                n9 = (n37 << 2) - n37;
                                n7 = 0;
                                int n39 = 0;
                                while (n7 < n38) {
                                    n8 = n9 + n7;
                                    int n40 = n9 + n39;
                                    n10 = n8 % 18;
                                    int n41 = (n8 - n10) / 18;
                                    c6.f[n40] = fArray[n41][n10];
                                    n10 = (n8 += n38) % 18;
                                    n41 = (n8 - n10) / 18;
                                    c6.f[++n40] = fArray[n41][n10];
                                    n10 = (n8 += n38) % 18;
                                    n41 = (n8 - n10) / 18;
                                    c6.f[++n40] = fArray[n41][n10];
                                    ++n7;
                                    n39 += 3;
                                }
                                ++n12;
                            }
                        } else {
                            n13 = 0;
                            while (n13 < 576) {
                                n9 = T[c6.z][n13];
                                n10 = n9 % 18;
                                int n42 = (n9 - n10) / 18;
                                c6.f[n13] = fArray[n42][n10];
                                ++n13;
                            }
                        }
                    } else {
                        n13 = 0;
                        while (n13 < 576) {
                            n9 = n13 % 18;
                            n10 = (n13 - n9) / 18;
                            c6.f[n13] = fArray[n10][n9];
                            ++n13;
                        }
                    }
                    n2 = n3;
                    int n43 = n5;
                    c6 = this;
                    n n44 = c6.q.b[n43].b[n2];
                    if (n44.e == 0 || n44.f != 2 || n44.g != 0) {
                        n7 = n44.e != 0 && n44.g != 0 && n44.f == 2 ? 18 : 558;
                        n11 = 0;
                        while (n11 < n7) {
                            int n45 = 0;
                            while (n45 < 8) {
                                n13 = n11 + 17 - n45;
                                n12 = n11 + 18 + n45;
                                float f3 = c6.f[n13];
                                float f4 = c6.f[n12];
                                c6.f[n13] = f3 * U[n45] - f4 * V[n45];
                                c6.f[n12] = f4 * U[n45] + f3 * V[n45];
                                ++n45;
                            }
                            n11 += 18;
                        }
                    }
                    this.a(n5, n3);
                    n2 = 18;
                    while (n2 < 576) {
                        int n46 = 1;
                        while (n46 < 18) {
                            this.f[n2 + n46] = -this.f[n2 + n46];
                            n46 += 2;
                        }
                        n2 += 36;
                    }
                    if (n5 == 0 || this.o == 2) {
                        n35 = 0;
                        while (n35 < 18) {
                            n43 = 0;
                            n2 = 0;
                            while (n2 < 576) {
                                this.A[n43] = this.f[n2 + n35];
                                ++n43;
                                n2 += 18;
                            }
                            this.l.a(this.A);
                            this.l.a(this.n);
                            ++n35;
                        }
                    } else {
                        n35 = 0;
                        while (n35 < 18) {
                            n43 = 0;
                            n2 = 0;
                            while (n2 < 576) {
                                this.B[n43] = this.f[n2 + n35];
                                ++n43;
                                n2 += 18;
                            }
                            this.m.a(this.B);
                            this.m.a(this.n);
                            ++n35;
                        }
                    }
                    ++n5;
                }
                ++n3;
            }
            ++this.L;
        }
    }

    private void a(int n2, int n3, int n4) {
        if (n2 == 0) {
            this.h[0][n4] = 1.0f;
            this.h[1][n4] = 1.0f;
            return;
        }
        if ((n2 & 1) != 0) {
            this.h[0][n4] = R[n3][n2 + 1 >>> 1];
            this.h[1][n4] = 1.0f;
            return;
        }
        this.h[0][n4] = 1.0f;
        this.h[1][n4] = R[n3][n2 >>> 1];
    }

    private void a(int n2) {
        if (this.w == 1) {
            n2 = 0;
            while (n2 < 32) {
                int n3 = 0;
                while (n3 < 18) {
                    this.e[0][n2][n3] = this.d[0][n2][n3];
                    this.e[0][n2][n3 + 1] = this.d[0][n2][n3 + 1];
                    this.e[0][n2][n3 + 2] = this.d[0][n2][n3 + 2];
                    n3 += 3;
                }
                ++n2;
            }
            return;
        }
        n n4 = this.q.b[0].b[n2];
        int n5 = this.k.i();
        boolean bl = this.k.f() == 1 && (n5 & 2) != 0;
        boolean bl2 = this.k.f() == 1 && (n5 & 1) != 0;
        boolean bl3 = this.k.a() == 0 || this.k.a() == 2;
        int n6 = n4.d & 1;
        int n7 = 0;
        while (n7 < 576) {
            this.H[n7] = 7;
            this.I[n7] = 0.0f;
            ++n7;
        }
        if (bl2) {
            if (n4.e != 0 && n4.f == 2) {
                int n8;
                int n9;
                if (n4.g != 0) {
                    n9 = 0;
                    n8 = 0;
                    while (n8 < 3) {
                        int n10 = 2;
                        n5 = 12;
                        while (n5 >= 3) {
                            n7 = this.O[this.z].b[n5];
                            int n11 = this.O[this.z].b[n5 + 1] - n7;
                            n7 = (n7 << 2) - n7 + (n8 + 1) * n11 - 1;
                            while (n11 > 0) {
                                if (this.d[1][n7 / 18][n7 % 18] != 0.0f) {
                                    n10 = n5;
                                    n5 = -10;
                                    n11 = -10;
                                }
                                --n11;
                                --n7;
                            }
                            --n5;
                        }
                        n5 = n10 + 1;
                        if (n5 > n9) {
                            n9 = n5;
                        }
                        while (n5 < 12) {
                            n7 = this.O[this.z].b[n5];
                            int n12 = this.O[this.z].b[n5 + 1] - n7;
                            n7 = (n7 << 2) - n7 + n8 * n12;
                            while (n12 > 0) {
                                this.H[n7] = this.s[1].b[n8][n5];
                                if (this.H[n7] != 7) {
                                    if (bl3) {
                                        this.a(this.H[n7], n6, n7);
                                    } else {
                                        this.I[n7] = S[this.H[n7]];
                                    }
                                }
                                ++n7;
                                --n12;
                            }
                            ++n5;
                        }
                        n5 = this.O[this.z].b[10];
                        int n13 = this.O[this.z].b[11] - n5;
                        n5 = (n5 << 2) - n5 + n8 * n13;
                        n7 = this.O[this.z].b[11];
                        n13 = this.O[this.z].b[12] - n7;
                        n7 = (n7 << 2) - n7 + n8 * n13;
                        while (n13 > 0) {
                            this.H[n7] = this.H[n5];
                            if (bl3) {
                                this.h[0][n7] = this.h[0][n5];
                                this.h[1][n7] = this.h[1][n5];
                            } else {
                                this.I[n7] = this.I[n5];
                            }
                            ++n7;
                            --n13;
                        }
                        ++n8;
                    }
                    if (n9 <= 3) {
                        n7 = 2;
                        n5 = 17;
                        int n14 = -1;
                        while (n7 >= 0) {
                            if (this.d[1][n7][n5] != 0.0f) {
                                n14 = (n7 << 4) + (n7 << 1) + n5;
                                n7 = -1;
                                continue;
                            }
                            if (--n5 >= 0) continue;
                            --n7;
                            n5 = 17;
                        }
                        n7 = 0;
                        while (this.O[this.z].a[n7] <= n14) {
                            ++n7;
                        }
                        n5 = n7;
                        n7 = this.O[this.z].a[n7];
                        while (n5 < 8) {
                            n14 = this.O[this.z].a[n5 + 1] - this.O[this.z].a[n5];
                            while (n14 > 0) {
                                this.H[n7] = this.s[1].a[n5];
                                if (this.H[n7] != 7) {
                                    if (bl3) {
                                        this.a(this.H[n7], n6, n7);
                                    } else {
                                        this.I[n7] = S[this.H[n7]];
                                    }
                                }
                                ++n7;
                                --n14;
                            }
                            ++n5;
                        }
                    }
                } else {
                    n9 = 0;
                    while (n9 < 3) {
                        n8 = -1;
                        n5 = 12;
                        while (n5 >= 0) {
                            n7 = this.O[this.z].b[n5];
                            int n15 = this.O[this.z].b[n5 + 1] - n7;
                            n7 = (n7 << 2) - n7 + (n9 + 1) * n15 - 1;
                            while (n15 > 0) {
                                if (this.d[1][n7 / 18][n7 % 18] != 0.0f) {
                                    n8 = n5;
                                    n5 = -10;
                                    n15 = -10;
                                }
                                --n15;
                                --n7;
                            }
                            --n5;
                        }
                        n5 = n8 + 1;
                        while (n5 < 12) {
                            n7 = this.O[this.z].b[n5];
                            int n16 = this.O[this.z].b[n5 + 1] - n7;
                            n7 = (n7 << 2) - n7 + n9 * n16;
                            while (n16 > 0) {
                                this.H[n7] = this.s[1].b[n9][n5];
                                if (this.H[n7] != 7) {
                                    if (bl3) {
                                        this.a(this.H[n7], n6, n7);
                                    } else {
                                        this.I[n7] = S[this.H[n7]];
                                    }
                                }
                                ++n7;
                                --n16;
                            }
                            ++n5;
                        }
                        n7 = this.O[this.z].b[10];
                        n8 = this.O[this.z].b[11];
                        int n17 = n8 - n7;
                        n5 = (n7 << 2) - n7 + n9 * n17;
                        n17 = this.O[this.z].b[12] - n8;
                        n7 = (n8 << 2) - n8 + n9 * n17;
                        while (n17 > 0) {
                            this.H[n7] = this.H[n5];
                            if (bl3) {
                                this.h[0][n7] = this.h[0][n5];
                                this.h[1][n7] = this.h[1][n5];
                            } else {
                                this.I[n7] = this.I[n5];
                            }
                            ++n7;
                            --n17;
                        }
                        ++n9;
                    }
                }
            } else {
                n7 = 31;
                n5 = 17;
                int n18 = 0;
                while (n7 >= 0) {
                    if (this.d[1][n7][n5] != 0.0f) {
                        n18 = (n7 << 4) + (n7 << 1) + n5;
                        n7 = -1;
                        continue;
                    }
                    if (--n5 >= 0) continue;
                    --n7;
                    n5 = 17;
                }
                n7 = 0;
                while (this.O[this.z].a[n7] <= n18) {
                    ++n7;
                }
                n5 = n7;
                n7 = this.O[this.z].a[n7];
                while (n5 < 21) {
                    n18 = this.O[this.z].a[n5 + 1] - this.O[this.z].a[n5];
                    while (n18 > 0) {
                        this.H[n7] = this.s[1].a[n5];
                        if (this.H[n7] != 7) {
                            if (bl3) {
                                this.a(this.H[n7], n6, n7);
                            } else {
                                this.I[n7] = S[this.H[n7]];
                            }
                        }
                        ++n7;
                        --n18;
                    }
                    ++n5;
                }
                n5 = this.O[this.z].a[20];
                n18 = 576 - this.O[this.z].a[21];
                while (n18 > 0 && n7 < 576) {
                    this.H[n7] = this.H[n5];
                    if (bl3) {
                        this.h[0][n7] = this.h[0][n5];
                        this.h[1][n7] = this.h[1][n5];
                    } else {
                        this.I[n7] = this.I[n5];
                    }
                    ++n7;
                    --n18;
                }
            }
        }
        n7 = 0;
        int n19 = 0;
        while (n19 < 32) {
            n5 = 0;
            while (n5 < 18) {
                if (this.H[n7] == 7) {
                    if (bl) {
                        this.e[0][n19][n5] = (this.d[0][n19][n5] + this.d[1][n19][n5]) * 0.70710677f;
                        this.e[1][n19][n5] = (this.d[0][n19][n5] - this.d[1][n19][n5]) * 0.70710677f;
                    } else {
                        this.e[0][n19][n5] = this.d[0][n19][n5];
                        this.e[1][n19][n5] = this.d[1][n19][n5];
                    }
                } else if (bl2) {
                    if (bl3) {
                        this.e[0][n19][n5] = this.d[0][n19][n5] * this.h[0][n7];
                        this.e[1][n19][n5] = this.d[0][n19][n5] * this.h[1][n7];
                    } else {
                        this.e[1][n19][n5] = this.d[0][n19][n5] / (1.0f + this.I[n7]);
                        this.e[0][n19][n5] = this.e[1][n19][n5] * this.I[n7];
                    }
                }
                ++n7;
                ++n5;
            }
            ++n19;
        }
    }

    private void a(int n2, int n3) {
        n n4 = this.q.b[n2].b[n3];
        int n5 = 0;
        while (n5 < 576) {
            n3 = n4.e != 0 && n4.g != 0 && n5 < 36 ? 0 : n4.f;
            float[] fArray = this.f;
            int n6 = 0;
            while (n6 < 18) {
                this.J[n6] = fArray[n6 + n5];
                ++n6;
            }
            int n7 = n3;
            float[] fArray2 = this.K;
            Object object = this.J;
            float f2 = 0.0f;
            float f3 = 0.0f;
            float f4 = 0.0f;
            float f5 = 0.0f;
            float f6 = 0.0f;
            float f7 = 0.0f;
            float f8 = 0.0f;
            float f9 = 0.0f;
            float f10 = 0.0f;
            float f11 = 0.0f;
            float f12 = 0.0f;
            float f13 = 0.0f;
            if (n7 == 2) {
                fArray2[0] = 0.0f;
                fArray2[1] = 0.0f;
                fArray2[2] = 0.0f;
                fArray2[3] = 0.0f;
                fArray2[4] = 0.0f;
                fArray2[5] = 0.0f;
                fArray2[6] = 0.0f;
                fArray2[7] = 0.0f;
                fArray2[8] = 0.0f;
                fArray2[9] = 0.0f;
                fArray2[10] = 0.0f;
                fArray2[11] = 0.0f;
                fArray2[12] = 0.0f;
                fArray2[13] = 0.0f;
                fArray2[14] = 0.0f;
                fArray2[15] = 0.0f;
                fArray2[16] = 0.0f;
                fArray2[17] = 0.0f;
                fArray2[18] = 0.0f;
                fArray2[19] = 0.0f;
                fArray2[20] = 0.0f;
                fArray2[21] = 0.0f;
                fArray2[22] = 0.0f;
                fArray2[23] = 0.0f;
                fArray2[24] = 0.0f;
                fArray2[25] = 0.0f;
                fArray2[26] = 0.0f;
                fArray2[27] = 0.0f;
                fArray2[28] = 0.0f;
                fArray2[29] = 0.0f;
                fArray2[30] = 0.0f;
                fArray2[31] = 0.0f;
                fArray2[32] = 0.0f;
                fArray2[33] = 0.0f;
                fArray2[34] = 0.0f;
                fArray2[35] = 0.0f;
                int n8 = 0;
                n7 = 0;
                while (n7 < 3) {
                    int n9 = n7 + 15;
                    object[n9] = object[n9] + object[n7 + 12];
                    int n10 = n7 + 12;
                    object[n10] = object[n10] + object[n7 + 9];
                    int n11 = n7 + 9;
                    object[n11] = object[n11] + object[n7 + 6];
                    int n12 = n7 + 6;
                    object[n12] = object[n12] + object[n7 + 3];
                    int n13 = n7 + 3;
                    object[n13] = object[n13] + object[n7 + 0];
                    int n14 = n7 + 15;
                    object[n14] = object[n14] + object[n7 + 9];
                    int n15 = n7 + 9;
                    object[n15] = object[n15] + object[n7 + 3];
                    f7 = object[n7 + 12] * 0.5f;
                    f6 = object[n7 + 6] * 0.8660254f;
                    f5 = object[n7 + 0] + f7;
                    f12 = object[n7 + 0] - object[n7 + 12];
                    f13 = f5 + f6;
                    f11 = f5 - f6;
                    f7 = object[n7 + 15] * 0.5f;
                    f6 = object[n7 + 9] * 0.8660254f;
                    f5 = object[n7 + 3] + f7;
                    f9 = object[n7 + 3] - object[n7 + 15];
                    f8 = f5 + f6;
                    f10 = f5 - f6;
                    f10 *= 1.9318516f;
                    f4 = f13;
                    f13 += (f8 *= 0.5176381f);
                    f8 = f4 - f8;
                    f4 = f12;
                    f12 += (f9 *= 0.70710677f);
                    f9 = f4 - f9;
                    f4 = f11;
                    f11 += f10;
                    f10 = f4 - f10;
                    f13 *= 0.5043145f;
                    f12 *= 0.5411961f;
                    f11 *= 0.6302362f;
                    f10 *= 0.8213398f;
                    f9 *= 1.306563f;
                    f8 *= 3.830649f;
                    f5 = -f13 * 0.7933533f;
                    f4 = -f13 * 0.6087614f;
                    f6 = -f12 * 0.9238795f;
                    f3 = -f12 * 0.38268343f;
                    f7 = -f11 * 0.9914449f;
                    f2 = -f11 * 0.13052619f;
                    f13 = f10;
                    f12 = f9 * 0.38268343f;
                    f11 = f8 * 0.6087614f;
                    f10 = -f8 * 0.7933533f;
                    f9 = -f9 * 0.9238795f;
                    f8 = -f13 * 0.9914449f;
                    int n16 = n8 + 6;
                    fArray2[n16] = fArray2[n16] + (f13 *= 0.13052619f);
                    int n17 = n8 + 7;
                    fArray2[n17] = fArray2[n17] + f12;
                    int n18 = n8 + 8;
                    fArray2[n18] = fArray2[n18] + f11;
                    int n19 = n8 + 9;
                    fArray2[n19] = fArray2[n19] + f10;
                    int n20 = n8 + 10;
                    fArray2[n20] = fArray2[n20] + f9;
                    int n21 = n8 + 11;
                    fArray2[n21] = fArray2[n21] + f8;
                    int n22 = n8 + 12;
                    fArray2[n22] = fArray2[n22] + f7;
                    int n23 = n8 + 13;
                    fArray2[n23] = fArray2[n23] + f6;
                    int n24 = n8 + 14;
                    fArray2[n24] = fArray2[n24] + f5;
                    int n25 = n8 + 15;
                    fArray2[n25] = fArray2[n25] + f4;
                    int n26 = n8 + 16;
                    fArray2[n26] = fArray2[n26] + f3;
                    int n27 = n8 + 17;
                    fArray2[n27] = fArray2[n27] + f2;
                    n8 += 6;
                    ++n7;
                }
            } else {
                object[17] = object[17] + object[16];
                object[16] = object[16] + object[15];
                object[15] = object[15] + object[14];
                object[14] = object[14] + object[13];
                object[13] = object[13] + object[12];
                object[12] = object[12] + object[11];
                object[11] = object[11] + object[10];
                object[10] = object[10] + object[9];
                object[9] = object[9] + object[8];
                object[8] = object[8] + object[7];
                object[7] = object[7] + object[6];
                object[6] = object[6] + object[5];
                object[5] = object[5] + object[4];
                object[4] = object[4] + object[3];
                object[3] = object[3] + object[2];
                object[2] = object[2] + object[1];
                object[1] = object[1] + object[0];
                object[17] = object[17] + object[15];
                object[15] = object[15] + object[13];
                object[13] = object[13] + object[11];
                object[11] = object[11] + object[9];
                object[9] = object[9] + object[7];
                object[7] = object[7] + object[5];
                object[5] = object[5] + object[3];
                object[3] = object[3] + object[1];
                f13 = object[0] + object[0];
                f12 = f13 + object[12];
                float f14 = f12 + object[4] * 1.8793852f + object[8] * 1.5320889f + object[16] * 0.34729636f;
                f6 = f13 + object[4] - object[8] - object[12] - object[12] - object[16];
                f7 = f12 - object[4] * 0.34729636f - object[8] * 1.8793852f + object[16] * 1.5320889f;
                f5 = f12 - object[4] * 1.5320889f + object[8] * 0.34729636f - object[16] * 1.8793852f;
                f4 = object[0] - object[4] + object[8] - object[12] + object[16];
                f13 = object[6] * 1.7320508f;
                float f15 = object[2] * 1.9696155f + f13 + object[10] * 1.2855753f + object[14] * 0.6840403f;
                f3 = (object[2] - object[10] - object[14]) * 1.7320508f;
                f2 = object[2] * 1.2855753f - f13 - object[10] * 0.6840403f + object[14] * 1.9696155f;
                f8 = object[2] * 0.6840403f - f13 + object[10] * 1.9696155f - object[14] * 1.2855753f;
                f13 = object[1] + object[1];
                f12 = f13 + object[13];
                float f16 = f12 + object[5] * 1.8793852f + object[9] * 1.5320889f + object[17] * 0.34729636f;
                float f17 = f13 + object[5] - object[9] - object[13] - object[13] - object[17];
                float f18 = f12 - object[5] * 0.34729636f - object[9] * 1.8793852f + object[17] * 1.5320889f;
                float f19 = f12 - object[5] * 1.5320889f + object[9] * 0.34729636f - object[17] * 1.8793852f;
                float f20 = (object[1] - object[5] + object[9] - object[13] + object[17]) * 0.70710677f;
                f13 = object[7] * 1.7320508f;
                float f21 = object[3] * 1.9696155f + f13 + object[11] * 1.2855753f + object[15] * 0.6840403f;
                float f22 = (object[3] - object[11] - object[15]) * 1.7320508f;
                float f23 = object[3] * 1.2855753f - f13 - object[11] * 0.6840403f + object[15] * 1.9696155f;
                float f24 = object[3] * 0.6840403f - f13 + object[11] * 1.9696155f - object[15] * 1.2855753f;
                float f25 = f14 + f15;
                float f26 = (f16 + f21) * 0.5019099f;
                f13 = f25 + f26;
                float f27 = f25 - f26;
                f25 = f6 + f3;
                f26 = (f17 + f22) * 0.5176381f;
                f12 = f25 + f26;
                float f28 = f25 - f26;
                f25 = f7 + f2;
                f26 = (f18 + f23) * 0.55168897f;
                f11 = f25 + f26;
                float f29 = f25 - f26;
                f25 = f5 + f8;
                f26 = (f19 + f24) * 0.61038727f;
                f10 = f25 + f26;
                float f30 = f25 - f26;
                f9 = f4 + f20;
                f20 = f4 - f20;
                f25 = f5 - f8;
                f26 = (f19 - f24) * 0.8717234f;
                f8 = f25 + f26;
                f19 = f25 - f26;
                f25 = f7 - f2;
                f26 = (f18 - f23) * 1.1831008f;
                f7 = f25 + f26;
                f2 = f25 - f26;
                f25 = f6 - f3;
                f26 = (f17 - f22) * 1.9318516f;
                f6 = f25 + f26;
                f3 = f25 - f26;
                f25 = f14 - f15;
                f26 = (f16 - f21) * 5.7368565f;
                f5 = f25 + f26;
                f4 = f25 - f26;
                object = W[n7];
                fArray2[0] = -f4 * object[0];
                fArray2[1] = -f3 * object[1];
                fArray2[2] = -f2 * object[2];
                fArray2[3] = -f19 * object[3];
                fArray2[4] = -f20 * object[4];
                fArray2[5] = -f30 * object[5];
                fArray2[6] = -f29 * object[6];
                fArray2[7] = -f28 * object[7];
                fArray2[8] = -f27 * object[8];
                fArray2[9] = f27 * object[9];
                fArray2[10] = f28 * object[10];
                fArray2[11] = f29 * object[11];
                fArray2[12] = f30 * object[12];
                fArray2[13] = f20 * object[13];
                fArray2[14] = f19 * object[14];
                fArray2[15] = f2 * object[15];
                fArray2[16] = f3 * object[16];
                fArray2[17] = f4 * object[17];
                fArray2[18] = f5 * object[18];
                fArray2[19] = f6 * object[19];
                fArray2[20] = f7 * object[20];
                fArray2[21] = f8 * object[21];
                fArray2[22] = f9 * object[22];
                fArray2[23] = f10 * object[23];
                fArray2[24] = f11 * object[24];
                fArray2[25] = f12 * object[25];
                fArray2[26] = f13 * object[26];
                fArray2[27] = f13 * object[27];
                fArray2[28] = f12 * object[28];
                fArray2[29] = f11 * object[29];
                fArray2[30] = f10 * object[30];
                fArray2[31] = f9 * object[31];
                fArray2[32] = f8 * object[32];
                fArray2[33] = f7 * object[33];
                fArray2[34] = f6 * object[34];
                fArray2[35] = f5 * object[35];
            }
            n6 = 0;
            while (n6 < 18) {
                fArray[n6 + n5] = this.J[n6];
                ++n6;
            }
            object = this.g;
            fArray[n5 + 0] = this.K[0] + object[n2][n5];
            object[n2][n5] = this.K[18];
            fArray[n5 + 1] = this.K[1] + object[n2][n5 + 1];
            object[n2][n5 + 1] = this.K[19];
            fArray[n5 + 2] = this.K[2] + object[n2][n5 + 2];
            object[n2][n5 + 2] = this.K[20];
            fArray[n5 + 3] = this.K[3] + object[n2][n5 + 3];
            object[n2][n5 + 3] = this.K[21];
            fArray[n5 + 4] = this.K[4] + object[n2][n5 + 4];
            object[n2][n5 + 4] = this.K[22];
            fArray[n5 + 5] = this.K[5] + object[n2][n5 + 5];
            object[n2][n5 + 5] = this.K[23];
            fArray[n5 + 6] = this.K[6] + object[n2][n5 + 6];
            object[n2][n5 + 6] = this.K[24];
            fArray[n5 + 7] = this.K[7] + object[n2][n5 + 7];
            object[n2][n5 + 7] = this.K[25];
            fArray[n5 + 8] = this.K[8] + object[n2][n5 + 8];
            object[n2][n5 + 8] = this.K[26];
            fArray[n5 + 9] = this.K[9] + object[n2][n5 + 9];
            object[n2][n5 + 9] = this.K[27];
            fArray[n5 + 10] = this.K[10] + object[n2][n5 + 10];
            object[n2][n5 + 10] = this.K[28];
            fArray[n5 + 11] = this.K[11] + object[n2][n5 + 11];
            object[n2][n5 + 11] = this.K[29];
            fArray[n5 + 12] = this.K[12] + object[n2][n5 + 12];
            object[n2][n5 + 12] = this.K[30];
            fArray[n5 + 13] = this.K[13] + object[n2][n5 + 13];
            object[n2][n5 + 13] = this.K[31];
            fArray[n5 + 14] = this.K[14] + object[n2][n5 + 14];
            object[n2][n5 + 14] = this.K[32];
            fArray[n5 + 15] = this.K[15] + object[n2][n5 + 15];
            object[n2][n5 + 15] = this.K[33];
            fArray[n5 + 16] = this.K[16] + object[n2][n5 + 16];
            object[n2][n5 + 16] = this.K[34];
            fArray[n5 + 17] = this.K[17] + object[n2][n5 + 17];
            object[n2][n5 + 17] = this.K[35];
            n5 += 18;
        }
    }

    private static int[] a(int[] nArray) {
        int n2 = 0;
        int[] nArray2 = new int[576];
        int n3 = 0;
        while (n3 < 13) {
            int n4 = nArray[n3];
            int n5 = nArray[n3 + 1];
            int n6 = 0;
            while (n6 < 3) {
                int n7 = n4;
                while (n7 < n5) {
                    nArray2[n7 * 3 + n6] = n2++;
                    ++n7;
                }
                ++n6;
            }
            ++n3;
        }
        return nArray2;
    }
}

