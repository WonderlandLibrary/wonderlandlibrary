package org.lwjgl.util.glu.tessellation;

class Normal {
   static boolean SLANTED_SWEEP;
   static double S_UNIT_X;
   static double S_UNIT_Y;
   private static final boolean TRUE_PROJECT = false;
   static final boolean $assertionsDisabled = !Normal.class.desiredAssertionStatus();

   private Normal() {
   }

   private static double Dot(double[] var0, double[] var1) {
      return var0[0] * var1[0] + var0[1] * var1[1] + var0[2] * var1[2];
   }

   static void Normalize(double[] var0) {
      double var1 = var0[0] * var0[0] + var0[1] * var0[1] + var0[2] * var0[2];
      if (!$assertionsDisabled && !(var1 > 0.0D)) {
         throw new AssertionError();
      } else {
         var1 = Math.sqrt(var1);
         var0[0] /= var1;
         var0[1] /= var1;
         var0[2] /= var1;
      }
   }

   static int LongAxis(double[] var0) {
      byte var1 = 0;
      if (Math.abs(var0[1]) > Math.abs(var0[0])) {
         var1 = 1;
      }

      if (Math.abs(var0[2]) > Math.abs(var0[var1])) {
         var1 = 2;
      }

      return var1;
   }

   static void ComputeNormal(GLUtessellatorImpl var0, double[] var1) {
      GLUvertex var18 = var0.mesh.vHead;
      double[] var11 = new double[3];
      double[] var12 = new double[3];
      GLUvertex[] var17 = new GLUvertex[3];
      GLUvertex[] var16 = new GLUvertex[3];
      double[] var13 = new double[3];
      double[] var14 = new double[3];
      double[] var15 = new double[3];
      var11[0] = var11[1] = var11[2] = -2.0E150D;
      var12[0] = var12[1] = var12[2] = 2.0E150D;

      GLUvertex var2;
      for(var2 = var18.next; var2 != var18; var2 = var2.next) {
         for(int var19 = 0; var19 < 3; ++var19) {
            double var5 = var2.coords[var19];
            if (var5 < var12[var19]) {
               var12[var19] = var5;
               var17[var19] = var2;
            }

            if (var5 > var11[var19]) {
               var11[var19] = var5;
               var16[var19] = var2;
            }
         }
      }

      byte var20 = 0;
      if (var11[1] - var12[1] > var11[0] - var12[0]) {
         var20 = 1;
      }

      if (var11[2] - var12[2] > var11[var20] - var12[var20]) {
         var20 = 2;
      }

      if (var12[var20] >= var11[var20]) {
         var1[0] = 0.0D;
         var1[1] = 0.0D;
         var1[2] = 1.0D;
      } else {
         double var9 = 0.0D;
         GLUvertex var3 = var17[var20];
         GLUvertex var4 = var16[var20];
         var13[0] = var3.coords[0] - var4.coords[0];
         var13[1] = var3.coords[1] - var4.coords[1];
         var13[2] = var3.coords[2] - var4.coords[2];

         for(var2 = var18.next; var2 != var18; var2 = var2.next) {
            var14[0] = var2.coords[0] - var4.coords[0];
            var14[1] = var2.coords[1] - var4.coords[1];
            var14[2] = var2.coords[2] - var4.coords[2];
            var15[0] = var13[1] * var14[2] - var13[2] * var14[1];
            var15[1] = var13[2] * var14[0] - var13[0] * var14[2];
            var15[2] = var13[0] * var14[1] - var13[1] * var14[0];
            double var7 = var15[0] * var15[0] + var15[1] * var15[1] + var15[2] * var15[2];
            if (var7 > var9) {
               var9 = var7;
               var1[0] = var15[0];
               var1[1] = var15[1];
               var1[2] = var15[2];
            }
         }

         if (var9 <= 0.0D) {
            var1[0] = var1[1] = var1[2] = 0.0D;
            var1[LongAxis(var13)] = 1.0D;
         }

      }
   }

   static void CheckOrientation(GLUtessellatorImpl var0) {
      GLUface var4 = var0.mesh.fHead;
      GLUvertex var6 = var0.mesh.vHead;
      double var1 = 0.0D;

      for(GLUface var3 = var4.next; var3 != var4; var3 = var3.next) {
         GLUhalfEdge var7 = var3.anEdge;
         if (var7.winding > 0) {
            do {
               var1 += (var7.Org.s - var7.Sym.Org.s) * (var7.Org.t + var7.Sym.Org.t);
               var7 = var7.Lnext;
            } while(var7 != var3.anEdge);
         }
      }

      if (var1 < 0.0D) {
         for(GLUvertex var5 = var6.next; var5 != var6; var5 = var5.next) {
            var5.t = -var5.t;
         }

         var0.tUnit[0] = -var0.tUnit[0];
         var0.tUnit[1] = -var0.tUnit[1];
         var0.tUnit[2] = -var0.tUnit[2];
      }

   }

   public static void __gl_projectPolygon(GLUtessellatorImpl var0) {
      GLUvertex var2 = var0.mesh.vHead;
      double[] var5 = new double[3];
      boolean var9 = false;
      var5[0] = var0.normal[0];
      var5[1] = var0.normal[1];
      var5[2] = var0.normal[2];
      if (var5[0] == 0.0D && var5[1] == 0.0D && var5[2] == 0.0D) {
         ComputeNormal(var0, var5);
         var9 = true;
      }

      double[] var6 = var0.sUnit;
      double[] var7 = var0.tUnit;
      int var8 = LongAxis(var5);
      var6[var8] = 0.0D;
      var6[(var8 + 1) % 3] = S_UNIT_X;
      var6[(var8 + 2) % 3] = S_UNIT_Y;
      var7[var8] = 0.0D;
      var7[(var8 + 1) % 3] = var5[var8] > 0.0D ? -S_UNIT_Y : S_UNIT_Y;
      var7[(var8 + 2) % 3] = var5[var8] > 0.0D ? S_UNIT_X : -S_UNIT_X;

      for(GLUvertex var1 = var2.next; var1 != var2; var1 = var1.next) {
         var1.s = Dot(var1.coords, var6);
         var1.t = Dot(var1.coords, var7);
      }

      if (var9) {
         CheckOrientation(var0);
      }

   }

   static {
      if (SLANTED_SWEEP) {
         S_UNIT_X = 0.5094153956495538D;
         S_UNIT_Y = 0.8605207462201063D;
      } else {
         S_UNIT_X = 1.0D;
         S_UNIT_Y = 0.0D;
      }

   }
}
