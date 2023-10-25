/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.IntPixelAccessor;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToIntPixelConverter;
import java.nio.IntBuffer;

public abstract class BaseIntToIntConverter
implements IntToIntPixelConverter {
    protected final IntPixelGetter getter;
    protected final IntPixelSetter setter;

    public BaseIntToIntConverter(IntPixelGetter intPixelGetter, IntPixelSetter intPixelSetter) {
        this.getter = intPixelGetter;
        this.setter = intPixelSetter;
    }

    public final IntPixelGetter getGetter() {
        return this.getter;
    }

    public final IntPixelSetter getSetter() {
        return this.setter;
    }

    abstract void doConvert(int[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8);

    abstract void doConvert(IntBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8);

    @Override
    public final void convert(int[] arrn, int n, int n2, int[] arrn2, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (n2 == n5 && n4 == n5) {
            n5 *= n6;
            n6 = 1;
        }
        this.doConvert(arrn, n, n2, arrn2, n3, n4, n5, n6);
    }

    @Override
    public final void convert(IntBuffer intBuffer, int n, int n2, IntBuffer intBuffer2, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (n2 == n5 && n4 == n5) {
            n5 *= n6;
            n6 = 1;
        }
        if (intBuffer.hasArray() && intBuffer2.hasArray()) {
            this.doConvert(intBuffer.array(), n += intBuffer.arrayOffset(), n2, intBuffer2.array(), n3 += intBuffer2.arrayOffset(), n4, n5, n6);
        } else {
            this.doConvert(intBuffer, n, n2, intBuffer2, n3, n4, n5, n6);
        }
    }

    @Override
    public final void convert(IntBuffer intBuffer, int n, int n2, int[] arrn, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (n2 == n5 && n4 == n5) {
            n5 *= n6;
            n6 = 1;
        }
        if (intBuffer.hasArray()) {
            int[] arrn2 = intBuffer.array();
            this.doConvert(arrn2, n += intBuffer.arrayOffset(), n2, arrn, n3, n4, n5, n6);
        } else {
            IntBuffer intBuffer2 = IntBuffer.wrap(arrn);
            this.doConvert(intBuffer, n, n2, intBuffer2, n3, n4, n5, n6);
        }
    }

    @Override
    public final void convert(int[] arrn, int n, int n2, IntBuffer intBuffer, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (n2 == n5 && n4 == n5) {
            n5 *= n6;
            n6 = 1;
        }
        if (intBuffer.hasArray()) {
            int[] arrn2 = intBuffer.array();
            this.doConvert(arrn, n, n2, arrn2, n3 += intBuffer.arrayOffset(), n4, n5, n6);
        } else {
            IntBuffer intBuffer2 = IntBuffer.wrap(arrn);
            this.doConvert(intBuffer2, n, n2, intBuffer, n3, n4, n5, n6);
        }
    }

    static IntToIntPixelConverter create(IntPixelAccessor intPixelAccessor) {
        return new IntAnyToSameConverter(intPixelAccessor);
    }

    static class IntAnyToSameConverter
    extends BaseIntToIntConverter {
        IntAnyToSameConverter(IntPixelAccessor intPixelAccessor) {
            super(intPixelAccessor, intPixelAccessor);
        }

        @Override
        void doConvert(int[] arrn, int n, int n2, int[] arrn2, int n3, int n4, int n5, int n6) {
            while (--n6 >= 0) {
                System.arraycopy(arrn, n, arrn2, n3, n5);
                n += n2;
                n3 += n4;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        void doConvert(IntBuffer intBuffer, int n, int n2, IntBuffer intBuffer2, int n3, int n4, int n5, int n6) {
            int n7 = intBuffer.limit();
            int n8 = intBuffer.position();
            int n9 = intBuffer2.position();
            try {
                while (--n6 >= 0) {
                    int n10 = n + n5;
                    if (n10 > n7) {
                        throw new IndexOutOfBoundsException("" + n7);
                    }
                    intBuffer.limit(n10);
                    intBuffer.position(n);
                    intBuffer2.position(n3);
                    intBuffer2.put(intBuffer);
                    n += n2;
                    n3 += n4;
                }
            }
            finally {
                intBuffer.limit(n7);
                intBuffer.position(n8);
                intBuffer2.position(n9);
            }
        }
    }
}

