/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.impl.BaseIntToByteConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class IntTo4ByteSameConverter
extends BaseIntToByteConverter {
    IntTo4ByteSameConverter(IntPixelGetter intPixelGetter, BytePixelSetter bytePixelSetter) {
        super(intPixelGetter, bytePixelSetter);
    }

    @Override
    void doConvert(int[] arrn, int n, int n2, byte[] arrby, int n3, int n4, int n5, int n6) {
        n2 -= n5;
        n4 -= n5 * 4;
        while (--n6 >= 0) {
            for (int i = 0; i < n5; ++i) {
                int n7 = arrn[n++];
                arrby[n3++] = (byte)n7;
                arrby[n3++] = (byte)(n7 >> 8);
                arrby[n3++] = (byte)(n7 >> 16);
                arrby[n3++] = (byte)(n7 >> 24);
            }
            n += n2;
            n3 += n4;
        }
    }

    @Override
    void doConvert(IntBuffer intBuffer, int n, int n2, ByteBuffer byteBuffer, int n3, int n4, int n5, int n6) {
        n4 -= n5 * 4;
        while (--n6 >= 0) {
            for (int i = 0; i < n5; ++i) {
                int n7 = intBuffer.get(n + i);
                byteBuffer.put(n3, (byte)n7);
                byteBuffer.put(n3 + 1, (byte)(n7 >> 8));
                byteBuffer.put(n3 + 2, (byte)(n7 >> 16));
                byteBuffer.put(n3 + 3, (byte)(n7 >> 24));
                n3 += 4;
            }
            n += n2;
            n3 += n4;
        }
    }
}

