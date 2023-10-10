package com.ibm.icu.impl;

import com.ibm.icu.util.Freezable;

public class Row implements Comparable, Cloneable, Freezable {
   protected Object[] items;
   protected boolean frozen;

   public static Row.R2 of(Object var0, Object var1) {
      return new Row.R2(var0, var1);
   }

   public static Row.R3 of(Object var0, Object var1, Object var2) {
      return new Row.R3(var0, var1, var2);
   }

   public static Row.R4 of(Object var0, Object var1, Object var2, Object var3) {
      return new Row.R4(var0, var1, var2, var3);
   }

   public static Row.R5 of(Object var0, Object var1, Object var2, Object var3, Object var4) {
      return new Row.R5(var0, var1, var2, var3, var4);
   }

   public Row set0(Object var1) {
      return this.set(0, var1);
   }

   public Object get0() {
      return this.items[0];
   }

   public Row set1(Object var1) {
      return this.set(1, var1);
   }

   public Object get1() {
      return this.items[1];
   }

   public Row set2(Object var1) {
      return this.set(2, var1);
   }

   public Object get2() {
      return this.items[2];
   }

   public Row set3(Object var1) {
      return this.set(3, var1);
   }

   public Object get3() {
      return this.items[3];
   }

   public Row set4(Object var1) {
      return this.set(4, var1);
   }

   public Object get4() {
      return this.items[4];
   }

   protected Row set(int var1, Object var2) {
      if (this.frozen) {
         throw new UnsupportedOperationException("Attempt to modify frozen object");
      } else {
         this.items[var1] = var2;
         return this;
      }
   }

   public int hashCode() {
      int var1 = this.items.length;
      Object[] var2 = this.items;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         var1 = var1 * 37 + Utility.checkHash(var5);
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else {
         try {
            Row var2 = (Row)var1;
            if (this.items.length != var2.items.length) {
               return false;
            } else {
               int var3 = 0;
               Object[] var4 = this.items;
               int var5 = var4.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  Object var7 = var4[var6];
                  if (!Utility.objectEquals(var7, var2.items[var3++])) {
                     return false;
                  }
               }

               return true;
            }
         } catch (Exception var8) {
            return false;
         }
      }
   }

   public int compareTo(Object var1) {
      Row var3 = (Row)var1;
      int var2 = this.items.length - var3.items.length;
      if (var2 != 0) {
         return var2;
      } else {
         int var4 = 0;
         Object[] var5 = this.items;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Object var8 = var5[var7];
            var2 = Utility.checkCompare((Comparable)var8, (Comparable)var3.items[var4++]);
            if (var2 != 0) {
               return var2;
            }
         }

         return 0;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("[");
      boolean var2 = true;
      Object[] var3 = this.items;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         if (var2) {
            var2 = false;
         } else {
            var1.append(", ");
         }

         var1.append(var6);
      }

      return var1.append("]").toString();
   }

   public boolean isFrozen() {
      return this.frozen;
   }

   public Row freeze() {
      this.frozen = true;
      return this;
   }

   public Object clone() {
      if (this.frozen) {
         return this;
      } else {
         try {
            Row var1 = (Row)super.clone();
            this.items = (Object[])this.items.clone();
            return var1;
         } catch (CloneNotSupportedException var2) {
            return null;
         }
      }
   }

   public Row cloneAsThawed() {
      try {
         Row var1 = (Row)super.clone();
         this.items = (Object[])this.items.clone();
         var1.frozen = false;
         return var1;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public Object cloneAsThawed() {
      return this.cloneAsThawed();
   }

   public Object freeze() {
      return this.freeze();
   }

   public static class R5 extends Row {
      public R5(Object var1, Object var2, Object var3, Object var4, Object var5) {
         this.items = new Object[]{var1, var2, var3, var4, var5};
      }

      public Object cloneAsThawed() {
         return super.cloneAsThawed();
      }

      public Object freeze() {
         return super.freeze();
      }
   }

   public static class R4 extends Row {
      public R4(Object var1, Object var2, Object var3, Object var4) {
         this.items = new Object[]{var1, var2, var3, var4};
      }

      public Object cloneAsThawed() {
         return super.cloneAsThawed();
      }

      public Object freeze() {
         return super.freeze();
      }
   }

   public static class R3 extends Row {
      public R3(Object var1, Object var2, Object var3) {
         this.items = new Object[]{var1, var2, var3};
      }

      public Object cloneAsThawed() {
         return super.cloneAsThawed();
      }

      public Object freeze() {
         return super.freeze();
      }
   }

   public static class R2 extends Row {
      public R2(Object var1, Object var2) {
         this.items = new Object[]{var1, var2};
      }

      public Object cloneAsThawed() {
         return super.cloneAsThawed();
      }

      public Object freeze() {
         return super.freeze();
      }
   }
}
