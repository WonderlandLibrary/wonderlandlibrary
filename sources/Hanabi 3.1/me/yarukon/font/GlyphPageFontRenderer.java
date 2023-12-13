package me.yarukon.font;

import java.awt.Font;
import java.util.Locale;
import me.yarukon.Yarukon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class GlyphPageFontRenderer {
   private float posX;
   private float posY;
   private final int[] colorCode = new int[32];
   private float red;
   private float blue;
   private float green;
   private float alpha;
   private int textColor;
   private boolean randomStyle;
   private boolean boldStyle;
   private boolean italicStyle;
   private boolean underlineStyle;
   private boolean strikethroughStyle;
   private GlyphPage regularGlyphPage;
   private GlyphPage boldGlyphPage;
   private GlyphPage italicGlyphPage;
   private GlyphPage boldItalicGlyphPage;

   public GlyphPageFontRenderer(GlyphPage regularGlyphPage, GlyphPage boldGlyphPage, GlyphPage italicGlyphPage, GlyphPage boldItalicGlyphPage) {
      this.regularGlyphPage = regularGlyphPage;
      this.boldGlyphPage = boldGlyphPage;
      this.italicGlyphPage = italicGlyphPage;
      this.boldItalicGlyphPage = boldItalicGlyphPage;

      for(int i = 0; i < 32; ++i) {
         int j = (i >> 3 & 1) * 85;
         int k = (i >> 2 & 1) * 170 + j;
         int l = (i >> 1 & 1) * 170 + j;
         int i1 = (i & 1) * 170 + j;
         if (i == 6) {
            k += 85;
         }

         if (i >= 16) {
            k /= 4;
            l /= 4;
            i1 /= 4;
         }

         this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
      }

   }

   public static GlyphPageFontRenderer create(Font font) {
      return create(font, false);
   }

   public static GlyphPageFontRenderer create(Font font, boolean allChars) {
      char[] chars = allChars ? Yarukon.INSTANCE.all_Chars : Yarukon.INSTANCE.ascii_Chars;
      GlyphPage regularPage = new GlyphPage(font, true, true, allChars);
      regularPage.generateGlyphPage(chars);
      return new GlyphPageFontRenderer(regularPage, regularPage, regularPage, regularPage);
   }

   public int drawCenteredString(String text, float x, float y, int color) {
      return this.drawString(text, x - (float)this.getStringWidth(text) / 2.0F, y, color, false);
   }

   public int drawCenteredStringWithShadow(String text, float x, float y, int color) {
      return this.drawString(text, x - (float)this.getStringWidth(text) / 2.0F, y, color, true);
   }

   public int drawStringWithShadow(String text, float x, float y, int color) {
      return this.drawString(text, x, y, color, true);
   }

   public int drawString(String text, float x, float y, int color) {
      return this.drawString(text, x, y, color, false);
   }

   public int drawString(String text, float x, float y, int color, boolean dropShadow) {
      GlStateManager.enableAlpha();
      this.resetStyles();
      int i;
      if (dropShadow) {
         i = this.renderString(text, x + 0.8F, y + 0.8F, color, true);
         i = Math.max(i, this.renderString(text, x, y, color, false));
      } else {
         i = this.renderString(text, x, y, color, false);
      }

      return i;
   }

   private int renderString(String text, float x, float y, int color, boolean dropShadow) {
      if (text == null) {
         return 0;
      } else {
         if ((color & -67108864) == 0) {
            color |= -16777216;
         }

         if (dropShadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
         }

         this.red = (float)(color >> 16 & 255) / 255.0F;
         this.blue = (float)(color >> 8 & 255) / 255.0F;
         this.green = (float)(color & 255) / 255.0F;
         this.alpha = (float)(color >> 24 & 255) / 255.0F;
         GlStateManager.color(this.red, this.blue, this.green, this.alpha);
         this.posX = x * 2.0F;
         this.posY = y * 2.0F;
         this.renderStringAtPos(text, dropShadow);
         return (int)(this.posX / 4.0F);
      }
   }

   private void renderStringAtPos(String text, boolean shadow) {
      GlyphPage glyphPage = this.getCurrentGlyphPage();
      GL11.glPushMatrix();
      GL11.glScaled(0.5, 0.5, 0.5);
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GlStateManager.enableTexture2D();
      glyphPage.bindTexture();
      GL11.glTexParameteri(3553, 10240, 9729);

      for(int i = 0; i < text.length(); ++i) {
         char c0 = text.charAt(i);
         if (c0 == 167 && i + 1 < text.length()) {
            int i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
            if (i1 < 16) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               if (i1 < 0) {
                  i1 = 15;
               }

               if (shadow) {
                  i1 += 16;
               }

               int j1 = this.colorCode[i1];
               this.textColor = j1;
               GlStateManager.color((float)(j1 >> 16) / 255.0F, (float)(j1 >> 8 & 255) / 255.0F, (float)(j1 & 255) / 255.0F, this.alpha);
            } else if (i1 == 16) {
               this.randomStyle = true;
            } else if (i1 == 17) {
               this.boldStyle = true;
            } else if (i1 == 18) {
               this.strikethroughStyle = true;
            } else if (i1 == 19) {
               this.underlineStyle = true;
            } else if (i1 == 20) {
               this.italicStyle = true;
            } else {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               GlStateManager.color(this.red, this.blue, this.green, this.alpha);
            }

            ++i;
         } else {
            glyphPage = this.getCurrentGlyphPage();
            glyphPage.bindTexture();
            float f = glyphPage.drawChar(c0, this.posX, this.posY);
            this.doDraw(f, glyphPage);
         }
      }

      glyphPage.unbindTexture();
      GL11.glPopMatrix();
   }

   private void doDraw(float f, GlyphPage glyphPage) {
      Tessellator tessellator1;
      WorldRenderer worldrenderer1;
      if (this.strikethroughStyle) {
         tessellator1 = Tessellator.getInstance();
         worldrenderer1 = tessellator1.getWorldRenderer();
         GlStateManager.disableTexture2D();
         worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
         worldrenderer1.pos((double)this.posX, (double)(this.posY + (float)(glyphPage.getMaxFontHeight() / 2)), 0.0).endVertex();
         worldrenderer1.pos((double)(this.posX + f), (double)(this.posY + (float)(glyphPage.getMaxFontHeight() / 2)), 0.0).endVertex();
         worldrenderer1.pos((double)(this.posX + f), (double)(this.posY + (float)(glyphPage.getMaxFontHeight() / 2) - 1.0F), 0.0).endVertex();
         worldrenderer1.pos((double)this.posX, (double)(this.posY + (float)(glyphPage.getMaxFontHeight() / 2) - 1.0F), 0.0).endVertex();
         tessellator1.draw();
         GlStateManager.enableTexture2D();
      }

      if (this.underlineStyle) {
         tessellator1 = Tessellator.getInstance();
         worldrenderer1 = tessellator1.getWorldRenderer();
         GlStateManager.disableTexture2D();
         worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
         int l = this.underlineStyle ? -1 : 0;
         worldrenderer1.pos((double)(this.posX + (float)l), (double)(this.posY + (float)glyphPage.getMaxFontHeight()), 0.0).endVertex();
         worldrenderer1.pos((double)(this.posX + f), (double)(this.posY + (float)glyphPage.getMaxFontHeight()), 0.0).endVertex();
         worldrenderer1.pos((double)(this.posX + f), (double)(this.posY + (float)glyphPage.getMaxFontHeight() - 1.0F), 0.0).endVertex();
         worldrenderer1.pos((double)(this.posX + (float)l), (double)(this.posY + (float)glyphPage.getMaxFontHeight() - 1.0F), 0.0).endVertex();
         tessellator1.draw();
         GlStateManager.enableTexture2D();
      }

      this.posX += f;
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

   private void resetStyles() {
      this.randomStyle = false;
      this.boldStyle = false;
      this.italicStyle = false;
      this.underlineStyle = false;
      this.strikethroughStyle = false;
   }

   public int getFontHeight() {
      return this.regularGlyphPage.getMaxFontHeight() / 2;
   }

   public int getStringWidth(String text) {
      if (text == null) {
         return 0;
      } else {
         int width = 0;
         int size = text.length();
         boolean on = false;

         for(int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == 167) {
               on = true;
            } else if (on && character >= '0' && character <= 'r') {
               int colorIndex = "0123456789abcdefklmnor".indexOf(character);
               if (colorIndex < 16) {
                  this.boldStyle = false;
                  this.italicStyle = false;
               } else if (colorIndex == 17) {
                  this.boldStyle = true;
               } else if (colorIndex == 20) {
                  this.italicStyle = true;
               } else if (colorIndex == 21) {
                  this.boldStyle = false;
                  this.italicStyle = false;
               }

               ++i;
               on = false;
            } else {
               if (on) {
                  --i;
               }

               character = text.charAt(i);
               GlyphPage currentPage = this.getCurrentGlyphPage();
               width = (int)((float)width + (currentPage.getWidth(character) - 8.0F));
            }
         }

         return width / 2;
      }
   }

   public String trimStringToWidth(String text, int width) {
      return this.trimStringToWidth(text, width, false);
   }

   public String trimStringToWidth(String text, int maxWidth, boolean reverse) {
      StringBuilder stringbuilder = new StringBuilder();
      boolean on = false;
      int j = reverse ? text.length() - 1 : 0;
      int k = reverse ? -1 : 1;
      int width = 0;

      for(int i = j; i >= 0 && i < text.length() && i < maxWidth; i += k) {
         char character = text.charAt(i);
         if (character == 167) {
            on = true;
         } else if (on && character >= '0' && character <= 'r') {
            int colorIndex = "0123456789abcdefklmnor".indexOf(character);
            if (colorIndex < 16) {
               this.boldStyle = false;
               this.italicStyle = false;
            } else if (colorIndex == 17) {
               this.boldStyle = true;
            } else if (colorIndex == 20) {
               this.italicStyle = true;
            } else if (colorIndex == 21) {
               this.boldStyle = false;
               this.italicStyle = false;
            }

            ++i;
            on = false;
         } else {
            if (on) {
               --i;
            }

            character = text.charAt(i);
            GlyphPage currentPage = this.getCurrentGlyphPage();
            width = (int)((float)width + (currentPage.getWidth(character) - 8.0F) / 2.0F);
         }

         if (i > width) {
            break;
         }

         if (reverse) {
            stringbuilder.insert(0, character);
         } else {
            stringbuilder.append(character);
         }
      }

      return stringbuilder.toString();
   }

   private static long bytesToMb(long bytes) {
      return bytes / 1024L / 1024L;
   }
}
