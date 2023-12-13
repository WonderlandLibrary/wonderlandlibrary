package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpVersion implements Comparable {
   private static final Pattern VERSION_PATTERN = Pattern.compile("(\\S+)/(\\d+)\\.(\\d+)");
   private static final String HTTP_1_0_STRING = "HTTP/1.0";
   private static final String HTTP_1_1_STRING = "HTTP/1.1";
   public static final HttpVersion HTTP_1_0 = new HttpVersion("HTTP", 1, 0, false, true);
   public static final HttpVersion HTTP_1_1 = new HttpVersion("HTTP", 1, 1, true, true);
   private final String protocolName;
   private final int majorVersion;
   private final int minorVersion;
   private final String text;
   private final boolean keepAliveDefault;
   private final byte[] bytes;

   public static HttpVersion valueOf(String var0) {
      if (var0 == null) {
         throw new NullPointerException("text");
      } else {
         var0 = var0.trim();
         if (var0.isEmpty()) {
            throw new IllegalArgumentException("text is empty");
         } else {
            HttpVersion var1 = version0(var0);
            if (var1 == null) {
               var0 = var0.toUpperCase();
               var1 = version0(var0);
               if (var1 == null) {
                  var1 = new HttpVersion(var0, true);
               }
            }

            return var1;
         }
      }
   }

   private static HttpVersion version0(String var0) {
      if ("HTTP/1.1".equals(var0)) {
         return HTTP_1_1;
      } else {
         return "HTTP/1.0".equals(var0) ? HTTP_1_0 : null;
      }
   }

   public HttpVersion(String var1, boolean var2) {
      if (var1 == null) {
         throw new NullPointerException("text");
      } else {
         var1 = var1.trim().toUpperCase();
         if (var1.isEmpty()) {
            throw new IllegalArgumentException("empty text");
         } else {
            Matcher var3 = VERSION_PATTERN.matcher(var1);
            if (!var3.matches()) {
               throw new IllegalArgumentException("invalid version format: " + var1);
            } else {
               this.protocolName = var3.group(1);
               this.majorVersion = Integer.parseInt(var3.group(2));
               this.minorVersion = Integer.parseInt(var3.group(3));
               this.text = this.protocolName + '/' + this.majorVersion + '.' + this.minorVersion;
               this.keepAliveDefault = var2;
               this.bytes = null;
            }
         }
      }
   }

   public HttpVersion(String var1, int var2, int var3, boolean var4) {
      this(var1, var2, var3, var4, false);
   }

   private HttpVersion(String var1, int var2, int var3, boolean var4, boolean var5) {
      if (var1 == null) {
         throw new NullPointerException("protocolName");
      } else {
         var1 = var1.trim().toUpperCase();
         if (var1.isEmpty()) {
            throw new IllegalArgumentException("empty protocolName");
         } else {
            for(int var6 = 0; var6 < var1.length(); ++var6) {
               if (Character.isISOControl(var1.charAt(var6)) || Character.isWhitespace(var1.charAt(var6))) {
                  throw new IllegalArgumentException("invalid character in protocolName");
               }
            }

            if (var2 < 0) {
               throw new IllegalArgumentException("negative majorVersion");
            } else if (var3 < 0) {
               throw new IllegalArgumentException("negative minorVersion");
            } else {
               this.protocolName = var1;
               this.majorVersion = var2;
               this.minorVersion = var3;
               this.text = var1 + '/' + var2 + '.' + var3;
               this.keepAliveDefault = var4;
               if (var5) {
                  this.bytes = this.text.getBytes(CharsetUtil.US_ASCII);
               } else {
                  this.bytes = null;
               }

            }
         }
      }
   }

   public String protocolName() {
      return this.protocolName;
   }

   public int majorVersion() {
      return this.majorVersion;
   }

   public int minorVersion() {
      return this.minorVersion;
   }

   public String text() {
      return this.text;
   }

   public boolean isKeepAliveDefault() {
      return this.keepAliveDefault;
   }

   public String toString() {
      return this.text();
   }

   public int hashCode() {
      return (this.protocolName().hashCode() * 31 + this.majorVersion()) * 31 + this.minorVersion();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof HttpVersion)) {
         return false;
      } else {
         HttpVersion var2 = (HttpVersion)var1;
         return this.minorVersion() == var2.minorVersion() && this.majorVersion() == var2.majorVersion() && this.protocolName().equals(var2.protocolName());
      }
   }

   public int compareTo(HttpVersion var1) {
      int var2 = this.protocolName().compareTo(var1.protocolName());
      if (var2 != 0) {
         return var2;
      } else {
         var2 = this.majorVersion() - var1.majorVersion();
         return var2 != 0 ? var2 : this.minorVersion() - var1.minorVersion();
      }
   }

   void encode(ByteBuf var1) {
      if (this.bytes == null) {
         HttpHeaders.encodeAscii0(this.text, var1);
      } else {
         var1.writeBytes(this.bytes);
      }

   }

   public int compareTo(Object var1) {
      return this.compareTo((HttpVersion)var1);
   }
}
