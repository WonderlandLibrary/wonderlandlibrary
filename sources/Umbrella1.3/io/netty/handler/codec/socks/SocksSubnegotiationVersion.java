/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.socks;

public enum SocksSubnegotiationVersion {
    AUTH_PASSWORD(1),
    UNKNOWN(-1);

    private final byte b;

    private SocksSubnegotiationVersion(byte b) {
        this.b = b;
    }

    public static SocksSubnegotiationVersion fromByte(byte b) {
        for (SocksSubnegotiationVersion code : SocksSubnegotiationVersion.values()) {
            if (code.b != b) continue;
            return code;
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return this.b;
    }
}

