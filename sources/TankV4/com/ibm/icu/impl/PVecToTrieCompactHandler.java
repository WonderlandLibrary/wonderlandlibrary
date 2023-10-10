package com.ibm.icu.impl;

public class PVecToTrieCompactHandler implements PropsVectors.CompactHandler {
   public IntTrieBuilder builder;
   public int initialValue;

   public void setRowIndexForErrorValue(int var1) {
   }

   public void setRowIndexForInitialValue(int var1) {
      this.initialValue = var1;
   }

   public void setRowIndexForRange(int var1, int var2, int var3) {
      this.builder.setRange(var1, var2 + 1, var3, true);
   }

   public void startRealValues(int var1) {
      if (var1 > 65535) {
         throw new IndexOutOfBoundsException();
      } else {
         this.builder = new IntTrieBuilder((int[])null, 100000, this.initialValue, this.initialValue, false);
      }
   }
}
