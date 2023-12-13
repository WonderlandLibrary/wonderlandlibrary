package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class PartialDisk extends Quadric {
   private static final int CACHE_SIZE = 240;

   public void draw(float var1, float var2, int var3, int var4, float var5, float var6) {
      float[] var9 = new float[240];
      float[] var10 = new float[240];
      float var17 = 0.0F;
      float var18 = 0.0F;
      if (var3 >= 240) {
         var3 = 239;
      }

      if (var3 >= 2 && var4 >= 1 && !(var2 <= 0.0F) && !(var1 < 0.0F) && !(var1 > var2)) {
         if (var6 < -360.0F) {
            var6 = 360.0F;
         }

         if (var6 > 360.0F) {
            var6 = 360.0F;
         }

         if (var6 < 0.0F) {
            var5 += var6;
            var6 = -var6;
         }

         int var20;
         if (var6 == 360.0F) {
            var20 = var3;
         } else {
            var20 = var3 + 1;
         }

         float var14 = var2 - var1;
         float var19 = var5 / 180.0F * 3.1415927F;

         int var7;
         for(var7 = 0; var7 <= var3; ++var7) {
            float var11 = var19 + 3.1415927F * var6 / 180.0F * (float)var7 / (float)var3;
            var9[var7] = this.sin(var11);
            var10[var7] = this.cos(var11);
         }

         if (var6 == 360.0F) {
            var9[var3] = var9[0];
            var10[var3] = var10[0];
         }

         switch(super.normals) {
         case 100000:
         case 100001:
            if (super.orientation == 100020) {
               GL11.glNormal3f(0.0F, 0.0F, 1.0F);
            } else {
               GL11.glNormal3f(0.0F, 0.0F, -1.0F);
            }
         case 100002:
         }

         int var8;
         float var12;
         float var13;
         float var15;
         switch(super.drawStyle) {
         case 100010:
            GL11.glBegin(0);

            for(var7 = 0; var7 < var20; ++var7) {
               var12 = var9[var7];
               var13 = var10[var7];

               for(var8 = 0; var8 <= var4; ++var8) {
                  var15 = var2 - var14 * ((float)var8 / (float)var4);
                  if (super.textureFlag) {
                     var17 = var15 / var2 / 2.0F;
                     GL11.glTexCoord2f(var17 * var9[var7] + 0.5F, var17 * var10[var7] + 0.5F);
                  }

                  GL11.glVertex3f(var15 * var12, var15 * var13, 0.0F);
               }
            }

            GL11.glEnd();
            break;
         case 100011:
            if (var1 == var2) {
               GL11.glBegin(3);

               for(var7 = 0; var7 <= var3; ++var7) {
                  if (super.textureFlag) {
                     GL11.glTexCoord2f(var9[var7] / 2.0F + 0.5F, var10[var7] / 2.0F + 0.5F);
                  }

                  GL11.glVertex3f(var1 * var9[var7], var1 * var10[var7], 0.0F);
               }

               GL11.glEnd();
               break;
            } else {
               for(var8 = 0; var8 <= var4; ++var8) {
                  var15 = var2 - var14 * ((float)var8 / (float)var4);
                  if (super.textureFlag) {
                     var17 = var15 / var2 / 2.0F;
                  }

                  GL11.glBegin(3);

                  for(var7 = 0; var7 <= var3; ++var7) {
                     if (super.textureFlag) {
                        GL11.glTexCoord2f(var17 * var9[var7] + 0.5F, var17 * var10[var7] + 0.5F);
                     }

                     GL11.glVertex3f(var15 * var9[var7], var15 * var10[var7], 0.0F);
                  }

                  GL11.glEnd();
               }

               for(var7 = 0; var7 < var20; ++var7) {
                  var12 = var9[var7];
                  var13 = var10[var7];
                  GL11.glBegin(3);

                  for(var8 = 0; var8 <= var4; ++var8) {
                     var15 = var2 - var14 * ((float)var8 / (float)var4);
                     if (super.textureFlag) {
                        var17 = var15 / var2 / 2.0F;
                     }

                     if (super.textureFlag) {
                        GL11.glTexCoord2f(var17 * var9[var7] + 0.5F, var17 * var10[var7] + 0.5F);
                     }

                     GL11.glVertex3f(var15 * var12, var15 * var13, 0.0F);
                  }

                  GL11.glEnd();
               }

               return;
            }
         case 100012:
            int var21;
            if (var1 != 0.0F) {
               var21 = var4;
            } else {
               var21 = var4 - 1;
               GL11.glBegin(6);
               if (super.textureFlag) {
                  GL11.glTexCoord2f(0.5F, 0.5F);
               }

               GL11.glVertex3f(0.0F, 0.0F, 0.0F);
               var15 = var2 - var14 * ((float)(var4 - 1) / (float)var4);
               if (super.textureFlag) {
                  var17 = var15 / var2 / 2.0F;
               }

               if (super.orientation == 100020) {
                  for(var7 = var3; var7 >= 0; --var7) {
                     if (super.textureFlag) {
                        GL11.glTexCoord2f(var17 * var9[var7] + 0.5F, var17 * var10[var7] + 0.5F);
                     }

                     GL11.glVertex3f(var15 * var9[var7], var15 * var10[var7], 0.0F);
                  }
               } else {
                  for(var7 = 0; var7 <= var3; ++var7) {
                     if (super.textureFlag) {
                        GL11.glTexCoord2f(var17 * var9[var7] + 0.5F, var17 * var10[var7] + 0.5F);
                     }

                     GL11.glVertex3f(var15 * var9[var7], var15 * var10[var7], 0.0F);
                  }
               }

               GL11.glEnd();
            }

            for(var8 = 0; var8 < var21; ++var8) {
               var15 = var2 - var14 * ((float)var8 / (float)var4);
               float var16 = var2 - var14 * ((float)(var8 + 1) / (float)var4);
               if (super.textureFlag) {
                  var17 = var15 / var2 / 2.0F;
                  var18 = var16 / var2 / 2.0F;
               }

               GL11.glBegin(8);

               for(var7 = 0; var7 <= var3; ++var7) {
                  if (super.orientation == 100020) {
                     if (super.textureFlag) {
                        GL11.glTexCoord2f(var17 * var9[var7] + 0.5F, var17 * var10[var7] + 0.5F);
                     }

                     GL11.glVertex3f(var15 * var9[var7], var15 * var10[var7], 0.0F);
                     if (super.textureFlag) {
                        GL11.glTexCoord2f(var18 * var9[var7] + 0.5F, var18 * var10[var7] + 0.5F);
                     }

                     GL11.glVertex3f(var16 * var9[var7], var16 * var10[var7], 0.0F);
                  } else {
                     if (super.textureFlag) {
                        GL11.glTexCoord2f(var18 * var9[var7] + 0.5F, var18 * var10[var7] + 0.5F);
                     }

                     GL11.glVertex3f(var16 * var9[var7], var16 * var10[var7], 0.0F);
                     if (super.textureFlag) {
                        GL11.glTexCoord2f(var17 * var9[var7] + 0.5F, var17 * var10[var7] + 0.5F);
                     }

                     GL11.glVertex3f(var15 * var9[var7], var15 * var10[var7], 0.0F);
                  }
               }

               GL11.glEnd();
            }

            return;
         case 100013:
            if (var6 < 360.0F) {
               for(var7 = 0; var7 <= var3; var7 += var3) {
                  var12 = var9[var7];
                  var13 = var10[var7];
                  GL11.glBegin(3);

                  for(var8 = 0; var8 <= var4; ++var8) {
                     var15 = var2 - var14 * ((float)var8 / (float)var4);
                     if (super.textureFlag) {
                        var17 = var15 / var2 / 2.0F;
                        GL11.glTexCoord2f(var17 * var9[var7] + 0.5F, var17 * var10[var7] + 0.5F);
                     }

                     GL11.glVertex3f(var15 * var12, var15 * var13, 0.0F);
                  }

                  GL11.glEnd();
               }
            }

            for(var8 = 0; var8 <= var4; var8 += var4) {
               var15 = var2 - var14 * ((float)var8 / (float)var4);
               if (super.textureFlag) {
                  var17 = var15 / var2 / 2.0F;
               }

               GL11.glBegin(3);

               for(var7 = 0; var7 <= var3; ++var7) {
                  if (super.textureFlag) {
                     GL11.glTexCoord2f(var17 * var9[var7] + 0.5F, var17 * var10[var7] + 0.5F);
                  }

                  GL11.glVertex3f(var15 * var9[var7], var15 * var10[var7], 0.0F);
               }

               GL11.glEnd();
               if (var1 == var2) {
                  break;
               }
            }
         }

      } else {
         System.err.println("PartialDisk: GLU_INVALID_VALUE");
      }
   }
}
