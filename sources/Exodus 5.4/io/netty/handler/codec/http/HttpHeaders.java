/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaderDateFormat;
import io.netty.handler.codec.http.HttpHeaderEntity;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class HttpHeaders
implements Iterable<Map.Entry<String, String>> {
    private static final CharSequence CONTINUE_ENTITY;
    private static final CharSequence HOST_ENTITY;
    private static final CharSequence SEC_WEBSOCKET_LOCATION_ENTITY;
    private static final CharSequence CONNECTION_ENTITY;
    private static final CharSequence TRANSFER_ENCODING_ENTITY;
    private static final CharSequence SEC_WEBSOCKET_KEY2_ENTITY;
    private static final CharSequence KEEP_ALIVE_ENTITY;
    private static final byte[] HEADER_SEPERATOR;
    private static final CharSequence SEC_WEBSOCKET_ORIGIN_ENTITY;
    public static final HttpHeaders EMPTY_HEADERS;
    private static final CharSequence DATE_ENTITY;
    private static final CharSequence SEC_WEBSOCKET_KEY1_ENTITY;
    private static final CharSequence CHUNKED_ENTITY;
    private static final byte[] CRLF;
    private static final CharSequence CONTENT_LENGTH_ENTITY;
    private static final CharSequence CLOSE_ENTITY;
    private static final CharSequence EXPECT_ENTITY;

    public abstract boolean isEmpty();

    public static Date getDateHeader(HttpMessage httpMessage, String string) throws ParseException {
        return HttpHeaders.getDateHeader(httpMessage, (CharSequence)string);
    }

    public static void setHeader(HttpMessage httpMessage, CharSequence charSequence, Iterable<?> iterable) {
        httpMessage.headers().set(charSequence, iterable);
    }

    public static void addIntHeader(HttpMessage httpMessage, CharSequence charSequence, int n) {
        httpMessage.headers().add(charSequence, (Object)n);
    }

    public abstract Set<String> names();

    public static Date getDate(HttpMessage httpMessage) throws ParseException {
        return HttpHeaders.getDateHeader(httpMessage, DATE_ENTITY);
    }

    public HttpHeaders set(CharSequence charSequence, Iterable<?> iterable) {
        return this.set(charSequence.toString(), iterable);
    }

    public abstract String get(String var1);

    public abstract HttpHeaders add(String var1, Iterable<?> var2);

    public static String getHost(HttpMessage httpMessage, String string) {
        return HttpHeaders.getHeader(httpMessage, HOST_ENTITY, string);
    }

    public static void setDateHeader(HttpMessage httpMessage, String string, Iterable<Date> iterable) {
        httpMessage.headers().set(string, iterable);
    }

    static void validateHeaderValue(CharSequence charSequence) {
        if (charSequence == null) {
            throw new NullPointerException("Header values cannot be null");
        }
        int n = 0;
        block19: for (int i = 0; i < charSequence.length(); ++i) {
            char c = charSequence.charAt(i);
            switch (c) {
                case '\u000b': {
                    throw new IllegalArgumentException("Header value contains a prohibited character '\\v': " + charSequence);
                }
                case '\f': {
                    throw new IllegalArgumentException("Header value contains a prohibited character '\\f': " + charSequence);
                }
            }
            switch (n) {
                case 0: {
                    switch (c) {
                        case '\r': {
                            n = 1;
                            break;
                        }
                        case '\n': {
                            n = 2;
                        }
                    }
                    continue block19;
                }
                case 1: {
                    switch (c) {
                        case '\n': {
                            n = 2;
                            continue block19;
                        }
                    }
                    throw new IllegalArgumentException("Only '\\n' is allowed after '\\r': " + charSequence);
                }
                case 2: {
                    switch (c) {
                        case '\t': 
                        case ' ': {
                            n = 0;
                            continue block19;
                        }
                    }
                    throw new IllegalArgumentException("Only ' ' and '\\t' are allowed after '\\n': " + charSequence);
                }
            }
        }
        if (n != 0) {
            throw new IllegalArgumentException("Header value must not end with '\\r' or '\\n':" + charSequence);
        }
    }

    public HttpHeaders add(CharSequence charSequence, Object object) {
        return this.add(charSequence.toString(), object);
    }

    public static void addHeader(HttpMessage httpMessage, CharSequence charSequence, Object object) {
        httpMessage.headers().add(charSequence, object);
    }

    protected HttpHeaders() {
    }

    public HttpHeaders set(HttpHeaders httpHeaders) {
        if (httpHeaders == null) {
            throw new NullPointerException("headers");
        }
        this.clear();
        for (Map.Entry entry : httpHeaders) {
            this.add((String)entry.getKey(), entry.getValue());
        }
        return this;
    }

    static void encode(CharSequence charSequence, CharSequence charSequence2, ByteBuf byteBuf) {
        if (!HttpHeaders.encodeAscii(charSequence, byteBuf)) {
            byteBuf.writeBytes(HEADER_SEPERATOR);
        }
        if (!HttpHeaders.encodeAscii(charSequence2, byteBuf)) {
            byteBuf.writeBytes(CRLF);
        }
    }

    public boolean contains(String string, String string2, boolean bl) {
        List<String> list = this.getAll(string);
        if (list.isEmpty()) {
            return false;
        }
        for (String string3 : list) {
            if (!(bl ? HttpHeaders.equalsIgnoreCase(string3, string2) : string3.equals(string2))) continue;
            return true;
        }
        return false;
    }

    public static int getIntHeader(HttpMessage httpMessage, String string, int n) {
        return HttpHeaders.getIntHeader(httpMessage, (CharSequence)string, n);
    }

    public static void setIntHeader(HttpMessage httpMessage, String string, int n) {
        httpMessage.headers().set(string, (Object)n);
    }

    public static long getContentLength(HttpMessage httpMessage, long l) {
        String string = httpMessage.headers().get(CONTENT_LENGTH_ENTITY);
        if (string != null) {
            try {
                return Long.parseLong(string);
            }
            catch (NumberFormatException numberFormatException) {
                return l;
            }
        }
        long l2 = HttpHeaders.getWebSocketContentLength(httpMessage);
        if (l2 >= 0L) {
            return l2;
        }
        return l;
    }

    public static boolean isTransferEncodingChunked(HttpMessage httpMessage) {
        return httpMessage.headers().contains(TRANSFER_ENCODING_ENTITY, CHUNKED_ENTITY, true);
    }

    public abstract HttpHeaders remove(String var1);

    static {
        HEADER_SEPERATOR = new byte[]{58, 32};
        CRLF = new byte[]{13, 10};
        CONTENT_LENGTH_ENTITY = HttpHeaders.newEntity("Content-Length");
        CONNECTION_ENTITY = HttpHeaders.newEntity("Connection");
        CLOSE_ENTITY = HttpHeaders.newEntity("close");
        KEEP_ALIVE_ENTITY = HttpHeaders.newEntity("keep-alive");
        HOST_ENTITY = HttpHeaders.newEntity("Host");
        DATE_ENTITY = HttpHeaders.newEntity("Date");
        EXPECT_ENTITY = HttpHeaders.newEntity("Expect");
        CONTINUE_ENTITY = HttpHeaders.newEntity("100-continue");
        TRANSFER_ENCODING_ENTITY = HttpHeaders.newEntity("Transfer-Encoding");
        CHUNKED_ENTITY = HttpHeaders.newEntity("chunked");
        SEC_WEBSOCKET_KEY1_ENTITY = HttpHeaders.newEntity("Sec-WebSocket-Key1");
        SEC_WEBSOCKET_KEY2_ENTITY = HttpHeaders.newEntity("Sec-WebSocket-Key2");
        SEC_WEBSOCKET_ORIGIN_ENTITY = HttpHeaders.newEntity("Sec-WebSocket-Origin");
        SEC_WEBSOCKET_LOCATION_ENTITY = HttpHeaders.newEntity("Sec-WebSocket-Location");
        EMPTY_HEADERS = new HttpHeaders(){

            @Override
            public HttpHeaders set(String string, Object object) {
                throw new UnsupportedOperationException("read only");
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Set<String> names() {
                return Collections.emptySet();
            }

            @Override
            public HttpHeaders clear() {
                throw new UnsupportedOperationException("read only");
            }

            @Override
            public boolean contains(String string) {
                return false;
            }

            @Override
            public List<Map.Entry<String, String>> entries() {
                return Collections.emptyList();
            }

            @Override
            public List<String> getAll(String string) {
                return Collections.emptyList();
            }

            @Override
            public HttpHeaders remove(String string) {
                throw new UnsupportedOperationException("read only");
            }

            @Override
            public String get(String string) {
                return null;
            }

            @Override
            public Iterator<Map.Entry<String, String>> iterator() {
                return this.entries().iterator();
            }

            @Override
            public HttpHeaders add(String string, Object object) {
                throw new UnsupportedOperationException("read only");
            }

            @Override
            public HttpHeaders set(String string, Iterable<?> iterable) {
                throw new UnsupportedOperationException("read only");
            }

            @Override
            public HttpHeaders add(String string, Iterable<?> iterable) {
                throw new UnsupportedOperationException("read only");
            }
        };
    }

    public static void removeTransferEncodingChunked(HttpMessage httpMessage) {
        List<String> list = httpMessage.headers().getAll(TRANSFER_ENCODING_ENTITY);
        if (list.isEmpty()) {
            return;
        }
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String string = iterator.next();
            if (!HttpHeaders.equalsIgnoreCase(string, CHUNKED_ENTITY)) continue;
            iterator.remove();
        }
        if (list.isEmpty()) {
            httpMessage.headers().remove(TRANSFER_ENCODING_ENTITY);
        } else {
            httpMessage.headers().set(TRANSFER_ENCODING_ENTITY, list);
        }
    }

    public static void setContentLength(HttpMessage httpMessage, long l) {
        httpMessage.headers().set(CONTENT_LENGTH_ENTITY, (Object)l);
    }

    public static Date getDate(HttpMessage httpMessage, Date date) {
        return HttpHeaders.getDateHeader(httpMessage, DATE_ENTITY, date);
    }

    public static CharSequence newNameEntity(String string) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        return new HttpHeaderEntity(string, HEADER_SEPERATOR);
    }

    static int hash(CharSequence charSequence) {
        if (charSequence instanceof HttpHeaderEntity) {
            return ((HttpHeaderEntity)charSequence).hash();
        }
        int n = 0;
        for (int i = charSequence.length() - 1; i >= 0; --i) {
            char c = charSequence.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c = (char)(c + 32);
            }
            n = 31 * n + c;
        }
        if (n > 0) {
            return n;
        }
        if (n == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        }
        return -n;
    }

    public static Date getDateHeader(HttpMessage httpMessage, CharSequence charSequence, Date date) {
        String string = HttpHeaders.getHeader(httpMessage, charSequence);
        if (string == null) {
            return date;
        }
        try {
            return HttpHeaderDateFormat.get().parse(string);
        }
        catch (ParseException parseException) {
            return date;
        }
    }

    public HttpHeaders add(HttpHeaders httpHeaders) {
        if (httpHeaders == null) {
            throw new NullPointerException("headers");
        }
        for (Map.Entry entry : httpHeaders) {
            this.add((String)entry.getKey(), entry.getValue());
        }
        return this;
    }

    public static CharSequence newValueEntity(String string) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        return new HttpHeaderEntity(string, CRLF);
    }

    public static long getContentLength(HttpMessage httpMessage) {
        String string = HttpHeaders.getHeader(httpMessage, CONTENT_LENGTH_ENTITY);
        if (string != null) {
            return Long.parseLong(string);
        }
        long l = HttpHeaders.getWebSocketContentLength(httpMessage);
        if (l >= 0L) {
            return l;
        }
        throw new NumberFormatException("header not found: Content-Length");
    }

    public static void addHeader(HttpMessage httpMessage, String string, Object object) {
        httpMessage.headers().add(string, object);
    }

    public abstract List<String> getAll(String var1);

    public HttpHeaders remove(CharSequence charSequence) {
        return this.remove(charSequence.toString());
    }

    public static void setTransferEncodingChunked(HttpMessage httpMessage) {
        HttpHeaders.addHeader(httpMessage, TRANSFER_ENCODING_ENTITY, (Object)CHUNKED_ENTITY);
        HttpHeaders.removeHeader(httpMessage, CONTENT_LENGTH_ENTITY);
    }

    public static void setKeepAlive(HttpMessage httpMessage, boolean bl) {
        HttpHeaders httpHeaders = httpMessage.headers();
        if (httpMessage.getProtocolVersion().isKeepAliveDefault()) {
            if (bl) {
                httpHeaders.remove(CONNECTION_ENTITY);
            } else {
                httpHeaders.set(CONNECTION_ENTITY, (Object)CLOSE_ENTITY);
            }
        } else if (bl) {
            httpHeaders.set(CONNECTION_ENTITY, (Object)KEEP_ALIVE_ENTITY);
        } else {
            httpHeaders.remove(CONNECTION_ENTITY);
        }
    }

    public static String getHost(HttpMessage httpMessage) {
        return httpMessage.headers().get(HOST_ENTITY);
    }

    public List<String> getAll(CharSequence charSequence) {
        return this.getAll(charSequence.toString());
    }

    public static int getIntHeader(HttpMessage httpMessage, String string) {
        return HttpHeaders.getIntHeader(httpMessage, (CharSequence)string);
    }

    public static void clearHeaders(HttpMessage httpMessage) {
        httpMessage.headers().clear();
    }

    public abstract boolean contains(String var1);

    public static void setDateHeader(HttpMessage httpMessage, String string, Date date) {
        HttpHeaders.setDateHeader(httpMessage, (CharSequence)string, date);
    }

    public String get(CharSequence charSequence) {
        return this.get(charSequence.toString());
    }

    public boolean contains(CharSequence charSequence, CharSequence charSequence2, boolean bl) {
        return this.contains(charSequence.toString(), charSequence2.toString(), bl);
    }

    static void validateHeaderName(CharSequence charSequence) {
        if (charSequence == null) {
            throw new NullPointerException("Header names cannot be null");
        }
        for (int i = 0; i < charSequence.length(); ++i) {
            char c = charSequence.charAt(i);
            if (c > '\u007f') {
                throw new IllegalArgumentException("Header name cannot contain non-ASCII characters: " + charSequence);
            }
            switch (c) {
                case '\t': 
                case '\n': 
                case '\u000b': 
                case '\f': 
                case '\r': 
                case ' ': 
                case ',': 
                case ':': 
                case ';': 
                case '=': {
                    throw new IllegalArgumentException("Header name cannot contain the following prohibited characters: =,;: \\t\\r\\n\\v\\f: " + charSequence);
                }
            }
        }
    }

    public static void setHeader(HttpMessage httpMessage, String string, Object object) {
        httpMessage.headers().set(string, object);
    }

    public static void setIntHeader(HttpMessage httpMessage, CharSequence charSequence, Iterable<Integer> iterable) {
        httpMessage.headers().set(charSequence, iterable);
    }

    public static CharSequence newEntity(String string) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        return new HttpHeaderEntity(string);
    }

    public static String getHeader(HttpMessage httpMessage, CharSequence charSequence, String string) {
        String string2 = httpMessage.headers().get(charSequence);
        if (string2 == null) {
            return string;
        }
        return string2;
    }

    static void encodeAscii0(CharSequence charSequence, ByteBuf byteBuf) {
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            byteBuf.writeByte((byte)charSequence.charAt(i));
        }
    }

    public static Date getDateHeader(HttpMessage httpMessage, String string, Date date) {
        return HttpHeaders.getDateHeader(httpMessage, (CharSequence)string, date);
    }

    public static void setIntHeader(HttpMessage httpMessage, String string, Iterable<Integer> iterable) {
        httpMessage.headers().set(string, iterable);
    }

    public static void setDateHeader(HttpMessage httpMessage, CharSequence charSequence, Date date) {
        if (date != null) {
            httpMessage.headers().set(charSequence, (Object)HttpHeaderDateFormat.get().format(date));
        } else {
            httpMessage.headers().set(charSequence, null);
        }
    }

    static void encode(HttpHeaders httpHeaders, ByteBuf byteBuf) {
        if (httpHeaders instanceof DefaultHttpHeaders) {
            ((DefaultHttpHeaders)httpHeaders).encode(byteBuf);
        } else {
            for (Map.Entry entry : httpHeaders) {
                HttpHeaders.encode((CharSequence)entry.getKey(), (CharSequence)entry.getValue(), byteBuf);
            }
        }
    }

    public static String getHeader(HttpMessage httpMessage, String string) {
        return httpMessage.headers().get(string);
    }

    public static void setIntHeader(HttpMessage httpMessage, CharSequence charSequence, int n) {
        httpMessage.headers().set(charSequence, (Object)n);
    }

    public abstract HttpHeaders set(String var1, Iterable<?> var2);

    public static void removeHeader(HttpMessage httpMessage, String string) {
        httpMessage.headers().remove(string);
    }

    public abstract HttpHeaders clear();

    public static void setHost(HttpMessage httpMessage, String string) {
        httpMessage.headers().set(HOST_ENTITY, (Object)string);
    }

    public boolean contains(CharSequence charSequence) {
        return this.contains(charSequence.toString());
    }

    public abstract HttpHeaders set(String var1, Object var2);

    public static String getHeader(HttpMessage httpMessage, CharSequence charSequence) {
        return httpMessage.headers().get(charSequence);
    }

    public HttpHeaders add(CharSequence charSequence, Iterable<?> iterable) {
        return this.add(charSequence.toString(), iterable);
    }

    private static int getWebSocketContentLength(HttpMessage httpMessage) {
        HttpResponse httpResponse;
        HttpHeaders httpHeaders = httpMessage.headers();
        if (httpMessage instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest)httpMessage;
            if (HttpMethod.GET.equals(httpRequest.getMethod()) && httpHeaders.contains(SEC_WEBSOCKET_KEY1_ENTITY) && httpHeaders.contains(SEC_WEBSOCKET_KEY2_ENTITY)) {
                return 8;
            }
        } else if (httpMessage instanceof HttpResponse && (httpResponse = (HttpResponse)httpMessage).getStatus().code() == 101 && httpHeaders.contains(SEC_WEBSOCKET_ORIGIN_ENTITY) && httpHeaders.contains(SEC_WEBSOCKET_LOCATION_ENTITY)) {
            return 16;
        }
        return -1;
    }

    public static int getIntHeader(HttpMessage httpMessage, CharSequence charSequence) {
        String string = HttpHeaders.getHeader(httpMessage, charSequence);
        if (string == null) {
            throw new NumberFormatException("header not found: " + charSequence);
        }
        return Integer.parseInt(string);
    }

    public static void set100ContinueExpected(HttpMessage httpMessage) {
        HttpHeaders.set100ContinueExpected(httpMessage, true);
    }

    public static void set100ContinueExpected(HttpMessage httpMessage, boolean bl) {
        if (bl) {
            httpMessage.headers().set(EXPECT_ENTITY, (Object)CONTINUE_ENTITY);
        } else {
            httpMessage.headers().remove(EXPECT_ENTITY);
        }
    }

    public static int getIntHeader(HttpMessage httpMessage, CharSequence charSequence, int n) {
        String string = HttpHeaders.getHeader(httpMessage, charSequence);
        if (string == null) {
            return n;
        }
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException numberFormatException) {
            return n;
        }
    }

    public static boolean isKeepAlive(HttpMessage httpMessage) {
        String string = httpMessage.headers().get(CONNECTION_ENTITY);
        if (string != null && HttpHeaders.equalsIgnoreCase(CLOSE_ENTITY, string)) {
            return false;
        }
        if (httpMessage.getProtocolVersion().isKeepAliveDefault()) {
            return !HttpHeaders.equalsIgnoreCase(CLOSE_ENTITY, string);
        }
        return HttpHeaders.equalsIgnoreCase(KEEP_ALIVE_ENTITY, string);
    }

    public static Date getDateHeader(HttpMessage httpMessage, CharSequence charSequence) throws ParseException {
        String string = HttpHeaders.getHeader(httpMessage, charSequence);
        if (string == null) {
            throw new ParseException("header not found: " + charSequence, 0);
        }
        return HttpHeaderDateFormat.get().parse(string);
    }

    public static void addDateHeader(HttpMessage httpMessage, CharSequence charSequence, Date date) {
        httpMessage.headers().add(charSequence, (Object)date);
    }

    public static void removeHeader(HttpMessage httpMessage, CharSequence charSequence) {
        httpMessage.headers().remove(charSequence);
    }

    public static void setDate(HttpMessage httpMessage, Date date) {
        if (date != null) {
            httpMessage.headers().set(DATE_ENTITY, (Object)HttpHeaderDateFormat.get().format(date));
        } else {
            httpMessage.headers().set(DATE_ENTITY, null);
        }
    }

    public abstract List<Map.Entry<String, String>> entries();

    public static String getHeader(HttpMessage httpMessage, String string, String string2) {
        return HttpHeaders.getHeader(httpMessage, (CharSequence)string, string2);
    }

    public HttpHeaders set(CharSequence charSequence, Object object) {
        return this.set(charSequence.toString(), object);
    }

    public static void setHeader(HttpMessage httpMessage, String string, Iterable<?> iterable) {
        httpMessage.headers().set(string, iterable);
    }

    public static void addDateHeader(HttpMessage httpMessage, String string, Date date) {
        httpMessage.headers().add(string, (Object)date);
    }

    public static void setHost(HttpMessage httpMessage, CharSequence charSequence) {
        httpMessage.headers().set(HOST_ENTITY, (Object)charSequence);
    }

    public static void setHeader(HttpMessage httpMessage, CharSequence charSequence, Object object) {
        httpMessage.headers().set(charSequence, object);
    }

    public static boolean encodeAscii(CharSequence charSequence, ByteBuf byteBuf) {
        if (charSequence instanceof HttpHeaderEntity) {
            return ((HttpHeaderEntity)charSequence).encode(byteBuf);
        }
        HttpHeaders.encodeAscii0(charSequence, byteBuf);
        return false;
    }

    public static boolean equalsIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == charSequence2) {
            return true;
        }
        if (charSequence == null || charSequence2 == null) {
            return false;
        }
        int n = charSequence.length();
        if (n != charSequence2.length()) {
            return false;
        }
        for (int i = n - 1; i >= 0; --i) {
            char c;
            char c2 = charSequence.charAt(i);
            if (c2 == (c = charSequence2.charAt(i))) continue;
            if (c2 >= 'A' && c2 <= 'Z') {
                c2 = (char)(c2 + 32);
            }
            if (c >= 'A' && c <= 'Z') {
                c = (char)(c + 32);
            }
            if (c2 == c) continue;
            return false;
        }
        return true;
    }

    public static boolean is100ContinueExpected(HttpMessage httpMessage) {
        if (!(httpMessage instanceof HttpRequest)) {
            return false;
        }
        if (httpMessage.getProtocolVersion().compareTo(HttpVersion.HTTP_1_1) < 0) {
            return false;
        }
        String string = httpMessage.headers().get(EXPECT_ENTITY);
        if (string == null) {
            return false;
        }
        if (HttpHeaders.equalsIgnoreCase(CONTINUE_ENTITY, string)) {
            return true;
        }
        return httpMessage.headers().contains(EXPECT_ENTITY, CONTINUE_ENTITY, true);
    }

    public static boolean isContentLengthSet(HttpMessage httpMessage) {
        return httpMessage.headers().contains(CONTENT_LENGTH_ENTITY);
    }

    public abstract HttpHeaders add(String var1, Object var2);

    public static void addIntHeader(HttpMessage httpMessage, String string, int n) {
        httpMessage.headers().add(string, (Object)n);
    }

    public static void setDateHeader(HttpMessage httpMessage, CharSequence charSequence, Iterable<Date> iterable) {
        httpMessage.headers().set(charSequence, iterable);
    }

    public static final class Names {
        public static final String ACCEPT_PATCH = "Accept-Patch";
        public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
        public static final String LOCATION = "Location";
        public static final String UPGRADE = "Upgrade";
        public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
        public static final String LAST_MODIFIED = "Last-Modified";
        public static final String TE = "TE";
        public static final String CONTENT_MD5 = "Content-MD5";
        public static final String TRAILER = "Trailer";
        public static final String AUTHORIZATION = "Authorization";
        public static final String HOST = "Host";
        public static final String SET_COOKIE2 = "Set-Cookie2";
        public static final String CONTENT_LOCATION = "Content-Location";
        public static final String WARNING = "Warning";
        public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
        public static final String FROM = "From";
        public static final String SEC_WEBSOCKET_ORIGIN = "Sec-WebSocket-Origin";
        public static final String ACCEPT_ENCODING = "Accept-Encoding";
        public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
        public static final String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";
        public static final String CONTENT_LANGUAGE = "Content-Language";
        public static final String CONTENT_LENGTH = "Content-Length";
        public static final String RETRY_AFTER = "Retry-After";
        public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
        public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
        public static final String SEC_WEBSOCKET_LOCATION = "Sec-WebSocket-Location";
        public static final String PRAGMA = "Pragma";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String COOKIE = "Cookie";
        public static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
        public static final String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
        public static final String CONTENT_ENCODING = "Content-Encoding";
        public static final String CONTENT_BASE = "Content-Base";
        public static final String CACHE_CONTROL = "Cache-Control";
        public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
        public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
        public static final String ACCEPT = "Accept";
        public static final String IF_MATCH = "If-Match";
        public static final String ACCEPT_CHARSET = "Accept-Charset";
        public static final String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";
        public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
        public static final String DATE = "Date";
        public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
        public static final String ACCEPT_RANGES = "Accept-Ranges";
        public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
        public static final String REFERER = "Referer";
        public static final String EXPECT = "Expect";
        public static final String IF_NONE_MATCH = "If-None-Match";
        public static final String VARY = "Vary";
        public static final String ETAG = "ETag";
        public static final String SEC_WEBSOCKET_KEY2 = "Sec-WebSocket-Key2";
        public static final String WEBSOCKET_ORIGIN = "WebSocket-Origin";
        public static final String VIA = "Via";
        public static final String MAX_FORWARDS = "Max-Forwards";
        public static final String SET_COOKIE = "Set-Cookie";
        public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
        public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
        public static final String CONTENT_RANGE = "Content-Range";
        public static final String IF_RANGE = "If-Range";
        public static final String ORIGIN = "Origin";
        public static final String USER_AGENT = "User-Agent";
        public static final String EXPIRES = "Expires";
        public static final String ACCEPT_LANGUAGE = "Accept-Language";
        public static final String ALLOW = "Allow";
        public static final String AGE = "Age";
        public static final String WEBSOCKET_LOCATION = "WebSocket-Location";
        public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
        public static final String WEBSOCKET_PROTOCOL = "WebSocket-Protocol";
        public static final String CONNECTION = "Connection";
        public static final String SERVER = "Server";
        public static final String SEC_WEBSOCKET_KEY1 = "Sec-WebSocket-Key1";
        public static final String TRANSFER_ENCODING = "Transfer-Encoding";
        public static final String RANGE = "Range";

        private Names() {
        }
    }

    public static final class Values {
        public static final String CLOSE = "close";
        public static final String PUBLIC = "public";
        public static final String GZIP = "gzip";
        public static final String CONTINUE = "100-continue";
        public static final String MIN_FRESH = "min-fresh";
        public static final String MAX_STALE = "max-stale";
        public static final String NO_STORE = "no-store";
        public static final String NO_TRANSFORM = "no-transform";
        public static final String DEFLATE = "deflate";
        public static final String BOUNDARY = "boundary";
        public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
        public static final String TRAILERS = "trailers";
        public static final String NONE = "none";
        public static final String PROXY_REVALIDATE = "proxy-revalidate";
        public static final String S_MAXAGE = "s-maxage";
        public static final String COMPRESS = "compress";
        public static final String MUST_REVALIDATE = "must-revalidate";
        public static final String CHUNKED = "chunked";
        public static final String ONLY_IF_CACHED = "only-if-cached";
        public static final String MAX_AGE = "max-age";
        public static final String BYTES = "bytes";
        public static final String CHARSET = "charset";
        public static final String NO_CACHE = "no-cache";
        public static final String KEEP_ALIVE = "keep-alive";
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";
        public static final String WEBSOCKET = "WebSocket";
        public static final String BASE64 = "base64";
        public static final String IDENTITY = "identity";
        public static final String UPGRADE = "Upgrade";
        public static final String BINARY = "binary";
        public static final String PRIVATE = "private";
        public static final String QUOTED_PRINTABLE = "quoted-printable";

        private Values() {
        }
    }
}

