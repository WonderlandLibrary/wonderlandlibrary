/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.socks.SocksAddressType;
import io.netty.handler.codec.socks.SocksCmdStatus;
import io.netty.handler.codec.socks.SocksResponse;
import io.netty.handler.codec.socks.SocksResponseType;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;
import java.net.IDN;

public final class SocksCmdResponse
extends SocksResponse {
    private static final byte[] DOMAIN_ZEROED = new byte[]{0};
    private final String host;
    private final int port;
    private final SocksCmdStatus cmdStatus;
    private final SocksAddressType addressType;
    private static final byte[] IPv6_HOSTNAME_ZEROED;
    private static final byte[] IPv4_HOSTNAME_ZEROED;

    @Override
    public void encodeAsByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(this.protocolVersion().byteValue());
        byteBuf.writeByte(this.cmdStatus.byteValue());
        byteBuf.writeByte(0);
        byteBuf.writeByte(this.addressType.byteValue());
        switch (this.addressType) {
            case IPv4: {
                byte[] byArray = this.host == null ? IPv4_HOSTNAME_ZEROED : NetUtil.createByteArrayFromIpAddressString(this.host);
                byteBuf.writeBytes(byArray);
                byteBuf.writeShort(this.port);
                break;
            }
            case DOMAIN: {
                byte[] byArray = this.host == null ? DOMAIN_ZEROED : this.host.getBytes(CharsetUtil.US_ASCII);
                byteBuf.writeByte(byArray.length);
                byteBuf.writeBytes(byArray);
                byteBuf.writeShort(this.port);
                break;
            }
            case IPv6: {
                byte[] byArray = this.host == null ? IPv6_HOSTNAME_ZEROED : NetUtil.createByteArrayFromIpAddressString(this.host);
                byteBuf.writeBytes(byArray);
                byteBuf.writeShort(this.port);
                break;
            }
        }
    }

    static {
        IPv4_HOSTNAME_ZEROED = new byte[]{0, 0, 0, 0};
        IPv6_HOSTNAME_ZEROED = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public String host() {
        if (this.host != null) {
            return IDN.toUnicode(this.host);
        }
        return null;
    }

    public SocksCmdStatus cmdStatus() {
        return this.cmdStatus;
    }

    public SocksCmdResponse(SocksCmdStatus socksCmdStatus, SocksAddressType socksAddressType) {
        this(socksCmdStatus, socksAddressType, null, 0);
    }

    public SocksCmdResponse(SocksCmdStatus socksCmdStatus, SocksAddressType socksAddressType, String string, int n) {
        super(SocksResponseType.CMD);
        if (socksCmdStatus == null) {
            throw new NullPointerException("cmdStatus");
        }
        if (socksAddressType == null) {
            throw new NullPointerException("addressType");
        }
        if (string != null) {
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
            string = IDN.toASCII(string);
        }
        if (n < 0 || n > 65535) {
            throw new IllegalArgumentException(n + " is not in bounds 0 <= x <= 65535");
        }
        this.cmdStatus = socksCmdStatus;
        this.addressType = socksAddressType;
        this.host = string;
        this.port = n;
    }

    public SocksAddressType addressType() {
        return this.addressType;
    }

    public int port() {
        return this.port;
    }
}

