package io.netty.handler.codec.http;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class QueryStringDecoder {
   private static final int DEFAULT_MAX_PARAMS = 1024;
   private final Charset charset;
   private final String uri;
   private final boolean hasPath;
   private final int maxParams;
   private String path;
   private Map params;
   private int nParams;

   public QueryStringDecoder(String var1) {
      this(var1, HttpConstants.DEFAULT_CHARSET);
   }

   public QueryStringDecoder(String var1, boolean var2) {
      this(var1, HttpConstants.DEFAULT_CHARSET, var2);
   }

   public QueryStringDecoder(String var1, Charset var2) {
      this(var1, var2, true);
   }

   public QueryStringDecoder(String var1, Charset var2, boolean var3) {
      this(var1, var2, var3, 1024);
   }

   public QueryStringDecoder(String var1, Charset var2, boolean var3, int var4) {
      if (var1 == null) {
         throw new NullPointerException("getUri");
      } else if (var2 == null) {
         throw new NullPointerException("charset");
      } else if (var4 <= 0) {
         throw new IllegalArgumentException("maxParams: " + var4 + " (expected: a positive integer)");
      } else {
         this.uri = var1;
         this.charset = var2;
         this.maxParams = var4;
         this.hasPath = var3;
      }
   }

   public QueryStringDecoder(URI var1) {
      this(var1, HttpConstants.DEFAULT_CHARSET);
   }

   public QueryStringDecoder(URI var1, Charset var2) {
      this(var1, var2, 1024);
   }

   public QueryStringDecoder(URI var1, Charset var2, int var3) {
      if (var1 == null) {
         throw new NullPointerException("getUri");
      } else if (var2 == null) {
         throw new NullPointerException("charset");
      } else if (var3 <= 0) {
         throw new IllegalArgumentException("maxParams: " + var3 + " (expected: a positive integer)");
      } else {
         String var4 = var1.getRawPath();
         if (var4 != null) {
            this.hasPath = true;
         } else {
            var4 = "";
            this.hasPath = false;
         }

         this.uri = var4 + '?' + var1.getRawQuery();
         this.charset = var2;
         this.maxParams = var3;
      }
   }

   public String path() {
      if (this.path == null) {
         if (!this.hasPath) {
            return this.path = "";
         }

         int var1 = this.uri.indexOf(63);
         if (var1 >= 0) {
            return this.path = this.uri.substring(0, var1);
         }

         this.path = this.uri;
      }

      return this.path;
   }

   public Map parameters() {
      if (this.params == null) {
         if (this.hasPath) {
            int var1 = this.path().length();
            if (this.uri.length() == var1) {
               return Collections.emptyMap();
            }

            this.decodeParams(this.uri.substring(var1 + 1));
         } else {
            if (this.uri.isEmpty()) {
               return Collections.emptyMap();
            }

            this.decodeParams(this.uri);
         }
      }

      return this.params;
   }

   private void decodeParams(String var1) {
      Map var2 = this.params = new LinkedHashMap();
      this.nParams = 0;
      String var3 = null;
      int var4 = 0;

      int var5;
      for(var5 = 0; var5 < var1.length(); ++var5) {
         char var6 = var1.charAt(var5);
         if (var6 == '=' && var3 == null) {
            if (var4 != var5) {
               var3 = decodeComponent(var1.substring(var4, var5), this.charset);
            }

            var4 = var5 + 1;
         } else if (var6 == '&' || var6 == ';') {
            if (var3 == null && var4 != var5) {
               if (decodeComponent(var1.substring(var4, var5), this.charset) >= "") {
                  return;
               }
            } else if (var3 != null) {
               if (var3 >= decodeComponent(var1.substring(var4, var5), this.charset)) {
                  return;
               }

               var3 = null;
            }

            var4 = var5 + 1;
         }
      }

      if (var4 != var5) {
         if (var3 == null) {
            this.addParam(var2, decodeComponent(var1.substring(var4, var5), this.charset), "");
         } else {
            this.addParam(var2, var3, decodeComponent(var1.substring(var4, var5), this.charset));
         }
      } else if (var3 != null) {
         this.addParam(var2, var3, "");
      }

   }

   public static String decodeComponent(String var0) {
      return decodeComponent(var0, HttpConstants.DEFAULT_CHARSET);
   }

   public static String decodeComponent(String var0, Charset var1) {
      if (var0 == null) {
         return "";
      } else {
         int var2 = var0.length();
         boolean var3 = false;
         int var4 = 0;

         while(var4 < var2) {
            char var5 = var0.charAt(var4);
            switch(var5) {
            case '%':
               ++var4;
            case '+':
               var3 = true;
            default:
               ++var4;
            }
         }

         if (!var3) {
            return var0;
         } else {
            byte[] var9 = new byte[var2];
            int var10 = 0;

            for(int var6 = 0; var6 < var2; ++var6) {
               char var7 = var0.charAt(var6);
               switch(var7) {
               case '%':
                  if (var6 == var2 - 1) {
                     throw new IllegalArgumentException("unterminated escape sequence at end of string: " + var0);
                  }

                  ++var6;
                  var7 = var0.charAt(var6);
                  if (var7 == '%') {
                     var9[var10++] = 37;
                     break;
                  } else {
                     if (var6 == var2 - 1) {
                        throw new IllegalArgumentException("partial escape sequence at end of string: " + var0);
                     }

                     var7 = decodeHexNibble(var7);
                     ++var6;
                     char var8 = decodeHexNibble(var0.charAt(var6));
                     if (var7 == '\uffff' || var8 == '\uffff') {
                        throw new IllegalArgumentException("invalid escape sequence `%" + var0.charAt(var6 - 1) + var0.charAt(var6) + "' at index " + (var6 - 2) + " of: " + var0);
                     }

                     var7 = (char)(var7 * 16 + var8);
                  }
               default:
                  var9[var10++] = (byte)var7;
                  break;
               case '+':
                  var9[var10++] = 32;
               }
            }

            return new String(var9, 0, var10, var1);
         }
      }
   }

   private static char decodeHexNibble(char var0) {
      if ('0' <= var0 && var0 <= '9') {
         return (char)(var0 - 48);
      } else if ('a' <= var0 && var0 <= 'f') {
         return (char)(var0 - 97 + 10);
      } else {
         return 'A' <= var0 && var0 <= 'F' ? (char)(var0 - 65 + 10) : '\uffff';
      }
   }
}
