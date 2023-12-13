package org.apache.commons.compress.archivers.sevenz;

import java.util.Arrays;

public enum SevenZMethod {
   COPY(new byte[]{0}),
   LZMA(new byte[]{3, 1, 1}),
   LZMA2(new byte[]{33}),
   DEFLATE(new byte[]{4, 1, 8}),
   BZIP2(new byte[]{4, 2, 2}),
   AES256SHA256(new byte[]{6, -15, 7, 1}),
   BCJ_X86_FILTER(new byte[]{3, 3, 1, 3}),
   BCJ_PPC_FILTER(new byte[]{3, 3, 2, 5}),
   BCJ_IA64_FILTER(new byte[]{3, 3, 4, 1}),
   BCJ_ARM_FILTER(new byte[]{3, 3, 5, 1}),
   BCJ_ARM_THUMB_FILTER(new byte[]{3, 3, 7, 1}),
   BCJ_SPARC_FILTER(new byte[]{3, 3, 8, 5}),
   DELTA_FILTER(new byte[]{3});

   private final byte[] id;
   private static final SevenZMethod[] $VALUES = new SevenZMethod[]{COPY, LZMA, LZMA2, DEFLATE, BZIP2, AES256SHA256, BCJ_X86_FILTER, BCJ_PPC_FILTER, BCJ_IA64_FILTER, BCJ_ARM_FILTER, BCJ_ARM_THUMB_FILTER, BCJ_SPARC_FILTER, DELTA_FILTER};

   private SevenZMethod(byte[] var3) {
      this.id = var3;
   }

   byte[] getId() {
      byte[] var1 = new byte[this.id.length];
      System.arraycopy(this.id, 0, var1, 0, this.id.length);
      return var1;
   }

   static SevenZMethod byId(byte[] var0) {
      SevenZMethod[] var1 = (SevenZMethod[])SevenZMethod.class.getEnumConstants();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SevenZMethod var4 = var1[var3];
         if (Arrays.equals(var4.id, var0)) {
            return var4;
         }
      }

      return null;
   }
}
