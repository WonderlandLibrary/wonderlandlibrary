/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.socks.SocksAddressType;
import io.netty.handler.codec.socks.SocksCmdStatus;
import io.netty.handler.codec.socks.SocksResponse;
import io.netty.handler.codec.socks.SocksResponseType;

public final class SocksCmdResponse
extends SocksResponse {
    private final SocksCmdStatus cmdStatus;
    private final SocksAddressType addressType;
    private static final byte[] IPv4_HOSTNAME_ZEROED = new byte[]{0, 0, 0, 0};
    private static final byte[] IPv6_HOSTNAME_ZEROED = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public SocksCmdResponse(SocksCmdStatus cmdStatus, SocksAddressType addressType) {
        super(SocksResponseType.CMD);
        if (cmdStatus == null) {
            throw new NullPointerException("cmdStatus");
        }
        if (addressType == null) {
            throw new NullPointerException("addressType");
        }
        this.cmdStatus = cmdStatus;
        this.addressType = addressType;
    }

    public SocksCmdStatus cmdStatus() {
        return this.cmdStatus;
    }

    public SocksAddressType addressType() {
        return this.addressType;
    }

    @Override
    public void encodeAsByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(this.protocolVersion().byteValue());
        byteBuf.writeByte(this.cmdStatus.byteValue());
        byteBuf.writeByte(0);
        byteBuf.writeByte(this.addressType.byteValue());
        switch (this.addressType) {
            case IPv4: {
                byteBuf.writeBytes(IPv4_HOSTNAME_ZEROED);
                byteBuf.writeShort(0);
                break;
            }
            case DOMAIN: {
                byteBuf.writeByte(1);
                byteBuf.writeByte(0);
                byteBuf.writeShort(0);
                break;
            }
            case IPv6: {
                byteBuf.writeBytes(IPv6_HOSTNAME_ZEROED);
                byteBuf.writeShort(0);
            }
        }
    }
}

