/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

public class SpdyStreamStatus
implements Comparable<SpdyStreamStatus> {
    public static final SpdyStreamStatus REFUSED_STREAM;
    public static final SpdyStreamStatus PROTOCOL_ERROR;
    public static final SpdyStreamStatus INVALID_CREDENTIALS;
    public static final SpdyStreamStatus INVALID_STREAM;
    public static final SpdyStreamStatus INTERNAL_ERROR;
    public static final SpdyStreamStatus FRAME_TOO_LARGE;
    public static final SpdyStreamStatus STREAM_IN_USE;
    public static final SpdyStreamStatus STREAM_ALREADY_CLOSED;
    public static final SpdyStreamStatus FLOW_CONTROL_ERROR;
    private final String statusPhrase;
    public static final SpdyStreamStatus UNSUPPORTED_VERSION;
    private final int code;
    public static final SpdyStreamStatus CANCEL;

    public SpdyStreamStatus(int n, String string) {
        if (n == 0) {
            throw new IllegalArgumentException("0 is not a valid status code for a RST_STREAM");
        }
        if (string == null) {
            throw new NullPointerException("statusPhrase");
        }
        this.code = n;
        this.statusPhrase = string;
    }

    public int code() {
        return this.code;
    }

    public String statusPhrase() {
        return this.statusPhrase;
    }

    static {
        PROTOCOL_ERROR = new SpdyStreamStatus(1, "PROTOCOL_ERROR");
        INVALID_STREAM = new SpdyStreamStatus(2, "INVALID_STREAM");
        REFUSED_STREAM = new SpdyStreamStatus(3, "REFUSED_STREAM");
        UNSUPPORTED_VERSION = new SpdyStreamStatus(4, "UNSUPPORTED_VERSION");
        CANCEL = new SpdyStreamStatus(5, "CANCEL");
        INTERNAL_ERROR = new SpdyStreamStatus(6, "INTERNAL_ERROR");
        FLOW_CONTROL_ERROR = new SpdyStreamStatus(7, "FLOW_CONTROL_ERROR");
        STREAM_IN_USE = new SpdyStreamStatus(8, "STREAM_IN_USE");
        STREAM_ALREADY_CLOSED = new SpdyStreamStatus(9, "STREAM_ALREADY_CLOSED");
        INVALID_CREDENTIALS = new SpdyStreamStatus(10, "INVALID_CREDENTIALS");
        FRAME_TOO_LARGE = new SpdyStreamStatus(11, "FRAME_TOO_LARGE");
    }

    public static SpdyStreamStatus valueOf(int n) {
        if (n == 0) {
            throw new IllegalArgumentException("0 is not a valid status code for a RST_STREAM");
        }
        switch (n) {
            case 1: {
                return PROTOCOL_ERROR;
            }
            case 2: {
                return INVALID_STREAM;
            }
            case 3: {
                return REFUSED_STREAM;
            }
            case 4: {
                return UNSUPPORTED_VERSION;
            }
            case 5: {
                return CANCEL;
            }
            case 6: {
                return INTERNAL_ERROR;
            }
            case 7: {
                return FLOW_CONTROL_ERROR;
            }
            case 8: {
                return STREAM_IN_USE;
            }
            case 9: {
                return STREAM_ALREADY_CLOSED;
            }
            case 10: {
                return INVALID_CREDENTIALS;
            }
            case 11: {
                return FRAME_TOO_LARGE;
            }
        }
        return new SpdyStreamStatus(n, "UNKNOWN (" + n + ')');
    }

    public String toString() {
        return this.statusPhrase();
    }

    public boolean equals(Object object) {
        if (!(object instanceof SpdyStreamStatus)) {
            return false;
        }
        return this.code() == ((SpdyStreamStatus)object).code();
    }

    @Override
    public int compareTo(SpdyStreamStatus spdyStreamStatus) {
        return this.code() - spdyStreamStatus.code();
    }

    public int hashCode() {
        return this.code();
    }
}

