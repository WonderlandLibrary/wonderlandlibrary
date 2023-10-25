/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.socks.SocksAddressType;
import io.netty.handler.codec.socks.SocksCmdType;
import io.netty.handler.codec.socks.SocksRequest;
import io.netty.handler.codec.socks.SocksRequestType;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;
import java.net.IDN;

public final class SocksCmdRequest
extends SocksRequest {
    private final SocksAddressType addressType;
    private final int port;
    private final SocksCmdType cmdType;
    private final String host;

    public SocksAddressType addressType() {
        return this.addressType;
    }

    public String host() {
        return IDN.toUnicode(this.host);
    }

    public int port() {
        return this.port;
    }

    public SocksCmdRequest(SocksCmdType socksCmdType, SocksAddressType socksAddressType, String string, int n) {
        super(SocksRequestType.CMD);
        if (socksCmdType == null) {
            throw new NullPointerException("cmdType");
        }
        if (socksAddressType == null) {
            throw new NullPointerException("addressType");
        }
        if (string == null) {
            throw new NullPointerException("host");
        }
        switch (socksAddressType) {
            case IPv4: {
                if (NetUtil.isValidIpV4Address(string)) break;
                throw new IllegalArgumentException(string + " is not a valid IPv4 address");
            }
            case DOMAIN: {
                if (IDN.toASCII(string).length() <= 255) break;
                throw new IllegalArgumentException(string + " IDN: " + IDN.toASCII(string) + " exceeds 255 char limit");
            }
            case IPv6: {
                if (NetUtil.isValidIpV6Address(string)) break;
                throw new IllegalArgumentException(string + " is not a valid IPv6 address");
            }
        }
        if (n <= 0 || n >= 65536) {
            throw new IllegalArgumentException(n + " is not in bounds 0 < x < 65536");
        }
        this.cmdType = socksCmdType;
        this.addressType = socksAddressType;
        this.host = IDN.toASCII(string);
        this.port = n;
    }

    public SocksCmdType cmdType() {
        return this.cmdType;
    }

    @Override
    public void encodeAsByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(this.protocolVersion().byteValue());
        byteBuf.writeByte(this.cmdType.byteValue());
        byteBuf.writeByte(0);
        byteBuf.writeByte(this.addressType.byteValue());
        switch (this.addressType) {
            case IPv4: {
                byteBuf.writeBytes(NetUtil.createByteArrayFromIpAddressString(this.host));
                byteBuf.writeShort(this.port);
                break;
            }
            case DOMAIN: {
                byteBuf.writeByte(this.host.length());
                byteBuf.writeBytes(this.host.getBytes(CharsetUtil.US_ASCII));
                byteBuf.writeShort(this.port);
                break;
            }
            case IPv6: {
                byteBuf.writeBytes(NetUtil.createByteArrayFromIpAddressString(this.host));
                byteBuf.writeShort(this.port);
            }
        }
    }
}

