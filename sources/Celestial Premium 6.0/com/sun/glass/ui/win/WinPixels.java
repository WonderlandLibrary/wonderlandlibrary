/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.Pixels;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

final class WinPixels
extends Pixels {
    private static final int nativeFormat = WinPixels._initIDs();

    private static native int _initIDs();

    protected WinPixels(int n, int n2, ByteBuffer byteBuffer) {
        super(n, n2, byteBuffer);
    }

    protected WinPixels(int n, int n2, IntBuffer intBuffer) {
        super(n, n2, intBuffer);
    }

    protected WinPixels(int n, int n2, IntBuffer intBuffer, float f, float f2) {
        super(n, n2, intBuffer, f, f2);
    }

    static int getNativeFormat_impl() {
        return nativeFormat;
    }

    @Override
    protected native void _fillDirectByteBuffer(ByteBuffer var1);

    @Override
    protected native void _attachInt(long var1, int var3, int var4, IntBuffer var5, int[] var6, int var7);

    @Override
    protected native void _attachByte(long var1, int var3, int var4, ByteBuffer var5, byte[] var6, int var7);
}

