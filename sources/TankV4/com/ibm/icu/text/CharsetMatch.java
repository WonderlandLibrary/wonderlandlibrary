package com.ibm.icu.text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class CharsetMatch implements Comparable {
   private int fConfidence;
   private byte[] fRawInput = null;
   private int fRawLength;
   private InputStream fInputStream = null;
   private String fCharsetName;
   private String fLang;

   public Reader getReader() {
      Object var1 = this.fInputStream;
      if (var1 == null) {
         var1 = new ByteArrayInputStream(this.fRawInput, 0, this.fRawLength);
      }

      try {
         ((InputStream)var1).reset();
         return new InputStreamReader((InputStream)var1, this.getName());
      } catch (IOException var3) {
         return null;
      }
   }

   public String getString() throws IOException {
      return this.getString(-1);
   }

   public String getString(int var1) throws IOException {
      String var2 = null;
      if (this.fInputStream == null) {
         String var8 = this.getName();
         int var9 = var8.indexOf("_rtl") < 0 ? var8.indexOf("_ltr") : var8.indexOf("_rtl");
         if (var9 > 0) {
            var8 = var8.substring(0, var9);
         }

         var2 = new String(this.fRawInput, var8);
         return var2;
      } else {
         StringBuilder var3 = new StringBuilder();
         char[] var4 = new char[1024];
         Reader var5 = this.getReader();
         int var6 = var1 < 0 ? Integer.MAX_VALUE : var1;

         int var10;
         for(boolean var7 = false; (var10 = var5.read(var4, 0, Math.min(var6, 1024))) >= 0; var6 -= var10) {
            var3.append(var4, 0, var10);
         }

         var5.close();
         return var3.toString();
      }
   }

   public int getConfidence() {
      return this.fConfidence;
   }

   public String getName() {
      return this.fCharsetName;
   }

   public String getLanguage() {
      return this.fLang;
   }

   public int compareTo(CharsetMatch var1) {
      byte var2 = 0;
      if (this.fConfidence > var1.fConfidence) {
         var2 = 1;
      } else if (this.fConfidence < var1.fConfidence) {
         var2 = -1;
      }

      return var2;
   }

   CharsetMatch(CharsetDetector var1, CharsetRecognizer var2, int var3) {
      this.fConfidence = var3;
      if (var1.fInputStream == null) {
         this.fRawInput = var1.fRawInput;
         this.fRawLength = var1.fRawLength;
      }

      this.fInputStream = var1.fInputStream;
      this.fCharsetName = var2.getName();
      this.fLang = var2.getLanguage();
   }

   CharsetMatch(CharsetDetector var1, CharsetRecognizer var2, int var3, String var4, String var5) {
      this.fConfidence = var3;
      if (var1.fInputStream == null) {
         this.fRawInput = var1.fRawInput;
         this.fRawLength = var1.fRawLength;
      }

      this.fInputStream = var1.fInputStream;
      this.fCharsetName = var4;
      this.fLang = var5;
   }

   public int compareTo(Object var1) {
      return this.compareTo((CharsetMatch)var1);
   }
}
