/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.DefaultHttpObject;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.internal.StringUtil;
import java.util.Map;

public abstract class DefaultHttpMessage
extends DefaultHttpObject
implements HttpMessage {
    private HttpVersion version;
    private final HttpHeaders headers;

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append("(version: ");
        stringBuilder.append(this.getProtocolVersion().text());
        stringBuilder.append(", keepAlive: ");
        stringBuilder.append(HttpHeaders.isKeepAlive(this));
        stringBuilder.append(')');
        stringBuilder.append(StringUtil.NEWLINE);
        this.appendHeaders(stringBuilder);
        stringBuilder.setLength(stringBuilder.length() - StringUtil.NEWLINE.length());
        return stringBuilder.toString();
    }

    void appendHeaders(StringBuilder stringBuilder) {
        for (Map.Entry entry : this.headers()) {
            stringBuilder.append((String)entry.getKey());
            stringBuilder.append(": ");
            stringBuilder.append((String)entry.getValue());
            stringBuilder.append(StringUtil.NEWLINE);
        }
    }

    @Override
    public HttpVersion getProtocolVersion() {
        return this.version;
    }

    protected DefaultHttpMessage(HttpVersion httpVersion) {
        this(httpVersion, true);
    }

    @Override
    public HttpMessage setProtocolVersion(HttpVersion httpVersion) {
        if (httpVersion == null) {
            throw new NullPointerException("version");
        }
        this.version = httpVersion;
        return this;
    }

    @Override
    public HttpHeaders headers() {
        return this.headers;
    }

    protected DefaultHttpMessage(HttpVersion httpVersion, boolean bl) {
        if (httpVersion == null) {
            throw new NullPointerException("version");
        }
        this.version = httpVersion;
        this.headers = new DefaultHttpHeaders(bl);
    }
}

