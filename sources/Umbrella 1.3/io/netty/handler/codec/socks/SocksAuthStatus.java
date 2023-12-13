/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.socks;

public enum SocksAuthStatus {
    SUCCESS(0),
    FAILURE(-1);

    private final byte b;

    private SocksAuthStatus(byte b) {
        this.b = b;
    }

    public static SocksAuthStatus fromByte(byte b) {
        for (SocksAuthStatus code : SocksAuthStatus.values()) {
            if (code.b != b) continue;
            return code;
        }
        return FAILURE;
    }

    public byte byteValue() {
        return this.b;
    }
}

