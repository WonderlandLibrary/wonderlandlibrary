package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import java.util.HashMap;
import java.util.Map;

public class HttpMethod implements Comparable {
   public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS", true);
   public static final HttpMethod GET = new HttpMethod("GET", true);
   public static final HttpMethod HEAD = new HttpMethod("HEAD", true);
   public static final HttpMethod POST = new HttpMethod("POST", true);
   public static final HttpMethod PUT = new HttpMethod("PUT", true);
   public static final HttpMethod PATCH = new HttpMethod("PATCH", true);
   public static final HttpMethod DELETE = new HttpMethod("DELETE", true);
   public static final HttpMethod TRACE = new HttpMethod("TRACE", true);
   public static final HttpMethod CONNECT = new HttpMethod("CONNECT", true);
   private static final Map methodMap = new HashMap();
   private final String name;
   private final byte[] bytes;

   public static HttpMethod valueOf(String var0) {
      if (var0 == null) {
         throw new NullPointerException("name");
      } else {
         var0 = var0.trim();
         if (var0.isEmpty()) {
            throw new IllegalArgumentException("empty name");
         } else {
            HttpMethod var1 = (HttpMethod)methodMap.get(var0);
            return var1 != null ? var1 : new HttpMethod(var0);
         }
      }
   }

   public HttpMethod(String var1) {
      this(var1, false);
   }

   private HttpMethod(String var1, boolean var2) {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         var1 = var1.trim();
         if (var1.isEmpty()) {
            throw new IllegalArgumentException("empty name");
         } else {
            for(int var3 = 0; var3 < var1.length(); ++var3) {
               if (Character.isISOControl(var1.charAt(var3)) || Character.isWhitespace(var1.charAt(var3))) {
                  throw new IllegalArgumentException("invalid character in name");
               }
            }

            this.name = var1;
            if (var2) {
               this.bytes = var1.getBytes(CharsetUtil.US_ASCII);
            } else {
               this.bytes = null;
            }

         }
      }
   }

   public String name() {
      return this.name;
   }

   public int hashCode() {
      return this.name().hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof HttpMethod)) {
         return false;
      } else {
         HttpMethod var2 = (HttpMethod)var1;
         return this.name().equals(var2.name());
      }
   }

   public String toString() {
      return this.name();
   }

   public int compareTo(HttpMethod var1) {
      return this.name().compareTo(var1.name());
   }

   void encode(ByteBuf var1) {
      if (this.bytes == null) {
         HttpHeaders.encodeAscii0(this.name, var1);
      } else {
         var1.writeBytes(this.bytes);
      }

   }

   public int compareTo(Object var1) {
      return this.compareTo((HttpMethod)var1);
   }

   static {
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
}
