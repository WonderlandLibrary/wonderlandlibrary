/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.rtsp;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.rtsp.RtspObjectEncoder;
import io.netty.util.CharsetUtil;

public class RtspRequestEncoder
extends RtspObjectEncoder<HttpRequest> {
    private static final byte[] CRLF = new byte[]{13, 10};

    @Override
    protected void encodeInitialLine(ByteBuf byteBuf, HttpRequest httpRequest) throws Exception {
        HttpHeaders.encodeAscii(httpRequest.getMethod().toString(), byteBuf);
        byteBuf.writeByte(32);
        byteBuf.writeBytes(httpRequest.getUri().getBytes(CharsetUtil.UTF_8));
        byteBuf.writeByte(32);
        RtspRequestEncoder.encodeAscii(httpRequest.getProtocolVersion().toString(), byteBuf);
        byteBuf.writeBytes(CRLF);
    }

    @Override
    public boolean acceptOutboundMessage(Object object) throws Exception {
        return object instanceof FullHttpRequest;
    }
}

