/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SpdyHeaders
implements Iterable<Map.Entry<String, String>> {
    public static final SpdyHeaders EMPTY_HEADERS = new SpdyHeaders(){

        @Override
        public Set<String> names() {
            return Collections.emptySet();
        }

        @Override
        public SpdyHeaders set(String string, Iterable<?> iterable) {
            throw new UnsupportedOperationException("read only");
        }

        @Override
        public SpdyHeaders clear() {
            throw new UnsupportedOperationException("read only");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public SpdyHeaders remove(String string) {
            throw new UnsupportedOperationException("read only");
        }

        @Override
        public List<String> getAll(String string) {
            return Collections.emptyList();
        }

        @Override
        public List<Map.Entry<String, String>> entries() {
            return Collections.emptyList();
        }

        @Override
        public String get(String string) {
            return null;
        }

        @Override
        public SpdyHeaders set(String string, Object object) {
            throw new UnsupportedOperationException("read only");
        }

        @Override
        public Iterator<Map.Entry<String, String>> iterator() {
            return this.entries().iterator();
        }

        @Override
        public SpdyHeaders add(String string, Object object) {
            throw new UnsupportedOperationException("read only");
        }

        @Override
        public SpdyHeaders add(String string, Iterable<?> iterable) {
            throw new UnsupportedOperationException("read only");
        }

        @Override
        public boolean contains(String string) {
            return false;
        }
    };

    public abstract Set<String> names();

    public static void setScheme(int n, SpdyHeadersFrame spdyHeadersFrame, String string) {
        spdyHeadersFrame.headers().set(":scheme", string);
    }

    public static void setHeader(SpdyHeadersFrame spdyHeadersFrame, String string, Iterable<?> iterable) {
        spdyHeadersFrame.headers().set(string, iterable);
    }

    public static void setStatus(int n, SpdyHeadersFrame spdyHeadersFrame, HttpResponseStatus httpResponseStatus) {
        spdyHeadersFrame.headers().set(":status", httpResponseStatus.toString());
    }

    public abstract SpdyHeaders add(String var1, Object var2);

    public static void setMethod(int n, SpdyHeadersFrame spdyHeadersFrame, HttpMethod httpMethod) {
        spdyHeadersFrame.headers().set(":method", httpMethod.name());
    }

    public abstract SpdyHeaders remove(String var1);

    public static String getHeader(SpdyHeadersFrame spdyHeadersFrame, String string, String string2) {
        String string3 = spdyHeadersFrame.headers().get(string);
        if (string3 == null) {
            return string2;
        }
        return string3;
    }

    public static String getUrl(int n, SpdyHeadersFrame spdyHeadersFrame) {
        return spdyHeadersFrame.headers().get(":path");
    }

    public abstract SpdyHeaders clear();

    public abstract SpdyHeaders set(String var1, Object var2);

    public abstract boolean contains(String var1);

    public abstract SpdyHeaders add(String var1, Iterable<?> var2);

    public abstract SpdyHeaders set(String var1, Iterable<?> var2);

    public static void removeScheme(int n, SpdyHeadersFrame spdyHeadersFrame) {
        spdyHeadersFrame.headers().remove(":scheme");
    }

    public static HttpResponseStatus getStatus(int n, SpdyHeadersFrame spdyHeadersFrame) {
        try {
            String string = spdyHeadersFrame.headers().get(":status");
            int n2 = string.indexOf(32);
            if (n2 == -1) {
                return HttpResponseStatus.valueOf(Integer.parseInt(string));
            }
            int n3 = Integer.parseInt(string.substring(0, n2));
            String string2 = string.substring(n2 + 1);
            HttpResponseStatus httpResponseStatus = HttpResponseStatus.valueOf(n3);
            if (httpResponseStatus.reasonPhrase().equals(string2)) {
                return httpResponseStatus;
            }
            return new HttpResponseStatus(n3, string2);
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static void setVersion(int n, SpdyHeadersFrame spdyHeadersFrame, HttpVersion httpVersion) {
        spdyHeadersFrame.headers().set(":version", httpVersion.text());
    }

    public static HttpMethod getMethod(int n, SpdyHeadersFrame spdyHeadersFrame) {
        try {
            return HttpMethod.valueOf(spdyHeadersFrame.headers().get(":method"));
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static void removeMethod(int n, SpdyHeadersFrame spdyHeadersFrame) {
        spdyHeadersFrame.headers().remove(":method");
    }

    public static void setHost(SpdyHeadersFrame spdyHeadersFrame, String string) {
        spdyHeadersFrame.headers().set(":host", string);
    }

    public static String getHeader(SpdyHeadersFrame spdyHeadersFrame, String string) {
        return spdyHeadersFrame.headers().get(string);
    }

    public static void removeUrl(int n, SpdyHeadersFrame spdyHeadersFrame) {
        spdyHeadersFrame.headers().remove(":path");
    }

    public static void removeStatus(int n, SpdyHeadersFrame spdyHeadersFrame) {
        spdyHeadersFrame.headers().remove(":status");
    }

    public abstract String get(String var1);

    public static void removeVersion(int n, SpdyHeadersFrame spdyHeadersFrame) {
        spdyHeadersFrame.headers().remove(":version");
    }

    public static void setUrl(int n, SpdyHeadersFrame spdyHeadersFrame, String string) {
        spdyHeadersFrame.headers().set(":path", string);
    }

    public static HttpVersion getVersion(int n, SpdyHeadersFrame spdyHeadersFrame) {
        try {
            return HttpVersion.valueOf(spdyHeadersFrame.headers().get(":version"));
        }
        catch (Exception exception) {
            return null;
        }
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return this.entries().iterator();
    }

    public abstract List<Map.Entry<String, String>> entries();

    public static String getScheme(int n, SpdyHeadersFrame spdyHeadersFrame) {
        return spdyHeadersFrame.headers().get(":scheme");
    }

    public static void addHeader(SpdyHeadersFrame spdyHeadersFrame, String string, Object object) {
        spdyHeadersFrame.headers().add(string, object);
    }

    public static void setHeader(SpdyHeadersFrame spdyHeadersFrame, String string, Object object) {
        spdyHeadersFrame.headers().set(string, object);
    }

    public static void removeHost(SpdyHeadersFrame spdyHeadersFrame) {
        spdyHeadersFrame.headers().remove(":host");
    }

    public static String getHost(SpdyHeadersFrame spdyHeadersFrame) {
        return spdyHeadersFrame.headers().get(":host");
    }

    public abstract boolean isEmpty();

    public abstract List<String> getAll(String var1);

    public static final class HttpNames {
        public static final String SCHEME = ":scheme";
        public static final String HOST = ":host";
        public static final String METHOD = ":method";
        public static final String VERSION = ":version";
        public static final String STATUS = ":status";
        public static final String PATH = ":path";

        private HttpNames() {
        }
    }
}

