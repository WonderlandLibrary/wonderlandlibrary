/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

public enum SocksSubnegotiationVersion {
    AUTH_PASSWORD(1),
    UNKNOWN(-1);

    private final byte b;

    private SocksSubnegotiationVersion(byte b2) {
        this.b = b2;
    }

    @Deprecated
    public static SocksSubnegotiationVersion fromByte(byte b2) {
        return SocksSubnegotiationVersion.valueOf(b2);
    }

    public static SocksSubnegotiationVersion valueOf(byte b2) {
        for (SocksSubnegotiationVersion code : SocksSubnegotiationVersion.values()) {
            if (code.b != b2) continue;
            return code;
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return this.b;
    }
}

