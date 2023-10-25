/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

public enum SocksSubnegotiationVersion {
    AUTH_PASSWORD(1),
    UNKNOWN(-1);

    private final byte b;

    @Deprecated
    public static SocksSubnegotiationVersion fromByte(byte by) {
        return SocksSubnegotiationVersion.valueOf(by);
    }

    private SocksSubnegotiationVersion(byte by) {
        this.b = by;
    }

    public static SocksSubnegotiationVersion valueOf(String string) {
        return Enum.valueOf(SocksSubnegotiationVersion.class, string);
    }

    public byte byteValue() {
        return this.b;
    }
}

