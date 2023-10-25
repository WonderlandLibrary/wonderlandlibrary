/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyPingFrame;
import io.netty.util.internal.StringUtil;

public class DefaultSpdyPingFrame
implements SpdyPingFrame {
    private int id;

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> ID = ");
        stringBuilder.append(this.id());
        return stringBuilder.toString();
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public SpdyPingFrame setId(int n) {
        this.id = n;
        return this;
    }

    public DefaultSpdyPingFrame(int n) {
        this.setId(n);
    }
}

