/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.CodeBook;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.FuncFloor;
import com.jcraft.jorbis.Info;
import com.jcraft.jorbis.InfoMode;
import com.jcraft.jorbis.Lpc;
import com.jcraft.jorbis.Lsp;
import com.jcraft.jorbis.Util;

class Floor0
extends FuncFloor {
    float[] lsp = null;

    Floor0() {
    }

    void pack(Object i2, Buffer opb) {
        InfoFloor0 info = (InfoFloor0)i2;
        opb.write(info.order, 8);
        opb.write(info.rate, 16);
        opb.write(info.barkmap, 16);
        opb.write(info.ampbits, 6);
        opb.write(info.ampdB, 8);
        opb.write(info.numbooks - 1, 4);
        for (int j2 = 0; j2 < info.numbooks; ++j2) {
            opb.write(info.books[j2], 8);
        }
    }

    Object unpack(Info vi, Buffer opb) {
        InfoFloor0 info = new InfoFloor0();
        info.order = opb.read(8);
        info.rate = opb.read(16);
        info.barkmap = opb.read(16);
        info.ampbits = opb.read(6);
        info.ampdB = opb.read(8);
        info.numbooks = opb.read(4) + 1;
        if (info.order < 1 || info.rate < 1 || info.barkmap < 1 || info.numbooks < 1) {
            return null;
        }
        for (int j2 = 0; j2 < info.numbooks; ++j2) {
            info.books[j2] = opb.read(8);
            if (info.books[j2] >= 0 && info.books[j2] < vi.books) continue;
            return null;
        }
        return info;
    }

    Object look(DspState vd, InfoMode mi, Object i2) {
        Info vi = vd.vi;
        InfoFloor0 info = (InfoFloor0)i2;
        LookFloor0 look = new LookFloor0();
        look.m = info.order;
        look.n = vi.blocksizes[mi.blockflag] / 2;
        look.ln = info.barkmap;
        look.vi = info;
        look.lpclook.init(look.ln, look.m);
        float scale = (float)look.ln / Floor0.toBARK((float)((double)info.rate / 2.0));
        look.linearmap = new int[look.n];
        for (int j2 = 0; j2 < look.n; ++j2) {
            int val2 = (int)Math.floor(Floor0.toBARK((float)((double)info.rate / 2.0 / (double)look.n * (double)j2)) * scale);
            if (val2 >= look.ln) {
                val2 = look.ln;
            }
            look.linearmap[j2] = val2;
        }
        return look;
    }

    static float toBARK(float f2) {
        return (float)(13.1 * Math.atan(7.4E-4 * (double)f2) + 2.24 * Math.atan((double)(f2 * f2) * 1.85E-8) + 1.0E-4 * (double)f2);
    }

    Object state(Object i2) {
        EchstateFloor0 state = new EchstateFloor0();
        InfoFloor0 info = (InfoFloor0)i2;
        state.codewords = new int[info.order];
        state.curve = new float[info.barkmap];
        state.frameno = -1L;
        return state;
    }

    void free_info(Object i2) {
    }

    void free_look(Object i2) {
    }

    void free_state(Object vs) {
    }

    int forward(Block vb, Object i2, float[] in, float[] out, Object vs) {
        return 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    int inverse(Block vb, Object i2, float[] out) {
        LookFloor0 look = (LookFloor0)i2;
        InfoFloor0 info = look.vi;
        int ampraw = vb.opb.read(info.ampbits);
        if (ampraw > 0) {
            int maxval = (1 << info.ampbits) - 1;
            float amp = (float)ampraw / (float)maxval * (float)info.ampdB;
            int booknum = vb.opb.read(Util.ilog(info.numbooks));
            if (booknum != -1 && booknum < info.numbooks) {
                Floor0 floor0 = this;
                synchronized (floor0) {
                    int j2;
                    if (this.lsp == null || this.lsp.length < look.m) {
                        this.lsp = new float[look.m];
                    } else {
                        for (int j3 = 0; j3 < look.m; ++j3) {
                            this.lsp[j3] = 0.0f;
                        }
                    }
                    CodeBook b2 = vb.vd.fullbooks[info.books[booknum]];
                    float last = 0.0f;
                    for (j2 = 0; j2 < look.m; ++j2) {
                        out[j2] = 0.0f;
                    }
                    for (j2 = 0; j2 < look.m; j2 += b2.dim) {
                        if (b2.decodevs(this.lsp, j2, vb.opb, 1, -1) != -1) continue;
                        for (int k2 = 0; k2 < look.n; ++k2) {
                            out[k2] = 0.0f;
                        }
                        return 0;
                    }
                    j2 = 0;
                    while (j2 < look.m) {
                        for (int k3 = 0; k3 < b2.dim; ++k3) {
                            int n2 = j2++;
                            this.lsp[n2] = this.lsp[n2] + last;
                        }
                        last = this.lsp[j2 - 1];
                    }
                    Lsp.lsp_to_curve(out, look.linearmap, look.n, look.ln, this.lsp, look.m, amp, info.ampdB);
                    return 1;
                }
            }
        }
        return 0;
    }

    Object inverse1(Block vb, Object i2, Object memo) {
        int ampraw;
        LookFloor0 look = (LookFloor0)i2;
        InfoFloor0 info = look.vi;
        float[] lsp = null;
        if (memo instanceof float[]) {
            lsp = (float[])memo;
        }
        if ((ampraw = vb.opb.read(info.ampbits)) > 0) {
            int maxval = (1 << info.ampbits) - 1;
            float amp = (float)ampraw / (float)maxval * (float)info.ampdB;
            int booknum = vb.opb.read(Util.ilog(info.numbooks));
            if (booknum != -1 && booknum < info.numbooks) {
                int j2;
                CodeBook b2 = vb.vd.fullbooks[info.books[booknum]];
                float last = 0.0f;
                if (lsp == null || lsp.length < look.m + 1) {
                    lsp = new float[look.m + 1];
                } else {
                    for (j2 = 0; j2 < lsp.length; ++j2) {
                        lsp[j2] = 0.0f;
                    }
                }
                for (j2 = 0; j2 < look.m; j2 += b2.dim) {
                    if (b2.decodev_set(lsp, j2, vb.opb, b2.dim) != -1) continue;
                    return null;
                }
                j2 = 0;
                while (j2 < look.m) {
                    for (int k2 = 0; k2 < b2.dim; ++k2) {
                        int n2 = j2++;
                        lsp[n2] = lsp[n2] + last;
                    }
                    last = lsp[j2 - 1];
                }
                lsp[look.m] = amp;
                return lsp;
            }
        }
        return null;
    }

    int inverse2(Block vb, Object i2, Object memo, float[] out) {
        LookFloor0 look = (LookFloor0)i2;
        InfoFloor0 info = look.vi;
        if (memo != null) {
            float[] lsp = (float[])memo;
            float amp = lsp[look.m];
            Lsp.lsp_to_curve(out, look.linearmap, look.n, look.ln, lsp, look.m, amp, info.ampdB);
            return 1;
        }
        for (int j2 = 0; j2 < look.n; ++j2) {
            out[j2] = 0.0f;
        }
        return 0;
    }

    static float fromdB(float x2) {
        return (float)Math.exp((double)x2 * 0.11512925);
    }

    static void lsp_to_lpc(float[] lsp, float[] lpc, int m2) {
        int j2;
        int i2;
        int m22 = m2 / 2;
        float[] O = new float[m22];
        float[] E2 = new float[m22];
        float[] Ae = new float[m22 + 1];
        float[] Ao = new float[m22 + 1];
        float[] Be = new float[m22];
        float[] Bo = new float[m22];
        for (i2 = 0; i2 < m22; ++i2) {
            O[i2] = (float)(-2.0 * Math.cos(lsp[i2 * 2]));
            E2[i2] = (float)(-2.0 * Math.cos(lsp[i2 * 2 + 1]));
        }
        for (j2 = 0; j2 < m22; ++j2) {
            Ae[j2] = 0.0f;
            Ao[j2] = 1.0f;
            Be[j2] = 0.0f;
            Bo[j2] = 1.0f;
        }
        Ao[j2] = 1.0f;
        Ae[j2] = 1.0f;
        for (i2 = 1; i2 < m2 + 1; ++i2) {
            float B = 0.0f;
            float A = 0.0f;
            for (j2 = 0; j2 < m22; ++j2) {
                float temp = O[j2] * Ao[j2] + Ae[j2];
                Ae[j2] = Ao[j2];
                Ao[j2] = A;
                A += temp;
                temp = E2[j2] * Bo[j2] + Be[j2];
                Be[j2] = Bo[j2];
                Bo[j2] = B;
                B += temp;
            }
            lpc[i2 - 1] = (A + Ao[j2] + B - Ae[j2]) / 2.0f;
            Ao[j2] = A;
            Ae[j2] = B;
        }
    }

    static void lpc_to_curve(float[] curve, float[] lpc, float amp, LookFloor0 l2, String name, int frameno) {
        float[] lcurve = new float[Math.max(l2.ln * 2, l2.m * 2 + 2)];
        if (amp == 0.0f) {
            for (int j2 = 0; j2 < l2.n; ++j2) {
                curve[j2] = 0.0f;
            }
            return;
        }
        l2.lpclook.lpc_to_curve(lcurve, lpc, amp);
        for (int i2 = 0; i2 < l2.n; ++i2) {
            curve[i2] = lcurve[l2.linearmap[i2]];
        }
    }

    class EchstateFloor0 {
        int[] codewords;
        float[] curve;
        long frameno;
        long codes;

        EchstateFloor0() {
        }
    }

    class LookFloor0 {
        int n;
        int ln;
        int m;
        int[] linearmap;
        InfoFloor0 vi;
        Lpc lpclook = new Lpc();

        LookFloor0() {
        }
    }

    class InfoFloor0 {
        int order;
        int rate;
        int barkmap;
        int ampbits;
        int ampdB;
        int numbooks;
        int[] books = new int[16];

        InfoFloor0() {
        }
    }
}

