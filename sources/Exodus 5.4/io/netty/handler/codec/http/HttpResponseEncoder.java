/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpObjectEncoder;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class HttpResponseEncoder
extends HttpObjectEncoder<HttpResponse> {
    private static final byte[] CRLF = new byte[]{13, 10};

    @Override
    public boolean acceptOutboundMessage(Object object) throws Exception {
        return super.acceptOutboundMessage(object) && !(object instanceof HttpRequest);
    }

    @Override
    protected void encodeInitialLine(ByteBuf byteBuf, HttpResponse httpResponse) throws Exception {
        httpResponse.getProtocolVersion().encode(byteBuf);
        byteBuf.writeByte(32);
        httpResponse.getStatus().encode(byteBuf);
        byteBuf.writeBytes(CRLF);
    }
}

