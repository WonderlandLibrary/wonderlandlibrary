/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.security.AccessController;

public class BufferUtil {
    public static final int SIZEOF_BYTE = 1;
    public static final int SIZEOF_SHORT = 2;
    public static final int SIZEOF_INT = 4;
    public static final int SIZEOF_FLOAT = 4;
    public static final int SIZEOF_DOUBLE = 8;
    private static boolean isCDCFP;
    private static Class byteOrderClass;
    private static Object nativeOrderObject;
    private static Method orderMethod;

    private BufferUtil() {
    }

    public static void nativeOrder(ByteBuffer byteBuffer) {
        if (!isCDCFP) {
            try {
                if (byteOrderClass == null) {
                    byteOrderClass = (Class)AccessController.doPrivileged(() -> Class.forName("java.nio.ByteOrder", true, null));
                    orderMethod = ByteBuffer.class.getMethod("order", byteOrderClass);
                    Method method = byteOrderClass.getMethod("nativeOrder", null);
                    nativeOrderObject = method.invoke(null, null);
                }
            }
            catch (Throwable throwable) {
                isCDCFP = true;
            }
            if (!isCDCFP) {
                try {
                    orderMethod.invoke(byteBuffer, nativeOrderObject);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
        }
    }

    public static ByteBuffer newByteBuffer(int n) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(n);
        BufferUtil.nativeOrder(byteBuffer);
        return byteBuffer;
    }

    public static DoubleBuffer newDoubleBuffer(int n) {
        ByteBuffer byteBuffer = BufferUtil.newByteBuffer(n * 8);
        return byteBuffer.asDoubleBuffer();
    }

    public static FloatBuffer newFloatBuffer(int n) {
        ByteBuffer byteBuffer = BufferUtil.newByteBuffer(n * 4);
        return byteBuffer.asFloatBuffer();
    }

    public static IntBuffer newIntBuffer(int n) {
        ByteBuffer byteBuffer = BufferUtil.newByteBuffer(n * 4);
        return byteBuffer.asIntBuffer();
    }

    public static ShortBuffer newShortBuffer(int n) {
        ByteBuffer byteBuffer = BufferUtil.newByteBuffer(n * 2);
        return byteBuffer.asShortBuffer();
    }
}

