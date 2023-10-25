/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

public class SpdySessionStatus
implements Comparable<SpdySessionStatus> {
    public static final SpdySessionStatus PROTOCOL_ERROR;
    public static final SpdySessionStatus INTERNAL_ERROR;
    private final int code;
    public static final SpdySessionStatus OK;
    private final String statusPhrase;

    public boolean equals(Object object) {
        if (!(object instanceof SpdySessionStatus)) {
            return false;
        }
        return this.code() == ((SpdySessionStatus)object).code();
    }

    public int code() {
        return this.code;
    }

    @Override
    public int compareTo(SpdySessionStatus spdySessionStatus) {
        return this.code() - spdySessionStatus.code();
    }

    public SpdySessionStatus(int n, String string) {
        if (string == null) {
            throw new NullPointerException("statusPhrase");
        }
        this.code = n;
        this.statusPhrase = string;
    }

    public String statusPhrase() {
        return this.statusPhrase;
    }

    static {
        OK = new SpdySessionStatus(0, "OK");
        PROTOCOL_ERROR = new SpdySessionStatus(1, "PROTOCOL_ERROR");
        INTERNAL_ERROR = new SpdySessionStatus(2, "INTERNAL_ERROR");
    }

    public int hashCode() {
        return this.code();
    }

    public String toString() {
        return this.statusPhrase();
    }

    public static SpdySessionStatus valueOf(int n) {
        switch (n) {
            case 0: {
                return OK;
            }
            case 1: {
                return PROTOCOL_ERROR;
            }
            case 2: {
                return INTERNAL_ERROR;
            }
        }
        return new SpdySessionStatus(n, "UNKNOWN (" + n + ')');
    }
}

