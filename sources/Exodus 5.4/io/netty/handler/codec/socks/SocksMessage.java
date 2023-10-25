/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.socks.SocksMessageType;
import io.netty.handler.codec.socks.SocksProtocolVersion;

public abstract class SocksMessage {
    private final SocksMessageType type;
    private final SocksProtocolVersion protocolVersion = SocksProtocolVersion.SOCKS5;

    public SocksMessageType type() {
        return this.type;
    }

    protected SocksMessage(SocksMessageType socksMessageType) {
        if (socksMessageType == null) {
            throw new NullPointerException("type");
        }
        this.type = socksMessageType;
    }

    public SocksProtocolVersion protocolVersion() {
        return this.protocolVersion;
    }

    @Deprecated
    public abstract void encodeAsByteBuf(ByteBuf var1);
}

