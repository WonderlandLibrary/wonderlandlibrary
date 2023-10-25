/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.compression;

import io.netty.handler.codec.compression.JZlibDecoder;
import io.netty.handler.codec.compression.JZlibEncoder;
import io.netty.handler.codec.compression.JdkZlibDecoder;
import io.netty.handler.codec.compression.JdkZlibEncoder;
import io.netty.handler.codec.compression.ZlibDecoder;
import io.netty.handler.codec.compression.ZlibEncoder;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public final class ZlibCodecFactory {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ZlibCodecFactory.class);
    private static final boolean noJdkZlibDecoder = SystemPropertyUtil.getBoolean("io.netty.noJdkZlibDecoder", true);

    public static ZlibDecoder newZlibDecoder(ZlibWrapper zlibWrapper) {
        if (PlatformDependent.javaVersion() < 7 || noJdkZlibDecoder) {
            return new JZlibDecoder(zlibWrapper);
        }
        return new JdkZlibDecoder(zlibWrapper);
    }

    public static ZlibEncoder newZlibEncoder(int n) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(n);
        }
        return new JdkZlibEncoder(n);
    }

    public static ZlibDecoder newZlibDecoder() {
        if (PlatformDependent.javaVersion() < 7 || noJdkZlibDecoder) {
            return new JZlibDecoder();
        }
        return new JdkZlibDecoder();
    }

    private ZlibCodecFactory() {
    }

    public static ZlibEncoder newZlibEncoder(ZlibWrapper zlibWrapper) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(zlibWrapper);
        }
        return new JdkZlibEncoder(zlibWrapper);
    }

    public static ZlibEncoder newZlibEncoder(ZlibWrapper zlibWrapper, int n, int n2, int n3) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(zlibWrapper, n, n2, n3);
        }
        return new JdkZlibEncoder(zlibWrapper, n);
    }

    public static ZlibDecoder newZlibDecoder(byte[] byArray) {
        if (PlatformDependent.javaVersion() < 7 || noJdkZlibDecoder) {
            return new JZlibDecoder(byArray);
        }
        return new JdkZlibDecoder(byArray);
    }

    public static ZlibEncoder newZlibEncoder(int n, int n2, int n3, byte[] byArray) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(n, n2, n3, byArray);
        }
        return new JdkZlibEncoder(n, byArray);
    }

    public static ZlibEncoder newZlibEncoder(ZlibWrapper zlibWrapper, int n) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(zlibWrapper, n);
        }
        return new JdkZlibEncoder(zlibWrapper, n);
    }

    public static ZlibEncoder newZlibEncoder(int n, byte[] byArray) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(n, byArray);
        }
        return new JdkZlibEncoder(n, byArray);
    }

    public static ZlibEncoder newZlibEncoder(byte[] byArray) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(byArray);
        }
        return new JdkZlibEncoder(byArray);
    }

    static {
        logger.debug("-Dio.netty.noJdkZlibDecoder: {}", (Object)noJdkZlibDecoder);
    }
}

