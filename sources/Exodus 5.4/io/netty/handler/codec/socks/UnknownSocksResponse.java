/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.socks.SocksResponse;
import io.netty.handler.codec.socks.SocksResponseType;

public final class UnknownSocksResponse
extends SocksResponse {
    @Override
    public void encodeAsByteBuf(ByteBuf byteBuf) {
    }

    public UnknownSocksResponse() {
        super(SocksResponseType.UNKNOWN);
    }
}

