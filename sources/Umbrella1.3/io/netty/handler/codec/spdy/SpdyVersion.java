/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

public enum SpdyVersion {
    SPDY_3(3, 0, false),
    SPDY_3_1(3, 1, true);

    private final int version;
    private final int minorVersion;
    private final boolean sessionFlowControl;

    private SpdyVersion(int version, int minorVersion, boolean sessionFlowControl) {
        this.version = version;
        this.minorVersion = minorVersion;
        this.sessionFlowControl = sessionFlowControl;
    }

    int getVersion() {
        return this.version;
    }

    int getMinorVersion() {
        return this.minorVersion;
    }

    boolean useSessionFlowControl() {
        return this.sessionFlowControl;
    }
}

