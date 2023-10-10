package com.ibm.icu.text;

public class BidiRun {
   int start;
   int limit;
   int insertRemove;
   byte level;

   BidiRun() {
      this(0, 0, (byte)0);
   }

   BidiRun(int var1, int var2, byte var3) {
      this.start = var1;
      this.limit = var2;
      this.level = var3;
   }

   void copyFrom(BidiRun var1) {
      this.start = var1.start;
      this.limit = var1.limit;
      this.level = var1.level;
      this.insertRemove = var1.insertRemove;
   }

   public int getStart() {
      return this.start;
   }

   public int getLimit() {
      return this.limit;
   }

   public int getLength() {
      return this.limit - this.start;
   }

   public byte getEmbeddingLevel() {
      return this.level;
   }

   public boolean isOddRun() {
      return (this.level & 1) == 1;
   }

   public boolean isEvenRun() {
      return (this.level & 1) == 0;
   }

   public byte getDirection() {
      return (byte)(this.level & 1);
   }

   public String toString() {
      return "BidiRun " + this.start + " - " + this.limit + " @ " + this.level;
   }
}
