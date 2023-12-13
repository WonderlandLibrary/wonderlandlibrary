package com.ibm.icu.impl;

public final class Differ {
   private int STACKSIZE;
   private int EQUALSIZE;
   private Object[] a;
   private Object[] b;
   private Object last = null;
   private Object next = null;
   private int aCount = 0;
   private int bCount = 0;
   private int aLine = 1;
   private int bLine = 1;
   private int maxSame = 0;
   private int aTop = 0;
   private int bTop = 0;

   public Differ(int var1, int var2) {
      this.STACKSIZE = var1;
      this.EQUALSIZE = var2;
      this.a = (Object[])(new Object[var1 + var2]);
      this.b = (Object[])(new Object[var1 + var2]);
   }

   public void add(Object var1, Object var2) {
      this.addA(var1);
      this.addB(var2);
   }

   public void addA(Object var1) {
      this.flush();
      this.a[this.aCount++] = var1;
   }

   public void addB(Object var1) {
      this.flush();
      this.b[this.bCount++] = var1;
   }

   public int getALine(int var1) {
      return this.aLine + this.maxSame + var1;
   }

   public Object getA(int var1) {
      if (var1 < 0) {
         return this.last;
      } else {
         return var1 > this.aTop - this.maxSame ? this.next : this.a[var1];
      }
   }

   public int getACount() {
      return this.aTop - this.maxSame;
   }

   public int getBCount() {
      return this.bTop - this.maxSame;
   }

   public int getBLine(int var1) {
      return this.bLine + this.maxSame + var1;
   }

   public Object getB(int var1) {
      if (var1 < 0) {
         return this.last;
      } else {
         return var1 > this.bTop - this.maxSame ? this.next : this.b[var1];
      }
   }

   public void checkMatch(boolean var1) {
      int var2 = this.aCount;
      if (var2 > this.bCount) {
         var2 = this.bCount;
      }

      int var3;
      for(var3 = 0; var3 < var2 && this.a[var3].equals(this.b[var3]); ++var3) {
      }

      this.maxSame = var3;
      this.aTop = this.bTop = this.maxSame;
      if (this.maxSame > 0) {
         this.last = this.a[this.maxSame - 1];
      }

      this.next = null;
      if (var1) {
         this.aTop = this.aCount;
         this.bTop = this.bCount;
         this.next = null;
      } else if (this.aCount - this.maxSame >= this.EQUALSIZE && this.bCount - this.maxSame >= this.EQUALSIZE) {
         int var4 = this.find(this.a, this.aCount - this.EQUALSIZE, this.aCount, this.b, this.maxSame, this.bCount);
         if (var4 != -1) {
            this.aTop = this.aCount - this.EQUALSIZE;
            this.bTop = var4;
            this.next = this.a[this.aTop];
         } else {
            var4 = this.find(this.b, this.bCount - this.EQUALSIZE, this.bCount, this.a, this.maxSame, this.aCount);
            if (var4 != -1) {
               this.bTop = this.bCount - this.EQUALSIZE;
               this.aTop = var4;
               this.next = this.b[this.bTop];
            } else {
               if (this.aCount >= this.STACKSIZE || this.bCount >= this.STACKSIZE) {
                  this.aCount = (this.aCount + this.maxSame) / 2;
                  this.bCount = (this.bCount + this.maxSame) / 2;
                  this.next = null;
               }

            }
         }
      }
   }

   public int find(Object[] var1, int var2, int var3, Object[] var4, int var5, int var6) {
      int var7 = var3 - var2;
      int var8 = var6 - var7;

      label24:
      for(int var9 = var5; var9 <= var8; ++var9) {
         for(int var10 = 0; var10 < var7; ++var10) {
            if (!var4[var9 + var10].equals(var1[var2 + var10])) {
               continue label24;
            }
         }

         return var9;
      }

      return -1;
   }

   private void flush() {
      int var1;
      if (this.aTop != 0) {
         var1 = this.aCount - this.aTop;
         System.arraycopy(this.a, this.aTop, this.a, 0, var1);
         this.aCount = var1;
         this.aLine += this.aTop;
         this.aTop = 0;
      }

      if (this.bTop != 0) {
         var1 = this.bCount - this.bTop;
         System.arraycopy(this.b, this.bTop, this.b, 0, var1);
         this.bCount = var1;
         this.bLine += this.bTop;
         this.bTop = 0;
      }

   }
}
