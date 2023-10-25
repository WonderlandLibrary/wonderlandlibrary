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

    private SocksCmdStatus(byte by) {
        this.b = by;
    }

    public byte byteValue() {
        return this.b;
    }

    @Deprecated
    public static SocksCmdStatus fromByte(byte by) {
        return SocksCmdStatus.valueOf(by);
    }

    public static SocksCmdStatus valueOf(byte by) {
        for (SocksCmdStatus socksCmdStatus : SocksCmdStatus.values()) {
            if (socksCmdStatus.b != by) continue;
            return socksCmdStatus;
        }
        return UNASSIGNED;
    }
}

