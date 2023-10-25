/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.http.HttpConstants;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

public class QueryStringEncoder {
    private final List<Param> params = new ArrayList<Param>();
    private final String uri;
    private final Charset charset;

    public void addParam(String string, String string2) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        this.params.add(new Param(string, string2));
    }

    public String toString() {
        if (this.params.isEmpty()) {
            return this.uri;
        }
        StringBuilder stringBuilder = new StringBuilder(this.uri).append('?');
        for (int i = 0; i < this.params.size(); ++i) {
            Param param = this.params.get(i);
            stringBuilder.append(QueryStringEncoder.encodeComponent(param.name, this.charset));
            if (param.value != null) {
                stringBuilder.append('=');
                stringBuilder.append(QueryStringEncoder.encodeComponent(param.value, this.charset));
            }
            if (i == this.params.size() - 1) continue;
            stringBuilder.append('&');
        }
        return stringBuilder.toString();
    }

    public QueryStringEncoder(String string, Charset charset) {
        if (string == null) {
            throw new NullPointerException("getUri");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.uri = string;
        this.charset = charset;
    }

    public URI toUri() throws URISyntaxException {
        return new URI(this.toString());
    }

    public QueryStringEncoder(String string) {
        this(string, HttpConstants.DEFAULT_CHARSET);
    }

    private static String encodeComponent(String string, Charset charset) {
        try {
            return URLEncoder.encode(string, charset.name()).replace("+", "%20");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new UnsupportedCharsetException(charset.name());
        }
    }

    private static final class Param {
        final String name;
        final String value;

        Param(String string, String string2) {
            this.value = string2;
            this.name = string;
        }
    }
}

