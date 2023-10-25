/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

public enum SocksAuthStatus {
    SUCCESS(0),
    FAILURE(-1);

    private final byte b;

    @Deprecated
    public static SocksAuthStatus fromByte(byte by) {
        return SocksAuthStatus.valueOf(by);
    }

    public byte byteValue() {
        return this.b;
    }

    public static SocksAuthStatus valueOf(String string) {
        return Enum.valueOf(SocksAuthStatus.class, string);
    }

    private SocksAuthStatus(byte by) {
        this.b = by;
    }
}

