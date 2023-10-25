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
    static CompressionException deflaterException(Deflater deflater, String string, int n) {
        return new CompressionException(string + " (" + n + ')' + (deflater.msg != null ? ": " + deflater.msg : ""));
    }

    static void fail(Inflater inflater, String string, int n) {
        throw ZlibUtil.inflaterException(inflater, string, n);
    }

    static void fail(Deflater deflater, String string, int n) {
        throw ZlibUtil.deflaterException(deflater, string, n);
    }

    static int wrapperOverhead(ZlibWrapper zlibWrapper) {
        int n;
        switch (zlibWrapper) {
            case NONE: {
                n = 0;
                break;
            }
            case ZLIB: 
            case ZLIB_OR_NONE: {
                n = 2;
                break;
            }
            case GZIP: {
                n = 10;
                break;
            }
            default: {
                throw new Error();
            }
        }
        return n;
    }

    static DecompressionException inflaterException(Inflater inflater, String string, int n) {
        return new DecompressionException(string + " (" + n + ')' + (inflater.msg != null ? ": " + inflater.msg : ""));
    }

    private ZlibUtil() {
    }

    static JZlib.WrapperType convertWrapperType(ZlibWrapper zlibWrapper) {
        JZlib.WrapperType wrapperType;
        switch (zlibWrapper) {
            case NONE: {
                wrapperType = JZlib.W_NONE;
                break;
            }
            case ZLIB: {
                wrapperType = JZlib.W_ZLIB;
                break;
            }
            case GZIP: {
                wrapperType = JZlib.W_GZIP;
                break;
            }
            case ZLIB_OR_NONE: {
                wrapperType = JZlib.W_ANY;
                break;
            }
            default: {
                throw new Error();
            }
        }
        return wrapperType;
    }
}

