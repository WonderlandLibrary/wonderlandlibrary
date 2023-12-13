package io.netty.handler.codec.spdy;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class SpdyHeaders implements Iterable {
   public static final SpdyHeaders EMPTY_HEADERS = new SpdyHeaders() {
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

      public SpdyHeaders add(String var1, Object var2) {
         throw new UnsupportedOperationException("read only");
      }

      public SpdyHeaders add(String var1, Iterable var2) {
         throw new UnsupportedOperationException("read only");
      }

      public SpdyHeaders set(String var1, Object var2) {
         throw new UnsupportedOperationException("read only");
      }

      public SpdyHeaders set(String var1, Iterable var2) {
         throw new UnsupportedOperationException("read only");
      }

      public SpdyHeaders remove(String var1) {
         throw new UnsupportedOperationException("read only");
      }

      public SpdyHeaders clear() {
         throw new UnsupportedOperationException("read only");
      }

      public Iterator iterator() {
         return this.entries().iterator();
      }

      public String get(String var1) {
         return null;
      }
   };

   public static String getHeader(SpdyHeadersFrame var0, String var1) {
      return var0.headers().get(var1);
   }

   public static String getHeader(SpdyHeadersFrame var0, String var1, String var2) {
      String var3 = var0.headers().get(var1);
      return var3 == null ? var2 : var3;
   }

   public static void setHeader(SpdyHeadersFrame var0, String var1, Object var2) {
      var0.headers().set(var1, var2);
   }

   public static void setHeader(SpdyHeadersFrame var0, String var1, Iterable var2) {
      var0.headers().set(var1, var2);
   }

   public static void addHeader(SpdyHeadersFrame var0, String var1, Object var2) {
      var0.headers().add(var1, var2);
   }

   public static void removeHost(SpdyHeadersFrame var0) {
      var0.headers().remove(":host");
   }

   public static String getHost(SpdyHeadersFrame var0) {
      return var0.headers().get(":host");
   }

   public static void setHost(SpdyHeadersFrame var0, String var1) {
      var0.headers().set(":host", (Object)var1);
   }

   public static void removeMethod(int var0, SpdyHeadersFrame var1) {
      var1.headers().remove(":method");
   }

   public static HttpMethod getMethod(int var0, SpdyHeadersFrame var1) {
      try {
         return HttpMethod.valueOf(var1.headers().get(":method"));
      } catch (Exception var3) {
         return null;
      }
   }

   public static void setMethod(int var0, SpdyHeadersFrame var1, HttpMethod var2) {
      var1.headers().set(":method", (Object)var2.name());
   }

   public static void removeScheme(int var0, SpdyHeadersFrame var1) {
      var1.headers().remove(":scheme");
   }

   public static String getScheme(int var0, SpdyHeadersFrame var1) {
      return var1.headers().get(":scheme");
   }

   public static void setScheme(int var0, SpdyHeadersFrame var1, String var2) {
      var1.headers().set(":scheme", (Object)var2);
   }

   public static void removeStatus(int var0, SpdyHeadersFrame var1) {
      var1.headers().remove(":status");
   }

   public static HttpResponseStatus getStatus(int var0, SpdyHeadersFrame var1) {
      try {
         String var2 = var1.headers().get(":status");
         int var3 = var2.indexOf(32);
         if (var3 == -1) {
            return HttpResponseStatus.valueOf(Integer.parseInt(var2));
         } else {
            int var4 = Integer.parseInt(var2.substring(0, var3));
            String var5 = var2.substring(var3 + 1);
            HttpResponseStatus var6 = HttpResponseStatus.valueOf(var4);
            return var6.reasonPhrase().equals(var5) ? var6 : new HttpResponseStatus(var4, var5);
         }
      } catch (Exception var7) {
         return null;
      }
   }

   public static void setStatus(int var0, SpdyHeadersFrame var1, HttpResponseStatus var2) {
      var1.headers().set(":status", (Object)var2.toString());
   }

   public static void removeUrl(int var0, SpdyHeadersFrame var1) {
      var1.headers().remove(":path");
   }

   public static String getUrl(int var0, SpdyHeadersFrame var1) {
      return var1.headers().get(":path");
   }

   public static void setUrl(int var0, SpdyHeadersFrame var1, String var2) {
      var1.headers().set(":path", (Object)var2);
   }

   public static void removeVersion(int var0, SpdyHeadersFrame var1) {
      var1.headers().remove(":version");
   }

   public static HttpVersion getVersion(int var0, SpdyHeadersFrame var1) {
      try {
         return HttpVersion.valueOf(var1.headers().get(":version"));
      } catch (Exception var3) {
         return null;
      }
   }

   public static void setVersion(int var0, SpdyHeadersFrame var1, HttpVersion var2) {
      var1.headers().set(":version", (Object)var2.text());
   }

   public Iterator iterator() {
      return this.entries().iterator();
   }

   public abstract String get(String var1);

   public abstract List getAll(String var1);

   public abstract List entries();

   public abstract boolean contains(String var1);

   public abstract Set names();

   public abstract SpdyHeaders add(String var1, Object var2);

   public abstract SpdyHeaders add(String var1, Iterable var2);

   public abstract SpdyHeaders set(String var1, Object var2);

   public abstract SpdyHeaders set(String var1, Iterable var2);

   public abstract SpdyHeaders remove(String var1);

   public abstract SpdyHeaders clear();

   public abstract boolean isEmpty();

   public static final class HttpNames {
      public static final String HOST = ":host";
      public static final String METHOD = ":method";
      public static final String PATH = ":path";
      public static final String SCHEME = ":scheme";
      public static final String STATUS = ":status";
      public static final String VERSION = ":version";

      private HttpNames() {
      }
   }
}
