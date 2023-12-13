/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

public enum SocksCmdType {
    CONNECT(1),
    BIND(2),
    UDP(3),
    UNKNOWN(-1);

    private final byte b;

    private SocksCmdType(byte b2) {
        this.b = b2;
    }

    @Deprecated
    public static SocksCmdType fromByte(byte b2) {
        return SocksCmdType.valueOf(b2);
    }

    public static SocksCmdType valueOf(byte b2) {
        for (SocksCmdType code : SocksCmdType.values()) {
            if (code.b != b2) continue;
            return code;
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return this.b;
    }
}

