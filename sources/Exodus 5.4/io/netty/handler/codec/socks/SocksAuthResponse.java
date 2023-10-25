/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.socks.SocksAuthStatus;
import io.netty.handler.codec.socks.SocksResponse;
import io.netty.handler.codec.socks.SocksResponseType;
import io.netty.handler.codec.socks.SocksSubnegotiationVersion;

public final class SocksAuthResponse
extends SocksResponse {
    private final SocksAuthStatus authStatus;
    private static final SocksSubnegotiationVersion SUBNEGOTIATION_VERSION = SocksSubnegotiationVersion.AUTH_PASSWORD;

    @Override
    public void encodeAsByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(SUBNEGOTIATION_VERSION.byteValue());
        byteBuf.writeByte(this.authStatus.byteValue());
    }

    public SocksAuthStatus authStatus() {
        return this.authStatus;
    }

    public SocksAuthResponse(SocksAuthStatus socksAuthStatus) {
        super(SocksResponseType.AUTH);
        if (socksAuthStatus == null) {
            throw new NullPointerException("authStatus");
        }
        this.authStatus = socksAuthStatus;
    }
}

