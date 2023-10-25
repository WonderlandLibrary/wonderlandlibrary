/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

public enum SpdyVersion {
    SPDY_3_1(3, 1);

    private final int minorVersion;
    private final int version;

    int getMinorVersion() {
        return this.minorVersion;
    }

    int getVersion() {
        return this.version;
    }

    private SpdyVersion(int n2, int n3) {
        this.version = n2;
        this.minorVersion = n3;
    }
}

