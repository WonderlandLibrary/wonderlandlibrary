/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

class Mdct {
    int n;
    int log2n;
    float[] trig;
    int[] bitrev;
    float scale;
    float[] _x = new float[1024];
    float[] _w = new float[1024];

    Mdct() {
    }

    void init(int n2) {
        int i2;
        this.bitrev = new int[n2 / 4];
        this.trig = new float[n2 + n2 / 4];
        this.log2n = (int)Math.rint(Math.log(n2) / Math.log(2.0));
        this.n = n2;
        int AE = 0;
        int AO = 1;
        int BE = AE + n2 / 2;
        int BO = BE + 1;
        int CE = BE + n2 / 2;
        int CO = CE + 1;
        for (i2 = 0; i2 < n2 / 4; ++i2) {
            this.trig[AE + i2 * 2] = (float)Math.cos(Math.PI / (double)n2 * (double)(4 * i2));
            this.trig[AO + i2 * 2] = (float)(-Math.sin(Math.PI / (double)n2 * (double)(4 * i2)));
            this.trig[BE + i2 * 2] = (float)Math.cos(Math.PI / (double)(2 * n2) * (double)(2 * i2 + 1));
            this.trig[BO + i2 * 2] = (float)Math.sin(Math.PI / (double)(2 * n2) * (double)(2 * i2 + 1));
        }
        for (i2 = 0; i2 < n2 / 8; ++i2) {
            this.trig[CE + i2 * 2] = (float)Math.cos(Math.PI / (double)n2 * (double)(4 * i2 + 2));
            this.trig[CO + i2 * 2] = (float)(-Math.sin(Math.PI / (double)n2 * (double)(4 * i2 + 2)));
        }
        int mask = (1 << this.log2n - 1) - 1;
        int msb = 1 << this.log2n - 2;
        for (int i3 = 0; i3 < n2 / 8; ++i3) {
            int acc = 0;
            int j2 = 0;
            while (msb >>> j2 != 0) {
                if ((msb >>> j2 & i3) != 0) {
                    acc |= 1 << j2;
                }
                ++j2;
            }
            this.bitrev[i3 * 2] = ~acc & mask;
            this.bitrev[i3 * 2 + 1] = acc;
        }
        this.scale = 4.0f / (float)n2;
    }

    void clear() {
    }

    void forward(float[] in, float[] out) {
    }

    synchronized void backward(float[] in, float[] out) {
        int i2;
        if (this._x.length < this.n / 2) {
            this._x = new float[this.n / 2];
        }
        if (this._w.length < this.n / 2) {
            this._w = new float[this.n / 2];
        }
        float[] x2 = this._x;
        float[] w2 = this._w;
        int n2 = this.n >>> 1;
        int n4 = this.n >>> 2;
        int n8 = this.n >>> 3;
        int inO = 1;
        int xO = 0;
        int A = n2;
        for (i2 = 0; i2 < n8; ++i2) {
            x2[xO++] = -in[inO + 2] * this.trig[(A -= 2) + 1] - in[inO] * this.trig[A];
            x2[xO++] = in[inO] * this.trig[A + 1] - in[inO + 2] * this.trig[A];
            inO += 4;
        }
        inO = n2 - 4;
        for (i2 = 0; i2 < n8; ++i2) {
            x2[xO++] = in[inO] * this.trig[(A -= 2) + 1] + in[inO + 2] * this.trig[A];
            x2[xO++] = in[inO] * this.trig[A] - in[inO + 2] * this.trig[A + 1];
            inO -= 4;
        }
        float[] xxx = this.mdct_kernel(x2, w2, this.n, n2, n4, n8);
        int xx = 0;
        int B = n2;
        int o1 = n4;
        int o2 = o1 - 1;
        int o3 = n4 + n2;
        int o4 = o3 - 1;
        for (int i3 = 0; i3 < n4; ++i3) {
            float temp1 = xxx[xx] * this.trig[B + 1] - xxx[xx + 1] * this.trig[B];
            float temp2 = -(xxx[xx] * this.trig[B] + xxx[xx + 1] * this.trig[B + 1]);
            out[o1] = -temp1;
            out[o2] = temp1;
            out[o3] = temp2;
            out[o4] = temp2;
            ++o1;
            --o2;
            ++o3;
            --o4;
            xx += 2;
            B += 2;
        }
    }

    private float[] mdct_kernel(float[] x2, float[] w2, int n2, int n22, int n4, int n8) {
        int i2;
        int xA = n4;
        int xB = 0;
        int w22 = n4;
        int A = n22;
        for (i2 = 0; i2 < n4; ++i2) {
            float x0 = x2[xA] - x2[xB];
            w2[w22 + i2] = x2[xA++] + x2[xB++];
            float x1 = x2[xA] - x2[xB];
            w2[i2++] = x0 * this.trig[A -= 4] + x1 * this.trig[A + 1];
            w2[i2] = x1 * this.trig[A] - x0 * this.trig[A + 1];
            w2[w22 + i2] = x2[xA++] + x2[xB++];
        }
        for (i2 = 0; i2 < this.log2n - 3; ++i2) {
            int k0 = n2 >>> i2 + 2;
            int k1 = 1 << i2 + 3;
            int wbase = n22 - 2;
            A = 0;
            for (int r2 = 0; r2 < k0 >>> 2; ++r2) {
                int w1 = wbase;
                w22 = w1 - (k0 >> 1);
                float AEv = this.trig[A];
                float AOv = this.trig[A + 1];
                wbase -= 2;
                ++k0;
                for (int s2 = 0; s2 < 2 << i2; ++s2) {
                    float wB = w2[w1] - w2[w22];
                    x2[w1] = w2[w1] + w2[w22];
                    float wA = w2[++w1] - w2[++w22];
                    x2[w1] = w2[w1] + w2[w22];
                    x2[w22] = wA * AEv - wB * AOv;
                    x2[w22 - 1] = wB * AEv + wA * AOv;
                    w1 -= k0;
                    w22 -= k0;
                }
                --k0;
                A += k1;
            }
            float[] temp = w2;
            w2 = x2;
            x2 = temp;
        }
        int C2 = n2;
        int bit = 0;
        int x1 = 0;
        int x22 = n22 - 1;
        for (int i3 = 0; i3 < n8; ++i3) {
            int t1 = this.bitrev[bit++];
            int t2 = this.bitrev[bit++];
            float wA = w2[t1] - w2[t2 + 1];
            float wB = w2[t1 - 1] + w2[t2];
            float wC = w2[t1] + w2[t2 + 1];
            float wD = w2[t1 - 1] - w2[t2];
            float wACE = wA * this.trig[C2];
            float wBCE = wB * this.trig[C2++];
            float wACO = wA * this.trig[C2];
            float wBCO = wB * this.trig[C2++];
            x2[x1++] = (wC + wACO + wBCE) * 0.5f;
            x2[x22--] = (-wD + wBCO - wACE) * 0.5f;
            x2[x1++] = (wD + wBCO - wACE) * 0.5f;
            x2[x22--] = (wC - wACO - wBCE) * 0.5f;
        }
        return x2;
    }
}

