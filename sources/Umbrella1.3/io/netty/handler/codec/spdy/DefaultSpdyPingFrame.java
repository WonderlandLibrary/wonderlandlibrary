/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.SpdyPingFrame;
import io.netty.util.internal.StringUtil;

public class DefaultSpdyPingFrame
implements SpdyPingFrame {
    private int id;

    public DefaultSpdyPingFrame(int id) {
        this.setId(id);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public SpdyPingFrame setId(int id) {
        this.id = id;
        return this;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append(StringUtil.NEWLINE);
        buf.append("--> ID = ");
        buf.append(this.getId());
        return buf.toString();
    }
}

