package org.apache.http.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;

public final class EntityUtils {
   private EntityUtils() {
   }

   public static void consumeQuietly(HttpEntity var0) {
      try {
         consume(var0);
      } catch (IOException var2) {
      }

   }

   public static void consume(HttpEntity var0) throws IOException {
      if (var0 != null) {
         if (var0.isStreaming()) {
            InputStream var1 = var0.getContent();
            if (var1 != null) {
               var1.close();
            }
         }

      }
   }

   public static void updateEntity(HttpResponse var0, HttpEntity var1) throws IOException {
      Args.notNull(var0, "Response");
      consume(var0.getEntity());
      var0.setEntity(var1);
   }

   public static byte[] toByteArray(HttpEntity var0) throws IOException {
      Args.notNull(var0, "Entity");
      InputStream var1 = var0.getContent();
      if (var1 == null) {
         return null;
      } else {
         Args.check(var0.getContentLength() <= 2147483647L, "HTTP entity too large to be buffered in memory");
         int var2 = (int)var0.getContentLength();
         if (var2 < 0) {
            var2 = 4096;
         }

         ByteArrayBuffer var3 = new ByteArrayBuffer(var2);
         byte[] var4 = new byte[4096];

         int var5;
         while((var5 = var1.read(var4)) != -1) {
            var3.append((byte[])var4, 0, var5);
         }

         byte[] var6 = var3.toByteArray();
         var1.close();
         return var6;
      }
   }

   /** @deprecated */
   @Deprecated
   public static String getContentCharSet(HttpEntity var0) throws ParseException {
      Args.notNull(var0, "Entity");
      String var1 = null;
      if (var0.getContentType() != null) {
         HeaderElement[] var2 = var0.getContentType().getElements();
         if (var2.length > 0) {
            NameValuePair var3 = var2[0].getParameterByName("charset");
            if (var3 != null) {
               var1 = var3.getValue();
            }
         }
      }

      return var1;
   }

   /** @deprecated */
   @Deprecated
   public static String getContentMimeType(HttpEntity var0) throws ParseException {
      Args.notNull(var0, "Entity");
      String var1 = null;
      if (var0.getContentType() != null) {
         HeaderElement[] var2 = var0.getContentType().getElements();
         if (var2.length > 0) {
            var1 = var2[0].getName();
         }
      }

      return var1;
   }

   public static String toString(HttpEntity var0, Charset var1) throws IOException, ParseException {
      Args.notNull(var0, "Entity");
      InputStream var2 = var0.getContent();
      if (var2 == null) {
         return null;
      } else {
         Args.check(var0.getContentLength() <= 2147483647L, "HTTP entity too large to be buffered in memory");
         int var3 = (int)var0.getContentLength();
         if (var3 < 0) {
            var3 = 4096;
         }

         Charset var4 = null;

         try {
            ContentType var5 = ContentType.get(var0);
            if (var5 != null) {
               var4 = var5.getCharset();
            }
         } catch (UnsupportedCharsetException var11) {
            throw new UnsupportedEncodingException(var11.getMessage());
         }

         if (var4 == null) {
            var4 = var1;
         }

         if (var4 == null) {
            var4 = HTTP.DEF_CONTENT_CHARSET;
         }

         InputStreamReader var12 = new InputStreamReader(var2, var4);
         CharArrayBuffer var6 = new CharArrayBuffer(var3);
         char[] var7 = new char[1024];

         int var8;
         while((var8 = var12.read(var7)) != -1) {
            var6.append((char[])var7, 0, var8);
         }

         String var9 = var6.toString();
         var2.close();
         return var9;
      }
   }

   public static String toString(HttpEntity var0, String var1) throws IOException, ParseException {
      return toString(var0, var1 != null ? Charset.forName(var1) : null);
   }

   public static String toString(HttpEntity var0) throws IOException, ParseException {
      return toString(var0, (Charset)null);
   }
}
