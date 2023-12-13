/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

public enum SocksCmdStatus {
    SUCCESS(0),
    FAILURE(1),
    FORBIDDEN(2),
    NETWORK_UNREACHABLE(3),
    HOST_UNREACHABLE(4),
    REFUSED(5),
    TTL_EXPIRED(6),
    COMMAND_NOT_SUPPORTED(7),
    ADDRESS_NOT_SUPPORTED(8),
    UNASSIGNED(-1);

    private final byte b;

    private SocksCmdStatus(byte b2) {
        this.b = b2;
    }

    @Deprecated
    public static SocksCmdStatus fromByte(byte b2) {
        return SocksCmdStatus.valueOf(b2);
    }

    public static SocksCmdStatus valueOf(byte b2) {
        for (SocksCmdStatus code : SocksCmdStatus.values()) {
            if (code.b != b2) continue;
            return code;
        }
        return UNASSIGNED;
    }

    public byte byteValue() {
        return this.b;
    }
}

