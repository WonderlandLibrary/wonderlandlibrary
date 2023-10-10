package my.NewSnake.utils;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class MCFontRenderer extends CFont {
   protected CFont.CharData[] boldChars = new CFont.CharData[256];
   protected DynamicTexture texItalicBold;
   protected CFont.CharData[] italicChars = new CFont.CharData[256];
   private final int[] colorCode = new int[32];
   protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
   protected DynamicTexture texBold;
   protected DynamicTexture texItalic;

   public float drawCenteredStringWithShadow(String var1, float var2, float var3, int var4) {
      this.drawString(var1, (double)(var2 - (float)(this.getStringWidth(var1) / 2)) + 0.6D, (double)var3 + 0.6D, var4, true);
      return this.drawString(var1, var2 - (float)(this.getStringWidth(var1) / 2), var3, var4);
   }

   public MCFontRenderer(Font var1, boolean var2, boolean var3) {
      super(var1, var2, var3);
      this.setupMinecraftColorcodes();
      this.setupBoldItalicIDs();
   }

   public float drawCenteredString(String var1, float var2, float var3, int var4) {
      return this.drawString(var1, var2 - (float)(this.getStringWidth(var1) / 2), var3, var4);
   }

   public void setFractionalMetrics(boolean var1) {
      super.setFractionalMetrics(var1);
      this.setupBoldItalicIDs();
   }

   private void setupBoldItalicIDs() {
      this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
      this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
      this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
   }

   public void setFont(Font var1) {
      super.setFont(var1);
      this.setupBoldItalicIDs();
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
            var6 = -16777216;
         }

         CFont.CharData[] var8 = this.charData;
         float var9 = (float)(var6 >> 24 & 255) / 255.0F;
         boolean var10 = false;
         boolean var11 = false;
         boolean var12 = false;
         boolean var13 = false;
         var2 *= 2.0D;
         var4 = (var4 - 3.0D) * 2.0D;
         GL11.glPushMatrix();
         GlStateManager.scale(0.5D, 0.5D, 0.5D);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         GlStateManager.color((float)(var6 >> 16 & 255) / 255.0F, (float)(var6 >> 8 & 255) / 255.0F, (float)(var6 & 255) / 255.0F, var9);
         int var14 = var1.length();
         GlStateManager.enableTexture2D();
         GlStateManager.bindTexture(this.tex.getGlTextureId());
         GL11.glBindTexture(3553, this.tex.getGlTextureId());

         for(int var15 = 0; var15 < var14; ++var15) {
            char var16 = var1.charAt(var15);
            if (var16 == 167) {
               int var17 = 21;

               try {
                  var17 = "0123456789abcdefklmnor".indexOf(var1.charAt(var15 + 1));
               } catch (Exception var20) {
                  var20.printStackTrace();
               }

               if (var17 < 16) {
                  var10 = false;
                  var11 = false;
                  var13 = false;
                  var12 = false;
                  GlStateManager.bindTexture(this.tex.getGlTextureId());
                  var8 = this.charData;
                  if (var17 < 0) {
                     var17 = 15;
                  }

                  if (var7) {
                     var17 += 16;
                  }

                  int var18 = this.colorCode[var17];
                  GlStateManager.color((float)(var18 >> 16 & 255) / 255.0F, (float)(var18 >> 8 & 255) / 255.0F, (float)(var18 & 255) / 255.0F, var9);
               } else if (var17 == 17) {
                  var10 = true;
                  if (var11) {
                     GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                     var8 = this.boldItalicChars;
                  } else {
                     GlStateManager.bindTexture(this.texBold.getGlTextureId());
                     var8 = this.boldChars;
                  }
               } else if (var17 == 18) {
                  var12 = true;
               } else if (var17 == 19) {
                  var13 = true;
               } else if (var17 == 20) {
                  var11 = true;
                  if (var10) {
                     GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                     var8 = this.boldItalicChars;
                  } else {
                     GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                     var8 = this.italicChars;
                  }
               } else {
                  var10 = false;
                  var11 = false;
                  var13 = false;
                  var12 = false;
                  GlStateManager.color((float)(var6 >> 16 & 255) / 255.0F, (float)(var6 >> 8 & 255) / 255.0F, (float)(var6 & 255) / 255.0F, var9);
                  GlStateManager.bindTexture(this.tex.getGlTextureId());
                  var8 = this.charData;
               }

               ++var15;
            } else if (var16 < var8.length) {
               GL11.glBegin(4);
               this.drawChar(var8, var16, (float)var2, (float)var4);
               GL11.glEnd();
               if (var12) {
                  this.drawLine(var2, var4 + (double)(var8[var16].height / 2), var2 + (double)var8[var16].width - 8.0D, var4 + (double)(var8[var16].height / 2), 1.0F);
               }

               if (var13) {
                  this.drawLine(var2, var4 + (double)var8[var16].height - 2.0D, var2 + (double)var8[var16].width - 8.0D, var4 + (double)var8[var16].height - 2.0D, 1.0F);
               }

               var2 += (double)(var8[var16].width - 8 + this.charOffset);
            }
         }

         GL11.glHint(3155, 4352);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
         return (float)var2 / 2.0F;
      }
   }

   public List wrapWords(String var1, double var2) {
      ArrayList var4 = new ArrayList();
      if ((double)this.getStringWidth(var1) > var2) {
         String[] var5 = var1.split(" ");
         String var6 = "";
         char var7 = '\uffff';
         String[] var11 = var5;
         int var10 = var5.length;

         for(int var9 = 0; var9 < var10; ++var9) {
            String var8 = var11[var9];

            for(int var12 = 0; var12 < var8.toCharArray().length; ++var12) {
               char var13 = var8.toCharArray()[var12];
               if (var13 == 167 && var12 < var8.toCharArray().length - 1) {
                  var7 = var8.toCharArray()[var12 + 1];
               }
            }

            if ((double)this.getStringWidth(var6 + var8 + " ") < var2) {
               var6 = var6 + var8 + " ";
            } else {
               var4.add(var6);
               var6 = "§" + var7 + var8 + " ";
            }
         }

         if (var6.length() > 0) {
            if ((double)this.getStringWidth(var6) < var2) {
               var4.add("§" + var7 + var6 + " ");
            } else {
               var4.addAll(this.formatString(var6, var2));
            }
         }
      } else {
         var4.add(var1);
      }

      return var4;
   }

   public int getStringWidth(String var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = 0;
         CFont.CharData[] var3 = this.charData;
         int var4 = var1.length();

         for(int var5 = 0; var5 < var4; ++var5) {
            char var6 = var1.charAt(var5);
            if (var6 == 167) {
               ++var5;
            } else if (var6 < var3.length) {
               var2 += var3[var6].width - 8 + this.charOffset;
            }
         }

         return var2 / 2;
      }
   }

   public float drawString(String var1, float var2, float var3, int var4) {
      return this.drawString(var1, (double)var2, (double)var3, var4, false);
   }

   public void setAntiAlias(boolean var1) {
      super.setAntiAlias(var1);
      this.setupBoldItalicIDs();
   }

   public float drawStringWithShadow(String var1, double var2, double var4, int var6) {
      float var7 = this.drawString(var1, var2 + 0.9D, var4 + 0.7D, var6, true);
      return Math.max(var7, this.drawString(var1, var2, var4, var6, false));
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

   public List formatString(String var1, double var2) {
      ArrayList var4 = new ArrayList();
      String var5 = "";
      char var6 = '\uffff';
      char[] var7 = var1.toCharArray();

      for(int var8 = 0; var8 < var7.length; ++var8) {
         char var9 = var7[var8];
         if (var9 == '&' && var8 < var7.length - 1) {
            var6 = var7[var8 + 1];
         }

         if ((double)this.getStringWidth(var5 + var9) < var2) {
            var5 = var5 + var9;
         } else {
            var4.add(var5);
            var5 = "§" + var6 + var9;
         }
      }

      if (var5.length() > 0) {
         var4.add(var5);
      }

      return var4;
   }

   private void setupMinecraftColorcodes() {
      for(int var1 = 0; var1 < 32; ++var1) {
         int var2 = (var1 >> 3 & 1) * 85;
         int var3 = (var1 >> 2 & 1) * 170 + var2;
         int var4 = (var1 >> 1 & 1) * 170 + var2;
         int var5 = (var1 & 1) * 170 + var2;
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
}
