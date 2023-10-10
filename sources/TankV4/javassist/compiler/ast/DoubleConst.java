package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class DoubleConst extends ASTree {
   protected double value;
   protected int type;

   public DoubleConst(double var1, int var3) {
      this.value = var1;
      this.type = var3;
   }

   public double get() {
      return this.value;
   }

   public void set(double var1) {
      this.value = var1;
   }

   public int getType() {
      return this.type;
   }

   public String toString() {
      return Double.toString(this.value);
   }

   public void accept(Visitor var1) throws CompileError {
      var1.atDoubleConst(this);
   }

   public ASTree compute(int var1, ASTree var2) {
      if (var2 instanceof IntConst) {
         return this.compute0(var1, (IntConst)var2);
      } else {
         return var2 instanceof DoubleConst ? this.compute0(var1, (DoubleConst)var2) : null;
      }
   }

   private DoubleConst compute0(int var1, DoubleConst var2) {
      short var3;
      if (this.type != 405 && var2.type != 405) {
         var3 = 404;
      } else {
         var3 = 405;
      }

      return compute(var1, this.value, var2.value, var3);
   }

   private DoubleConst compute0(int var1, IntConst var2) {
      return compute(var1, this.value, (double)var2.value, this.type);
   }

   private static DoubleConst compute(int var0, double var1, double var3, int var5) {
      double var6;
      switch(var0) {
      case 37:
         var6 = var1 % var3;
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
         var6 = var1 * var3;
         break;
      case 43:
         var6 = var1 + var3;
         break;
      case 45:
         var6 = var1 - var3;
         break;
      case 47:
         var6 = var1 / var3;
      }

      return new DoubleConst(var6, var5);
   }
}
