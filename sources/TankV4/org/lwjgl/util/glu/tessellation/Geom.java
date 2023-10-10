package org.lwjgl.util.glu.tessellation;

class Geom {
   static final boolean $assertionsDisabled = !Geom.class.desiredAssertionStatus();

   private Geom() {
   }

   static double EdgeEval(GLUvertex var0, GLUvertex var1, GLUvertex var2) {
      if (!$assertionsDisabled && (!(var1 >= 0) || var2 >= 0)) {
         throw new AssertionError();
      } else {
         double var3 = var1.s - var0.s;
         double var5 = var2.s - var1.s;
         if (var3 + var5 > 0.0D) {
            return var3 < var5 ? var1.t - var0.t + (var0.t - var2.t) * (var3 / (var3 + var5)) : var1.t - var2.t + (var2.t - var0.t) * (var5 / (var3 + var5));
         } else {
            return 0.0D;
         }
      }
   }

   static double EdgeSign(GLUvertex var0, GLUvertex var1, GLUvertex var2) {
      if ($assertionsDisabled || var1 >= 0 && !(var2 >= 0)) {
         double var3 = var1.s - var0.s;
         double var5 = var2.s - var1.s;
         return var3 + var5 > 0.0D ? (var1.t - var2.t) * var3 + (var1.t - var0.t) * var5 : 0.0D;
      } else {
         throw new AssertionError();
      }
   }

   static double TransEval(GLUvertex var0, GLUvertex var1, GLUvertex var2) {
      if (!$assertionsDisabled && (!(var1 >= 0) || var2 >= 0)) {
         throw new AssertionError();
      } else {
         double var3 = var1.t - var0.t;
         double var5 = var2.t - var1.t;
         if (var3 + var5 > 0.0D) {
            return var3 < var5 ? var1.s - var0.s + (var0.s - var2.s) * (var3 / (var3 + var5)) : var1.s - var2.s + (var2.s - var0.s) * (var5 / (var3 + var5));
         } else {
            return 0.0D;
         }
      }
   }

   static double TransSign(GLUvertex var0, GLUvertex var1, GLUvertex var2) {
      if ($assertionsDisabled || var1 >= 0 && !(var2 >= 0)) {
         double var3 = var1.t - var0.t;
         double var5 = var2.t - var1.t;
         return var3 + var5 > 0.0D ? (var1.s - var2.s) * var3 + (var1.s - var0.s) * var5 : 0.0D;
      } else {
         throw new AssertionError();
      }
   }

   static boolean VertCCW(GLUvertex var0, GLUvertex var1, GLUvertex var2) {
      return var0.s * (var1.t - var2.t) + var1.s * (var2.t - var0.t) + var2.s * (var0.t - var1.t) >= 0.0D;
   }

   static double Interpolate(double var0, double var2, double var4, double var6) {
      var0 = var0 < 0.0D ? 0.0D : var0;
      var4 = var4 < 0.0D ? 0.0D : var4;
      if (var0 <= var4) {
         return var4 == 0.0D ? (var2 + var6) / 2.0D : var2 + (var6 - var2) * (var0 / (var0 + var4));
      } else {
         return var6 + (var2 - var6) * (var4 / (var0 + var4));
      }
   }

   static void EdgeIntersect(GLUvertex var0, GLUvertex var1, GLUvertex var2, GLUvertex var3, GLUvertex var4) {
      GLUvertex var9;
      if (var1 >= 0) {
         var9 = var0;
         var0 = var1;
         var1 = var9;
      }

      if (var3 >= 0) {
         var9 = var2;
         var2 = var3;
         var3 = var9;
      }

      if (var2 >= 0) {
         var9 = var0;
         var0 = var2;
         var2 = var9;
         var9 = var1;
         var1 = var3;
         var3 = var9;
      }

      double var5;
      double var7;
      if (var1 >= 0) {
         var4.s = (var2.s + var1.s) / 2.0D;
      } else if (var3 >= 0) {
         var5 = EdgeEval(var0, var2, var1);
         var7 = EdgeEval(var2, var1, var3);
         if (var5 + var7 < 0.0D) {
            var5 = -var5;
            var7 = -var7;
         }

         var4.s = Interpolate(var5, var2.s, var7, var1.s);
      } else {
         var5 = EdgeSign(var0, var2, var1);
         var7 = -EdgeSign(var0, var3, var1);
         if (var5 + var7 < 0.0D) {
            var5 = -var5;
            var7 = -var7;
         }

         var4.s = Interpolate(var5, var2.s, var7, var3.s);
      }

      if (var1 >= 0) {
         var9 = var0;
         var0 = var1;
         var1 = var9;
      }

      if (var3 >= 0) {
         var9 = var2;
         var2 = var3;
         var3 = var9;
      }

      if (var2 >= 0) {
         var9 = var2;
         var2 = var0;
         var0 = var9;
         var9 = var3;
         var3 = var1;
         var1 = var9;
      }

      if (var1 >= 0) {
         var4.t = (var2.t + var1.t) / 2.0D;
      } else if (var3 >= 0) {
         var5 = TransEval(var0, var2, var1);
         var7 = TransEval(var2, var1, var3);
         if (var5 + var7 < 0.0D) {
            var5 = -var5;
            var7 = -var7;
         }

         var4.t = Interpolate(var5, var2.t, var7, var1.t);
      } else {
         var5 = TransSign(var0, var2, var1);
         var7 = -TransSign(var0, var3, var1);
         if (var5 + var7 < 0.0D) {
            var5 = -var5;
            var7 = -var7;
         }

         var4.t = Interpolate(var5, var2.t, var7, var3.t);
      }

   }

   static boolean VertEq(GLUvertex var0, GLUvertex var1) {
      return var0.s == var1.s && var0.t == var1.t;
   }

   static boolean EdgeGoesLeft(GLUhalfEdge var0) {
      return VertLeq(var0.Sym.Org, var0.Org);
   }

   static boolean EdgeGoesRight(GLUhalfEdge var0) {
      return VertLeq(var0.Org, var0.Sym.Org);
   }

   static double VertL1dist(GLUvertex var0, GLUvertex var1) {
      return Math.abs(var0.s - var1.s) + Math.abs(var0.t - var1.t);
   }
}
