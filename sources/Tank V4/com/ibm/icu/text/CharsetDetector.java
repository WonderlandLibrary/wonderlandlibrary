package com.ibm.icu.text;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class CharsetDetector {
   private static final int kBufSize = 8000;
   byte[] fInputBytes = new byte[8000];
   int fInputLen;
   short[] fByteStats = new short[256];
   boolean fC1Bytes = false;
   String fDeclaredEncoding;
   byte[] fRawInput;
   int fRawLength;
   InputStream fInputStream;
   boolean fStripTags = false;
   private static ArrayList fCSRecognizers = createRecognizers();
   private static String[] fCharsetNames;

   public CharsetDetector setDeclaredEncoding(String var1) {
      this.fDeclaredEncoding = var1;
      return this;
   }

   public CharsetDetector setText(byte[] var1) {
      this.fRawInput = var1;
      this.fRawLength = var1.length;
      return this;
   }

   public CharsetDetector setText(InputStream var1) throws IOException {
      this.fInputStream = var1;
      this.fInputStream.mark(8000);
      this.fRawInput = new byte[8000];
      this.fRawLength = 0;

      int var3;
      for(int var2 = 8000; var2 > 0; var2 -= var3) {
         var3 = this.fInputStream.read(this.fRawInput, this.fRawLength, var2);
         if (var3 <= 0) {
            break;
         }

         this.fRawLength += var3;
      }

      this.fInputStream.reset();
      return this;
   }

   public CharsetMatch detect() {
      CharsetMatch[] var1 = this.detectAll();
      return var1 != null && var1.length != 0 ? var1[0] : null;
   }

   public CharsetMatch[] detectAll() {
      ArrayList var1 = new ArrayList();
      this.MungeInput();
      Iterator var2 = fCSRecognizers.iterator();

      while(var2.hasNext()) {
         CharsetRecognizer var3 = (CharsetRecognizer)var2.next();
         CharsetMatch var4 = var3.match(this);
         if (var4 != null) {
            var1.add(var4);
         }
      }

      Collections.sort(var1);
      Collections.reverse(var1);
      CharsetMatch[] var5 = new CharsetMatch[var1.size()];
      var5 = (CharsetMatch[])var1.toArray(var5);
      return var5;
   }

   public Reader getReader(InputStream var1, String var2) {
      this.fDeclaredEncoding = var2;

      try {
         this.setText(var1);
         CharsetMatch var3 = this.detect();
         return var3 == null ? null : var3.getReader();
      } catch (IOException var4) {
         return null;
      }
   }

   public String getString(byte[] var1, String var2) {
      this.fDeclaredEncoding = var2;

      try {
         this.setText(var1);
         CharsetMatch var3 = this.detect();
         return var3 == null ? null : var3.getString(-1);
      } catch (IOException var4) {
         return null;
      }
   }

   public static String[] getAllDetectableCharsets() {
      return fCharsetNames;
   }

   public boolean inputFilterEnabled() {
      return this.fStripTags;
   }

   public boolean enableInputFilter(boolean var1) {
      boolean var2 = this.fStripTags;
      this.fStripTags = var1;
      return var2;
   }

   private void MungeInput() {
      boolean var1 = false;
      int var2 = 0;
      boolean var4 = false;
      int var5 = 0;
      int var6 = 0;
      int var8;
      if (this.fStripTags) {
         for(var8 = 0; var8 < this.fRawLength && var2 < this.fInputBytes.length; ++var8) {
            byte var3 = this.fRawInput[var8];
            if (var3 == 60) {
               if (var4) {
                  ++var6;
               }

               var4 = true;
               ++var5;
            }

            if (!var4) {
               this.fInputBytes[var2++] = var3;
            }

            if (var3 == 62) {
               var4 = false;
            }
         }

         this.fInputLen = var2;
      }

      int var7;
      if (var5 < 5 || var5 / 5 < var6 || this.fInputLen < 100 && this.fRawLength > 600) {
         var7 = this.fRawLength;
         if (var7 > 8000) {
            var7 = 8000;
         }

         for(var8 = 0; var8 < var7; ++var8) {
            this.fInputBytes[var8] = this.fRawInput[var8];
         }

         this.fInputLen = var8;
      }

      Arrays.fill(this.fByteStats, (short)0);

      for(var8 = 0; var8 < this.fInputLen; ++var8) {
         var7 = this.fInputBytes[var8] & 255;
         ++this.fByteStats[var7];
      }

      this.fC1Bytes = false;

      for(var7 = 128; var7 <= 159; ++var7) {
         if (this.fByteStats[var7] != 0) {
            this.fC1Bytes = true;
            break;
         }
      }

   }

   private static ArrayList createRecognizers() {
      ArrayList var0 = new ArrayList();
      var0.add(new CharsetRecog_UTF8());
      var0.add(new CharsetRecog_Unicode.CharsetRecog_UTF_16_BE());
      var0.add(new CharsetRecog_Unicode.CharsetRecog_UTF_16_LE());
      var0.add(new CharsetRecog_Unicode.CharsetRecog_UTF_32_BE());
      var0.add(new CharsetRecog_Unicode.CharsetRecog_UTF_32_LE());
      var0.add(new CharsetRecog_mbcs.CharsetRecog_sjis());
      var0.add(new CharsetRecog_2022.CharsetRecog_2022JP());
      var0.add(new CharsetRecog_2022.CharsetRecog_2022CN());
      var0.add(new CharsetRecog_2022.CharsetRecog_2022KR());
      var0.add(new CharsetRecog_mbcs.CharsetRecog_gb_18030());
      var0.add(new CharsetRecog_mbcs.CharsetRecog_euc.CharsetRecog_euc_jp());
      var0.add(new CharsetRecog_mbcs.CharsetRecog_euc.CharsetRecog_euc_kr());
      var0.add(new CharsetRecog_mbcs.CharsetRecog_big5());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_8859_1());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_8859_2());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_8859_5_ru());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_8859_6_ar());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_8859_7_el());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_8859_8_I_he());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_8859_8_he());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_windows_1251());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_windows_1256());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_KOI8_R());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_8859_9_tr());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_IBM424_he_rtl());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_IBM424_he_ltr());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_IBM420_ar_rtl());
      var0.add(new CharsetRecog_sbcs.CharsetRecog_IBM420_ar_ltr());
      String[] var1 = new String[var0.size()];
      int var2 = 0;

      for(int var3 = 0; var3 < var0.size(); ++var3) {
         String var4 = ((CharsetRecognizer)var0.get(var3)).getName();
         if (var2 == 0 || !var4.equals(var1[var2 - 1])) {
            var1[var2++] = var4;
         }
      }

      fCharsetNames = new String[var2];
      System.arraycopy(var1, 0, fCharsetNames, 0, var2);
      return var0;
   }
}
