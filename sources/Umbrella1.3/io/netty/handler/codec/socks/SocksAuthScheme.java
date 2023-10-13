/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.socks;

public enum SocksAuthScheme {
    NO_AUTH(0),
    AUTH_GSSAPI(1),
    AUTH_PASSWORD(2),
    UNKNOWN(-1);

    private final byte b;

    private SocksAuthScheme(byte b) {
        this.b = b;
    }

    public static SocksAuthScheme fromByte(byte b) {
        for (SocksAuthScheme code : SocksAuthScheme.values()) {
            if (code.b != b) continue;
            return code;
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return this.b;
    }
}

