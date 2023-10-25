/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObjectDecoder;

public abstract class RtspObjectDecoder
extends HttpObjectDecoder {
    @Override
    protected boolean isContentAlwaysEmpty(HttpMessage httpMessage) {
        boolean bl = super.isContentAlwaysEmpty(httpMessage);
        if (bl) {
            return true;
        }
        if (!httpMessage.headers().contains("Content-Length")) {
            return true;
        }
        return bl;
    }

    protected RtspObjectDecoder() {
        this(4096, 8192, 8192);
    }

    protected RtspObjectDecoder(int n, int n2, int n3, boolean bl) {
        super(n, n2, n3 * 2, false, bl);
    }

    protected RtspObjectDecoder(int n, int n2, int n3) {
        super(n, n2, n3 * 2, false);
    }
}

