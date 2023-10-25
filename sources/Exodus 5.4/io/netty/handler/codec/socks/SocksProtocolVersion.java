/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

public enum SocksProtocolVersion {
    SOCKS4a(4),
    SOCKS5(5),
    UNKNOWN(-1);

    private final byte b;

    public byte byteValue() {
        return this.b;
    }

    @Deprecated
    public static SocksProtocolVersion fromByte(byte by) {
        return SocksProtocolVersion.valueOf(by);
    }

    private SocksProtocolVersion(byte by) {
        this.b = by;
    }

    public static SocksProtocolVersion valueOf(byte by) {
        for (SocksProtocolVersion socksProtocolVersion : SocksProtocolVersion.values()) {
            if (socksProtocolVersion.b != by) continue;
            return socksProtocolVersion;
        }
        return UNKNOWN;
    }
}

