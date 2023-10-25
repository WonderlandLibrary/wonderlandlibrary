/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public abstract class BaseByteToIntConverter
implements ByteToIntPixelConverter {
    protected final BytePixelGetter getter;
    protected final IntPixelSetter setter;
    protected final int nSrcElems;

    BaseByteToIntConverter(BytePixelGetter bytePixelGetter, IntPixelSetter intPixelSetter) {
        this.getter = bytePixelGetter;
        this.setter = intPixelSetter;
        this.nSrcElems = bytePixelGetter.getNumElements();
    }

    public final BytePixelGetter getGetter() {
        return this.getter;
    }

    public final IntPixelSetter getSetter() {
        return this.setter;
    }

    abstract void doConvert(byte[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8);

    abstract void doConvert(ByteBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8);

    @Override
    public final void convert(byte[] arrby, int n, int n2, int[] arrn, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (n2 == n5 * this.nSrcElems && n4 == n5) {
            n5 *= n6;
            n6 = 1;
        }
        this.doConvert(arrby, n, n2, arrn, n3, n4, n5, n6);
    }

    @Override
    public final void convert(ByteBuffer byteBuffer, int n, int n2, IntBuffer intBuffer, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (n2 == n5 * this.nSrcElems && n4 == n5) {
            n5 *= n6;
            n6 = 1;
        }
        if (byteBuffer.hasArray() && intBuffer.hasArray()) {
            this.doConvert(byteBuffer.array(), n += byteBuffer.arrayOffset(), n2, intBuffer.array(), n3 += intBuffer.arrayOffset(), n4, n5, n6);
        } else {
            this.doConvert(byteBuffer, n, n2, intBuffer, n3, n4, n5, n6);
        }
    }

    @Override
    public final void convert(ByteBuffer byteBuffer, int n, int n2, int[] arrn, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (n2 == n5 * this.nSrcElems && n4 == n5) {
            n5 *= n6;
            n6 = 1;
        }
        if (byteBuffer.hasArray()) {
            byte[] arrby = byteBuffer.array();
            this.doConvert(arrby, n += byteBuffer.arrayOffset(), n2, arrn, n3, n4, n5, n6);
        } else {
            IntBuffer intBuffer = IntBuffer.wrap(arrn);
            this.doConvert(byteBuffer, n, n2, intBuffer, n3, n4, n5, n6);
        }
    }

    @Override
    public final void convert(byte[] arrby, int n, int n2, IntBuffer intBuffer, int n3, int n4, int n5, int n6) {
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (n2 == n5 * this.nSrcElems && n4 == n5) {
            n5 *= n6;
            n6 = 1;
        }
        if (intBuffer.hasArray()) {
            int[] arrn = intBuffer.array();
            this.doConvert(arrby, n, n2, arrn, n3 += intBuffer.arrayOffset(), n4, n5, n6);
        } else {
            ByteBuffer byteBuffer = ByteBuffer.wrap(arrby);
            this.doConvert(byteBuffer, n, n2, intBuffer, n3, n4, n5, n6);
        }
    }
}

