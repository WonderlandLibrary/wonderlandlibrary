package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.util.UResourceBundle;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

final class DictionaryData {
   public static final int TRIE_TYPE_BYTES = 0;
   public static final int TRIE_TYPE_UCHARS = 1;
   public static final int TRIE_TYPE_MASK = 7;
   public static final int TRIE_HAS_VALUES = 8;
   public static final int TRANSFORM_NONE = 0;
   public static final int TRANSFORM_TYPE_OFFSET = 16777216;
   public static final int TRANSFORM_TYPE_MASK = 2130706432;
   public static final int TRANSFORM_OFFSET_MASK = 2097151;
   public static final int IX_STRING_TRIE_OFFSET = 0;
   public static final int IX_RESERVED1_OFFSET = 1;
   public static final int IX_RESERVED2_OFFSET = 2;
   public static final int IX_TOTAL_SIZE = 3;
   public static final int IX_TRIE_TYPE = 4;
   public static final int IX_TRANSFORM = 5;
   public static final int IX_RESERVED6 = 6;
   public static final int IX_RESERVED7 = 7;
   public static final int IX_COUNT = 8;
   private static final byte[] DATA_FORMAT_ID = new byte[]{68, 105, 99, 116};

   private DictionaryData() {
   }

   public static DictionaryMatcher loadDictionaryFor(String var0) throws IOException {
      ICUResourceBundle var1 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/brkitr");
      String var2 = var1.getStringWithFallback("dictionaries/" + var0);
      var2 = "data/icudt51b/brkitr/" + var2;
      InputStream var3 = ICUData.getStream(var2);
      ICUBinary.readHeader(var3, DATA_FORMAT_ID, (ICUBinary.Authenticate)null);
      DataInputStream var4 = new DataInputStream(var3);
      int[] var5 = new int[8];

      int var6;
      for(var6 = 0; var6 < 8; ++var6) {
         var5[var6] = var4.readInt();
      }

      var6 = var5[0];
      Assert.assrt(var6 >= 32);
      int var7;
      if (var6 > 32) {
         var7 = var6 - 32;
         var4.skipBytes(var7);
      }

      var7 = var5[4] & 7;
      int var8 = var5[3] - var6;
      Object var9 = null;
      int var10;
      int var12;
      if (var7 == 0) {
         var10 = var5[5];
         byte[] var11 = new byte[var8];

         for(var12 = 0; var12 < var11.length; ++var12) {
            var11[var12] = var4.readByte();
         }

         Assert.assrt(var12 == var8);
         var9 = new BytesDictionaryMatcher(var11, var10);
      } else if (var7 == 1) {
         Assert.assrt(var8 % 2 == 0);
         var10 = var8 / 2;
         char[] var13 = new char[var8 / 2];

         for(var12 = 0; var12 < var10; ++var12) {
            var13[var12] = var4.readChar();
         }

         var9 = new CharsDictionaryMatcher(new String(var13));
      } else {
         var9 = null;
      }

      var4.close();
      var3.close();
      return (DictionaryMatcher)var9;
   }
}
