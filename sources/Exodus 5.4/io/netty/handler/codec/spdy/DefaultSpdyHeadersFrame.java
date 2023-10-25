/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.DefaultSpdyHeaders;
import io.netty.handler.codec.spdy.DefaultSpdyStreamFrame;
import io.netty.handler.codec.spdy.SpdyHeaders;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.util.internal.StringUtil;
import java.util.Map;

public class DefaultSpdyHeadersFrame
extends DefaultSpdyStreamFrame
implements SpdyHeadersFrame {
    private final SpdyHeaders headers = new DefaultSpdyHeaders();
    private boolean invalid;
    private boolean truncated;

    protected void appendHeaders(StringBuilder stringBuilder) {
        for (Map.Entry<String, String> entry : this.headers()) {
            stringBuilder.append("    ");
            stringBuilder.append(entry.getKey());
            stringBuilder.append(": ");
            stringBuilder.append(entry.getValue());
            stringBuilder.append(StringUtil.NEWLINE);
        }
    }

    @Override
    public boolean isInvalid() {
        return this.invalid;
    }

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
    public SpdyHeaders headers() {
        return this.headers;
    }

    @Override
    public SpdyHeadersFrame setLast(boolean bl) {
        super.setLast(bl);
        return this;
    }

    @Override
    public SpdyHeadersFrame setInvalid() {
        this.invalid = true;
        return this;
    }

    @Override
    public SpdyHeadersFrame setTruncated() {
        this.truncated = true;
        return this;
    }

    @Override
    public boolean isTruncated() {
        return this.truncated;
    }

    @Override
    public SpdyHeadersFrame setStreamId(int n) {
        super.setStreamId(n);
        return this;
    }

    public DefaultSpdyHeadersFrame(int n) {
        super(n);
    }
}

