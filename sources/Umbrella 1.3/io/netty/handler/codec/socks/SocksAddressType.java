/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.socks;

public enum SocksAddressType {
    IPv4(1),
    DOMAIN(3),
    IPv6(4),
    UNKNOWN(-1);

    private final byte b;

    private SocksAddressType(byte b) {
        this.b = b;
    }

    public static SocksAddressType fromByte(byte b) {
        for (SocksAddressType code : SocksAddressType.values()) {
            if (code.b != b) continue;
            return code;
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return this.b;
    }
}

