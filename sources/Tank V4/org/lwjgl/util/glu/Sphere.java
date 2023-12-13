package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class Sphere extends Quadric {
   public void draw(float var1, int var2, int var3) {
      boolean var19 = super.normals != 100002;
      float var20;
      if (super.orientation == 100021) {
         var20 = -1.0F;
      } else {
         var20 = 1.0F;
      }

      float var5 = 3.1415927F / (float)var3;
      float var7 = 6.2831855F / (float)var2;
      float var4;
      float var6;
      float var8;
      float var9;
      float var10;
      int var15;
      int var16;
      if (super.drawStyle == 100012) {
         if (!super.textureFlag) {
            GL11.glBegin(6);
            GL11.glNormal3f(0.0F, 0.0F, 1.0F);
            GL11.glVertex3f(0.0F, 0.0F, var20 * var1);

            for(var16 = 0; var16 <= var2; ++var16) {
               var6 = var16 == var2 ? 0.0F : (float)var16 * var7;
               var8 = -this.sin(var6) * this.sin(var5);
               var9 = this.cos(var6) * this.sin(var5);
               var10 = var20 * this.cos(var5);
               if (var19) {
                  GL11.glNormal3f(var8 * var20, var9 * var20, var10 * var20);
               }

               GL11.glVertex3f(var8 * var1, var9 * var1, var10 * var1);
            }

            GL11.glEnd();
         }

         float var13 = 1.0F / (float)var2;
         float var14 = 1.0F / (float)var3;
         float var12 = 1.0F;
         byte var17;
         int var18;
         if (super.textureFlag) {
            var17 = 0;
            var18 = var3;
         } else {
            var17 = 1;
            var18 = var3 - 1;
         }

         float var11;
         for(var15 = var17; var15 < var18; ++var15) {
            var4 = (float)var15 * var5;
            GL11.glBegin(8);
            var11 = 0.0F;

            for(var16 = 0; var16 <= var2; ++var16) {
               var6 = var16 == var2 ? 0.0F : (float)var16 * var7;
               var8 = -this.sin(var6) * this.sin(var4);
               var9 = this.cos(var6) * this.sin(var4);
               var10 = var20 * this.cos(var4);
               if (var19) {
                  GL11.glNormal3f(var8 * var20, var9 * var20, var10 * var20);
               }

               this.TXTR_COORD(var11, var12);
               GL11.glVertex3f(var8 * var1, var9 * var1, var10 * var1);
               var8 = -this.sin(var6) * this.sin(var4 + var5);
               var9 = this.cos(var6) * this.sin(var4 + var5);
               var10 = var20 * this.cos(var4 + var5);
               if (var19) {
                  GL11.glNormal3f(var8 * var20, var9 * var20, var10 * var20);
               }

               this.TXTR_COORD(var11, var12 - var14);
               var11 += var13;
               GL11.glVertex3f(var8 * var1, var9 * var1, var10 * var1);
            }

            GL11.glEnd();
            var12 -= var14;
         }

         if (!super.textureFlag) {
            GL11.glBegin(6);
            GL11.glNormal3f(0.0F, 0.0F, -1.0F);
            GL11.glVertex3f(0.0F, 0.0F, -var1 * var20);
            var4 = 3.1415927F - var5;
            var11 = 1.0F;

            for(var16 = var2; var16 >= 0; --var16) {
               var6 = var16 == var2 ? 0.0F : (float)var16 * var7;
               var8 = -this.sin(var6) * this.sin(var4);
               var9 = this.cos(var6) * this.sin(var4);
               var10 = var20 * this.cos(var4);
               if (var19) {
                  GL11.glNormal3f(var8 * var20, var9 * var20, var10 * var20);
               }

               var11 -= var13;
               GL11.glVertex3f(var8 * var1, var9 * var1, var10 * var1);
            }

            GL11.glEnd();
         }
      } else if (super.drawStyle != 100011 && super.drawStyle != 100013) {
         if (super.drawStyle == 100010) {
            GL11.glBegin(0);
            if (var19) {
               GL11.glNormal3f(0.0F, 0.0F, var20);
            }

            GL11.glVertex3f(0.0F, 0.0F, var1);
            if (var19) {
               GL11.glNormal3f(0.0F, 0.0F, -var20);
            }

            GL11.glVertex3f(0.0F, 0.0F, -var1);

            for(var15 = 1; var15 < var3 - 1; ++var15) {
               var4 = (float)var15 * var5;

               for(var16 = 0; var16 < var2; ++var16) {
                  var6 = (float)var16 * var7;
                  var8 = this.cos(var6) * this.sin(var4);
                  var9 = this.sin(var6) * this.sin(var4);
                  var10 = this.cos(var4);
                  if (var19) {
                     GL11.glNormal3f(var8 * var20, var9 * var20, var10 * var20);
                  }

                  GL11.glVertex3f(var8 * var1, var9 * var1, var10 * var1);
               }
            }

            GL11.glEnd();
         }
      } else {
         for(var15 = 1; var15 < var3; ++var15) {
            var4 = (float)var15 * var5;
            GL11.glBegin(2);

            for(var16 = 0; var16 < var2; ++var16) {
               var6 = (float)var16 * var7;
               var8 = this.cos(var6) * this.sin(var4);
               var9 = this.sin(var6) * this.sin(var4);
               var10 = this.cos(var4);
               if (var19) {
                  GL11.glNormal3f(var8 * var20, var9 * var20, var10 * var20);
               }

               GL11.glVertex3f(var8 * var1, var9 * var1, var10 * var1);
            }

            GL11.glEnd();
         }

         for(var16 = 0; var16 < var2; ++var16) {
            var6 = (float)var16 * var7;
            GL11.glBegin(3);

            for(var15 = 0; var15 <= var3; ++var15) {
               var4 = (float)var15 * var5;
               var8 = this.cos(var6) * this.sin(var4);
               var9 = this.sin(var6) * this.sin(var4);
               var10 = this.cos(var4);
               if (var19) {
                  GL11.glNormal3f(var8 * var20, var9 * var20, var10 * var20);
               }

               GL11.glVertex3f(var8 * var1, var9 * var1, var10 * var1);
            }

            GL11.glEnd();
         }
      }

   }
}
