/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

public enum SocksAuthStatus {
    SUCCESS(0),
    FAILURE(-1);

    private final byte b;

    private SocksAuthStatus(byte b2) {
        this.b = b2;
    }

    @Deprecated
    public static SocksAuthStatus fromByte(byte b2) {
        return SocksAuthStatus.valueOf(b2);
    }

    public static SocksAuthStatus valueOf(byte b2) {
        for (SocksAuthStatus code : SocksAuthStatus.values()) {
            if (code.b != b2) continue;
            return code;
        }
        return FAILURE;
    }

    public byte byteValue() {
        return this.b;
    }
}

