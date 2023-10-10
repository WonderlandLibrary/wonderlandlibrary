package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public abstract class HttpHeaders implements Iterable {
   private static final byte[] HEADER_SEPERATOR = new byte[]{58, 32};
   private static final byte[] CRLF = new byte[]{13, 10};
   private static final CharSequence CONTENT_LENGTH_ENTITY = newEntity("Content-Length");
   private static final CharSequence CONNECTION_ENTITY = newEntity("Connection");
   private static final CharSequence CLOSE_ENTITY = newEntity("close");
   private static final CharSequence KEEP_ALIVE_ENTITY = newEntity("keep-alive");
   private static final CharSequence HOST_ENTITY = newEntity("Host");
   private static final CharSequence DATE_ENTITY = newEntity("Date");
   private static final CharSequence EXPECT_ENTITY = newEntity("Expect");
   private static final CharSequence CONTINUE_ENTITY = newEntity("100-continue");
   private static final CharSequence TRANSFER_ENCODING_ENTITY = newEntity("Transfer-Encoding");
   private static final CharSequence CHUNKED_ENTITY = newEntity("chunked");
   private static final CharSequence SEC_WEBSOCKET_KEY1_ENTITY = newEntity("Sec-WebSocket-Key1");
   private static final CharSequence SEC_WEBSOCKET_KEY2_ENTITY = newEntity("Sec-WebSocket-Key2");
   private static final CharSequence SEC_WEBSOCKET_ORIGIN_ENTITY = newEntity("Sec-WebSocket-Origin");
   private static final CharSequence SEC_WEBSOCKET_LOCATION_ENTITY = newEntity("Sec-WebSocket-Location");
   public static final HttpHeaders EMPTY_HEADERS = new HttpHeaders() {
      public String get(String var1) {
         return null;
      }

      public List getAll(String var1) {
         return Collections.emptyList();
      }

      public List entries() {
         return Collections.emptyList();
      }

      public boolean contains(String var1) {
         return false;
      }

      public boolean isEmpty() {
         return true;
      }

      public Set names() {
         return Collections.emptySet();
      }

      public HttpHeaders add(String var1, Object var2) {
         throw new UnsupportedOperationException("read only");
      }

      public HttpHeaders add(String var1, Iterable var2) {
         throw new UnsupportedOperationException("read only");
      }

      public HttpHeaders set(String var1, Object var2) {
         throw new UnsupportedOperationException("read only");
      }

      public HttpHeaders set(String var1, Iterable var2) {
         throw new UnsupportedOperationException("read only");
      }

      public HttpHeaders remove(String var1) {
         throw new UnsupportedOperationException("read only");
      }

      public HttpHeaders clear() {
         throw new UnsupportedOperationException("read only");
      }

      public Iterator iterator() {
         return this.entries().iterator();
      }
   };

   public static boolean isKeepAlive(HttpMessage var0) {
      String var1 = var0.headers().get(CONNECTION_ENTITY);
      if (var1 != null && CLOSE_ENTITY == var1) {
         return false;
      } else if (var0.getProtocolVersion().isKeepAliveDefault()) {
         return CLOSE_ENTITY == var1;
      } else {
         return equalsIgnoreCase(KEEP_ALIVE_ENTITY, var1);
      }
   }

   public static void setKeepAlive(HttpMessage var0, boolean var1) {
      HttpHeaders var2 = var0.headers();
      if (var0.getProtocolVersion().isKeepAliveDefault()) {
         if (var1) {
            var2.remove(CONNECTION_ENTITY);
         } else {
            var2.set((CharSequence)CONNECTION_ENTITY, (Object)CLOSE_ENTITY);
         }
      } else if (var1) {
         var2.set((CharSequence)CONNECTION_ENTITY, (Object)KEEP_ALIVE_ENTITY);
      } else {
         var2.remove(CONNECTION_ENTITY);
      }

   }

   public static String getHeader(HttpMessage var0, String var1) {
      return var0.headers().get(var1);
   }

   public static String getHeader(HttpMessage var0, CharSequence var1) {
      return var0.headers().get(var1);
   }

   public static String getHeader(HttpMessage var0, String var1, String var2) {
      return getHeader(var0, (CharSequence)var1, var2);
   }

   public static String getHeader(HttpMessage var0, CharSequence var1, String var2) {
      String var3 = var0.headers().get(var1);
      return var3 == null ? var2 : var3;
   }

   public static void setHeader(HttpMessage var0, String var1, Object var2) {
      var0.headers().set(var1, var2);
   }

   public static void setHeader(HttpMessage var0, CharSequence var1, Object var2) {
      var0.headers().set(var1, var2);
   }

   public static void setHeader(HttpMessage var0, String var1, Iterable var2) {
      var0.headers().set(var1, var2);
   }

   public static void setHeader(HttpMessage var0, CharSequence var1, Iterable var2) {
      var0.headers().set(var1, var2);
   }

   public static void addHeader(HttpMessage var0, String var1, Object var2) {
      var0.headers().add(var1, var2);
   }

   public static void addHeader(HttpMessage var0, CharSequence var1, Object var2) {
      var0.headers().add(var1, var2);
   }

   public static void removeHeader(HttpMessage var0, String var1) {
      var0.headers().remove(var1);
   }

   public static void removeHeader(HttpMessage var0, CharSequence var1) {
      var0.headers().remove(var1);
   }

   public static void clearHeaders(HttpMessage var0) {
      var0.headers().clear();
   }

   public static int getIntHeader(HttpMessage var0, String var1) {
      return getIntHeader(var0, (CharSequence)var1);
   }

   public static int getIntHeader(HttpMessage var0, CharSequence var1) {
      String var2 = getHeader(var0, var1);
      if (var2 == null) {
         throw new NumberFormatException("header not found: " + var1);
      } else {
         return Integer.parseInt(var2);
      }
   }

   public static int getIntHeader(HttpMessage var0, String var1, int var2) {
      return getIntHeader(var0, (CharSequence)var1, var2);
   }

   public static int getIntHeader(HttpMessage var0, CharSequence var1, int var2) {
      String var3 = getHeader(var0, var1);
      if (var3 == null) {
         return var2;
      } else {
         try {
            return Integer.parseInt(var3);
         } catch (NumberFormatException var5) {
            return var2;
         }
      }
   }

   public static void setIntHeader(HttpMessage var0, String var1, int var2) {
      var0.headers().set((String)var1, (Object)var2);
   }

   public static void setIntHeader(HttpMessage var0, CharSequence var1, int var2) {
      var0.headers().set((CharSequence)var1, (Object)var2);
   }

   public static void setIntHeader(HttpMessage var0, String var1, Iterable var2) {
      var0.headers().set(var1, var2);
   }

   public static void setIntHeader(HttpMessage var0, CharSequence var1, Iterable var2) {
      var0.headers().set(var1, var2);
   }

   public static void addIntHeader(HttpMessage var0, String var1, int var2) {
      var0.headers().add((String)var1, (Object)var2);
   }

   public static void addIntHeader(HttpMessage var0, CharSequence var1, int var2) {
      var0.headers().add((CharSequence)var1, (Object)var2);
   }

   public static Date getDateHeader(HttpMessage var0, String var1) throws ParseException {
      return getDateHeader(var0, (CharSequence)var1);
   }

   public static Date getDateHeader(HttpMessage var0, CharSequence var1) throws ParseException {
      String var2 = getHeader(var0, var1);
      if (var2 == null) {
         throw new ParseException("header not found: " + var1, 0);
      } else {
         return HttpHeaderDateFormat.get().parse(var2);
      }
   }

   public static Date getDateHeader(HttpMessage var0, String var1, Date var2) {
      return getDateHeader(var0, (CharSequence)var1, var2);
   }

   public static Date getDateHeader(HttpMessage var0, CharSequence var1, Date var2) {
      String var3 = getHeader(var0, var1);
      if (var3 == null) {
         return var2;
      } else {
         try {
            return HttpHeaderDateFormat.get().parse(var3);
         } catch (ParseException var5) {
            return var2;
         }
      }
   }

   public static void setDateHeader(HttpMessage var0, String var1, Date var2) {
      setDateHeader(var0, (CharSequence)var1, (Date)var2);
   }

   public static void setDateHeader(HttpMessage var0, CharSequence var1, Date var2) {
      if (var2 != null) {
         var0.headers().set((CharSequence)var1, (Object)HttpHeaderDateFormat.get().format(var2));
      } else {
         var0.headers().set((CharSequence)var1, (Iterable)null);
      }

   }

   public static void setDateHeader(HttpMessage var0, String var1, Iterable var2) {
      var0.headers().set(var1, var2);
   }

   public static void setDateHeader(HttpMessage var0, CharSequence var1, Iterable var2) {
      var0.headers().set(var1, var2);
   }

   public static void addDateHeader(HttpMessage var0, String var1, Date var2) {
      var0.headers().add((String)var1, (Object)var2);
   }

   public static void addDateHeader(HttpMessage var0, CharSequence var1, Date var2) {
      var0.headers().add((CharSequence)var1, (Object)var2);
   }

   public static long getContentLength(HttpMessage var0) {
      String var1 = getHeader(var0, CONTENT_LENGTH_ENTITY);
      if (var1 != null) {
         return Long.parseLong(var1);
      } else {
         long var2 = (long)getWebSocketContentLength(var0);
         if (var2 >= 0L) {
            return var2;
         } else {
            throw new NumberFormatException("header not found: Content-Length");
         }
      }
   }

   public static long getContentLength(HttpMessage var0, long var1) {
      String var3 = var0.headers().get(CONTENT_LENGTH_ENTITY);
      if (var3 != null) {
         try {
            return Long.parseLong(var3);
         } catch (NumberFormatException var6) {
            return var1;
         }
      } else {
         long var4 = (long)getWebSocketContentLength(var0);
         return var4 >= 0L ? var4 : var1;
      }
   }

   private static int getWebSocketContentLength(HttpMessage var0) {
      HttpHeaders var1 = var0.headers();
      if (var0 instanceof HttpRequest) {
         HttpRequest var2 = (HttpRequest)var0;
         if (HttpMethod.GET.equals(var2.getMethod()) && var1.contains(SEC_WEBSOCKET_KEY1_ENTITY) && var1.contains(SEC_WEBSOCKET_KEY2_ENTITY)) {
            return 8;
         }
      } else if (var0 instanceof HttpResponse) {
         HttpResponse var3 = (HttpResponse)var0;
         if (var3.getStatus().code() == 101 && var1.contains(SEC_WEBSOCKET_ORIGIN_ENTITY) && var1.contains(SEC_WEBSOCKET_LOCATION_ENTITY)) {
            return 16;
         }
      }

      return -1;
   }

   public static void setContentLength(HttpMessage var0, long var1) {
      var0.headers().set((CharSequence)CONTENT_LENGTH_ENTITY, (Object)var1);
   }

   public static String getHost(HttpMessage var0) {
      return var0.headers().get(HOST_ENTITY);
   }

   public static String getHost(HttpMessage var0, String var1) {
      return getHeader(var0, HOST_ENTITY, var1);
   }

   public static void setHost(HttpMessage var0, String var1) {
      var0.headers().set((CharSequence)HOST_ENTITY, (Object)var1);
   }

   public static void setHost(HttpMessage var0, CharSequence var1) {
      var0.headers().set((CharSequence)HOST_ENTITY, (Object)var1);
   }

   public static Date getDate(HttpMessage var0) throws ParseException {
      return getDateHeader(var0, DATE_ENTITY);
   }

   public static Date getDate(HttpMessage var0, Date var1) {
      return getDateHeader(var0, DATE_ENTITY, var1);
   }

   public static void setDate(HttpMessage var0, Date var1) {
      if (var1 != null) {
         var0.headers().set((CharSequence)DATE_ENTITY, (Object)HttpHeaderDateFormat.get().format(var1));
      } else {
         var0.headers().set((CharSequence)DATE_ENTITY, (Iterable)null);
      }

   }

   public static boolean is100ContinueExpected(HttpMessage var0) {
      if (!(var0 instanceof HttpRequest)) {
         return false;
      } else if (var0.getProtocolVersion().compareTo(HttpVersion.HTTP_1_1) < 0) {
         return false;
      } else {
         String var1 = var0.headers().get(EXPECT_ENTITY);
         if (var1 == null) {
            return false;
         } else {
            return CONTINUE_ENTITY == var1 ? true : var0.headers().contains(EXPECT_ENTITY, CONTINUE_ENTITY, true);
         }
      }
   }

   public static void set100ContinueExpected(HttpMessage var0) {
      set100ContinueExpected(var0, true);
   }

   public static void set100ContinueExpected(HttpMessage var0, boolean var1) {
      if (var1) {
         var0.headers().set((CharSequence)EXPECT_ENTITY, (Object)CONTINUE_ENTITY);
      } else {
         var0.headers().remove(EXPECT_ENTITY);
      }

   }

   static void validateHeaderName(CharSequence var0) {
      if (var0 == null) {
         throw new NullPointerException("Header names cannot be null");
      } else {
         int var1 = 0;

         while(var1 < var0.length()) {
            char var2 = var0.charAt(var1);
            if (var2 > 127) {
               throw new IllegalArgumentException("Header name cannot contain non-ASCII characters: " + var0);
            }

            switch(var2) {
            case '\t':
            case '\n':
            case '\u000b':
            case '\f':
            case '\r':
            case ' ':
            case ',':
            case ':':
            case ';':
            case '=':
               throw new IllegalArgumentException("Header name cannot contain the following prohibited characters: =,;: \\t\\r\\n\\v\\f: " + var0);
            default:
               ++var1;
            }
         }

      }
   }

   static void validateHeaderValue(CharSequence var0) {
      if (var0 == null) {
         throw new NullPointerException("Header values cannot be null");
      } else {
         byte var1 = 0;

         for(int var2 = 0; var2 < var0.length(); ++var2) {
            char var3 = var0.charAt(var2);
            switch(var3) {
            case '\u000b':
               throw new IllegalArgumentException("Header value contains a prohibited character '\\v': " + var0);
            case '\f':
               throw new IllegalArgumentException("Header value contains a prohibited character '\\f': " + var0);
            }

            switch(var1) {
            case 0:
               switch(var3) {
               case '\n':
                  var1 = 2;
                  continue;
               case '\r':
                  var1 = 1;
               default:
                  continue;
               }
            case 1:
               switch(var3) {
               case '\n':
                  var1 = 2;
                  continue;
               default:
                  throw new IllegalArgumentException("Only '\\n' is allowed after '\\r': " + var0);
               }
            case 2:
               switch(var3) {
               case '\t':
               case ' ':
                  var1 = 0;
                  break;
               default:
                  throw new IllegalArgumentException("Only ' ' and '\\t' are allowed after '\\n': " + var0);
               }
            }
         }

         if (var1 != 0) {
            throw new IllegalArgumentException("Header value must not end with '\\r' or '\\n':" + var0);
         }
      }
   }

   public static boolean isTransferEncodingChunked(HttpMessage var0) {
      return var0.headers().contains(TRANSFER_ENCODING_ENTITY, CHUNKED_ENTITY, true);
   }

   public static void removeTransferEncodingChunked(HttpMessage var0) {
      List var1 = var0.headers().getAll(TRANSFER_ENCODING_ENTITY);
      if (!var1.isEmpty()) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (var3 == CHUNKED_ENTITY) {
               var2.remove();
            }
         }

         if (var1.isEmpty()) {
            var0.headers().remove(TRANSFER_ENCODING_ENTITY);
         } else {
            var0.headers().set((CharSequence)TRANSFER_ENCODING_ENTITY, (Iterable)var1);
         }

      }
   }

   public static void setTransferEncodingChunked(HttpMessage var0) {
      addHeader(var0, (CharSequence)TRANSFER_ENCODING_ENTITY, CHUNKED_ENTITY);
      removeHeader(var0, CONTENT_LENGTH_ENTITY);
   }

   public static boolean isContentLengthSet(HttpMessage var0) {
      return var0.headers().contains(CONTENT_LENGTH_ENTITY);
   }

   static int hash(CharSequence var0) {
      if (var0 instanceof HttpHeaderEntity) {
         return ((HttpHeaderEntity)var0).hash();
      } else {
         int var1 = 0;

         for(int var2 = var0.length() - 1; var2 >= 0; --var2) {
            char var3 = var0.charAt(var2);
            if (var3 >= 'A' && var3 <= 'Z') {
               var3 = (char)(var3 + 32);
            }

            var1 = 31 * var1 + var3;
         }

         if (var1 > 0) {
            return var1;
         } else if (var1 == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
         } else {
            return -var1;
         }
      }
   }

   static void encode(HttpHeaders var0, ByteBuf var1) {
      if (var0 instanceof DefaultHttpHeaders) {
         ((DefaultHttpHeaders)var0).encode(var1);
      } else {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            encode((CharSequence)var3.getKey(), (CharSequence)var3.getValue(), var1);
         }
      }

   }

   static void encode(CharSequence var0, CharSequence var1, ByteBuf var2) {
      encodeAscii(var0, var2);
      var2.writeBytes(HEADER_SEPERATOR);
      encodeAscii(var1, var2);
      var2.writeBytes(CRLF);
   }

   public static void encodeAscii(CharSequence var0, ByteBuf var1) {
      if (var0 instanceof HttpHeaderEntity) {
         ((HttpHeaderEntity)var0).encode(var1);
      } else {
         encodeAscii0(var0, var1);
      }

   }

   static void encodeAscii0(CharSequence var0, ByteBuf var1) {
      int var2 = var0.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         var1.writeByte((byte)var0.charAt(var3));
      }

   }

   public static CharSequence newEntity(String var0) {
      if (var0 == null) {
         throw new NullPointerException("name");
      } else {
         return new HttpHeaderEntity(var0);
      }
   }

   protected HttpHeaders() {
   }

   public abstract String get(String var1);

   public String get(CharSequence var1) {
      return this.get(var1.toString());
   }

   public abstract List getAll(String var1);

   public List getAll(CharSequence var1) {
      return this.getAll(var1.toString());
   }

   public abstract List entries();

   public abstract boolean contains(String var1);

   public boolean contains(CharSequence var1) {
      return this.contains(var1.toString());
   }

   public abstract boolean isEmpty();

   public abstract Set names();

   public abstract HttpHeaders add(String var1, Object var2);

   public HttpHeaders add(CharSequence var1, Object var2) {
      return this.add(var1.toString(), var2);
   }

   public abstract HttpHeaders add(String var1, Iterable var2);

   public HttpHeaders add(CharSequence var1, Iterable var2) {
      return this.add(var1.toString(), var2);
   }

   public HttpHeaders add(HttpHeaders var1) {
      if (var1 == null) {
         throw new NullPointerException("headers");
      } else {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            this.add((String)var3.getKey(), var3.getValue());
         }

         return this;
      }
   }

   public abstract HttpHeaders set(String var1, Object var2);

   public HttpHeaders set(CharSequence var1, Object var2) {
      return this.set(var1.toString(), var2);
   }

   public abstract HttpHeaders set(String var1, Iterable var2);

   public HttpHeaders set(CharSequence var1, Iterable var2) {
      return this.set(var1.toString(), var2);
   }

   public HttpHeaders set(HttpHeaders var1) {
      if (var1 == null) {
         throw new NullPointerException("headers");
      } else {
         this.clear();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            this.add((String)var3.getKey(), var3.getValue());
         }

         return this;
      }
   }

   public abstract HttpHeaders remove(String var1);

   public HttpHeaders remove(CharSequence var1) {
      return this.remove(var1.toString());
   }

   public abstract HttpHeaders clear();

   public boolean contains(String var1, String var2, boolean var3) {
      List var4 = this.getAll(var1);
      if (var4.isEmpty()) {
         return false;
      } else {
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (var3) {
               if (var6 == var2) {
                  return true;
               }
            } else if (var6.equals(var2)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean contains(CharSequence var1, CharSequence var2, boolean var3) {
      return this.contains(var1.toString(), var2.toString(), var3);
   }

   public static final class Values {
      public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
      public static final String BASE64 = "base64";
      public static final String BINARY = "binary";
      public static final String BOUNDARY = "boundary";
      public static final String BYTES = "bytes";
      public static final String CHARSET = "charset";
      public static final String CHUNKED = "chunked";
      public static final String CLOSE = "close";
      public static final String COMPRESS = "compress";
      public static final String CONTINUE = "100-continue";
      public static final String DEFLATE = "deflate";
      public static final String GZIP = "gzip";
      public static final String IDENTITY = "identity";
      public static final String KEEP_ALIVE = "keep-alive";
      public static final String MAX_AGE = "max-age";
      public static final String MAX_STALE = "max-stale";
      public static final String MIN_FRESH = "min-fresh";
      public static final String MULTIPART_FORM_DATA = "multipart/form-data";
      public static final String MUST_REVALIDATE = "must-revalidate";
      public static final String NO_CACHE = "no-cache";
      public static final String NO_STORE = "no-store";
      public static final String NO_TRANSFORM = "no-transform";
      public static final String NONE = "none";
      public static final String ONLY_IF_CACHED = "only-if-cached";
      public static final String PRIVATE = "private";
      public static final String PROXY_REVALIDATE = "proxy-revalidate";
      public static final String PUBLIC = "public";
      public static final String QUOTED_PRINTABLE = "quoted-printable";
      public static final String S_MAXAGE = "s-maxage";
      public static final String TRAILERS = "trailers";
      public static final String UPGRADE = "Upgrade";
      public static final String WEBSOCKET = "WebSocket";

      private Values() {
      }
   }

   public static final class Names {
      public static final String ACCEPT = "Accept";
      public static final String ACCEPT_CHARSET = "Accept-Charset";
      public static final String ACCEPT_ENCODING = "Accept-Encoding";
      public static final String ACCEPT_LANGUAGE = "Accept-Language";
      public static final String ACCEPT_RANGES = "Accept-Ranges";
      public static final String ACCEPT_PATCH = "Accept-Patch";
      public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
      public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
      public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
      public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
      public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
      public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
      public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
      public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
      public static final String AGE = "Age";
      public static final String ALLOW = "Allow";
      public static final String AUTHORIZATION = "Authorization";
      public static final String CACHE_CONTROL = "Cache-Control";
      public static final String CONNECTION = "Connection";
      public static final String CONTENT_BASE = "Content-Base";
      public static final String CONTENT_ENCODING = "Content-Encoding";
      public static final String CONTENT_LANGUAGE = "Content-Language";
      public static final String CONTENT_LENGTH = "Content-Length";
      public static final String CONTENT_LOCATION = "Content-Location";
      public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
      public static final String CONTENT_MD5 = "Content-MD5";
      public static final String CONTENT_RANGE = "Content-Range";
      public static final String CONTENT_TYPE = "Content-Type";
      public static final String COOKIE = "Cookie";
      public static final String DATE = "Date";
      public static final String ETAG = "ETag";
      public static final String EXPECT = "Expect";
      public static final String EXPIRES = "Expires";
      public static final String FROM = "From";
      public static final String HOST = "Host";
      public static final String IF_MATCH = "If-Match";
      public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
      public static final String IF_NONE_MATCH = "If-None-Match";
      public static final String IF_RANGE = "If-Range";
      public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
      public static final String LAST_MODIFIED = "Last-Modified";
      public static final String LOCATION = "Location";
      public static final String MAX_FORWARDS = "Max-Forwards";
      public static final String ORIGIN = "Origin";
      public static final String PRAGMA = "Pragma";
      public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
      public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
      public static final String RANGE = "Range";
      public static final String REFERER = "Referer";
      public static final String RETRY_AFTER = "Retry-After";
      public static final String SEC_WEBSOCKET_KEY1 = "Sec-WebSocket-Key1";
      public static final String SEC_WEBSOCKET_KEY2 = "Sec-WebSocket-Key2";
      public static final String SEC_WEBSOCKET_LOCATION = "Sec-WebSocket-Location";
      public static final String SEC_WEBSOCKET_ORIGIN = "Sec-WebSocket-Origin";
      public static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
      public static final String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";
      public static final String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
      public static final String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";
      public static final String SERVER = "Server";
      public static final String SET_COOKIE = "Set-Cookie";
      public static final String SET_COOKIE2 = "Set-Cookie2";
      public static final String TE = "TE";
      public static final String TRAILER = "Trailer";
      public static final String TRANSFER_ENCODING = "Transfer-Encoding";
      public static final String UPGRADE = "Upgrade";
      public static final String USER_AGENT = "User-Agent";
      public static final String VARY = "Vary";
      public static final String VIA = "Via";
      public static final String WARNING = "Warning";
      public static final String WEBSOCKET_LOCATION = "WebSocket-Location";
      public static final String WEBSOCKET_ORIGIN = "WebSocket-Origin";
      public static final String WEBSOCKET_PROTOCOL = "WebSocket-Protocol";
      public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

      private Names() {
      }
   }
}
