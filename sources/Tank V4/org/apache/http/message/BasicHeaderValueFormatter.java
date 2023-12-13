package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicHeaderValueFormatter implements HeaderValueFormatter {
   /** @deprecated */
   @Deprecated
   public static final BasicHeaderValueFormatter DEFAULT = new BasicHeaderValueFormatter();
   public static final BasicHeaderValueFormatter INSTANCE = new BasicHeaderValueFormatter();
   public static final String SEPARATORS = " ;,:@()<>\\\"/[]?={}\t";
   public static final String UNSAFE_CHARS = "\"\\";

   public static String formatElements(HeaderElement[] var0, boolean var1, HeaderValueFormatter var2) {
      return ((HeaderValueFormatter)(var2 != null ? var2 : INSTANCE)).formatElements((CharArrayBuffer)null, var0, var1).toString();
   }

   public CharArrayBuffer formatElements(CharArrayBuffer var1, HeaderElement[] var2, boolean var3) {
      Args.notNull(var2, "Header element array");
      int var4 = this.estimateElementsLen(var2);
      CharArrayBuffer var5 = var1;
      if (var1 == null) {
         var5 = new CharArrayBuffer(var4);
      } else {
         var1.ensureCapacity(var4);
      }

      for(int var6 = 0; var6 < var2.length; ++var6) {
         if (var6 > 0) {
            var5.append(", ");
         }

         this.formatHeaderElement(var5, var2[var6], var3);
      }

      return var5;
   }

   protected int estimateElementsLen(HeaderElement[] var1) {
      if (var1 != null && var1.length >= 1) {
         int var2 = (var1.length - 1) * 2;
         HeaderElement[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            HeaderElement var6 = var3[var5];
            var2 += this.estimateHeaderElementLen(var6);
         }

         return var2;
      } else {
         return 0;
      }
   }

   public static String formatHeaderElement(HeaderElement var0, boolean var1, HeaderValueFormatter var2) {
      return ((HeaderValueFormatter)(var2 != null ? var2 : INSTANCE)).formatHeaderElement((CharArrayBuffer)null, var0, var1).toString();
   }

   public CharArrayBuffer formatHeaderElement(CharArrayBuffer var1, HeaderElement var2, boolean var3) {
      Args.notNull(var2, "Header element");
      int var4 = this.estimateHeaderElementLen(var2);
      CharArrayBuffer var5 = var1;
      if (var1 == null) {
         var5 = new CharArrayBuffer(var4);
      } else {
         var1.ensureCapacity(var4);
      }

      var5.append(var2.getName());
      String var6 = var2.getValue();
      if (var6 != null) {
         var5.append('=');
         this.doFormatValue(var5, var6, var3);
      }

      int var7 = var2.getParameterCount();
      if (var7 > 0) {
         for(int var8 = 0; var8 < var7; ++var8) {
            var5.append("; ");
            this.formatNameValuePair(var5, var2.getParameter(var8), var3);
         }
      }

      return var5;
   }

   protected int estimateHeaderElementLen(HeaderElement var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = var1.getName().length();
         String var3 = var1.getValue();
         if (var3 != null) {
            var2 += 3 + var3.length();
         }

         int var4 = var1.getParameterCount();
         if (var4 > 0) {
            for(int var5 = 0; var5 < var4; ++var5) {
               var2 += 2 + this.estimateNameValuePairLen(var1.getParameter(var5));
            }
         }

         return var2;
      }
   }

   public static String formatParameters(NameValuePair[] var0, boolean var1, HeaderValueFormatter var2) {
      return ((HeaderValueFormatter)(var2 != null ? var2 : INSTANCE)).formatParameters((CharArrayBuffer)null, var0, var1).toString();
   }

   public CharArrayBuffer formatParameters(CharArrayBuffer var1, NameValuePair[] var2, boolean var3) {
      Args.notNull(var2, "Header parameter array");
      int var4 = this.estimateParametersLen(var2);
      CharArrayBuffer var5 = var1;
      if (var1 == null) {
         var5 = new CharArrayBuffer(var4);
      } else {
         var1.ensureCapacity(var4);
      }

      for(int var6 = 0; var6 < var2.length; ++var6) {
         if (var6 > 0) {
            var5.append("; ");
         }

         this.formatNameValuePair(var5, var2[var6], var3);
      }

      return var5;
   }

   protected int estimateParametersLen(NameValuePair[] var1) {
      if (var1 != null && var1.length >= 1) {
         int var2 = (var1.length - 1) * 2;
         NameValuePair[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            NameValuePair var6 = var3[var5];
            var2 += this.estimateNameValuePairLen(var6);
         }

         return var2;
      } else {
         return 0;
      }
   }

   public static String formatNameValuePair(NameValuePair var0, boolean var1, HeaderValueFormatter var2) {
      return ((HeaderValueFormatter)(var2 != null ? var2 : INSTANCE)).formatNameValuePair((CharArrayBuffer)null, var0, var1).toString();
   }

   public CharArrayBuffer formatNameValuePair(CharArrayBuffer var1, NameValuePair var2, boolean var3) {
      Args.notNull(var2, "Name / value pair");
      int var4 = this.estimateNameValuePairLen(var2);
      CharArrayBuffer var5 = var1;
      if (var1 == null) {
         var5 = new CharArrayBuffer(var4);
      } else {
         var1.ensureCapacity(var4);
      }

      var5.append(var2.getName());
      String var6 = var2.getValue();
      if (var6 != null) {
         var5.append('=');
         this.doFormatValue(var5, var6, var3);
      }

      return var5;
   }

   protected int estimateNameValuePairLen(NameValuePair var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = var1.getName().length();
         String var3 = var1.getValue();
         if (var3 != null) {
            var2 += 3 + var3.length();
         }

         return var2;
      }
   }

   protected void doFormatValue(CharArrayBuffer var1, String var2, boolean var3) {
      boolean var4 = var3;
      int var5;
      if (!var3) {
         for(var5 = 0; var5 < var2.length() && !var4; ++var5) {
            var4 = this.isSeparator(var2.charAt(var5));
         }
      }

      if (var4) {
         var1.append('"');
      }

      for(var5 = 0; var5 < var2.length(); ++var5) {
         char var6 = var2.charAt(var5);
         if (var6 >= 0) {
            var1.append('\\');
         }

         var1.append(var6);
      }

      if (var4) {
         var1.append('"');
      }

   }

   protected boolean isSeparator(char var1) {
      return " ;,:@()<>\\\"/[]?={}\t".indexOf(var1) >= 0;
   }
}
