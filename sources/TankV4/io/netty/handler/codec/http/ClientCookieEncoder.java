package io.netty.handler.codec.http;

import java.util.Iterator;

public final class ClientCookieEncoder {
   public static String encode(String var0, String var1) {
      return encode((Cookie)(new DefaultCookie(var0, var1)));
   }

   public static String encode(Cookie var0) {
      if (var0 == null) {
         throw new NullPointerException("cookie");
      } else {
         StringBuilder var1 = (StringBuilder)CookieEncoderUtil.buffer.get();
         encode(var1, var0);
         return CookieEncoderUtil.stripTrailingSeparator(var1);
      }
   }

   public static String encode(Cookie... var0) {
      if (var0 == null) {
         throw new NullPointerException("cookies");
      } else {
         StringBuilder var1 = (StringBuilder)CookieEncoderUtil.buffer.get();
         Cookie[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Cookie var5 = var2[var4];
            if (var5 == null) {
               break;
            }

            encode(var1, var5);
         }

         return CookieEncoderUtil.stripTrailingSeparator(var1);
      }
   }

   public static String encode(Iterable var0) {
      if (var0 == null) {
         throw new NullPointerException("cookies");
      } else {
         StringBuilder var1 = (StringBuilder)CookieEncoderUtil.buffer.get();
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Cookie var3 = (Cookie)var2.next();
            if (var3 == null) {
               break;
            }

            encode(var1, var3);
         }

         return CookieEncoderUtil.stripTrailingSeparator(var1);
      }
   }

   private static void encode(StringBuilder var0, Cookie var1) {
      if (var1.getVersion() >= 1) {
         CookieEncoderUtil.add(var0, "$Version", 1L);
      }

      CookieEncoderUtil.add(var0, var1.getName(), var1.getValue());
      if (var1.getPath() != null) {
         CookieEncoderUtil.add(var0, "$Path", var1.getPath());
      }

      if (var1.getDomain() != null) {
         CookieEncoderUtil.add(var0, "$Domain", var1.getDomain());
      }

      if (var1.getVersion() >= 1 && !var1.getPorts().isEmpty()) {
         var0.append('$');
         var0.append("Port");
         var0.append('=');
         var0.append('"');
         Iterator var2 = var1.getPorts().iterator();

         while(var2.hasNext()) {
            int var3 = (Integer)var2.next();
            var0.append(var3);
            var0.append(',');
         }

         var0.setCharAt(var0.length() - 1, '"');
         var0.append(';');
         var0.append(' ');
      }

   }

   private ClientCookieEncoder() {
   }
}
