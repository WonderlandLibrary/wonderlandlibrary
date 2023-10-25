/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

public final class ChannelMetadata {
    private final boolean hasDisconnect;

    public boolean hasDisconnect() {
        return this.hasDisconnect;
    }

    public ChannelMetadata(boolean bl) {
        this.hasDisconnect = bl;
    }
}

