/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

public enum SocksAddressType {
    IPv4(1),
    DOMAIN(3),
    IPv6(4),
    UNKNOWN(-1);

    private final byte b;

    @Deprecated
    public static SocksAddressType fromByte(byte by) {
        return SocksAddressType.valueOf(by);
    }

    public byte byteValue() {
        return this.b;
    }

    private SocksAddressType(byte by) {
        this.b = by;
    }

    public static SocksAddressType valueOf(String string) {
        return Enum.valueOf(SocksAddressType.class, string);
    }
}

