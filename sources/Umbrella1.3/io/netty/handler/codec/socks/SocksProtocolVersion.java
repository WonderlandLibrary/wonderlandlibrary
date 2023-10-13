/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.socks;

public enum SocksProtocolVersion {
    SOCKS4a(4),
    SOCKS5(5),
    UNKNOWN(-1);

    private final byte b;

    private SocksProtocolVersion(byte b) {
        this.b = b;
    }

    public static SocksProtocolVersion fromByte(byte b) {
        for (SocksProtocolVersion code : SocksProtocolVersion.values()) {
            if (code.b != b) continue;
            return code;
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return this.b;
    }
}

