package my.NewSnake.utils;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFontRenderer extends CFont {
   protected DynamicTexture texBold;
   private final int[] colorCode = new int[32];
   protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
   protected CFont.CharData[] boldChars = new CFont.CharData[256];
   protected DynamicTexture texItalic;
   protected CFont.CharData[] italicChars = new CFont.CharData[256];
   protected DynamicTexture texItalicBold;
   private final String colorcodeIdentifiers = "0123456789abcdefklmnor";

   public float drawCenteredStringWithShadow(String var1, double var2, double var4, int var6) {
      return this.drawStringWithShadow(var1, var2 - (double)(this.getStringWidth(var1) / 2), var4, var6);
   }

   public void setAntiAlias(boolean var1) {
      super.setAntiAlias(var1);
      this.setupBoldItalicIDs();
   }

   public int getStringWidth(String var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = 0;
         CFont.CharData[] var3 = this.charData;
         boolean var4 = false;
         boolean var5 = false;
         int var6 = var1.length();

         for(int var7 = 0; var7 < var6; ++var7) {
            char var8 = var1.charAt(var7);
            if (var8 == 167 && var7 < var6) {
               int var9 = "0123456789abcdefklmnor".indexOf(var8);
               if (var9 < 16) {
                  var4 = false;
                  var5 = false;
               } else if (var9 == 17) {
                  var4 = true;
                  var3 = var5 ? this.boldItalicChars : this.boldChars;
               } else if (var9 == 20) {
                  var5 = true;
                  var3 = var4 ? this.boldItalicChars : this.italicChars;
               } else if (var9 == 21) {
                  var4 = false;
                  var5 = false;
                  var3 = this.charData;
               }

               ++var7;
            } else if (var8 < var3.length && var8 >= 0) {
               var2 += var3[var8].width - 8 + this.charOffset;
            }
         }

         return var2 / 2;
      }
   }

   private void setupMinecraftColorcodes() {
      for(int var1 = 0; var1 < 32; ++var1) {
         int var2 = (var1 >> 3 & 1) * 85;
         int var3 = (var1 >> 2 & 1) * 170 + var2;
         int var4 = (var1 >> 1 & 1) * 170 + var2;
         int var5 = (var1 >> 0 & 1) * 170 + var2;
         if (var1 == 6) {
            var3 += 85;
         }

         if (var1 >= 16) {
            var3 /= 4;
            var4 /= 4;
            var5 /= 4;
         }

         this.colorCode[var1] = (var3 & 255) << 16 | (var4 & 255) << 8 | var5 & 255;
      }

   }

   private void setupBoldItalicIDs() {
      this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
      this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
   }

   public float drawCenteredString(String var1, float var2, float var3, int var4) {
      return this.drawString(var1, var2 - (float)(this.getStringWidth(var1) / 2), var3, var4);
   }

   public float drawStringWithShadowNew(String var1, double var2, double var4, int var6) {
      float var7 = this.drawString(var1, var2 + 0.5D, var4 + 0.5D, var6, true);
      return Math.max(var7, this.drawString(var1, var2, var4, var6, false));
   }

   public float drawString(String var1, double var2, double var4, int var6, boolean var7) {
      --var2;
      if (var1 == null) {
         return 0.0F;
      } else {
         if (var6 == 553648127) {
            var6 = 16777215;
         }

         if ((var6 & -67108864) == 0) {
            var6 |= -16777216;
         }

         if (var7) {
            var6 = (var6 & 16579836) >> 2 | var6 & -16777216;
         }

         CFont.CharData[] var8 = this.charData;
         float var9 = (float)(var6 >> 24 & 255) / 255.0F;
         boolean var10 = false;
         boolean var11 = false;
         boolean var12 = false;
         boolean var13 = false;
         boolean var14 = false;
         boolean var15 = true;
         var2 *= 2.0D;
         var4 = (var4 - 3.0D) * 2.0D;
         if (var15) {
            GL11.glPushMatrix();
            GlStateManager.scale(0.5D, 0.5D, 0.5D);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color((float)(var6 >> 16 & 255) / 255.0F, (float)(var6 >> 8 & 255) / 255.0F, (float)(var6 & 255) / 255.0F, var9);
            int var16 = var1.length();
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(this.tex.getGlTextureId());
            GL11.glBindTexture(3553, this.tex.getGlTextureId());

            for(int var17 = 0; var17 < var16; ++var17) {
               char var18 = var1.charAt(var17);
               if (var18 == 167 && var17 < var16) {
                  int var19 = 21;

                  try {
                     var19 = "0123456789abcdefklmnor".indexOf(var1.charAt(var17 + 1));
                  } catch (Exception var21) {
                     var21.printStackTrace();
                  }

                  if (var19 < 16) {
                     var11 = false;
                     var12 = false;
                     var10 = false;
                     var14 = false;
                     var13 = false;
                     GlStateManager.bindTexture(this.tex.getGlTextureId());
                     var8 = this.charData;
                     if (var19 < 0 || var19 > 15) {
                        var19 = 15;
                     }

                     if (var7) {
                        var19 += 16;
                     }

                     int var20 = this.colorCode[var19];
                     GlStateManager.color((float)(var20 >> 16 & 255) / 255.0F, (float)(var20 >> 8 & 255) / 255.0F, (float)(var20 & 255) / 255.0F, var9);
                  } else if (var19 == 16) {
                     var10 = true;
                  } else if (var19 == 17) {
                     var11 = true;
                     if (var12) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        var8 = this.boldItalicChars;
                     } else {
                        GlStateManager.bindTexture(this.texBold.getGlTextureId());
                        var8 = this.boldChars;
                     }
                  } else if (var19 == 18) {
                     var13 = true;
                  } else if (var19 == 19) {
                     var14 = true;
                  } else if (var19 == 20) {
                     var12 = true;
                     if (var11) {
                        GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                        var8 = this.boldItalicChars;
                     } else {
                        GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                        var8 = this.italicChars;
                     }
                  } else if (var19 == 21) {
                     var11 = false;
                     var12 = false;
                     var10 = false;
                     var14 = false;
                     var13 = false;
                     GlStateManager.color((float)(var6 >> 16 & 255) / 255.0F, (float)(var6 >> 8 & 255) / 255.0F, (float)(var6 & 255) / 255.0F, var9);
                     GlStateManager.bindTexture(this.tex.getGlTextureId());
                     var8 = this.charData;
                  }

                  ++var17;
               } else if (var18 < var8.length && var18 >= 0) {
                  GL11.glBegin(4);
                  this.drawChar(var8, var18, (float)var2, (float)var4);
                  GL11.glEnd();
                  if (var13) {
                     this.drawLine(var2, var4 + (double)(var8[var18].height / 2), var2 + (double)var8[var18].width - 8.0D, var4 + (double)(var8[var18].height / 2), 1.0F);
                  }

                  if (var14) {
                     this.drawLine(var2, var4 + (double)var8[var18].height - 2.0D, var2 + (double)var8[var18].width - 8.0D, var4 + (double)var8[var18].height - 2.0D, 1.0F);
                  }

                  var2 += (double)(var8[var18].width - 8 + this.charOffset);
               }
            }

            GL11.glHint(3155, 4352);
            GL11.glPopMatrix();
         }

         return (float)var2 / 2.0F;
      }
   }

   public CFontRenderer(Font var1, boolean var2, boolean var3) {
      super(var1, var2, var3);
      this.setupMinecraftColorcodes();
      this.setupBoldItalicIDs();
   }

   public float drawStringWithShadow(String var1, double var2, double var4, int var6) {
      float var7 = this.drawString(var1, var2 + 0.5D, var4 + 0.5D, var6, true);
      return Math.max(var7, this.drawString(var1, var2, var4, var6, false));
   }

   public void setFont(Font var1) {
      super.setFont(var1);
      this.setupBoldItalicIDs();
   }

   public void setFractionalMetrics(boolean var1) {
      super.setFractionalMetrics(var1);
      this.setupBoldItalicIDs();
   }

   public List wrapWords(String var1, double var2) {
      ArrayList var4 = new ArrayList();
      if ((double)this.getStringWidth(var1) > var2) {
         String[] var5 = var1.split(" ");
         String var6 = "";
         char var7 = '\uffff';
         String[] var8 = var5;
         int var9 = var5.length;

         String var11;
         for(int var10 = 0; var10 < var9; ++var10) {
            var11 = var8[var10];

            for(int var12 = 0; var12 < var11.toCharArray().length; ++var12) {
               char var13 = var11.toCharArray()[var12];
               if (var13 == 167 && var12 < var11.toCharArray().length - 1) {
                  var7 = var11.toCharArray()[var12 + 1];
               }
            }

            if ((double)this.getStringWidth(String.valueOf(var6) + var11 + " ") < var2) {
               var6 = String.valueOf(var6) + var11 + " ";
            } else {
               var4.add(var6);
               var6 = String.valueOf(167 + var7) + var11 + " ";
            }
         }

         if (var6.length() > 0) {
            if ((double)this.getStringWidth(var6) < var2) {
               var4.add(String.valueOf(167 + var7) + var6 + " ");
               var6 = "";
            } else {
               Iterator var14 = this.formatString(var6, var2).iterator();

               while(var14.hasNext()) {
                  var11 = (String)var14.next();
                  var4.add(var11);
               }
            }
         }
      } else {
         var4.add(var1);
      }

      return var4;
   }

   public List formatString(String var1, double var2) {
      ArrayList var4 = new ArrayList();
      String var5 = "";
      char var6 = '\uffff';
      char[] var7 = var1.toCharArray();

      for(int var8 = 0; var8 < var7.length; ++var8) {
         char var9 = var7[var8];
         if (var9 == 167 && var8 < var7.length - 1) {
            var6 = var7[var8 + 1];
         }

         if ((double)this.getStringWidth(String.valueOf(var5) + var9) < var2) {
            var5 = String.valueOf(var5) + var9;
         } else {
            var4.add(var5);
            var5 = String.valueOf(167 + var6) + String.valueOf(var9);
         }
      }

      if (var5.length() > 0) {
         var4.add(var5);
      }

      return var4;
   }

   public float drawCenteredStringWithShadow(String var1, float var2, float var3, int var4) {
      return this.drawStringWithShadow(var1, (double)(var2 - (float)(this.getStringWidth(var1) / 2)), (double)var3, var4);
   }

   public float drawString(String var1, float var2, float var3, int var4) {
      return this.drawString(var1, (double)var2, (double)var3, var4, false);
   }

   private void drawLine(double var1, double var3, double var5, double var7, float var9) {
      GL11.glDisable(3553);
      GL11.glLineWidth(var9);
      GL11.glBegin(1);
      GL11.glVertex2d(var1, var3);
      GL11.glVertex2d(var5, var7);
      GL11.glEnd();
      GL11.glEnable(3553);
   }
}
