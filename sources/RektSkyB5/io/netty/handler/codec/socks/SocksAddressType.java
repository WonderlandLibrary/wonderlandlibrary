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

    private SocksAddressType(byte b2) {
        this.b = b2;
    }

    @Deprecated
    public static SocksAddressType fromByte(byte b2) {
        return SocksAddressType.valueOf(b2);
    }

    public static SocksAddressType valueOf(byte b2) {
        for (SocksAddressType code : SocksAddressType.values()) {
            if (code.b != b2) continue;
            return code;
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return this.b;
    }
}

