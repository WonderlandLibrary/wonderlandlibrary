/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

public class SpdySessionStatus
implements Comparable<SpdySessionStatus> {
    public static final SpdySessionStatus OK = new SpdySessionStatus(0, "OK");
    public static final SpdySessionStatus PROTOCOL_ERROR = new SpdySessionStatus(1, "PROTOCOL_ERROR");
    public static final SpdySessionStatus INTERNAL_ERROR = new SpdySessionStatus(2, "INTERNAL_ERROR");
    private final int code;
    private final String statusPhrase;

    public static SpdySessionStatus valueOf(int code) {
        switch (code) {
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
        return new SpdySessionStatus(code, "UNKNOWN (" + code + ')');
    }

    public SpdySessionStatus(int code, String statusPhrase) {
        if (statusPhrase == null) {
            throw new NullPointerException("statusPhrase");
        }
        this.code = code;
        this.statusPhrase = statusPhrase;
    }

    public int getCode() {
        return this.code;
    }

    public String getStatusPhrase() {
        return this.statusPhrase;
    }

    public int hashCode() {
        return this.getCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof SpdySessionStatus)) {
            return false;
        }
        return this.getCode() == ((SpdySessionStatus)o).getCode();
    }

    public String toString() {
        return this.getStatusPhrase();
    }

    @Override
    public int compareTo(SpdySessionStatus o) {
        return this.getCode() - o.getCode();
    }
}

