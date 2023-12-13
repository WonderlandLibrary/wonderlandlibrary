/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.jcraft.jzlib.Deflater
 *  com.jcraft.jzlib.Inflater
 *  com.jcraft.jzlib.JZlib
 *  com.jcraft.jzlib.JZlib$WrapperType
 */
package io.netty.handler.codec.compression;

import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;
import io.netty.handler.codec.compression.CompressionException;
import io.netty.handler.codec.compression.DecompressionException;
import io.netty.handler.codec.compression.ZlibWrapper;

final class ZlibUtil {
    static void fail(Inflater z2, String message, int resultCode) {
        throw ZlibUtil.inflaterException(z2, message, resultCode);
    }

    static void fail(Deflater z2, String message, int resultCode) {
        throw ZlibUtil.deflaterException(z2, message, resultCode);
    }

    static DecompressionException inflaterException(Inflater z2, String message, int resultCode) {
        return new DecompressionException(message + " (" + resultCode + ')' + (z2.msg != null ? ": " + z2.msg : ""));
    }

    static CompressionException deflaterException(Deflater z2, String message, int resultCode) {
        return new CompressionException(message + " (" + resultCode + ')' + (z2.msg != null ? ": " + z2.msg : ""));
    }

    static JZlib.WrapperType convertWrapperType(ZlibWrapper wrapper) {
        JZlib.WrapperType convertedWrapperType;
        switch (wrapper) {
            case NONE: {
                convertedWrapperType = JZlib.W_NONE;
                break;
            }
            case ZLIB: {
                convertedWrapperType = JZlib.W_ZLIB;
                break;
            }
            case GZIP: {
                convertedWrapperType = JZlib.W_GZIP;
                break;
            }
            case ZLIB_OR_NONE: {
                convertedWrapperType = JZlib.W_ANY;
                break;
            }
            default: {
                throw new Error();
            }
        }
        return convertedWrapperType;
    }

    static int wrapperOverhead(ZlibWrapper wrapper) {
        int overhead;
        switch (wrapper) {
            case NONE: {
                overhead = 0;
                break;
            }
            case ZLIB: 
            case ZLIB_OR_NONE: {
                overhead = 2;
                break;
            }
            case GZIP: {
                overhead = 10;
                break;
            }
            default: {
                throw new Error();
            }
        }
        return overhead;
    }

    private ZlibUtil() {
    }
}

