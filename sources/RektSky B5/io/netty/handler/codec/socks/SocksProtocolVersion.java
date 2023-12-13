/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

public enum SocksProtocolVersion {
    SOCKS4a(4),
    SOCKS5(5),
    UNKNOWN(-1);

    private final byte b;

    private SocksProtocolVersion(byte b2) {
        this.b = b2;
    }

    @Deprecated
    public static SocksProtocolVersion fromByte(byte b2) {
        return SocksProtocolVersion.valueOf(b2);
    }

    public static SocksProtocolVersion valueOf(byte b2) {
        for (SocksProtocolVersion code : SocksProtocolVersion.values()) {
            if (code.b != b2) continue;
            return code;
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return this.b;
    }
}

