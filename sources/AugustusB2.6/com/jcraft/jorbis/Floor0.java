// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Floor0 extends FuncFloor
{
    float[] lsp;
    
    Floor0() {
        this.lsp = null;
    }
    
    void pack(final Object o, final Buffer buffer) {
        final InfoFloor0 infoFloor0 = (InfoFloor0)o;
        buffer.write(infoFloor0.order, 8);
        buffer.write(infoFloor0.rate, 16);
        buffer.write(infoFloor0.barkmap, 16);
        buffer.write(infoFloor0.ampbits, 6);
        buffer.write(infoFloor0.ampdB, 8);
        buffer.write(infoFloor0.numbooks - 1, 4);
        for (int i = 0; i < infoFloor0.numbooks; ++i) {
            buffer.write(infoFloor0.books[i], 8);
        }
    }
    
    Object unpack(final Info info, final Buffer buffer) {
        final InfoFloor0 infoFloor0 = new InfoFloor0();
        infoFloor0.order = buffer.read(8);
        infoFloor0.rate = buffer.read(16);
        infoFloor0.barkmap = buffer.read(16);
        infoFloor0.ampbits = buffer.read(6);
        infoFloor0.ampdB = buffer.read(8);
        infoFloor0.numbooks = buffer.read(4) + 1;
        if (infoFloor0.order < 1 || infoFloor0.rate < 1 || infoFloor0.barkmap < 1 || infoFloor0.numbooks < 1) {
            return null;
        }
        for (int i = 0; i < infoFloor0.numbooks; ++i) {
            infoFloor0.books[i] = buffer.read(8);
            if (infoFloor0.books[i] < 0 || infoFloor0.books[i] >= info.books) {
                return null;
            }
        }
        return infoFloor0;
    }
    
    Object look(final DspState dspState, final InfoMode infoMode, final Object o) {
        final Info vi = dspState.vi;
        final InfoFloor0 vi2 = (InfoFloor0)o;
        final LookFloor0 lookFloor0 = new LookFloor0();
        lookFloor0.m = vi2.order;
        lookFloor0.n = vi.blocksizes[infoMode.blockflag] / 2;
        lookFloor0.ln = vi2.barkmap;
        lookFloor0.vi = vi2;
        lookFloor0.lpclook.init(lookFloor0.ln, lookFloor0.m);
        final float n = lookFloor0.ln / toBARK((float)(vi2.rate / 2.0));
        lookFloor0.linearmap = new int[lookFloor0.n];
        for (int i = 0; i < lookFloor0.n; ++i) {
            int ln = (int)Math.floor(toBARK((float)(vi2.rate / 2.0 / lookFloor0.n * i)) * n);
            if (ln >= lookFloor0.ln) {
                ln = lookFloor0.ln;
            }
            lookFloor0.linearmap[i] = ln;
        }
        return lookFloor0;
    }
    
    static float toBARK(final float n) {
        return (float)(13.1 * Math.atan(7.4E-4 * n) + 2.24 * Math.atan(n * n * 1.85E-8) + 1.0E-4 * n);
    }
    
    Object state(final Object o) {
        final EchstateFloor0 echstateFloor0 = new EchstateFloor0();
        final InfoFloor0 infoFloor0 = (InfoFloor0)o;
        echstateFloor0.codewords = new int[infoFloor0.order];
        echstateFloor0.curve = new float[infoFloor0.barkmap];
        echstateFloor0.frameno = -1L;
        return echstateFloor0;
    }
    
    void free_info(final Object o) {
    }
    
    void free_look(final Object o) {
    }
    
    void free_state(final Object o) {
    }
    
    int forward(final Block block, final Object o, final float[] array, final float[] array2, final Object o2) {
        return 0;
    }
    
    int inverse(final Block block, final Object o, final float[] array) {
        final LookFloor0 lookFloor0 = (LookFloor0)o;
        final InfoFloor0 vi = lookFloor0.vi;
        final int read = block.opb.read(vi.ampbits);
        if (read > 0) {
            final float n = read / (float)((1 << vi.ampbits) - 1) * vi.ampdB;
            final int read2 = block.opb.read(ilog(vi.numbooks));
            if (read2 != -1 && read2 < vi.numbooks) {
                synchronized (this) {
                    if (this.lsp == null || this.lsp.length < lookFloor0.m) {
                        this.lsp = new float[lookFloor0.m];
                    }
                    else {
                        for (int i = 0; i < lookFloor0.m; ++i) {
                            this.lsp[i] = 0.0f;
                        }
                    }
                    final CodeBook codeBook = block.vd.fullbooks[vi.books[read2]];
                    float n2 = 0.0f;
                    for (int j = 0; j < lookFloor0.m; ++j) {
                        array[j] = 0.0f;
                    }
                    for (int k = 0; k < lookFloor0.m; k += codeBook.dim) {
                        if (codeBook.decodevs(this.lsp, k, block.opb, 1, -1) == -1) {
                            for (int l = 0; l < lookFloor0.n; ++l) {
                                array[l] = 0.0f;
                            }
                            return 0;
                        }
                    }
                    int n3 = 0;
                    while (n3 < lookFloor0.m) {
                        for (int n4 = 0; n4 < codeBook.dim; ++n4, ++n3) {
                            final float[] lsp = this.lsp;
                            final int n5 = n3;
                            lsp[n5] += n2;
                        }
                        n2 = this.lsp[n3 - 1];
                    }
                    Lsp.lsp_to_curve(array, lookFloor0.linearmap, lookFloor0.n, lookFloor0.ln, this.lsp, lookFloor0.m, n, (float)vi.ampdB);
                    return 1;
                }
            }
        }
        return 0;
    }
    
    Object inverse1(final Block block, final Object o, final Object o2) {
        final LookFloor0 lookFloor0 = (LookFloor0)o;
        final InfoFloor0 vi = lookFloor0.vi;
        float[] array = null;
        if (o2 instanceof float[]) {
            array = (float[])o2;
        }
        final int read = block.opb.read(vi.ampbits);
        if (read > 0) {
            final float n = read / (float)((1 << vi.ampbits) - 1) * vi.ampdB;
            final int read2 = block.opb.read(ilog(vi.numbooks));
            if (read2 != -1 && read2 < vi.numbooks) {
                final CodeBook codeBook = block.vd.fullbooks[vi.books[read2]];
                float n2 = 0.0f;
                if (array == null || array.length < lookFloor0.m + 1) {
                    array = new float[lookFloor0.m + 1];
                }
                else {
                    for (int i = 0; i < array.length; ++i) {
                        array[i] = 0.0f;
                    }
                }
                for (int j = 0; j < lookFloor0.m; j += codeBook.dim) {
                    if (codeBook.decodev_set(array, j, block.opb, codeBook.dim) == -1) {
                        return null;
                    }
                }
                int k = 0;
                while (k < lookFloor0.m) {
                    for (int l = 0; l < codeBook.dim; ++l, ++k) {
                        final float[] array2 = array;
                        final int n3 = k;
                        array2[n3] += n2;
                    }
                    n2 = array[k - 1];
                }
                array[lookFloor0.m] = n;
                return array;
            }
        }
        return null;
    }
    
    int inverse2(final Block block, final Object o, final Object o2, final float[] array) {
        final LookFloor0 lookFloor0 = (LookFloor0)o;
        final InfoFloor0 vi = lookFloor0.vi;
        if (o2 != null) {
            final float[] array2 = (float[])o2;
            Lsp.lsp_to_curve(array, lookFloor0.linearmap, lookFloor0.n, lookFloor0.ln, array2, lookFloor0.m, array2[lookFloor0.m], (float)vi.ampdB);
            return 1;
        }
        for (int i = 0; i < lookFloor0.n; ++i) {
            array[i] = 0.0f;
        }
        return 0;
    }
    
    static float fromdB(final float n) {
        return (float)Math.exp(n * 0.11512925);
    }
    
    private static int ilog(int i) {
        int n = 0;
        while (i != 0) {
            ++n;
            i >>>= 1;
        }
        return n;
    }
    
    static void lsp_to_lpc(final float[] array, final float[] array2, final int n) {
        final int n2 = n / 2;
        final float[] array3 = new float[n2];
        final float[] array4 = new float[n2];
        final float[] array5 = new float[n2 + 1];
        final float[] array6 = new float[n2 + 1];
        final float[] array7 = new float[n2];
        final float[] array8 = new float[n2];
        for (int i = 0; i < n2; ++i) {
            array3[i] = (float)(-2.0 * Math.cos(array[i * 2]));
            array4[i] = (float)(-2.0 * Math.cos(array[i * 2 + 1]));
        }
        int j;
        for (j = 0; j < n2; ++j) {
            array5[j] = 0.0f;
            array6[j] = 1.0f;
            array7[j] = 0.0f;
            array8[j] = 1.0f;
        }
        array5[j] = (array6[j] = 1.0f);
        for (int k = 1; k < n + 1; ++k) {
            float n4;
            float n3 = n4 = 0.0f;
            int l;
            for (l = 0; l < n2; ++l) {
                final float n5 = array3[l] * array6[l] + array5[l];
                array5[l] = array6[l];
                array6[l] = n4;
                n4 += n5;
                final float n6 = array4[l] * array8[l] + array7[l];
                array7[l] = array8[l];
                array8[l] = n3;
                n3 += n6;
            }
            array2[k - 1] = (n4 + array6[l] + n3 - array5[l]) / 2.0f;
            array6[l] = n4;
            array5[l] = n3;
        }
    }
    
    static void lpc_to_curve(final float[] array, final float[] array2, final float n, final LookFloor0 lookFloor0, final String s, final int n2) {
        final float[] array3 = new float[Math.max(lookFloor0.ln * 2, lookFloor0.m * 2 + 2)];
        if (n == 0.0f) {
            for (int i = 0; i < lookFloor0.n; ++i) {
                array[i] = 0.0f;
            }
            return;
        }
        lookFloor0.lpclook.lpc_to_curve(array3, array2, n);
        for (int j = 0; j < lookFloor0.n; ++j) {
            array[j] = array3[lookFloor0.linearmap[j]];
        }
    }
}
