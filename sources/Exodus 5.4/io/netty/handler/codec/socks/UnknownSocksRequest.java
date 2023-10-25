/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.socks.SocksRequest;
import io.netty.handler.codec.socks.SocksRequestType;

public final class UnknownSocksRequest
extends SocksRequest {
    @Override
    public void encodeAsByteBuf(ByteBuf byteBuf) {
    }

    public UnknownSocksRequest() {
        super(SocksRequestType.UNKNOWN);
    }
}

