/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public final class HttpServerCodec
extends CombinedChannelDuplexHandler<HttpRequestDecoder, HttpResponseEncoder> {
    public HttpServerCodec(int n, int n2, int n3, boolean bl) {
        super(new HttpRequestDecoder(n, n2, n3, bl), new HttpResponseEncoder());
    }

    public HttpServerCodec() {
        this(4096, 8192, 8192);
    }

    public HttpServerCodec(int n, int n2, int n3) {
        super(new HttpRequestDecoder(n, n2, n3), new HttpResponseEncoder());
    }
}

