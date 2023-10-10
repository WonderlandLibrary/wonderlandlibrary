package org.apache.commons.compress.archivers.zip;

public final class GeneralPurposeBit {
   private static final int ENCRYPTION_FLAG = 1;
   private static final int SLIDING_DICTIONARY_SIZE_FLAG = 2;
   private static final int NUMBER_OF_SHANNON_FANO_TREES_FLAG = 4;
   private static final int DATA_DESCRIPTOR_FLAG = 8;
   private static final int STRONG_ENCRYPTION_FLAG = 64;
   public static final int UFT8_NAMES_FLAG = 2048;
   private boolean languageEncodingFlag = false;
   private boolean dataDescriptorFlag = false;
   private boolean encryptionFlag = false;
   private boolean strongEncryptionFlag = false;
   private int slidingDictionarySize;
   private int numberOfShannonFanoTrees;

   public boolean usesUTF8ForNames() {
      return this.languageEncodingFlag;
   }

   public void useUTF8ForNames(boolean var1) {
      this.languageEncodingFlag = var1;
   }

   public boolean usesDataDescriptor() {
      return this.dataDescriptorFlag;
   }

   public void useDataDescriptor(boolean var1) {
      this.dataDescriptorFlag = var1;
   }

   public boolean usesEncryption() {
      return this.encryptionFlag;
   }

   public void useEncryption(boolean var1) {
      this.encryptionFlag = var1;
   }

   public boolean usesStrongEncryption() {
      return this.encryptionFlag && this.strongEncryptionFlag;
   }

   public void useStrongEncryption(boolean var1) {
      this.strongEncryptionFlag = var1;
      if (var1) {
         this.useEncryption(true);
      }

   }

   int getSlidingDictionarySize() {
      return this.slidingDictionarySize;
   }

   int getNumberOfShannonFanoTrees() {
      return this.numberOfShannonFanoTrees;
   }

   public byte[] encode() {
      return ZipShort.getBytes((this.dataDescriptorFlag ? 8 : 0) | (this.languageEncodingFlag ? 2048 : 0) | (this.encryptionFlag ? 1 : 0) | (this.strongEncryptionFlag ? 64 : 0));
   }

   public static GeneralPurposeBit parse(byte[] var0, int var1) {
      int var2 = ZipShort.getValue(var0, var1);
      GeneralPurposeBit var3 = new GeneralPurposeBit();
      var3.useDataDescriptor((var2 & 8) != 0);
      var3.useUTF8ForNames((var2 & 2048) != 0);
      var3.useStrongEncryption((var2 & 64) != 0);
      var3.useEncryption((var2 & 1) != 0);
      var3.slidingDictionarySize = (var2 & 2) != 0 ? 8192 : 4096;
      var3.numberOfShannonFanoTrees = (var2 & 4) != 0 ? 3 : 2;
      return var3;
   }

   public int hashCode() {
      return 3 * (7 * (13 * (17 * (this.encryptionFlag ? 1 : 0) + (this.strongEncryptionFlag ? 1 : 0)) + (this.languageEncodingFlag ? 1 : 0)) + (this.dataDescriptorFlag ? 1 : 0));
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof GeneralPurposeBit)) {
         return false;
      } else {
         GeneralPurposeBit var2 = (GeneralPurposeBit)var1;
         return var2.encryptionFlag == this.encryptionFlag && var2.strongEncryptionFlag == this.strongEncryptionFlag && var2.languageEncodingFlag == this.languageEncodingFlag && var2.dataDescriptorFlag == this.dataDescriptorFlag;
      }
   }
}
