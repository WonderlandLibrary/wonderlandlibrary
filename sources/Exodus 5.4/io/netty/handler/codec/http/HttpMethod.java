/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;
import java.util.HashMap;
import java.util.Map;

public class HttpMethod
implements Comparable<HttpMethod> {
    public static final HttpMethod PATCH;
    public static final HttpMethod PUT;
    public static final HttpMethod POST;
    public static final HttpMethod CONNECT;
    public static final HttpMethod OPTIONS;
    public static final HttpMethod DELETE;
    private final byte[] bytes;
    public static final HttpMethod TRACE;
    private static final Map<String, HttpMethod> methodMap;
    public static final HttpMethod GET;
    private final String name;
    public static final HttpMethod HEAD;

    private HttpMethod(String string, boolean bl) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        if ((string = string.trim()).isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        for (int i = 0; i < string.length(); ++i) {
            if (!Character.isISOControl(string.charAt(i)) && !Character.isWhitespace(string.charAt(i))) continue;
            throw new IllegalArgumentException("invalid character in name");
        }
        this.name = string;
        this.bytes = (byte[])(bl ? string.getBytes(CharsetUtil.US_ASCII) : null);
    }

    public boolean equals(Object object) {
        if (!(object instanceof HttpMethod)) {
            return false;
        }
        HttpMethod httpMethod = (HttpMethod)object;
        return this.name().equals(httpMethod.name());
    }

    static {
        OPTIONS = new HttpMethod("OPTIONS", true);
        GET = new HttpMethod("GET", true);
        HEAD = new HttpMethod("HEAD", true);
        POST = new HttpMethod("POST", true);
        PUT = new HttpMethod("PUT", true);
        PATCH = new HttpMethod("PATCH", true);
        DELETE = new HttpMethod("DELETE", true);
        TRACE = new HttpMethod("TRACE", true);
        CONNECT = new HttpMethod("CONNECT", true);
        methodMap = new HashMap<String, HttpMethod>();
        methodMap.put(OPTIONS.toString(), OPTIONS);
        methodMap.put(GET.toString(), GET);
        methodMap.put(HEAD.toString(), HEAD);
        methodMap.put(POST.toString(), POST);
        methodMap.put(PUT.toString(), PUT);
        methodMap.put(PATCH.toString(), PATCH);
        methodMap.put(DELETE.toString(), DELETE);
        methodMap.put(TRACE.toString(), TRACE);
        methodMap.put(CONNECT.toString(), CONNECT);
    }

    public HttpMethod(String string) {
        this(string, false);
    }

    public String toString() {
        return this.name();
    }

    @Override
    public int compareTo(HttpMethod httpMethod) {
        return this.name().compareTo(httpMethod.name());
    }

    public int hashCode() {
        return this.name().hashCode();
    }

    public String name() {
        return this.name;
    }

    public static HttpMethod valueOf(String string) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        if ((string = string.trim()).isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        HttpMethod httpMethod = methodMap.get(string);
        if (httpMethod != null) {
            return httpMethod;
        }
        return new HttpMethod(string);
    }

    void encode(ByteBuf byteBuf) {
        if (this.bytes == null) {
            HttpHeaders.encodeAscii0(this.name, byteBuf);
        } else {
            byteBuf.writeBytes(this.bytes);
        }
    }
}

