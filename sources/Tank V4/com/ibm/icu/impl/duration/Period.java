package com.ibm.icu.impl.duration;

public final class Period {
   final byte timeLimit;
   final boolean inFuture;
   final int[] counts;

   public static Period at(float var0, TimeUnit var1) {
      checkCount(var0);
      return new Period(0, false, var0, var1);
   }

   public static Period moreThan(float var0, TimeUnit var1) {
      checkCount(var0);
      return new Period(2, false, var0, var1);
   }

   public static Period lessThan(float var0, TimeUnit var1) {
      checkCount(var0);
      return new Period(1, false, var0, var1);
   }

   public Period and(float var1, TimeUnit var2) {
      checkCount(var1);
      return this.setTimeUnitValue(var2, var1);
   }

   public Period omit(TimeUnit var1) {
      return this.setTimeUnitInternalValue(var1, 0);
   }

   public Period at() {
      return this.setTimeLimit((byte)0);
   }

   public Period moreThan() {
      return this.setTimeLimit((byte)2);
   }

   public Period lessThan() {
      return this.setTimeLimit((byte)1);
   }

   public Period inFuture() {
      return this.setFuture(true);
   }

   public Period inPast() {
      return this.setFuture(false);
   }

   public Period inFuture(boolean var1) {
      return this.setFuture(var1);
   }

   public Period inPast(boolean var1) {
      return this.setFuture(!var1);
   }

   public boolean isSet() {
      for(int var1 = 0; var1 < this.counts.length; ++var1) {
         if (this.counts[var1] != 0) {
            return true;
         }
      }

      return false;
   }

   public boolean isSet(TimeUnit var1) {
      return this.counts[var1.ordinal] > 0;
   }

   public float getCount(TimeUnit var1) {
      byte var2 = var1.ordinal;
      return this.counts[var2] == 0 ? 0.0F : (float)(this.counts[var2] - 1) / 1000.0F;
   }

   public boolean isInFuture() {
      return this.inFuture;
   }

   public boolean isInPast() {
      return !this.inFuture;
   }

   public boolean isMoreThan() {
      return this.timeLimit == 2;
   }

   public boolean isLessThan() {
      return this.timeLimit == 1;
   }

   public boolean equals(Object var1) {
      try {
         return this.equals((Period)var1);
      } catch (ClassCastException var3) {
         return false;
      }
   }

   public boolean equals(Period var1) {
      if (var1 != null && this.timeLimit == var1.timeLimit && this.inFuture == var1.inFuture) {
         for(int var2 = 0; var2 < this.counts.length; ++var2) {
            if (this.counts[var2] != var1.counts[var2]) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.timeLimit << 1 | (this.inFuture ? 1 : 0);

      for(int var2 = 0; var2 < this.counts.length; ++var2) {
         var1 = var1 << 2 ^ this.counts[var2];
      }

      return var1;
   }

   private Period(int var1, boolean var2, float var3, TimeUnit var4) {
      this.timeLimit = (byte)var1;
      this.inFuture = var2;
      this.counts = new int[TimeUnit.units.length];
      this.counts[var4.ordinal] = (int)(var3 * 1000.0F) + 1;
   }

   Period(int var1, boolean var2, int[] var3) {
      this.timeLimit = (byte)var1;
      this.inFuture = var2;
      this.counts = var3;
   }

   private Period setTimeUnitValue(TimeUnit var1, float var2) {
      if (var2 < 0.0F) {
         throw new IllegalArgumentException("value: " + var2);
      } else {
         return this.setTimeUnitInternalValue(var1, (int)(var2 * 1000.0F) + 1);
      }
   }

   private Period setTimeUnitInternalValue(TimeUnit var1, int var2) {
      byte var3 = var1.ordinal;
      if (this.counts[var3] == var2) {
         return this;
      } else {
         int[] var4 = new int[this.counts.length];

         for(int var5 = 0; var5 < this.counts.length; ++var5) {
            var4[var5] = this.counts[var5];
         }

         var4[var3] = var2;
         return new Period(this.timeLimit, this.inFuture, var4);
      }
   }

   private Period setFuture(boolean var1) {
      return this.inFuture != var1 ? new Period(this.timeLimit, var1, this.counts) : this;
   }

   private Period setTimeLimit(byte var1) {
      return this.timeLimit != var1 ? new Period(var1, this.inFuture, this.counts) : this;
   }

   private static void checkCount(float var0) {
      if (var0 < 0.0F) {
         throw new IllegalArgumentException("count (" + var0 + ") cannot be negative");
      }
   }
}
