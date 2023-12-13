package com.google.zxing.oned.rss;

public class DataCharacter {
   private final int value;
   private final int checksumPortion;

   public DataCharacter(int value, int checksumPortion) {
      this.value = value;
      this.checksumPortion = checksumPortion;
   }

   public final int getValue() {
      return this.value;
   }

   public final int getChecksumPortion() {
      return this.checksumPortion;
   }
}
