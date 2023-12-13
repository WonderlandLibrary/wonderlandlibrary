package org.apache.http.client.utils;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

@Immutable
public class URLEncodedUtils {
   public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
   private static final char QP_SEP_A = '&';
   private static final char QP_SEP_S = ';';
   private static final String NAME_VALUE_SEPARATOR = "=";
   private static final char[] QP_SEPS = new char[]{'&', ';'};
   private static final String QP_SEP_PATTERN;
   private static final BitSet UNRESERVED;
   private static final BitSet PUNCT;
   private static final BitSet USERINFO;
   private static final BitSet PATHSAFE;
   private static final BitSet URIC;
   private static final BitSet RESERVED;
   private static final BitSet URLENCODER;
   private static final int RADIX = 16;

   public static List parse(URI var0, String var1) {
      String var2 = var0.getRawQuery();
      if (var2 != null && var2.length() > 0) {
         ArrayList var3 = new ArrayList();
         Scanner var4 = new Scanner(var2);
         parse(var3, var4, QP_SEP_PATTERN, var1);
         return var3;
      } else {
         return Collections.emptyList();
      }
   }

   public static List parse(HttpEntity var0) throws IOException {
      ContentType var1 = ContentType.get(var0);
      if (var1 != null && var1.getMimeType().equalsIgnoreCase("application/x-www-form-urlencoded")) {
         String var2 = EntityUtils.toString(var0, Consts.ASCII);
         if (var2 != null && var2.length() > 0) {
            Charset var3 = var1.getCharset();
            if (var3 == null) {
               var3 = HTTP.DEF_CONTENT_CHARSET;
            }

            return parse(var2, var3, QP_SEPS);
         }
      }

      return Collections.emptyList();
   }

   public static boolean isEncoded(HttpEntity var0) {
      Header var1 = var0.getContentType();
      if (var1 != null) {
         HeaderElement[] var2 = var1.getElements();
         if (var2.length > 0) {
            String var3 = var2[0].getName();
            return var3.equalsIgnoreCase("application/x-www-form-urlencoded");
         }
      }

      return false;
   }

   public static void parse(List var0, Scanner var1, String var2) {
      parse(var0, var1, QP_SEP_PATTERN, var2);
   }

   public static void parse(List var0, Scanner var1, String var2, String var3) {
      var1.useDelimiter(var2);

      String var4;
      String var5;
      for(; var1.hasNext(); var0.add(new BasicNameValuePair(var4, var5))) {
         var4 = null;
         var5 = null;
         String var6 = var1.next();
         int var7 = var6.indexOf("=");
         if (var7 != -1) {
            var4 = decodeFormFields(var6.substring(0, var7).trim(), var3);
            var5 = decodeFormFields(var6.substring(var7 + 1).trim(), var3);
         } else {
            var4 = decodeFormFields(var6.trim(), var3);
         }
      }

   }

   public static List parse(String var0, Charset var1) {
      return parse(var0, var1, QP_SEPS);
   }

   public static List parse(String var0, Charset var1, char... var2) {
      if (var0 == null) {
         return Collections.emptyList();
      } else {
         BasicHeaderValueParser var3 = BasicHeaderValueParser.INSTANCE;
         CharArrayBuffer var4 = new CharArrayBuffer(var0.length());
         var4.append(var0);
         ParserCursor var5 = new ParserCursor(0, var4.length());
         ArrayList var6 = new ArrayList();

         while(!var5.atEnd()) {
            NameValuePair var7 = var3.parseNameValuePair(var4, var5, var2);
            if (var7.getName().length() > 0) {
               var6.add(new BasicNameValuePair(decodeFormFields(var7.getName(), var1), decodeFormFields(var7.getValue(), var1)));
            }
         }

         return var6;
      }
   }

   public static String format(List var0, String var1) {
      return format(var0, '&', var1);
   }

   public static String format(List var0, char var1, String var2) {
      StringBuilder var3 = new StringBuilder();
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         NameValuePair var5 = (NameValuePair)var4.next();
         String var6 = encodeFormFields(var5.getName(), var2);
         String var7 = encodeFormFields(var5.getValue(), var2);
         if (var3.length() > 0) {
            var3.append(var1);
         }

         var3.append(var6);
         if (var7 != null) {
            var3.append("=");
            var3.append(var7);
         }
      }

      return var3.toString();
   }

   public static String format(Iterable var0, Charset var1) {
      return format(var0, '&', var1);
   }

   public static String format(Iterable var0, char var1, Charset var2) {
      StringBuilder var3 = new StringBuilder();
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         NameValuePair var5 = (NameValuePair)var4.next();
         String var6 = encodeFormFields(var5.getName(), var2);
         String var7 = encodeFormFields(var5.getValue(), var2);
         if (var3.length() > 0) {
            var3.append(var1);
         }

         var3.append(var6);
         if (var7 != null) {
            var3.append("=");
            var3.append(var7);
         }
      }

      return var3.toString();
   }

   private static String urlEncode(String var0, Charset var1, BitSet var2, boolean var3) {
      if (var0 == null) {
         return null;
      } else {
         StringBuilder var4 = new StringBuilder();
         ByteBuffer var5 = var1.encode(var0);

         while(true) {
            while(var5.hasRemaining()) {
               int var6 = var5.get() & 255;
               if (var2.get(var6)) {
                  var4.append((char)var6);
               } else if (var3 && var6 == 32) {
                  var4.append('+');
               } else {
                  var4.append("%");
                  char var7 = Character.toUpperCase(Character.forDigit(var6 >> 4 & 15, 16));
                  char var8 = Character.toUpperCase(Character.forDigit(var6 & 15, 16));
                  var4.append(var7);
                  var4.append(var8);
               }
            }

            return var4.toString();
         }
      }
   }

   private static String urlDecode(String var0, Charset var1, boolean var2) {
      if (var0 == null) {
         return null;
      } else {
         ByteBuffer var3 = ByteBuffer.allocate(var0.length());
         CharBuffer var4 = CharBuffer.wrap(var0);

         while(true) {
            while(true) {
               while(var4.hasRemaining()) {
                  char var5 = var4.get();
                  if (var5 == '%' && var4.remaining() >= 2) {
                     char var6 = var4.get();
                     char var7 = var4.get();
                     int var8 = Character.digit(var6, 16);
                     int var9 = Character.digit(var7, 16);
                     if (var8 != -1 && var9 != -1) {
                        var3.put((byte)((var8 << 4) + var9));
                     } else {
                        var3.put((byte)37);
                        var3.put((byte)var6);
                        var3.put((byte)var7);
                     }
                  } else if (var2 && var5 == '+') {
                     var3.put((byte)32);
                  } else {
                     var3.put((byte)var5);
                  }
               }

               var3.flip();
               return var1.decode(var3).toString();
            }
         }
      }
   }

   private static String decodeFormFields(String var0, String var1) {
      return var0 == null ? null : urlDecode(var0, var1 != null ? Charset.forName(var1) : Consts.UTF_8, true);
   }

   private static String decodeFormFields(String var0, Charset var1) {
      return var0 == null ? null : urlDecode(var0, var1 != null ? var1 : Consts.UTF_8, true);
   }

   private static String encodeFormFields(String var0, String var1) {
      return var0 == null ? null : urlEncode(var0, var1 != null ? Charset.forName(var1) : Consts.UTF_8, URLENCODER, true);
   }

   private static String encodeFormFields(String var0, Charset var1) {
      return var0 == null ? null : urlEncode(var0, var1 != null ? var1 : Consts.UTF_8, URLENCODER, true);
   }

   static String encUserInfo(String var0, Charset var1) {
      return urlEncode(var0, var1, USERINFO, false);
   }

   static String encUric(String var0, Charset var1) {
      return urlEncode(var0, var1, URIC, false);
   }

   static String encPath(String var0, Charset var1) {
      return urlEncode(var0, var1, PATHSAFE, false);
   }

   static {
      QP_SEP_PATTERN = "[" + new String(QP_SEPS) + "]";
      UNRESERVED = new BitSet(256);
      PUNCT = new BitSet(256);
      USERINFO = new BitSet(256);
      PATHSAFE = new BitSet(256);
      URIC = new BitSet(256);
      RESERVED = new BitSet(256);
      URLENCODER = new BitSet(256);

      int var0;
      for(var0 = 97; var0 <= 122; ++var0) {
         UNRESERVED.set(var0);
      }

      for(var0 = 65; var0 <= 90; ++var0) {
         UNRESERVED.set(var0);
      }

      for(var0 = 48; var0 <= 57; ++var0) {
         UNRESERVED.set(var0);
      }

      UNRESERVED.set(95);
      UNRESERVED.set(45);
      UNRESERVED.set(46);
      UNRESERVED.set(42);
      URLENCODER.or(UNRESERVED);
      UNRESERVED.set(33);
      UNRESERVED.set(126);
      UNRESERVED.set(39);
      UNRESERVED.set(40);
      UNRESERVED.set(41);
      PUNCT.set(44);
      PUNCT.set(59);
      PUNCT.set(58);
      PUNCT.set(36);
      PUNCT.set(38);
      PUNCT.set(43);
      PUNCT.set(61);
      USERINFO.or(UNRESERVED);
      USERINFO.or(PUNCT);
      PATHSAFE.or(UNRESERVED);
      PATHSAFE.set(47);
      PATHSAFE.set(59);
      PATHSAFE.set(58);
      PATHSAFE.set(64);
      PATHSAFE.set(38);
      PATHSAFE.set(61);
      PATHSAFE.set(43);
      PATHSAFE.set(36);
      PATHSAFE.set(44);
      RESERVED.set(59);
      RESERVED.set(47);
      RESERVED.set(63);
      RESERVED.set(58);
      RESERVED.set(64);
      RESERVED.set(38);
      RESERVED.set(61);
      RESERVED.set(43);
      RESERVED.set(36);
      RESERVED.set(44);
      RESERVED.set(91);
      RESERVED.set(93);
      URIC.or(RESERVED);
      URIC.or(UNRESERVED);
   }
}
