/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.rtsp;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.rtsp.RtspObjectEncoder;
import io.netty.util.CharsetUtil;

public class RtspResponseEncoder
extends RtspObjectEncoder<HttpResponse> {
    private static final byte[] CRLF = new byte[]{13, 10};

    @Override
    public boolean acceptOutboundMessage(Object object) throws Exception {
        return object instanceof FullHttpResponse;
    }

    @Override
    protected void encodeInitialLine(ByteBuf byteBuf, HttpResponse httpResponse) throws Exception {
        HttpHeaders.encodeAscii(httpResponse.getProtocolVersion().toString(), byteBuf);
        byteBuf.writeByte(32);
        byteBuf.writeBytes(String.valueOf(httpResponse.getStatus().code()).getBytes(CharsetUtil.US_ASCII));
        byteBuf.writeByte(32);
        RtspResponseEncoder.encodeAscii(String.valueOf(httpResponse.getStatus().reasonPhrase()), byteBuf);
        byteBuf.writeBytes(CRLF);
    }
}

