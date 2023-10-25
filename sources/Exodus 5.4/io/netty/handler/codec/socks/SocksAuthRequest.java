/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.socks.SocksRequest;
import io.netty.handler.codec.socks.SocksRequestType;
import io.netty.handler.codec.socks.SocksSubnegotiationVersion;
import io.netty.util.CharsetUtil;
import java.nio.charset.CharsetEncoder;

public final class SocksAuthRequest
extends SocksRequest {
    private static final CharsetEncoder asciiEncoder = CharsetUtil.getEncoder(CharsetUtil.US_ASCII);
    private static final SocksSubnegotiationVersion SUBNEGOTIATION_VERSION = SocksSubnegotiationVersion.AUTH_PASSWORD;
    private final String password;
    private final String username;

    public String password() {
        return this.password;
    }

    public SocksAuthRequest(String string, String string2) {
        super(SocksRequestType.AUTH);
        if (string == null) {
            throw new NullPointerException("username");
        }
        if (string2 == null) {
            throw new NullPointerException("username");
        }
        if (!asciiEncoder.canEncode(string) || !asciiEncoder.canEncode(string2)) {
            throw new IllegalArgumentException(" username: " + string + " or password: " + string2 + " values should be in pure ascii");
        }
        if (string.length() > 255) {
            throw new IllegalArgumentException(string + " exceeds 255 char limit");
        }
        if (string2.length() > 255) {
            throw new IllegalArgumentException(string2 + " exceeds 255 char limit");
        }
        this.username = string;
        this.password = string2;
    }

    public String username() {
        return this.username;
    }

    @Override
    public void encodeAsByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(SUBNEGOTIATION_VERSION.byteValue());
        byteBuf.writeByte(this.username.length());
        byteBuf.writeBytes(this.username.getBytes(CharsetUtil.US_ASCII));
        byteBuf.writeByte(this.password.length());
        byteBuf.writeBytes(this.password.getBytes(CharsetUtil.US_ASCII));
    }
}

