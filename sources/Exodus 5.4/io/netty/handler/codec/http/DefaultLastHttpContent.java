/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.internal.StringUtil;
import java.util.Map;

public class DefaultLastHttpContent
extends DefaultHttpContent
implements LastHttpContent {
    private final HttpHeaders trailingHeaders;
    private final boolean validateHeaders;

    @Override
    public HttpHeaders trailingHeaders() {
        return this.trailingHeaders;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.append(StringUtil.NEWLINE);
        this.appendHeaders(stringBuilder);
        stringBuilder.setLength(stringBuilder.length() - StringUtil.NEWLINE.length());
        return stringBuilder.toString();
    }

    public DefaultLastHttpContent(ByteBuf byteBuf, boolean bl) {
        super(byteBuf);
        this.trailingHeaders = new TrailingHeaders(bl);
        this.validateHeaders = bl;
    }

    @Override
    public LastHttpContent retain() {
        super.retain();
        return this;
    }

    private void appendHeaders(StringBuilder stringBuilder) {
        for (Map.Entry entry : this.trailingHeaders()) {
            stringBuilder.append((String)entry.getKey());
            stringBuilder.append(": ");
            stringBuilder.append((String)entry.getValue());
            stringBuilder.append(StringUtil.NEWLINE);
        }
    }

    @Override
    public LastHttpContent copy() {
        DefaultLastHttpContent defaultLastHttpContent = new DefaultLastHttpContent(this.content().copy(), this.validateHeaders);
        defaultLastHttpContent.trailingHeaders().set(this.trailingHeaders());
        return defaultLastHttpContent;
    }

    @Override
    public LastHttpContent duplicate() {
        DefaultLastHttpContent defaultLastHttpContent = new DefaultLastHttpContent(this.content().duplicate(), this.validateHeaders);
        defaultLastHttpContent.trailingHeaders().set(this.trailingHeaders());
        return defaultLastHttpContent;
    }

    @Override
    public LastHttpContent retain(int n) {
        super.retain(n);
        return this;
    }

    public DefaultLastHttpContent() {
        this(Unpooled.buffer(0));
    }

    public DefaultLastHttpContent(ByteBuf byteBuf) {
        this(byteBuf, true);
    }

    private static final class TrailingHeaders
    extends DefaultHttpHeaders {
        @Override
        void validateHeaderName0(CharSequence charSequence) {
            super.validateHeaderName0(charSequence);
            if (HttpHeaders.equalsIgnoreCase("Content-Length", charSequence) || HttpHeaders.equalsIgnoreCase("Transfer-Encoding", charSequence) || HttpHeaders.equalsIgnoreCase("Trailer", charSequence)) {
                throw new IllegalArgumentException("prohibited trailing header: " + charSequence);
            }
        }

        TrailingHeaders(boolean bl) {
            super(bl);
        }
    }
}

