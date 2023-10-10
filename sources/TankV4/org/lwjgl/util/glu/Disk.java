package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class Disk extends Quadric {
   public void draw(float var1, float var2, int var3, int var4) {
      if (super.normals != 100002) {
         if (super.orientation == 100020) {
            GL11.glNormal3f(0.0F, 0.0F, 1.0F);
         } else {
            GL11.glNormal3f(0.0F, 0.0F, -1.0F);
         }
      }

      float var5 = 6.2831855F / (float)var3;
      float var6 = (var2 - var1) / (float)var4;
      float var7;
      float var8;
      float var9;
      float var10;
      int var11;
      float var12;
      int var15;
      switch(super.drawStyle) {
      case 100010:
         GL11.glBegin(0);

         for(var15 = 0; var15 < var3; ++var15) {
            var8 = (float)var15 * var5;
            var9 = this.sin(var8);
            var10 = this.cos(var8);

            for(var11 = 0; var11 <= var4; ++var11) {
               var12 = var1 * (float)var11 * var6;
               GL11.glVertex2f(var12 * var9, var12 * var10);
            }
         }

         GL11.glEnd();
         break;
      case 100011:
         int var16;
         for(var15 = 0; var15 <= var4; ++var15) {
            var9 = var1 + (float)var15 * var6;
            GL11.glBegin(2);

            for(var16 = 0; var16 < var3; ++var16) {
               var10 = (float)var16 * var5;
               GL11.glVertex2f(var9 * this.sin(var10), var9 * this.cos(var10));
            }

            GL11.glEnd();
         }

         for(var16 = 0; var16 < var3; ++var16) {
            var9 = (float)var16 * var5;
            var10 = this.sin(var9);
            float var17 = this.cos(var9);
            GL11.glBegin(3);

            for(var15 = 0; var15 <= var4; ++var15) {
               var12 = var1 + (float)var15 * var6;
               GL11.glVertex2f(var12 * var10, var12 * var17);
            }

            GL11.glEnd();
         }

         return;
      case 100012:
         var7 = 2.0F * var2;
         var10 = var1;

         for(var11 = 0; var11 < var4; ++var11) {
            var12 = var10 + var6;
            int var13;
            float var14;
            if (super.orientation == 100020) {
               GL11.glBegin(8);

               for(var13 = 0; var13 <= var3; ++var13) {
                  if (var13 == var3) {
                     var14 = 0.0F;
                  } else {
                     var14 = (float)var13 * var5;
                  }

                  var8 = this.sin(var14);
                  var9 = this.cos(var14);
                  this.TXTR_COORD(0.5F + var8 * var12 / var7, 0.5F + var9 * var12 / var7);
                  GL11.glVertex2f(var12 * var8, var12 * var9);
                  this.TXTR_COORD(0.5F + var8 * var10 / var7, 0.5F + var9 * var10 / var7);
                  GL11.glVertex2f(var10 * var8, var10 * var9);
               }

               GL11.glEnd();
            } else {
               GL11.glBegin(8);

               for(var13 = var3; var13 >= 0; --var13) {
                  if (var13 == var3) {
                     var14 = 0.0F;
                  } else {
                     var14 = (float)var13 * var5;
                  }

                  var8 = this.sin(var14);
                  var9 = this.cos(var14);
                  this.TXTR_COORD(0.5F - var8 * var12 / var7, 0.5F + var9 * var12 / var7);
                  GL11.glVertex2f(var12 * var8, var12 * var9);
                  this.TXTR_COORD(0.5F - var8 * var10 / var7, 0.5F + var9 * var10 / var7);
                  GL11.glVertex2f(var10 * var8, var10 * var9);
               }

               GL11.glEnd();
            }

            var10 = var12;
         }

         return;
      case 100013:
         if ((double)var1 != 0.0D) {
            GL11.glBegin(2);

            for(var7 = 0.0F; (double)var7 < 6.2831854820251465D; var7 += var5) {
               var8 = var1 * this.sin(var7);
               var9 = var1 * this.cos(var7);
               GL11.glVertex2f(var8, var9);
            }

            GL11.glEnd();
         }

         GL11.glBegin(2);

         for(var7 = 0.0F; var7 < 6.2831855F; var7 += var5) {
            var8 = var2 * this.sin(var7);
            var9 = var2 * this.cos(var7);
            GL11.glVertex2f(var8, var9);
         }

         GL11.glEnd();
         break;
      default:
         return;
      }

   }
}
