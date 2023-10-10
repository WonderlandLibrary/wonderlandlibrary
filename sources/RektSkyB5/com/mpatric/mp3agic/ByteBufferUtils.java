/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import java.nio.ByteBuffer;

public class ByteBufferUtils {
    public static String extractNullTerminatedString(ByteBuffer byteBuffer) {
        int n2 = byteBuffer.position();
        byte[] byArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(byArray);
        String string = new String(byArray);
        int n3 = string.indexOf(0);
        string = string.substring(0, n3);
        byteBuffer.position(n2 + string.length() + 1);
        return string;
    }
}

