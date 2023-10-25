/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.rtsp.RtspObjectDecoder;
import io.netty.handler.codec.rtsp.RtspVersions;

public class RtspResponseDecoder
extends RtspObjectDecoder {
    private static final HttpResponseStatus UNKNOWN_STATUS = new HttpResponseStatus(999, "Unknown");

    @Override
    protected HttpMessage createMessage(String[] stringArray) throws Exception {
        return new DefaultHttpResponse(RtspVersions.valueOf(stringArray[0]), new HttpResponseStatus(Integer.parseInt(stringArray[1]), stringArray[2]), this.validateHeaders);
    }

    public RtspResponseDecoder(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    @Override
    protected HttpMessage createInvalidMessage() {
        return new DefaultHttpResponse(RtspVersions.RTSP_1_0, UNKNOWN_STATUS, this.validateHeaders);
    }

    public RtspResponseDecoder() {
    }

    @Override
    protected boolean isDecodingRequest() {
        return false;
    }

    public RtspResponseDecoder(int n, int n2, int n3, boolean bl) {
        super(n, n2, n3, bl);
    }
}

