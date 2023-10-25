/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import java.nio.ByteBuffer;

abstract class BaseByteToByteConverter
implements ByteToBytePixelConverter {
    protected final BytePixelGetter getter;
    protected final BytePixelSetter setter;
    protected final int nSrcElems;
    protected final int nDstElems;

    BaseByteToByteConverter(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter) {
        this.getter = bytePixelGetter;
        this.setter = bytePixelSetter;
        this.nSrcElems = bytePixelGetter.getNumElements();
        this.nDstElems = bytePixelSetter.getNumElements();
    }

    public final BytePixelGetter getGetter() {
        return this.getter;
    }

    public final BytePixelSetter getSetter() {
        return this.setter;
    }

    abstract void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8);

    abstract void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8);

    @Override
    public final void convert(byte[] arrby, int n, int n2, byte[] arrby2, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (n2 == n5 * this.nSrcElems && n4 == n5 * this.nDstElems) {
            n5 *= n6;
            n6 = 1;
        }
        this.doConvert(arrby, n, n2, arrby2, n3, n4, n5, n6);
    }

    @Override
    public final void convert(ByteBuffer byteBuffer, int n, int n2, ByteBuffer byteBuffer2, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (n2 == n5 * this.nSrcElems && n4 == n5 * this.nDstElems) {
            n5 *= n6;
            n6 = 1;
        }
        if (byteBuffer.hasArray() && byteBuffer2.hasArray()) {
            this.doConvert(byteBuffer.array(), n += byteBuffer.arrayOffset(), n2, byteBuffer2.array(), n3 += byteBuffer2.arrayOffset(), n4, n5, n6);
        } else {
            this.doConvert(byteBuffer, n, n2, byteBuffer2, n3, n4, n5, n6);
        }
    }

    @Override
    public final void convert(ByteBuffer byteBuffer, int n, int n2, byte[] arrby, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (n2 == n5 * this.nSrcElems && n4 == n5 * this.nDstElems) {
            n5 *= n6;
            n6 = 1;
        }
        if (byteBuffer.hasArray()) {
            byte[] arrby2 = byteBuffer.array();
            this.doConvert(arrby2, n += byteBuffer.arrayOffset(), n2, arrby, n3, n4, n5, n6);
        } else {
            ByteBuffer byteBuffer2 = ByteBuffer.wrap(arrby);
            this.doConvert(byteBuffer, n, n2, byteBuffer2, n3, n4, n5, n6);
        }
    }

    @Override
    public final void convert(byte[] arrby, int n, int n2, ByteBuffer byteBuffer, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (n2 == n5 * this.nSrcElems && n4 == n5 * this.nDstElems) {
            n5 *= n6;
            n6 = 1;
        }
        if (byteBuffer.hasArray()) {
            byte[] arrby2 = byteBuffer.array();
            this.doConvert(arrby, n, n2, arrby2, n3 += byteBuffer.arrayOffset(), n4, n5, n6);
        } else {
            ByteBuffer byteBuffer2 = ByteBuffer.wrap(arrby);
            this.doConvert(byteBuffer2, n, n2, byteBuffer, n3, n4, n5, n6);
        }
    }

    static ByteToBytePixelConverter create(BytePixelAccessor bytePixelAccessor) {
        return new ByteAnyToSameConverter(bytePixelAccessor);
    }

    public static ByteToBytePixelConverter createReorderer(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter, int n, int n2, int n3, int n4) {
        return new FourByteReorderer(bytePixelGetter, bytePixelSetter, n, n2, n3, n4);
    }

    static class ByteAnyToSameConverter
    extends BaseByteToByteConverter {
        ByteAnyToSameConverter(BytePixelAccessor bytePixelAccessor) {
            super(bytePixelAccessor, bytePixelAccessor);
        }

        @Override
        void doConvert(byte[] arrby, int n, int n2, byte[] arrby2, int n3, int n4, int n5, int n6) {
            while (--n6 >= 0) {
                System.arraycopy(arrby, n, arrby2, n3, n5 * this.nSrcElems);
                n += n2;
                n3 += n4;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        void doConvert(ByteBuffer byteBuffer, int n, int n2, ByteBuffer byteBuffer2, int n3, int n4, int n5, int n6) {
            int n7 = byteBuffer.limit();
            int n8 = byteBuffer.position();
            int n9 = byteBuffer2.position();
            try {
                while (--n6 >= 0) {
                    int n10 = n + n5 * this.nSrcElems;
                    if (n10 > n7) {
                        throw new IndexOutOfBoundsException("" + n7);
                    }
                    byteBuffer.limit(n10);
                    byteBuffer.position(n);
                    byteBuffer2.position(n3);
                    byteBuffer2.put(byteBuffer);
                    n += n2;
                    n3 += n4;
                }
            }
            finally {
                byteBuffer.limit(n7);
                byteBuffer.position(n8);
                byteBuffer2.position(n9);
            }
        }
    }

    static class FourByteReorderer
    extends BaseByteToByteConverter {
        private final int c0;
        private final int c1;
        private final int c2;
        private final int c3;

        FourByteReorderer(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter, int n, int n2, int n3, int n4) {
            super(bytePixelGetter, bytePixelSetter);
            this.c0 = n;
            this.c1 = n2;
            this.c2 = n3;
            this.c3 = n4;
        }

        @Override
        void doConvert(byte[] arrby, int n, int n2, byte[] arrby2, int n3, int n4, int n5, int n6) {
            n2 -= n5 * 4;
            n4 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    byte by = arrby[n + this.c0];
                    byte by2 = arrby[n + this.c1];
                    byte by3 = arrby[n + this.c2];
                    byte by4 = arrby[n + this.c3];
                    arrby2[n3++] = by;
                    arrby2[n3++] = by2;
                    arrby2[n3++] = by3;
                    arrby2[n3++] = by4;
                    n += 4;
                }
                n += n2;
                n3 += n4;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n, int n2, ByteBuffer byteBuffer2, int n3, int n4, int n5, int n6) {
            n2 -= n5 * 4;
            n4 -= n5 * 4;
            while (--n6 >= 0) {
                for (int i = 0; i < n5; ++i) {
                    byte by = byteBuffer.get(n + this.c0);
                    byte by2 = byteBuffer.get(n + this.c1);
                    byte by3 = byteBuffer.get(n + this.c2);
                    byte by4 = byteBuffer.get(n + this.c3);
                    byteBuffer2.put(n3, by);
                    byteBuffer2.put(n3 + 1, by2);
                    byteBuffer2.put(n3 + 2, by3);
                    byteBuffer2.put(n3 + 3, by4);
                    n += 4;
                    n3 += 4;
                }
                n += n2;
                n3 += n4;
            }
        }
    }
}

