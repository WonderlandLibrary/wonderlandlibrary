package my.NewSnake.Tank.Ui.font;

import java.awt.Font;
import java.util.Locale;
import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class GlyphPageFontRenderer {
   private boolean boldStyle;
   private boolean italicStyle;
   private float posX;
   private boolean strikethroughStyle;
   public Random fontRandom = new Random();
   private float posY;
   private boolean underlineStyle;
   private float green;
   private boolean randomStyle;
   private float blue;
   private GlyphPage italicGlyphPage;
   private GlyphPage boldItalicGlyphPage;
   private int[] colorCode = new int[32];
   private float alpha;
   private float red;
   private GlyphPage boldGlyphPage;
   private int textColor;
   private GlyphPage regularGlyphPage;

   private void renderStringAtPos(String var1, boolean var2) {
      GlyphPage var3 = this.getCurrentGlyphPage();
      GL11.glPushMatrix();
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GlStateManager.enableTexture2D();
      var3.bindTexture();
      GL11.glTexParameteri(3553, 10240, 9729);

      for(int var4 = 0; var4 < var1.length(); ++var4) {
         char var5 = var1.charAt(var4);
         if (var5 == 167 && var4 + 1 < var1.length()) {
            int var9 = "0123456789abcdefklmnor".indexOf(var1.toLowerCase(Locale.ENGLISH).charAt(var4 + 1));
            if (var9 < 16) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               if (var9 < 0) {
                  var9 = 15;
               }

               if (var2) {
                  var9 += 16;
               }

               int var7 = this.colorCode[var9];
               this.textColor = var7;
               GlStateManager.color((float)(var7 >> 16) / 255.0F, (float)(var7 >> 8 & 255) / 255.0F, (float)(var7 & 255) / 255.0F, this.alpha);
            } else if (var9 == 16) {
               this.randomStyle = true;
            } else if (var9 == 17) {
               this.boldStyle = true;
            } else if (var9 == 18) {
               this.strikethroughStyle = true;
            } else if (var9 == 19) {
               this.underlineStyle = true;
            } else if (var9 == 20) {
               this.italicStyle = true;
            } else {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               GlStateManager.color(this.red, this.blue, this.green, this.alpha);
            }

            ++var4;
         } else {
            var3 = this.getCurrentGlyphPage();
            var3.bindTexture();
            float var6 = var3.drawChar(var5, this.posX, this.posY);
            this.doDraw(var6, var3);
         }
      }

      var3.unbindTexture();
      GL11.glPopMatrix();
   }

   public GlyphPageFontRenderer(GlyphPage var1, GlyphPage var2, GlyphPage var3, GlyphPage var4) {
      this.regularGlyphPage = var1;
      this.boldGlyphPage = var2;
      this.italicGlyphPage = var3;
      this.boldItalicGlyphPage = var4;

      for(int var5 = 0; var5 < 32; ++var5) {
         int var6 = (var5 >> 3 & 1) * 85;
         int var7 = (var5 >> 2 & 1) * 170 + var6;
         int var8 = (var5 >> 1 & 1) * 170 + var6;
         int var9 = (var5 & 1) * 170 + var6;
         if (var5 == 6) {
            var7 += 85;
         }

         if (var5 >= 16) {
            var7 /= 4;
            var8 /= 4;
            var9 /= 4;
         }

         this.colorCode[var5] = (var7 & 255) << 16 | (var8 & 255) << 8 | var9 & 255;
      }

   }

   private void resetStyles() {
      this.randomStyle = false;
      this.boldStyle = false;
      this.italicStyle = false;
      this.underlineStyle = false;
      this.strikethroughStyle = false;
   }

   public String trimStringToWidth(String var1, int var2) {
      return this.trimStringToWidth(var1, var2, false);
   }

   private GlyphPage getCurrentGlyphPage() {
      if (this.boldStyle && this.italicStyle) {
         return this.boldItalicGlyphPage;
      } else if (this.boldStyle) {
         return this.boldGlyphPage;
      } else {
         return this.italicStyle ? this.italicGlyphPage : this.regularGlyphPage;
      }
   }

   public int drawString(String var1, float var2, float var3, int var4, boolean var5) {
      GlStateManager.enableAlpha();
      this.resetStyles();
      int var6;
      if (var5) {
         var6 = this.renderString(var1, var2 + 0.6F, var3 + 0.6F, var4, true);
         var6 = Math.max(var6, this.renderString(var1, var2, var3, var4, false));
      } else {
         var6 = this.renderString(var1, var2, var3, var4, false);
      }

      return var6;
   }

   public static GlyphPageFontRenderer create(String var0, int var1, boolean var2, boolean var3, boolean var4) {
      char[] var5 = new char[256];

      for(int var6 = 0; var6 < var5.length; ++var6) {
         var5[var6] = (char)var6;
      }

      GlyphPage var10 = new GlyphPage(new Font(var0, 0, var1), true, true);
      var10.generateGlyphPage(var5);
      var10.setupTexture();
      GlyphPage var7 = var10;
      GlyphPage var8 = var10;
      GlyphPage var9 = var10;
      if (var2) {
         var7 = new GlyphPage(new Font(var0, 1, var1), true, true);
         var7.generateGlyphPage(var5);
         var7.setupTexture();
      }

      if (var3) {
         var8 = new GlyphPage(new Font(var0, 2, var1), true, true);
         var8.generateGlyphPage(var5);
         var8.setupTexture();
      }

      if (var4) {
         var9 = new GlyphPage(new Font(var0, 3, var1), true, true);
         var9.generateGlyphPage(var5);
         var9.setupTexture();
      }

      return new GlyphPageFontRenderer(var10, var7, var8, var9);
   }

   public int getFontHeight() {
      return this.regularGlyphPage.getMaxFontHeight() / 2;
   }

   private int renderString(String var1, float var2, float var3, int var4, boolean var5) {
      if (var1 == null) {
         return 0;
      } else {
         if ((var4 & -67108864) == 0) {
            var4 |= -16777216;
         }

         if (var5) {
            var4 = (var4 & 16579836) >> 2 | var4 & -16777216;
         }

         this.red = (float)(var4 >> 16 & 255) / 255.0F;
         this.blue = (float)(var4 >> 8 & 255) / 255.0F;
         this.green = (float)(var4 & 255) / 255.0F;
         this.alpha = (float)(var4 >> 24 & 255) / 255.0F;
         GlStateManager.color(this.red, this.blue, this.green, this.alpha);
         this.posX = var2 * 2.0F;
         this.posY = var3 * 2.0F;
         this.renderStringAtPos(var1, var5);
         return (int)(this.posX / 4.0F);
      }
   }

   public String trimStringToWidth(String var1, int var2, boolean var3) {
      StringBuilder var4 = new StringBuilder();
      boolean var5 = false;
      int var6 = var3 ? var1.length() - 1 : 0;
      int var7 = var3 ? -1 : 1;
      int var8 = 0;

      for(int var10 = var6; var10 >= 0 && var10 < var1.length() && var10 < var2; var10 += var7) {
         char var11 = var1.charAt(var10);
         if (var11 == 167) {
            var5 = true;
         } else if (var5 && var11 >= '0' && var11 <= 'r') {
            int var12 = "0123456789abcdefklmnor".indexOf(var11);
            if (var12 < 16) {
               this.boldStyle = false;
               this.italicStyle = false;
            } else if (var12 == 17) {
               this.boldStyle = true;
            } else if (var12 == 20) {
               this.italicStyle = true;
            } else if (var12 == 21) {
               this.boldStyle = false;
               this.italicStyle = false;
            }

            ++var10;
            var5 = false;
         } else {
            if (var5) {
               --var10;
            }

            var11 = var1.charAt(var10);
            GlyphPage var9 = this.getCurrentGlyphPage();
            var8 = (int)((float)var8 + (var9.getWidth(var11) - 8.0F) / 2.0F);
         }

         if (var10 > var8) {
            break;
         }

         if (var3) {
            var4.insert(0, var11);
         } else {
            var4.append(var11);
         }
      }

      return String.valueOf(var4);
   }

   public int getStringWidth(String var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = 0;
         int var4 = var1.length();
         boolean var5 = false;

         for(int var6 = 0; var6 < var4; ++var6) {
            char var7 = var1.charAt(var6);
            if (var7 == 167) {
               var5 = true;
            } else if (var5 && var7 >= '0' && var7 <= 'r') {
               int var8 = "0123456789abcdefklmnor".indexOf(var7);
               if (var8 < 16) {
                  this.boldStyle = false;
                  this.italicStyle = false;
               } else if (var8 == 17) {
                  this.boldStyle = true;
               } else if (var8 == 20) {
                  this.italicStyle = true;
               } else if (var8 == 21) {
                  this.boldStyle = false;
                  this.italicStyle = false;
               }

               ++var6;
               var5 = false;
            } else {
               if (var5) {
                  --var6;
               }

               var7 = var1.charAt(var6);
               GlyphPage var3 = this.getCurrentGlyphPage();
               var2 = (int)((float)var2 + (var3.getWidth(var7) - 8.0F));
            }
         }

         return var2 / 2;
      }
   }

   private void doDraw(float var1, GlyphPage var2) {
      Tessellator var3;
      WorldRenderer var4;
      if (this.strikethroughStyle) {
         var3 = Tessellator.getInstance();
         var4 = var3.getWorldRenderer();
         GlStateManager.disableTexture2D();
         var4.begin(7, DefaultVertexFormats.POSITION);
         var4.pos((double)this.posX, (double)(this.posY + (float)(var2.getMaxFontHeight() / 2)), 0.0D).endVertex();
         var4.pos((double)(this.posX + var1), (double)(this.posY + (float)(var2.getMaxFontHeight() / 2)), 0.0D).endVertex();
         var4.pos((double)(this.posX + var1), (double)(this.posY + (float)(var2.getMaxFontHeight() / 2) - 1.0F), 0.0D).endVertex();
         var4.pos((double)this.posX, (double)(this.posY + (float)(var2.getMaxFontHeight() / 2) - 1.0F), 0.0D).endVertex();
         var3.draw();
         GlStateManager.enableTexture2D();
      }

      if (this.underlineStyle) {
         var3 = Tessellator.getInstance();
         var4 = var3.getWorldRenderer();
         GlStateManager.disableTexture2D();
         var4.begin(7, DefaultVertexFormats.POSITION);
         int var5 = this.underlineStyle ? -1 : 0;
         var4.pos((double)(this.posX + (float)var5), (double)(this.posY + (float)var2.getMaxFontHeight()), 0.0D).endVertex();
         var4.pos((double)(this.posX + var1), (double)(this.posY + (float)var2.getMaxFontHeight()), 0.0D).endVertex();
         var4.pos((double)(this.posX + var1), (double)(this.posY + (float)var2.getMaxFontHeight() - 1.0F), 0.0D).endVertex();
         var4.pos((double)(this.posX + (float)var5), (double)(this.posY + (float)var2.getMaxFontHeight() - 1.0F), 0.0D).endVertex();
         var3.draw();
         GlStateManager.enableTexture2D();
      }

      this.posX += var1;
   }
}
