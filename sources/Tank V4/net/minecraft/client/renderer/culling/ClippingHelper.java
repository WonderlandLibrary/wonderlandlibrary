package net.minecraft.client.renderer.culling;

public class ClippingHelper {
   private static final String __OBFID = "CL_00000977";
   public float[] modelviewMatrix = new float[16];
   public float[] clippingMatrix = new float[16];
   public float[] projectionMatrix = new float[16];
   public float[][] frustum = new float[6][4];

   private float dot(float[] var1, float var2, float var3, float var4) {
      return var1[0] * var2 + var1[1] * var3 + var1[2] * var4 + var1[3];
   }

   public boolean isBoxInFrustum(double var1, double var3, double var5, double var7, double var9, double var11) {
      float var13 = (float)var1;
      float var14 = (float)var3;
      float var15 = (float)var5;
      float var16 = (float)var7;
      float var17 = (float)var9;
      float var18 = (float)var11;

      for(int var19 = 0; var19 < 6; ++var19) {
         float[] var20 = this.frustum[var19];
         if (this.dot(var20, var13, var14, var15) <= 0.0F && this.dot(var20, var16, var14, var15) <= 0.0F && this.dot(var20, var13, var17, var15) <= 0.0F && this.dot(var20, var16, var17, var15) <= 0.0F && this.dot(var20, var13, var14, var18) <= 0.0F && this.dot(var20, var16, var14, var18) <= 0.0F && this.dot(var20, var13, var17, var18) <= 0.0F && this.dot(var20, var16, var17, var18) <= 0.0F) {
            return false;
         }
      }

      return true;
   }
}
