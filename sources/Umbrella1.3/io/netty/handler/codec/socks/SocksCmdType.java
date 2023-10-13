/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.socks;

public enum SocksCmdType {
    CONNECT(1),
    BIND(2),
    UDP(3),
    UNKNOWN(-1);

    private final byte b;

    private SocksCmdType(byte b) {
        this.b = b;
    }

    public static SocksCmdType fromByte(byte b) {
        for (SocksCmdType code : SocksCmdType.values()) {
            if (code.b != b) continue;
            return code;
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return this.b;
    }
}

