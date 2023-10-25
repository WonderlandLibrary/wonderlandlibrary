/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.DefaultSpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdySynReplyFrame;
import io.netty.util.internal.StringUtil;

public class DefaultSpdySynReplyFrame
extends DefaultSpdyHeadersFrame
implements SpdySynReplyFrame {
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append("(last: ");
        stringBuilder.append(this.isLast());
        stringBuilder.append(')');
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> Stream-ID = ");
        stringBuilder.append(this.streamId());
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("--> Headers:");
        stringBuilder.append(StringUtil.NEWLINE);
        this.appendHeaders(stringBuilder);
        stringBuilder.setLength(stringBuilder.length() - StringUtil.NEWLINE.length());
        return stringBuilder.toString();
    }

    @Override
    public SpdySynReplyFrame setStreamId(int n) {
        super.setStreamId(n);
        return this;
    }

    public DefaultSpdySynReplyFrame(int n) {
        super(n);
    }

    @Override
    public SpdySynReplyFrame setInvalid() {
        super.setInvalid();
        return this;
    }

    @Override
    public SpdySynReplyFrame setLast(boolean bl) {
        super.setLast(bl);
        return this;
    }
}

