/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;

public final class SpdyHttpHeaders {
    public static void removeScheme(HttpMessage httpMessage) {
        httpMessage.headers().remove("X-SPDY-Scheme");
    }

    public static String getScheme(HttpMessage httpMessage) {
        return httpMessage.headers().get("X-SPDY-Scheme");
    }

    public static void removeAssociatedToStreamId(HttpMessage httpMessage) {
        httpMessage.headers().remove("X-SPDY-Associated-To-Stream-ID");
    }

    public static void removeUrl(HttpMessage httpMessage) {
        httpMessage.headers().remove("X-SPDY-URL");
    }

    public static void removePriority(HttpMessage httpMessage) {
        httpMessage.headers().remove("X-SPDY-Priority");
    }

    public static void setPriority(HttpMessage httpMessage, byte by) {
        HttpHeaders.setIntHeader(httpMessage, "X-SPDY-Priority", (int)by);
    }

    public static int getStreamId(HttpMessage httpMessage) {
        return HttpHeaders.getIntHeader(httpMessage, "X-SPDY-Stream-ID");
    }

    public static int getAssociatedToStreamId(HttpMessage httpMessage) {
        return HttpHeaders.getIntHeader(httpMessage, "X-SPDY-Associated-To-Stream-ID", 0);
    }

    public static void removeStreamId(HttpMessage httpMessage) {
        httpMessage.headers().remove("X-SPDY-Stream-ID");
    }

    public static void setAssociatedToStreamId(HttpMessage httpMessage, int n) {
        HttpHeaders.setIntHeader(httpMessage, "X-SPDY-Associated-To-Stream-ID", n);
    }

    public static void setScheme(HttpMessage httpMessage, String string) {
        httpMessage.headers().set("X-SPDY-Scheme", (Object)string);
    }

    public static byte getPriority(HttpMessage httpMessage) {
        return (byte)HttpHeaders.getIntHeader(httpMessage, "X-SPDY-Priority", 0);
    }

    public static void setUrl(HttpMessage httpMessage, String string) {
        httpMessage.headers().set("X-SPDY-URL", (Object)string);
    }

    public static String getUrl(HttpMessage httpMessage) {
        return httpMessage.headers().get("X-SPDY-URL");
    }

    public static void setStreamId(HttpMessage httpMessage, int n) {
        HttpHeaders.setIntHeader(httpMessage, "X-SPDY-Stream-ID", n);
    }

    private SpdyHttpHeaders() {
    }

    public static final class Names {
        public static final String URL = "X-SPDY-URL";
        public static final String SCHEME = "X-SPDY-Scheme";
        public static final String STREAM_ID = "X-SPDY-Stream-ID";
        public static final String ASSOCIATED_TO_STREAM_ID = "X-SPDY-Associated-To-Stream-ID";
        public static final String PRIORITY = "X-SPDY-Priority";

        private Names() {
        }
    }
}

