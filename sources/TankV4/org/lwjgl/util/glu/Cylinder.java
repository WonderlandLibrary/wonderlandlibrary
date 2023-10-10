package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class Cylinder extends Quadric {
   public void draw(float var1, float var2, float var3, int var4, int var5) {
      float var14;
      if (super.orientation == 100021) {
         var14 = -1.0F;
      } else {
         var14 = 1.0F;
      }

      float var6 = 6.2831855F / (float)var4;
      float var8 = (var2 - var1) / (float)var5;
      float var9 = var3 / (float)var5;
      float var13 = (var1 - var2) / var3;
      float var7;
      float var10;
      float var11;
      float var12;
      int var15;
      int var16;
      if (super.drawStyle == 100010) {
         GL11.glBegin(0);

         for(var15 = 0; var15 < var4; ++var15) {
            var10 = this.cos((float)var15 * var6);
            var11 = this.sin((float)var15 * var6);
            this.normal3f(var10 * var14, var11 * var14, var13 * var14);
            var12 = 0.0F;
            var7 = var1;

            for(var16 = 0; var16 <= var5; ++var16) {
               GL11.glVertex3f(var10 * var7, var11 * var7, var12);
               var12 += var9;
               var7 += var8;
            }
         }

         GL11.glEnd();
      } else if (super.drawStyle != 100011 && super.drawStyle != 100013) {
         if (super.drawStyle == 100012) {
            float var17 = 1.0F / (float)var4;
            float var18 = 1.0F / (float)var5;
            float var19 = 0.0F;
            var12 = 0.0F;
            var7 = var1;

            for(var16 = 0; var16 < var5; ++var16) {
               float var20 = 0.0F;
               GL11.glBegin(8);

               for(var15 = 0; var15 <= var4; ++var15) {
                  if (var15 == var4) {
                     var10 = this.sin(0.0F);
                     var11 = this.cos(0.0F);
                  } else {
                     var10 = this.sin((float)var15 * var6);
                     var11 = this.cos((float)var15 * var6);
                  }

                  if (var14 == 1.0F) {
                     this.normal3f(var10 * var14, var11 * var14, var13 * var14);
                     this.TXTR_COORD(var20, var19);
                     GL11.glVertex3f(var10 * var7, var11 * var7, var12);
                     this.normal3f(var10 * var14, var11 * var14, var13 * var14);
                     this.TXTR_COORD(var20, var19 + var18);
                     GL11.glVertex3f(var10 * (var7 + var8), var11 * (var7 + var8), var12 + var9);
                  } else {
                     this.normal3f(var10 * var14, var11 * var14, var13 * var14);
                     this.TXTR_COORD(var20, var19);
                     GL11.glVertex3f(var10 * var7, var11 * var7, var12);
                     this.normal3f(var10 * var14, var11 * var14, var13 * var14);
                     this.TXTR_COORD(var20, var19 + var18);
                     GL11.glVertex3f(var10 * (var7 + var8), var11 * (var7 + var8), var12 + var9);
                  }

                  var20 += var17;
               }

               GL11.glEnd();
               var7 += var8;
               var19 += var18;
               var12 += var9;
            }
         }
      } else {
         if (super.drawStyle == 100011) {
            var12 = 0.0F;
            var7 = var1;

            for(var16 = 0; var16 <= var5; ++var16) {
               GL11.glBegin(2);

               for(var15 = 0; var15 < var4; ++var15) {
                  var10 = this.cos((float)var15 * var6);
                  var11 = this.sin((float)var15 * var6);
                  this.normal3f(var10 * var14, var11 * var14, var13 * var14);
                  GL11.glVertex3f(var10 * var7, var11 * var7, var12);
               }

               GL11.glEnd();
               var12 += var9;
               var7 += var8;
            }
         } else if ((double)var1 != 0.0D) {
            GL11.glBegin(2);

            for(var15 = 0; var15 < var4; ++var15) {
               var10 = this.cos((float)var15 * var6);
               var11 = this.sin((float)var15 * var6);
               this.normal3f(var10 * var14, var11 * var14, var13 * var14);
               GL11.glVertex3f(var10 * var1, var11 * var1, 0.0F);
            }

            GL11.glEnd();
            GL11.glBegin(2);

            for(var15 = 0; var15 < var4; ++var15) {
               var10 = this.cos((float)var15 * var6);
               var11 = this.sin((float)var15 * var6);
               this.normal3f(var10 * var14, var11 * var14, var13 * var14);
               GL11.glVertex3f(var10 * var2, var11 * var2, var3);
            }

            GL11.glEnd();
         }

         GL11.glBegin(1);

         for(var15 = 0; var15 < var4; ++var15) {
            var10 = this.cos((float)var15 * var6);
            var11 = this.sin((float)var15 * var6);
            this.normal3f(var10 * var14, var11 * var14, var13 * var14);
            GL11.glVertex3f(var10 * var1, var11 * var1, 0.0F);
            GL11.glVertex3f(var10 * var2, var11 * var2, var3);
         }

         GL11.glEnd();
      }

   }
}
