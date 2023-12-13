package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class IntConst extends ASTree {
   protected long value;
   protected int type;

   public IntConst(long var1, int var3) {
      this.value = var1;
      this.type = var3;
   }

   public long get() {
      return this.value;
   }

   public void set(long var1) {
      this.value = var1;
   }

   public int getType() {
      return this.type;
   }

   public String toString() {
      return Long.toString(this.value);
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atIntConst(this);
   }

   public ASTree compute(int var1, ASTree var2) {
      if (var2 instanceof IntConst) {
         return this.compute0(var1, (IntConst)var2);
      } else {
         return var2 instanceof DoubleConst ? this.compute0(var1, (DoubleConst)var2) : null;
      }
   }

   private IntConst compute0(int var1, IntConst var2) {
      int var3 = this.type;
      int var4 = var2.type;
      int var5;
      if (var3 != 403 && var4 != 403) {
         if (var3 == 401 && var4 == 401) {
            var5 = 401;
         } else {
            var5 = 402;
         }
      } else {
         var5 = 403;
      }

      long var6 = this.value;
      long var8 = var2.value;
      long var10;
      switch(var1) {
      case 37:
         var10 = var6 % var8;
         break;
      case 38:
         var10 = var6 & var8;
         break;
      case 42:
         var10 = var6 * var8;
         break;
      case 43:
         var10 = var6 + var8;
         break;
      case 45:
         var10 = var6 - var8;
         break;
      case 47:
         var10 = var6 / var8;
         break;
      case 94:
         var10 = var6 ^ var8;
         break;
      case 124:
         var10 = var6 | var8;
         break;
      case 364:
         var10 = this.value << (int)var8;
         var5 = var3;
         break;
      case 366:
         var10 = this.value >> (int)var8;
         var5 = var3;
         break;
      case 370:
         var10 = this.value >>> (int)var8;
         var5 = var3;
         break;
      default:
         return null;
      }

      return new IntConst(var10, var5);
   }

   private DoubleConst compute0(int var1, DoubleConst var2) {
      double var3 = (double)this.value;
      double var5 = var2.value;
      double var7;
      switch(var1) {
      case 37:
         var7 = var3 % var5;
         break;
      case 38:
      case 39:
      case 40:
      case 41:
      case 44:
      case 46:
      default:
         return null;
      case 42:
         var7 = var3 * var5;
         break;
      case 43:
         var7 = var3 + var5;
         break;
      case 45:
         var7 = var3 - var5;
         break;
      case 47:
         var7 = var3 / var5;
      }

      return new DoubleConst(var7, var2.type);
   }
}
