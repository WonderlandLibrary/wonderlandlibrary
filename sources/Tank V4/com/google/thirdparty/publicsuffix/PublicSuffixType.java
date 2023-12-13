package com.google.thirdparty.publicsuffix;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
enum PublicSuffixType {
   PRIVATE(':', ','),
   ICANN('!', '?');

   private final char innerNodeCode;
   private final char leafNodeCode;
   private static final PublicSuffixType[] $VALUES = new PublicSuffixType[]{PRIVATE, ICANN};

   private PublicSuffixType(char var3, char var4) {
      this.innerNodeCode = var3;
      this.leafNodeCode = var4;
   }

   char getLeafNodeCode() {
      return this.leafNodeCode;
   }

   char getInnerNodeCode() {
      return this.innerNodeCode;
   }

   static PublicSuffixType fromCode(char var0) {
      PublicSuffixType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PublicSuffixType var4 = var1[var3];
         if (var4.getInnerNodeCode() == var0 || var4.getLeafNodeCode() == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("No enum corresponding to given code: " + var0);
   }

   static PublicSuffixType fromIsPrivate(boolean var0) {
      return var0 ? PRIVATE : ICANN;
   }
}
