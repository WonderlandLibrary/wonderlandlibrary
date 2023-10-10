package io.netty.handler.codec.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public final class ServerCookieEncoder {
   public static String encode(String var0, String var1) {
      return encode((Cookie)(new DefaultCookie(var0, var1)));
   }

   public static String encode(Cookie var0) {
      if (var0 == null) {
         throw new NullPointerException("cookie");
      } else {
         StringBuilder var1 = (StringBuilder)CookieEncoderUtil.buffer.get();
         CookieEncoderUtil.add(var1, var0.getName(), var0.getValue());
         if (var0.getMaxAge() != Long.MIN_VALUE) {
            if (var0.getVersion() == 0) {
               CookieEncoderUtil.addUnquoted(var1, "Expires", HttpHeaderDateFormat.get().format(new Date(System.currentTimeMillis() + var0.getMaxAge() * 1000L)));
            } else {
               CookieEncoderUtil.add(var1, "Max-Age", var0.getMaxAge());
            }
         }

         if (var0.getPath() != null) {
            if (var0.getVersion() > 0) {
               CookieEncoderUtil.add(var1, "Path", var0.getPath());
            } else {
               CookieEncoderUtil.addUnquoted(var1, "Path", var0.getPath());
            }
         }

         if (var0.getDomain() != null) {
            if (var0.getVersion() > 0) {
               CookieEncoderUtil.add(var1, "Domain", var0.getDomain());
            } else {
               CookieEncoderUtil.addUnquoted(var1, "Domain", var0.getDomain());
            }
         }

         if (var0.isSecure()) {
            var1.append("Secure");
            var1.append(';');
            var1.append(' ');
         }

         if (var0.isHttpOnly()) {
            var1.append("HTTPOnly");
            var1.append(';');
            var1.append(' ');
         }

         if (var0.getVersion() >= 1) {
            if (var0.getComment() != null) {
               CookieEncoderUtil.add(var1, "Comment", var0.getComment());
            }

            CookieEncoderUtil.add(var1, "Version", 1L);
            if (var0.getCommentUrl() != null) {
               CookieEncoderUtil.addQuoted(var1, "CommentURL", var0.getCommentUrl());
            }

            if (!var0.getPorts().isEmpty()) {
               var1.append("Port");
               var1.append('=');
               var1.append('"');
               Iterator var2 = var0.getPorts().iterator();

               while(var2.hasNext()) {
                  int var3 = (Integer)var2.next();
                  var1.append(var3);
                  var1.append(',');
               }

               var1.setCharAt(var1.length() - 1, '"');
               var1.append(';');
               var1.append(' ');
            }

            if (var0.isDiscard()) {
               var1.append("Discard");
               var1.append(';');
               var1.append(' ');
            }
         }

         return CookieEncoderUtil.stripTrailingSeparator(var1);
      }
   }

   public static List encode(Cookie... var0) {
      if (var0 == null) {
         throw new NullPointerException("cookies");
      } else {
         ArrayList var1 = new ArrayList(var0.length);
         Cookie[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Cookie var5 = var2[var4];
            if (var5 == null) {
               break;
            }

            var1.add(encode(var5));
         }

         return var1;
      }
   }

   public static List encode(Collection var0) {
      if (var0 == null) {
         throw new NullPointerException("cookies");
      } else {
         ArrayList var1 = new ArrayList(var0.size());
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Cookie var3 = (Cookie)var2.next();
            if (var3 == null) {
               break;
            }

            var1.add(encode(var3));
         }

         return var1;
      }
   }

   public static List encode(Iterable var0) {
      if (var0 == null) {
         throw new NullPointerException("cookies");
      } else {
         ArrayList var1 = new ArrayList();
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Cookie var3 = (Cookie)var2.next();
            if (var3 == null) {
               break;
            }

            var1.add(encode(var3));
         }

         return var1;
      }
   }

   private ServerCookieEncoder() {
   }
}
