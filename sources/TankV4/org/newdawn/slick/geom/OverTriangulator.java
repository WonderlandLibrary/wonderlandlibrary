package org.newdawn.slick.geom;

public class OverTriangulator implements Triangulator {
   private float[][] triangles;

   public OverTriangulator(Triangulator var1) {
      this.triangles = new float[var1.getTriangleCount() * 6 * 3][2];
      int var2 = 0;

      for(int var3 = 0; var3 < var1.getTriangleCount(); ++var3) {
         float var4 = 0.0F;
         float var5 = 0.0F;

         int var6;
         for(var6 = 0; var6 < 3; ++var6) {
            float[] var7 = var1.getTrianglePoint(var3, var6);
            var4 += var7[0];
            var5 += var7[1];
         }

         var4 /= 3.0F;
         var5 /= 3.0F;

         float[] var8;
         float[] var9;
         int var10;
         for(var6 = 0; var6 < 3; ++var6) {
            var10 = var6 + 1;
            if (var10 > 2) {
               var10 = 0;
            }

            var8 = var1.getTrianglePoint(var3, var6);
            var9 = var1.getTrianglePoint(var3, var10);
            var8[0] = (var8[0] + var9[0]) / 2.0F;
            var8[1] = (var8[1] + var9[1]) / 2.0F;
            this.triangles[var2 * 3 + 0][0] = var4;
            this.triangles[var2 * 3 + 0][1] = var5;
            this.triangles[var2 * 3 + 1][0] = var8[0];
            this.triangles[var2 * 3 + 1][1] = var8[1];
            this.triangles[var2 * 3 + 2][0] = var9[0];
            this.triangles[var2 * 3 + 2][1] = var9[1];
            ++var2;
         }

         for(var6 = 0; var6 < 3; ++var6) {
            var10 = var6 + 1;
            if (var10 > 2) {
               var10 = 0;
            }

            var8 = var1.getTrianglePoint(var3, var6);
            var9 = var1.getTrianglePoint(var3, var10);
            var9[0] = (var8[0] + var9[0]) / 2.0F;
            var9[1] = (var8[1] + var9[1]) / 2.0F;
            this.triangles[var2 * 3 + 0][0] = var4;
            this.triangles[var2 * 3 + 0][1] = var5;
            this.triangles[var2 * 3 + 1][0] = var8[0];
            this.triangles[var2 * 3 + 1][1] = var8[1];
            this.triangles[var2 * 3 + 2][0] = var9[0];
            this.triangles[var2 * 3 + 2][1] = var9[1];
            ++var2;
         }
      }

   }

   public void addPolyPoint(float var1, float var2) {
   }

   public int getTriangleCount() {
      return this.triangles.length / 3;
   }

   public float[] getTrianglePoint(int var1, int var2) {
      float[] var3 = this.triangles[var1 * 3 + var2];
      return new float[]{var3[0], var3[1]};
   }

   public void startHole() {
   }

   public boolean triangulate() {
      return true;
   }
}
