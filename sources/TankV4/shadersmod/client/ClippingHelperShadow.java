package shadersmod.client;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.util.MathHelper;

public class ClippingHelperShadow extends ClippingHelper {
   private static ClippingHelperShadow instance = new ClippingHelperShadow();
   float[] frustumTest = new float[6];
   float[][] shadowClipPlanes = new float[10][4];
   int shadowClipPlaneCount;
   float[] matInvMP = new float[16];
   float[] vecIntersection = new float[4];

   public boolean isBoxInFrustum(double var1, double var3, double var5, double var7, double var9, double var11) {
      for(int var13 = 0; var13 < this.shadowClipPlaneCount; ++var13) {
         float[] var14 = this.shadowClipPlanes[var13];
         if (this.dot4(var14, var1, var3, var5) <= 0.0D && this.dot4(var14, var7, var3, var5) <= 0.0D && this.dot4(var14, var1, var9, var5) <= 0.0D && this.dot4(var14, var7, var9, var5) <= 0.0D && this.dot4(var14, var1, var3, var11) <= 0.0D && this.dot4(var14, var7, var3, var11) <= 0.0D && this.dot4(var14, var1, var9, var11) <= 0.0D && this.dot4(var14, var7, var9, var11) <= 0.0D) {
            return false;
         }
      }

      return true;
   }

   private double dot4(float[] var1, double var2, double var4, double var6) {
      return (double)var1[0] * var2 + (double)var1[1] * var4 + (double)var1[2] * var6 + (double)var1[3];
   }

   private double dot3(float[] var1, float[] var2) {
      return (double)var1[0] * (double)var2[0] + (double)var1[1] * (double)var2[1] + (double)var1[2] * (double)var2[2];
   }

   public static ClippingHelper getInstance() {
      instance.init();
      return instance;
   }

   private void normalizePlane(float[] var1) {
      float var2 = MathHelper.sqrt_float(var1[0] * var1[0] + var1[1] * var1[1] + var1[2] * var1[2]);
      var1[0] /= var2;
      var1[1] /= var2;
      var1[2] /= var2;
      var1[3] /= var2;
   }

   private void normalize3(float[] var1) {
      float var2 = MathHelper.sqrt_float(var1[0] * var1[0] + var1[1] * var1[1] + var1[2] * var1[2]);
      if (var2 == 0.0F) {
         var2 = 1.0F;
      }

      var1[0] /= var2;
      var1[1] /= var2;
      var1[2] /= var2;
   }

   private void assignPlane(float[] var1, float var2, float var3, float var4, float var5) {
      float var6 = (float)Math.sqrt((double)(var2 * var2 + var3 * var3 + var4 * var4));
      var1[0] = var2 / var6;
      var1[1] = var3 / var6;
      var1[2] = var4 / var6;
      var1[3] = var5 / var6;
   }

   private void copyPlane(float[] var1, float[] var2) {
      var1[0] = var2[0];
      var1[1] = var2[1];
      var1[2] = var2[2];
      var1[3] = var2[3];
   }

   private void cross3(float[] var1, float[] var2, float[] var3) {
      var1[0] = var2[1] * var3[2] - var2[2] * var3[1];
      var1[1] = var2[2] * var3[0] - var2[0] * var3[2];
      var1[2] = var2[0] * var3[1] - var2[1] * var3[0];
   }

   private void addShadowClipPlane(float[] var1) {
      this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], var1);
   }

   private float length(float var1, float var2, float var3) {
      return (float)Math.sqrt((double)(var1 * var1 + var2 * var2 + var3 * var3));
   }

   private float distance(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.length(var1 - var4, var2 - var5, var3 - var6);
   }

   private void makeShadowPlane(float[] var1, float[] var2, float[] var3, float[] var4) {
      this.cross3(this.vecIntersection, var2, var3);
      this.cross3(var1, this.vecIntersection, var4);
      this.normalize3(var1);
      float var5 = (float)this.dot3(var2, var3);
      float var6 = (float)this.dot3(var1, var3);
      float var7 = this.distance(var1[0], var1[1], var1[2], var3[0] * var6, var3[1] * var6, var3[2] * var6);
      float var8 = this.distance(var2[0], var2[1], var2[2], var3[0] * var5, var3[1] * var5, var3[2] * var5);
      float var9 = var7 / var8;
      float var10 = (float)this.dot3(var1, var2);
      float var11 = this.distance(var1[0], var1[1], var1[2], var2[0] * var10, var2[1] * var10, var2[2] * var10);
      float var12 = this.distance(var3[0], var3[1], var3[2], var2[0] * var5, var2[1] * var5, var2[2] * var5);
      float var13 = var11 / var12;
      var1[3] = var2[3] * var9 + var3[3] * var13;
   }

   public void init() {
      float[] var1 = this.projectionMatrix;
      float[] var2 = this.modelviewMatrix;
      float[] var3 = this.clippingMatrix;
      System.arraycopy(Shaders.faProjection, 0, var1, 0, 16);
      System.arraycopy(Shaders.faModelView, 0, var2, 0, 16);
      SMath.multiplyMat4xMat4(var3, var2, var1);
      this.assignPlane(this.frustum[0], var3[3] - var3[0], var3[7] - var3[4], var3[11] - var3[8], var3[15] - var3[12]);
      this.assignPlane(this.frustum[1], var3[3] + var3[0], var3[7] + var3[4], var3[11] + var3[8], var3[15] + var3[12]);
      this.assignPlane(this.frustum[2], var3[3] + var3[1], var3[7] + var3[5], var3[11] + var3[9], var3[15] + var3[13]);
      this.assignPlane(this.frustum[3], var3[3] - var3[1], var3[7] - var3[5], var3[11] - var3[9], var3[15] - var3[13]);
      this.assignPlane(this.frustum[4], var3[3] - var3[2], var3[7] - var3[6], var3[11] - var3[10], var3[15] - var3[14]);
      this.assignPlane(this.frustum[5], var3[3] + var3[2], var3[7] + var3[6], var3[11] + var3[10], var3[15] + var3[14]);
      float[] var4 = Shaders.shadowLightPositionVector;
      float var5 = (float)this.dot3(this.frustum[0], var4);
      float var6 = (float)this.dot3(this.frustum[1], var4);
      float var7 = (float)this.dot3(this.frustum[2], var4);
      float var8 = (float)this.dot3(this.frustum[3], var4);
      float var9 = (float)this.dot3(this.frustum[4], var4);
      float var10 = (float)this.dot3(this.frustum[5], var4);
      this.shadowClipPlaneCount = 0;
      if (var5 >= 0.0F) {
         this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0]);
         if (var5 > 0.0F) {
            if (var7 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0], this.frustum[2], var4);
            }

            if (var8 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0], this.frustum[3], var4);
            }

            if (var9 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0], this.frustum[4], var4);
            }

            if (var10 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0], this.frustum[5], var4);
            }
         }
      }

      if (var6 >= 0.0F) {
         this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1]);
         if (var6 > 0.0F) {
            if (var7 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1], this.frustum[2], var4);
            }

            if (var8 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1], this.frustum[3], var4);
            }

            if (var9 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1], this.frustum[4], var4);
            }

            if (var10 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1], this.frustum[5], var4);
            }
         }
      }

      if (var7 >= 0.0F) {
         this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2]);
         if (var7 > 0.0F) {
            if (var5 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2], this.frustum[0], var4);
            }

            if (var6 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2], this.frustum[1], var4);
            }

            if (var9 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2], this.frustum[4], var4);
            }

            if (var10 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2], this.frustum[5], var4);
            }
         }
      }

      if (var8 >= 0.0F) {
         this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3]);
         if (var8 > 0.0F) {
            if (var5 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3], this.frustum[0], var4);
            }

            if (var6 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3], this.frustum[1], var4);
            }

            if (var9 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3], this.frustum[4], var4);
            }

            if (var10 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3], this.frustum[5], var4);
            }
         }
      }

      if (var9 >= 0.0F) {
         this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4]);
         if (var9 > 0.0F) {
            if (var5 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4], this.frustum[0], var4);
            }

            if (var6 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4], this.frustum[1], var4);
            }

            if (var7 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4], this.frustum[2], var4);
            }

            if (var8 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4], this.frustum[3], var4);
            }
         }
      }

      if (var10 >= 0.0F) {
         this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5]);
         if (var10 > 0.0F) {
            if (var5 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5], this.frustum[0], var4);
            }

            if (var6 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5], this.frustum[1], var4);
            }

            if (var7 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5], this.frustum[2], var4);
            }

            if (var8 < 0.0F) {
               this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5], this.frustum[3], var4);
            }
         }
      }

   }
}
