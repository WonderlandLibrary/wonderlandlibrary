/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.FloatMath;
import com.sun.marlin.IntArrayCache;
import com.sun.marlin.MarlinAlphaConsumer;
import com.sun.marlin.MarlinConst;
import com.sun.marlin.OffHeapArray;
import com.sun.prism.impl.shape.MaskData;
import java.nio.ByteBuffer;
import java.util.Arrays;
import sun.misc.Unsafe;

public final class MaskMarlinAlphaConsumer
implements MarlinAlphaConsumer {
    int x;
    int y;
    int width;
    int height;
    final byte[] alphas;
    final ByteBuffer alphabuffer;
    final MaskData maskdata = new MaskData();
    boolean useFastFill;
    int fastFillThreshold;
    OffHeapArray ALPHA_MAP_USED = null;
    static final byte[] ALPHA_MAP;
    static final OffHeapArray ALPHA_MAP_UNSAFE;
    static final byte[] ALPHA_MAP_NO_AA;
    static final OffHeapArray ALPHA_MAP_UNSAFE_NO_AA;

    public MaskMarlinAlphaConsumer(int n) {
        this.alphas = new byte[n];
        this.alphabuffer = ByteBuffer.wrap(this.alphas);
    }

    public void setBoundsNoClone(int n, int n2, int n3, int n4) {
        this.x = n;
        this.y = n2;
        this.width = n3;
        this.height = n4;
        this.maskdata.update(this.alphabuffer, n, n2, n3, n4);
        boolean bl = this.useFastFill = n3 >= 32;
        if (this.useFastFill) {
            this.fastFillThreshold = n3 >= 128 ? n3 >> 1 : n3 >> 2;
        }
    }

    @Override
    public int getOriginX() {
        return this.x;
    }

    @Override
    public int getOriginY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public int getAlphaLength() {
        return this.alphas.length;
    }

    public MaskData getMaskData() {
        return this.maskdata;
    }

    @Override
    public void setMaxAlpha(int n) {
        this.ALPHA_MAP_USED = n == 1 ? ALPHA_MAP_UNSAFE_NO_AA : ALPHA_MAP_UNSAFE;
    }

    private static byte[] buildAlphaMap(int n) {
        byte[] arrby = new byte[n << 1];
        int n2 = n >> 2;
        for (int i = 0; i <= n; ++i) {
            arrby[i] = (byte)((i * 255 + n2) / n);
        }
        return arrby;
    }

    @Override
    public boolean supportBlockFlags() {
        return true;
    }

    @Override
    public void clearAlphas(int n) {
        int n2 = this.width;
        int n3 = (n - this.y) * n2;
        Arrays.fill(this.alphas, n3, n3 + n2, (byte)0);
    }

    @Override
    public void setAndClearRelativeAlphas(int[] arrn, int n, int n2, int n3) {
        boolean bl;
        byte[] arrby = this.alphas;
        int n4 = this.width;
        int n5 = (n - this.y) * n4;
        Unsafe unsafe = OffHeapArray.UNSAFE;
        long l = this.ALPHA_MAP_USED.address;
        int n6 = n2 - this.x;
        int n7 = n3 - this.x;
        int n8 = Math.min(n7, this.width);
        boolean bl2 = bl = this.useFastFill && n8 - n6 < this.fastFillThreshold;
        if (bl) {
            Arrays.fill(arrby, n5, n5 + n4, (byte)0);
            int n9 = 0;
            for (int i = n6; i < n8; ++i) {
                arrby[n5 + i] = unsafe.getByte(l + (long)(n9 += arrn[i]));
            }
        } else {
            int n10;
            for (n10 = 0; n10 < n6; ++n10) {
                arrby[n5 + n10] = 0;
            }
            int n11 = 0;
            while (n10 < n8) {
                arrby[n5 + n10] = unsafe.getByte(l + (long)(n11 += arrn[n10]));
                ++n10;
            }
            while (n10 < n4) {
                arrby[n5 + n10] = 0;
                ++n10;
            }
        }
        IntArrayCache.fill(arrn, n6, n7 + 1, 0);
    }

    @Override
    public void setAndClearRelativeAlphas(int[] arrn, int[] arrn2, int n, int n2, int n3) {
        byte[] arrby = this.alphas;
        int n4 = this.width;
        int n5 = (n - this.y) * n4;
        Unsafe unsafe = OffHeapArray.UNSAFE;
        long l = this.ALPHA_MAP_USED.address;
        int n6 = n2 - this.x;
        int n7 = n3 - this.x;
        int n8 = Math.min(n7, this.width);
        boolean bl = this.useFastFill && n8 - n6 < this.fastFillThreshold;
        int n9 = MarlinConst.BLOCK_SIZE_LG;
        int n10 = n6 >> n9;
        int n11 = (n8 >> n9) + 1;
        arrn[n11] = 0;
        int n12 = 0;
        int n13 = Integer.MAX_VALUE;
        if (bl) {
            byte by;
            int n14 = n6;
            Arrays.fill(arrby, n5, n5 + n4, (byte)0);
            for (int i = n10; i <= n11; ++i) {
                if (arrn[i] != 0) {
                    arrn[i] = 0;
                    if (n13 != Integer.MAX_VALUE) continue;
                    n13 = i;
                    continue;
                }
                if (n13 == Integer.MAX_VALUE) continue;
                int n15 = FloatMath.max(n13 << n9, n6);
                n13 = Integer.MAX_VALUE;
                int n16 = FloatMath.min((i << n9) + 1, n8);
                for (int j = n15; j < n16; ++j) {
                    int n17 = arrn2[j];
                    if (n17 == 0) continue;
                    arrn2[j] = 0;
                    if (j != n14) {
                        if (n12 == 0) {
                            n14 = j;
                        } else {
                            by = unsafe.getByte(l + (long)n12);
                            do {
                                arrby[n5 + n14] = by;
                            } while (++n14 < j);
                        }
                    }
                    n12 += n17;
                }
            }
            if (n12 != 0) {
                by = unsafe.getByte(l + (long)n12);
                while (n14 < n8) {
                    arrby[n5 + n14] = by;
                    ++n14;
                }
            }
        } else {
            byte by;
            int n18;
            for (n18 = 0; n18 < n6; ++n18) {
                arrby[n5 + n18] = 0;
            }
            for (int i = n10; i <= n11; ++i) {
                if (arrn[i] != 0) {
                    arrn[i] = 0;
                    if (n13 != Integer.MAX_VALUE) continue;
                    n13 = i;
                    continue;
                }
                if (n13 == Integer.MAX_VALUE) continue;
                int n19 = FloatMath.max(n13 << n9, n6);
                n13 = Integer.MAX_VALUE;
                int n20 = FloatMath.min((i << n9) + 1, n8);
                for (int j = n19; j < n20; ++j) {
                    int n21 = arrn2[j];
                    if (n21 == 0) continue;
                    arrn2[j] = 0;
                    if (j != n18) {
                        by = unsafe.getByte(l + (long)n12);
                        do {
                            arrby[n5 + n18] = by;
                        } while (++n18 < j);
                    }
                    n12 += n21;
                }
            }
            if (n12 != 0) {
                by = unsafe.getByte(l + (long)n12);
                while (n18 < n8) {
                    arrby[n5 + n18] = by;
                    ++n18;
                }
            }
            while (n18 < n4) {
                arrby[n5 + n18] = 0;
                ++n18;
            }
        }
        arrn2[n8] = 0;
        if (MarlinConst.DO_CHECKS) {
            IntArrayCache.check(arrn, n10, n11, 0);
            IntArrayCache.check(arrn2, n6, n7 + 1, 0);
        }
    }

    static {
        Unsafe unsafe = OffHeapArray.UNSAFE;
        byte[] arrby = MaskMarlinAlphaConsumer.buildAlphaMap(MarlinConst.MAX_AA_ALPHA);
        ALPHA_MAP = arrby;
        ALPHA_MAP_UNSAFE = new OffHeapArray(ALPHA_MAP, ALPHA_MAP.length);
        long l = MaskMarlinAlphaConsumer.ALPHA_MAP_UNSAFE.address;
        for (int i = 0; i < arrby.length; ++i) {
            unsafe.putByte(l + (long)i, arrby[i]);
        }
        byte[] arrby2 = MaskMarlinAlphaConsumer.buildAlphaMap(1);
        ALPHA_MAP_NO_AA = arrby2;
        ALPHA_MAP_UNSAFE_NO_AA = new OffHeapArray(ALPHA_MAP_NO_AA, ALPHA_MAP_NO_AA.length);
        l = MaskMarlinAlphaConsumer.ALPHA_MAP_UNSAFE_NO_AA.address;
        for (int i = 0; i < arrby2.length; ++i) {
            unsafe.putByte(l + (long)i, arrby2[i]);
        }
    }
}

