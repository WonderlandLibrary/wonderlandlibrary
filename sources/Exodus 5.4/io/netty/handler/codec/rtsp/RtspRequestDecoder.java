/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.rtsp.RtspMethods;
import io.netty.handler.codec.rtsp.RtspObjectDecoder;
import io.netty.handler.codec.rtsp.RtspVersions;

public class RtspRequestDecoder
extends RtspObjectDecoder {
    public RtspRequestDecoder(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    @Override
    protected HttpMessage createMessage(String[] stringArray) throws Exception {
        return new DefaultHttpRequest(RtspVersions.valueOf(stringArray[2]), RtspMethods.valueOf(stringArray[0]), stringArray[1], this.validateHeaders);
    }

    @Override
    protected HttpMessage createInvalidMessage() {
        return new DefaultHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.OPTIONS, "/bad-request", this.validateHeaders);
    }

    public RtspRequestDecoder(int n, int n2, int n3, boolean bl) {
        super(n, n2, n3, bl);
    }

    public RtspRequestDecoder() {
    }

    @Override
    protected boolean isDecodingRequest() {
        return true;
    }
}

