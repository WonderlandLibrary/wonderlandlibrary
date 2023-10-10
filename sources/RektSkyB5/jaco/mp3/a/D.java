/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.o;
import jaco.mp3.a.s;
import jaco.mp3.a.z;

public final class D {
    private static int[][] a = new int[][]{{22050, 24000, 16000, 1}, {44100, 48000, 32000, 1}, {11025, 12000, 8000, 1}};
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    private int j;
    private int k;
    private double[] l = new double[]{-1.0, 384.0, 1152.0, 1152.0};
    private boolean m;
    private int n;
    private int o;
    private byte[] p;
    private byte q = 0;
    private o r;
    private short s;
    private int t;
    private int u;
    private static int[][][] v;
    private static String[][][] w;

    static {
        int[][][] nArrayArray = new int[3][][];
        int[][] nArrayArray2 = new int[3][];
        int[] nArray = new int[16];
        nArray[1] = 32000;
        nArray[2] = 48000;
        nArray[3] = 56000;
        nArray[4] = 64000;
        nArray[5] = 80000;
        nArray[6] = 96000;
        nArray[7] = 112000;
        nArray[8] = 128000;
        nArray[9] = 144000;
        nArray[10] = 160000;
        nArray[11] = 176000;
        nArray[12] = 192000;
        nArray[13] = 224000;
        nArray[14] = 256000;
        nArrayArray2[0] = nArray;
        int[] nArray2 = new int[16];
        nArray2[1] = 8000;
        nArray2[2] = 16000;
        nArray2[3] = 24000;
        nArray2[4] = 32000;
        nArray2[5] = 40000;
        nArray2[6] = 48000;
        nArray2[7] = 56000;
        nArray2[8] = 64000;
        nArray2[9] = 80000;
        nArray2[10] = 96000;
        nArray2[11] = 112000;
        nArray2[12] = 128000;
        nArray2[13] = 144000;
        nArray2[14] = 160000;
        nArrayArray2[1] = nArray2;
        int[] nArray3 = new int[16];
        nArray3[1] = 8000;
        nArray3[2] = 16000;
        nArray3[3] = 24000;
        nArray3[4] = 32000;
        nArray3[5] = 40000;
        nArray3[6] = 48000;
        nArray3[7] = 56000;
        nArray3[8] = 64000;
        nArray3[9] = 80000;
        nArray3[10] = 96000;
        nArray3[11] = 112000;
        nArray3[12] = 128000;
        nArray3[13] = 144000;
        nArray3[14] = 160000;
        nArrayArray2[2] = nArray3;
        nArrayArray[0] = nArrayArray2;
        int[][] nArrayArray3 = new int[3][];
        int[] nArray4 = new int[16];
        nArray4[1] = 32000;
        nArray4[2] = 64000;
        nArray4[3] = 96000;
        nArray4[4] = 128000;
        nArray4[5] = 160000;
        nArray4[6] = 192000;
        nArray4[7] = 224000;
        nArray4[8] = 256000;
        nArray4[9] = 288000;
        nArray4[10] = 320000;
        nArray4[11] = 352000;
        nArray4[12] = 384000;
        nArray4[13] = 416000;
        nArray4[14] = 448000;
        nArrayArray3[0] = nArray4;
        int[] nArray5 = new int[16];
        nArray5[1] = 32000;
        nArray5[2] = 48000;
        nArray5[3] = 56000;
        nArray5[4] = 64000;
        nArray5[5] = 80000;
        nArray5[6] = 96000;
        nArray5[7] = 112000;
        nArray5[8] = 128000;
        nArray5[9] = 160000;
        nArray5[10] = 192000;
        nArray5[11] = 224000;
        nArray5[12] = 256000;
        nArray5[13] = 320000;
        nArray5[14] = 384000;
        nArrayArray3[1] = nArray5;
        int[] nArray6 = new int[16];
        nArray6[1] = 32000;
        nArray6[2] = 40000;
        nArray6[3] = 48000;
        nArray6[4] = 56000;
        nArray6[5] = 64000;
        nArray6[6] = 80000;
        nArray6[7] = 96000;
        nArray6[8] = 112000;
        nArray6[9] = 128000;
        nArray6[10] = 160000;
        nArray6[11] = 192000;
        nArray6[12] = 224000;
        nArray6[13] = 256000;
        nArray6[14] = 320000;
        nArrayArray3[2] = nArray6;
        nArrayArray[1] = nArrayArray3;
        int[][] nArrayArray4 = new int[3][];
        int[] nArray7 = new int[16];
        nArray7[1] = 32000;
        nArray7[2] = 48000;
        nArray7[3] = 56000;
        nArray7[4] = 64000;
        nArray7[5] = 80000;
        nArray7[6] = 96000;
        nArray7[7] = 112000;
        nArray7[8] = 128000;
        nArray7[9] = 144000;
        nArray7[10] = 160000;
        nArray7[11] = 176000;
        nArray7[12] = 192000;
        nArray7[13] = 224000;
        nArray7[14] = 256000;
        nArrayArray4[0] = nArray7;
        int[] nArray8 = new int[16];
        nArray8[1] = 8000;
        nArray8[2] = 16000;
        nArray8[3] = 24000;
        nArray8[4] = 32000;
        nArray8[5] = 40000;
        nArray8[6] = 48000;
        nArray8[7] = 56000;
        nArray8[8] = 64000;
        nArray8[9] = 80000;
        nArray8[10] = 96000;
        nArray8[11] = 112000;
        nArray8[12] = 128000;
        nArray8[13] = 144000;
        nArray8[14] = 160000;
        nArrayArray4[1] = nArray8;
        int[] nArray9 = new int[16];
        nArray9[1] = 8000;
        nArray9[2] = 16000;
        nArray9[3] = 24000;
        nArray9[4] = 32000;
        nArray9[5] = 40000;
        nArray9[6] = 48000;
        nArray9[7] = 56000;
        nArray9[8] = 64000;
        nArray9[9] = 80000;
        nArray9[10] = 96000;
        nArray9[11] = 112000;
        nArray9[12] = 128000;
        nArray9[13] = 144000;
        nArray9[14] = 160000;
        nArrayArray4[2] = nArray9;
        nArrayArray[2] = nArrayArray4;
        v = nArrayArray;
        w = new String[][][]{{{"free format", "32 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "176 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}}, {{"free format", "32 kbit/s", "64 kbit/s", "96 kbit/s", "128 kbit/s", "160 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "288 kbit/s", "320 kbit/s", "352 kbit/s", "384 kbit/s", "416 kbit/s", "448 kbit/s", "forbidden"}, {"free format", "32 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "160 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "320 kbit/s", "384 kbit/s", "forbidden"}, {"free format", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "160 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "320 kbit/s", "forbidden"}}, {{"free format", "32 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "176 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}}};
    }

    D() {
    }

    public final String toString() {
        String string;
        Object object;
        String string2;
        String string3;
        String string4;
        String string5;
        StringBuffer stringBuffer = new StringBuffer(200);
        stringBuffer.append("Layer ");
        D d2 = object;
        switch (d2.b) {
            case 1: {
                string5 = "I";
                break;
            }
            case 2: {
                string5 = "II";
                break;
            }
            case 3: {
                string5 = "III";
                break;
            }
            default: {
                string5 = null;
            }
        }
        stringBuffer.append(string5);
        stringBuffer.append(" frame ");
        d2 = object;
        switch (d2.h) {
            case 0: {
                string4 = "Stereo";
                break;
            }
            case 1: {
                string4 = "Joint stereo";
                break;
            }
            case 2: {
                string4 = "Dual channel";
                break;
            }
            case 3: {
                string4 = "Single channel";
                break;
            }
            default: {
                string4 = null;
            }
        }
        stringBuffer.append(string4);
        stringBuffer.append(' ');
        d2 = object;
        switch (d2.g) {
            case 1: {
                string3 = "MPEG-1";
                break;
            }
            case 0: {
                string3 = "MPEG-2 LSF";
                break;
            }
            case 2: {
                string3 = "MPEG-2.5 LSF";
                break;
            }
            default: {
                string3 = null;
            }
        }
        stringBuffer.append(string3);
        d2 = object;
        if (!(d2.c == 0)) {
            stringBuffer.append(" no");
        }
        stringBuffer.append(" checksums");
        stringBuffer.append(' ');
        d2 = object;
        switch (d2.i) {
            case 2: {
                if (d2.g == 1) {
                    string2 = "32 kHz";
                    break;
                }
                if (d2.g == 0) {
                    string2 = "16 kHz";
                    break;
                }
                string2 = "8 kHz";
                break;
            }
            case 0: {
                if (d2.g == 1) {
                    string2 = "44.1 kHz";
                    break;
                }
                if (d2.g == 0) {
                    string2 = "22.05 kHz";
                    break;
                }
                string2 = "11.025 kHz";
                break;
            }
            case 1: {
                if (d2.g == 1) {
                    string2 = "48 kHz";
                    break;
                }
                if (d2.g == 0) {
                    string2 = "24 kHz";
                    break;
                }
                string2 = "12 kHz";
                break;
            }
            default: {
                string2 = null;
            }
        }
        stringBuffer.append(string2);
        stringBuffer.append(',');
        stringBuffer.append(' ');
        d2 = object;
        if (d2.m) {
            int n2;
            object = d2;
            if (((D)object).m) {
                float f2;
                float f3 = ((D)object).o << 3;
                d2 = object;
                if (d2.m) {
                    D d3 = d2;
                    double d4 = d2.l[d3.b] / (double)d2.e();
                    if (d2.g == 0 || d2.g == 2) {
                        d4 /= 2.0;
                    }
                    f2 = (float)(d4 * 1000.0);
                } else {
                    float[][] fArrayArray = new float[][]{{8.707483f, 8.0f, 12.0f}, {26.12245f, 24.0f, 36.0f}, {26.12245f, 24.0f, 36.0f}};
                    f2 = fArrayArray[d2.b - 1][d2.i];
                }
                n2 = (int)(f3 / (f2 * (float)((D)object).n)) * 1000;
            } else {
                n2 = v[((D)object).g][((D)object).b - 1][((D)object).d];
            }
            string = String.valueOf(Integer.toString(n2 / 1000)) + " kb/s";
        } else {
            string = w[d2.g][d2.b - 1][d2.d];
        }
        stringBuffer.append(string);
        object = stringBuffer.toString();
        return object;
    }

    /*
     * Unable to fully structure code
     */
    final void a(z var1_1, o[] var2_2) {
        var5_3 = false;
        do {
            block20: {
                var3_4 = var1_1.a(this.q);
                if (this.q == 0) {
                    this.g = var3_4 >>> 19 & 1;
                    if ((var3_4 >>> 20 & 1) == 0) {
                        if (this.g == 0) {
                            this.g = 2;
                        } else {
                            throw z.b(256);
                        }
                    }
                    if ((this.i = var3_4 >>> 10 & 3) == 3) {
                        throw z.b(256);
                    }
                }
                this.b = 4 - (var3_4 >>> 17) & 3;
                this.c = var3_4 >>> 16 & 1;
                this.d = var3_4 >>> 12 & 15;
                this.e = var3_4 >>> 9 & 1;
                this.h = var3_4 >>> 6 & 3;
                this.f = var3_4 >>> 4 & 3;
                this.k = this.h == 1 ? (this.f << 2) + 4 : 0;
                if (this.b == 1) {
                    this.j = 32;
                } else {
                    var4_5 = this.d;
                    if (this.h != 3) {
                        var4_5 = var4_5 == 4 ? 1 : (var4_5 -= 4);
                    }
                    this.j = var4_5 == 1 || var4_5 == 2 ? (this.i == 2 ? 12 : 8) : (this.i == 1 || var4_5 >= 3 && var4_5 <= 5 ? 27 : 30);
                }
                if (this.k > this.j) {
                    this.k = this.j;
                }
                var4_6 = this;
                if (var4_6.b != 1) break block20;
                var4_6.t = 12 * D.v[var4_6.g][0][var4_6.d] / D.a[var4_6.g][var4_6.i];
                if (var4_6.e != 0) {
                    ++var4_6.t;
                }
                var4_6.t <<= 2;
                ** GOTO lbl-1000
            }
            var4_6.t = 144 * D.v[var4_6.g][var4_6.b - 1][var4_6.d] / D.a[var4_6.g][var4_6.i];
            if (var4_6.g == 0 || var4_6.g == 2) {
                var4_6.t >>= 1;
            }
            if (var4_6.e != 0) {
                ++var4_6.t;
            }
            if (var4_6.b == 3) {
                var4_6.u = var4_6.g == 1 ? var4_6.t - (var4_6.h == 3 ? 17 : 32) - (var4_6.c != 0 ? 0 : 2) - 4 : var4_6.t - (var4_6.h == 3 ? 9 : 17) - (var4_6.c != 0 ? 0 : 2) - 4;
            } else lbl-1000:
            // 2 sources

            {
                var4_6.u = 0;
            }
            var4_6.t -= 4;
            var4_6.t;
            var4_5 = var1_1.c(this.t);
            if (this.t >= 0 && var4_5 != this.t) {
                throw z.b(261);
            }
            if (var1_1.a((int)this.q)) {
                if (this.q == 0) {
                    this.q = z.b;
                    var1_1.e(var3_4 & -521024);
                }
                var5_3 = true;
                continue;
            }
            var1_1.c();
        } while (!var5_3);
        var1_1.e();
        if (this.c == 0) {
            this.s = (short)var1_1.d(16);
            if (this.r == null) {
                this.r = new o();
            }
            this.r.a(var3_4, 16);
            var2_2[0] = this.r;
            return;
        }
        var2_2[0] = null;
    }

    final void a(byte[] byArray) {
        String string = "Xing";
        byte[] byArray2 = new byte[4];
        int n2 = this.g == 1 ? (this.h == 3 ? 17 : 32) : (this.h == 3 ? 9 : 17);
        try {
            System.arraycopy(byArray, n2, byArray2, 0, 4);
            if (string.equals(new String(byArray2))) {
                this.m = true;
                this.n = -1;
                this.o = -1;
                this.p = new byte[100];
                byte[] byArray3 = new byte[4];
                System.arraycopy(byArray, n2 + 4, byArray3, 0, byArray3.length);
                int n3 = 4 + byArray3.length;
                if ((byArray3[3] & 1) != 0) {
                    System.arraycopy(byArray, n2 + n3, byArray2, 0, byArray2.length);
                    this.n = byArray2[0] << 24 & 0xFF000000 | byArray2[1] << 16 & 0xFF0000 | byArray2[2] << 8 & 0xFF00 | byArray2[3] & 0xFF;
                    n3 += 4;
                }
                if ((byArray3[3] & 2) != 0) {
                    System.arraycopy(byArray, n2 + n3, byArray2, 0, byArray2.length);
                    this.o = byArray2[0] << 24 & 0xFF000000 | byArray2[1] << 16 & 0xFF0000 | byArray2[2] << 8 & 0xFF00 | byArray2[3] & 0xFF;
                    n3 += 4;
                }
                if ((byArray3[3] & 4) != 0) {
                    System.arraycopy(byArray, n2 + n3, this.p, 0, this.p.length);
                    n3 += this.p.length;
                }
                if ((byArray3[3] & 8) != 0) {
                    System.arraycopy(byArray, n2 + n3, byArray2, 0, byArray2.length);
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new s("XingVBRHeader Corrupted", (Throwable)arrayIndexOutOfBoundsException);
        }
        String string2 = "VBRI";
        try {
            System.arraycopy(byArray, 32, byArray2, 0, 4);
            if (string2.equals(new String(byArray2))) {
                this.m = true;
                this.n = -1;
                this.o = -1;
                this.p = new byte[100];
                System.arraycopy(byArray, 42, byArray2, 0, byArray2.length);
                this.o = byArray2[0] << 24 & 0xFF000000 | byArray2[1] << 16 & 0xFF0000 | byArray2[2] << 8 & 0xFF00 | byArray2[3] & 0xFF;
                System.arraycopy(byArray, 46, byArray2, 0, byArray2.length);
                this.n = byArray2[0] << 24 & 0xFF000000 | byArray2[1] << 16 & 0xFF0000 | byArray2[2] << 8 & 0xFF00 | byArray2[3] & 0xFF;
                return;
            }
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new s("VBRIVBRHeader Corrupted", (Throwable)arrayIndexOutOfBoundsException);
        }
    }

    public final int a() {
        return this.g;
    }

    public final int b() {
        return this.b;
    }

    public final int c() {
        return this.d;
    }

    public final int d() {
        return this.i;
    }

    public final int e() {
        return a[this.g][this.i];
    }

    public final int f() {
        return this.h;
    }

    public final boolean g() {
        return this.s == this.r.a();
    }

    public final int h() {
        return this.u;
    }

    public final int i() {
        return this.f;
    }

    public final int j() {
        return this.j;
    }

    public final int k() {
        return this.k;
    }
}

