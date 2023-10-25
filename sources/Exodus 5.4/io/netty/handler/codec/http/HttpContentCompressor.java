/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.codec.http.HttpContentEncoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.internal.StringUtil;

public class HttpContentCompressor
extends HttpContentEncoder {
    private final int memLevel;
    private final int windowBits;
    private final int compressionLevel;

    public HttpContentCompressor(int n, int n2, int n3) {
        if (n < 0 || n > 9) {
            throw new IllegalArgumentException("compressionLevel: " + n + " (expected: 0-9)");
        }
        if (n2 < 9 || n2 > 15) {
            throw new IllegalArgumentException("windowBits: " + n2 + " (expected: 9-15)");
        }
        if (n3 < 1 || n3 > 9) {
            throw new IllegalArgumentException("memLevel: " + n3 + " (expected: 1-9)");
        }
        this.compressionLevel = n;
        this.windowBits = n2;
        this.memLevel = n3;
    }

    public HttpContentCompressor() {
        this(6);
    }

    protected ZlibWrapper determineWrapper(String string) {
        float f = -1.0f;
        float f2 = -1.0f;
        float f3 = -1.0f;
        for (String string2 : StringUtil.split(string, ',')) {
            float f4 = 1.0f;
            int n = string2.indexOf(61);
            if (n != -1) {
                try {
                    f4 = Float.valueOf(string2.substring(n + 1)).floatValue();
                }
                catch (NumberFormatException numberFormatException) {
                    f4 = 0.0f;
                }
            }
            if (string2.contains("*")) {
                f = f4;
                continue;
            }
            if (string2.contains("gzip") && f4 > f2) {
                f2 = f4;
                continue;
            }
            if (!string2.contains("deflate") || !(f4 > f3)) continue;
            f3 = f4;
        }
        if (f2 > 0.0f || f3 > 0.0f) {
            if (f2 >= f3) {
                return ZlibWrapper.GZIP;
            }
            return ZlibWrapper.ZLIB;
        }
        if (f > 0.0f) {
            if (f2 == -1.0f) {
                return ZlibWrapper.GZIP;
            }
            if (f3 == -1.0f) {
                return ZlibWrapper.ZLIB;
            }
        }
        return null;
    }

    @Override
    protected HttpContentEncoder.Result beginEncode(HttpResponse httpResponse, String string) throws Exception {
        String string2;
        String string3 = httpResponse.headers().get("Content-Encoding");
        if (string3 != null && !"identity".equalsIgnoreCase(string3)) {
            return null;
        }
        ZlibWrapper zlibWrapper = this.determineWrapper(string);
        if (zlibWrapper == null) {
            return null;
        }
        switch (zlibWrapper) {
            case GZIP: {
                string2 = "gzip";
                break;
            }
            case ZLIB: {
                string2 = "deflate";
                break;
            }
            default: {
                throw new Error();
            }
        }
        return new HttpContentEncoder.Result(string2, new EmbeddedChannel(ZlibCodecFactory.newZlibEncoder(zlibWrapper, this.compressionLevel, this.windowBits, this.memLevel)));
    }

    public HttpContentCompressor(int n) {
        this(n, 15, 8);
    }
}

