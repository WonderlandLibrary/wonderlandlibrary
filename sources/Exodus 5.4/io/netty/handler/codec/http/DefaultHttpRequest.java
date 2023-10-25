/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.http.DefaultHttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.internal.StringUtil;

public class DefaultHttpRequest
extends DefaultHttpMessage
implements HttpRequest {
    private HttpMethod method;
    private String uri;

    @Override
    public HttpRequest setProtocolVersion(HttpVersion httpVersion) {
        super.setProtocolVersion(httpVersion);
        return this;
    }

    @Override
    public HttpRequest setMethod(HttpMethod httpMethod) {
        if (httpMethod == null) {
            throw new NullPointerException("method");
        }
        this.method = httpMethod;
        return this;
    }

    public DefaultHttpRequest(HttpVersion httpVersion, HttpMethod httpMethod, String string) {
        this(httpVersion, httpMethod, string, true);
    }

    @Override
    public HttpRequest setUri(String string) {
        if (string == null) {
            throw new NullPointerException("uri");
        }
        this.uri = string;
        return this;
    }

    public DefaultHttpRequest(HttpVersion httpVersion, HttpMethod httpMethod, String string, boolean bl) {
        super(httpVersion, bl);
        if (httpMethod == null) {
            throw new NullPointerException("method");
        }
        if (string == null) {
            throw new NullPointerException("uri");
        }
        this.method = httpMethod;
        this.uri = string;
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    @Override
    public HttpMethod getMethod() {
        return this.method;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append("(decodeResult: ");
        stringBuilder.append(this.getDecoderResult());
        stringBuilder.append(')');
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append(this.getMethod());
        stringBuilder.append(' ');
        stringBuilder.append(this.getUri());
        stringBuilder.append(' ');
        stringBuilder.append(this.getProtocolVersion().text());
        stringBuilder.append(StringUtil.NEWLINE);
        this.appendHeaders(stringBuilder);
        stringBuilder.setLength(stringBuilder.length() - StringUtil.NEWLINE.length());
        return stringBuilder.toString();
    }
}

