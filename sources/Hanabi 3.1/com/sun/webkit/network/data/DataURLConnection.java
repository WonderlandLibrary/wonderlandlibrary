package com.sun.webkit.network.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedList;

final class DataURLConnection extends URLConnection {
   private static final Charset US_ASCII = Charset.forName("US-ASCII");
   private final String mediaType;
   private final byte[] data;
   private final InputStream inputStream;

   DataURLConnection(URL var1) throws IOException {
      super(var1);
      String var2 = var1.toString();
      var2 = var2.substring(var2.indexOf(58) + 1);
      int var3 = var2.indexOf(44);
      if (var3 < 0) {
         throw new ProtocolException("Invalid URL, ',' not found in: " + this.getURL());
      } else {
         String var4 = var2.substring(0, var3);
         String var5 = var2.substring(var3 + 1);
         String var6 = null;
         LinkedList var7 = new LinkedList();
         Charset var8 = null;
         boolean var9 = false;
         String[] var10 = var4.split(";", -1);

         String var12;
         for(int var11 = 0; var11 < var10.length; ++var11) {
            var12 = var10[var11];
            if (var12.equalsIgnoreCase("base64")) {
               var9 = true;
            } else if (var11 == 0 && !var12.contains("=")) {
               var6 = var12;
            } else {
               var7.add(var12);
               if (var12.toLowerCase().startsWith("charset=")) {
                  try {
                     var8 = Charset.forName(var12.substring(8));
                  } catch (IllegalArgumentException var15) {
                     UnsupportedEncodingException var14 = new UnsupportedEncodingException();
                     var14.initCause(var15);
                     throw var14;
                  }
               }
            }
         }

         if (var6 == null || var6.isEmpty()) {
            var6 = "text/plain";
         }

         if (var8 == null) {
            var8 = US_ASCII;
            if (var6.toLowerCase().startsWith("text/")) {
               var7.addFirst("charset=" + var8.name());
            }
         }

         StringBuilder var16 = new StringBuilder();
         var16.append(var6);
         Iterator var17 = var7.iterator();

         while(var17.hasNext()) {
            String var13 = (String)var17.next();
            var16.append(';').append(var13);
         }

         this.mediaType = var16.toString();
         if (var9) {
            var12 = urlDecode(var5, US_ASCII);
            var12 = var12.replaceAll("\\s+", "");
            this.data = Base64.getMimeDecoder().decode(var12);
         } else {
            var12 = urlDecode(var5, var8);
            this.data = var12.getBytes(var8);
         }

         this.inputStream = new ByteArrayInputStream(this.data);
      }
   }

   public void connect() {
      this.connected = true;
   }

   public InputStream getInputStream() {
      return this.inputStream;
   }

   public String getContentType() {
      return this.mediaType;
   }

   public String getContentEncoding() {
      return null;
   }

   public int getContentLength() {
      return this.data != null ? this.data.length : -1;
   }

   private static String urlDecode(String var0, Charset var1) {
      int var2 = var0.length();
      StringBuilder var3 = new StringBuilder(var2);
      byte[] var4 = null;
      int var5 = 0;

      while(true) {
         while(var5 < var2) {
            char var6 = var0.charAt(var5);
            if (var6 == '%') {
               if (var4 == null) {
                  var4 = new byte[(var2 - var5) / 3];
               }

               int var7 = 0;

               int var8;
               for(var8 = var5; var5 < var2; var5 += 3) {
                  var6 = var0.charAt(var5);
                  if (var6 != '%') {
                     break;
                  }

                  if (var5 + 2 >= var2) {
                     var8 = var2;
                     break;
                  }

                  byte var9;
                  try {
                     var9 = (byte)Integer.parseInt(var0.substring(var5 + 1, var5 + 3), 16);
                  } catch (NumberFormatException var11) {
                     var8 = var5 + 3;
                     break;
                  }

                  var4[var7++] = var9;
               }

               if (var7 > 0) {
                  var3.append(new String(var4, 0, var7, var1));
               }

               while(var5 < var8) {
                  var3.append(var0.charAt(var5++));
               }
            } else {
               var3.append(var6);
               ++var5;
            }
         }

         return var3.toString();
      }
   }
}
