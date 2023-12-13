package my.NewSnake.utils.minecraft;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

public class FontRenderer implements IResourceManagerReloadListener {
   private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
   private boolean strikethroughStyle;
   private int textColor;
   private final TextureManager renderEngine;
   private static float[] charWidth;
   public int[] colorCode;
   private boolean underlineStyle;
   private byte[] glyphWidth;
   private float green;
   public float scaleFactor;
   private double posY;
   private float red;
   public GameSettings gameSettings;
   private boolean italicStyle;
   private float blue;
   public ResourceLocation locationFontTextureBase;
   private static ResourceLocation locationFontTexture;
   private double posX;
   private boolean unicodeFlag;
   private float alpha;
   public Random fontRandom;
   public int FONT_HEIGHT;
   private boolean bidiFlag;
   private boolean boldStyle;
   private boolean randomStyle;
   public boolean enabled;

   private float renderDefaultChar(int var1, boolean var2) {
      float var3 = (float)(var1 % 16 * 8);
      float var4 = (float)(var1 / 16 * 8);
      float var5 = var2 ? 1.0F : 0.0F;
      this.renderEngine.bindTexture(locationFontTexture);
      float var6 = 7.99F;
      GL11.glBegin(5);
      GL11.glTexCoord2f(var3 / 128.0F, var4 / 128.0F);
      GL11.glVertex3d(this.posX + (double)var5, this.posY, 0.0D);
      GL11.glTexCoord2f(var3 / 128.0F, (var4 + 7.99F) / 128.0F);
      GL11.glVertex3d(this.posX - (double)var5, this.posY + 7.989999771118164D, 0.0D);
      GL11.glTexCoord2f((var3 + 7.99F - 1.0F) / 128.0F, var4 / 128.0F);
      GL11.glVertex3d(this.posX + 7.989999771118164D - 1.0D + (double)var5, this.posY, 0.0D);
      GL11.glTexCoord2f((var3 + 7.99F - 1.0F) / 128.0F, (var4 + 7.99F) / 128.0F);
      GL11.glVertex3d(this.posX + 7.989999771118164D - 1.0D - (double)var5, this.posY + 7.989999771118164D, 0.0D);
      GL11.glEnd();
      return charWidth[var1];
   }

   public int func_175064_b(char var1) {
      int var2 = "0123456789abcdef".indexOf(var1);
      return var2 >= 0 && var2 < this.colorCode.length ? this.colorCode[var2] : 16777215;
   }

   private static ResourceLocation getHdFontLocation(ResourceLocation var0) {
      String var1 = var0.getResourcePath();
      String var2 = "textures/";
      String var3 = "client/";
      if (!var1.startsWith("textures/")) {
         return var0;
      } else {
         var1 = var1.substring("textures/".length());
         var1 = String.valueOf("client/") + var1;
         ResourceLocation var4 = new ResourceLocation(var0.getResourceDomain(), var1);
         return Config.hasResource(Config.getResourceManager(), var4) ? var4 : var0;
      }
   }

   public int drawString(String var1, double var2, double var4, int var6) {
      return this.enabled ? this.func_175065_a(var1, var2, var4, var6, false) : 0;
   }

   private float getCharWidthFloat(char var1) {
      if (var1 == 167) {
         return -1.0F;
      } else if (var1 == ' ') {
         return charWidth[32];
      } else {
         int var2 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜøÂ£Ø×ƒáíóúñÑÂªÂºÂ¿Â®Â¬Â½Â¼Â¡Â«Â»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡Â±≥≤⌠⌡÷≈Â°∙Â·√ⁿÂ²■\u0000".indexOf(var1);
         if (var1 > 0 && var2 != -1 && !this.unicodeFlag) {
            return charWidth[var2];
         } else if (this.glyphWidth[var1] != 0) {
            int var3 = this.glyphWidth[var1] >>> 4;
            int var4 = this.glyphWidth[var1] & 15;
            if (var4 > 7) {
               var4 = 15;
               var3 = 0;
            }

            ++var4;
            return (float)((var4 - var3) / 2 + 1);
         } else {
            return 0.0F;
         }
      }
   }

   private String bidiReorder(String var1) {
      try {
         Bidi var2 = new Bidi((new ArabicShaping(8)).shape(var1), 127);
         var2.setReorderingMode(0);
         return var2.writeReordered(2);
      } catch (ArabicShapingException var3) {
         return var1;
      }
   }

   public static String getFormatFromString(String var0) {
      String var1 = "";
      int var2 = -1;
      int var3 = var0.length();

      while((var2 = var0.indexOf(167, var2 + 1)) != -1) {
         if (var2 < var3 - 1) {
            char var4 = var0.charAt(var2 + 1);
            if (var4 != 0) {
               var1 = "§" + var4;
            } else if (var4 == 0) {
               var1 = String.valueOf(var1) + "§" + var4;
            }
         }
      }

      return var1;
   }

   private void readFontTexture() {
      BufferedImage var1;
      try {
         var1 = TextureUtil.readBufferedImage(ClientUtils.mc().getResourceManager().getResource(locationFontTexture).getInputStream());
      } catch (IOException var19) {
         throw new RuntimeException(var19);
      }

      int var2 = var1.getWidth();
      int var3 = var1.getHeight();
      int var4 = var2 / 16;
      int var5 = var3 / 16;
      float var6 = (float)var2 / 128.0F;
      this.scaleFactor = var6;
      int[] var7 = new int[var2 * var3];
      var1.getRGB(0, 0, var2, var3, var7, 0, var2);

      for(int var8 = 0; var8 < 256; ++var8) {
         int var9 = var8 % 16;
         int var10 = var8 / 16;
         boolean var11 = false;

         int var12;
         for(var12 = var4 - 1; var12 >= 0; --var12) {
            int var13 = var9 * var4 + var12;
            boolean var14 = true;

            for(int var15 = 0; var15 < var5 && var14; ++var15) {
               int var16 = (var10 * var5 + var15) * var2;
               int var17 = var7[var13 + var16];
               int var18 = var17 >> 24 & 255;
               if (var18 > 16) {
                  var14 = false;
               }
            }

            if (!var14) {
               break;
            }
         }

         if (var8 == 32) {
            if (var4 <= 8) {
               var12 = (int)(2.0F * var6);
            } else {
               var12 = (int)(1.5F * var6);
            }
         }

         charWidth[var8] = (float)(var12 + 1) / var6 + 1.0F;
      }

      readCustomCharWidths();
   }

   public void drawCenteredString(String var1, double var2, double var4, int var6) {
      this.drawStringWithShadow(var1, (double)((float)(var2 - (double)(this.getStringWidth(var1) / 2))), (double)((float)var4), var6);
   }

   public FontRenderer(GameSettings var1, ResourceLocation var2, TextureManager var3, boolean var4) {
      charWidth = new float[256];
      this.FONT_HEIGHT = 9;
      this.fontRandom = new Random();
      this.glyphWidth = new byte[65536];
      this.colorCode = new int[32];
      this.enabled = true;
      this.scaleFactor = 1.0F;
      this.gameSettings = var1;
      this.locationFontTextureBase = var2;
      locationFontTexture = var2;
      this.renderEngine = var3;
      this.unicodeFlag = var4;
      var3.bindTexture(locationFontTexture);

      for(int var5 = 0; var5 < 32; ++var5) {
         int var6 = (var5 >> 3 & 1) * 85;
         int var7 = (var5 >> 2 & 1) * 170 + var6;
         int var8 = (var5 >> 1 & 1) * 170 + var6;
         int var9 = (var5 >> 0 & 1) * 170 + var6;
         if (var5 == 6) {
            var7 += 85;
         }

         if (var1.anaglyph) {
            int var10 = (var7 * 30 + var8 * 59 + var9 * 11) / 100;
            int var11 = (var7 * 30 + var8 * 70) / 100;
            int var12 = (var7 * 30 + var9 * 70) / 100;
            var7 = var10;
            var8 = var11;
            var9 = var12;
         }

         if (var5 >= 16) {
            var7 /= 4;
            var8 /= 4;
            var9 /= 4;
         }

         this.colorCode[var5] = (var7 & 255) << 16 | (var8 & 255) << 8 | var9 & 255;
      }

      this.readGlyphSizes();
   }

   private ResourceLocation getUnicodePageLocation(int var1) {
      if (unicodePageLocations[var1] == null) {
         unicodePageLocations[var1] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", var1));
         unicodePageLocations[var1] = getHdFontLocation(unicodePageLocations[var1]);
      }

      return unicodePageLocations[var1];
   }

   public void drawScaledString(String var1, double var2, double var4, int var6, double var7) {
      int var9 = this.getStringWidth(var1);
      var9 *= (int)var7;
      double var10 = var2 - (double)(var9 / 2);
      double var12 = var4 - (double)this.FONT_HEIGHT * var7 / 2.0D;
      var10 /= var7;
      var12 /= var7;
      GL11.glPushMatrix();
      GL11.glScaled(var7, var7, 1.0D);
      this.drawString(var1, var10, var12, var6);
      GL11.glPopMatrix();
   }

   public void setBidiFlag(boolean var1) {
      this.bidiFlag = var1;
   }

   private void loadGlyphTexture(int var1) {
      this.renderEngine.bindTexture(this.getUnicodePageLocation(var1));
   }

   public boolean getUnicodeFlag() {
      return this.unicodeFlag;
   }

   private String trimStringNewline(String var1) {
      while(var1 != null && var1.endsWith("\n")) {
         var1 = var1.substring(0, var1.length() - 1);
      }

      return var1;
   }

   public void drawSplitString(String var1, int var2, int var3, int var4, int var5) {
      this.resetStyles();
      this.textColor = var5;
      var1 = this.trimStringNewline(var1);
      this.renderSplitString(var1, var2, var3, var4, false);
   }

   private void renderStringAtPos(String var1, boolean var2) {
      for(int var3 = 0; var3 < var1.length(); ++var3) {
         char var4 = var1.charAt(var3);
         int var5;
         int var6;
         if (var4 == 167 && var3 + 1 < var1.length()) {
            var5 = "0123456789abcdefklmnor".indexOf(var1.toLowerCase().charAt(var3 + 1));
            if (var5 < 16) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               if (var5 < 0 || var5 > 15) {
                  var5 = 15;
               }

               if (var2) {
                  var5 += 16;
               }

               var6 = this.colorCode[var5];
               this.textColor = var6;
               GlStateManager.color((float)(var6 >> 16) / 255.0F, (float)(var6 >> 8 & 255) / 255.0F, (float)(var6 & 255) / 255.0F, this.alpha);
            } else if (var5 == 16) {
               this.randomStyle = true;
            } else if (var5 == 17) {
               this.boldStyle = true;
            } else if (var5 == 18) {
               this.strikethroughStyle = true;
            } else if (var5 == 19) {
               this.underlineStyle = true;
            } else if (var5 == 20) {
               this.italicStyle = true;
            } else if (var5 == 21) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               GlStateManager.color(this.red, this.blue, this.green, this.alpha);
            }

            ++var3;
         } else {
            var5 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜøÂ£Ø×ƒáíóúñÑÂªÂºÂ¿Â®Â¬Â½Â¼Â¡Â«Â»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡Â±≥≤⌠⌡÷≈Â°∙Â·√ⁿÂ²■\u0000".indexOf(var4);
            if (this.randomStyle && var5 != -1) {
               do {
                  var6 = this.fontRandom.nextInt(charWidth.length);
               } while((int)charWidth[var5] != (int)charWidth[var6]);

               var5 = var6;
            }

            float var12 = this.unicodeFlag ? 0.5F : 1.0F / this.scaleFactor;
            boolean var7 = (var4 == 0 || var5 == -1 || this.unicodeFlag) && var2;
            if (var7) {
               this.posX -= (double)var12;
               this.posY -= (double)var12;
            }

            float var8 = this.renderCharAtPos(var5, var4, this.italicStyle);
            if (var7) {
               this.posX += (double)var12;
               this.posY += (double)var12;
            }

            if (this.boldStyle) {
               this.posX += (double)var12;
               if (var7) {
                  this.posX -= (double)var12;
                  this.posY -= (double)var12;
               }

               this.renderCharAtPos(var5, var4, this.italicStyle);
               this.posX -= (double)var12;
               if (var7) {
                  this.posX += (double)var12;
                  this.posY += (double)var12;
               }

               var8 += var12;
            }

            Tessellator var9;
            WorldRenderer var10;
            if (this.strikethroughStyle) {
               var9 = Tessellator.getInstance();
               var10 = var9.getWorldRenderer();
               GlStateManager.disableTexture2D();
               var10.begin(7, DefaultVertexFormats.POSITION);
               var10.pos(this.posX, this.posY + (double)((float)(this.FONT_HEIGHT / 2)), 0.0D).endVertex();
               var10.pos(this.posX + (double)var8, this.posY + (double)((float)(this.FONT_HEIGHT / 2)), 0.0D).endVertex();
               var10.pos(this.posX + (double)var8, this.posY + (double)((float)(this.FONT_HEIGHT / 2)) - 1.0D, 0.0D).endVertex();
               var10.pos(this.posX, this.posY + (double)((float)(this.FONT_HEIGHT / 2)) - 1.0D, 0.0D).endVertex();
               var9.draw();
               GlStateManager.enableTexture2D();
            }

            if (this.underlineStyle) {
               var9 = Tessellator.getInstance();
               var10 = var9.getWorldRenderer();
               GlStateManager.disableTexture2D();
               var10.begin(7, DefaultVertexFormats.POSITION);
               int var11 = this.underlineStyle ? -1 : 0;
               var10.pos(this.posX + (double)((float)var11), this.posY + (double)((float)this.FONT_HEIGHT), 0.0D).endVertex();
               var10.pos(this.posX + (double)var8, this.posY + (double)((float)this.FONT_HEIGHT), 0.0D).endVertex();
               var10.pos(this.posX + (double)var8, this.posY + (double)((float)this.FONT_HEIGHT) - 1.0D, 0.0D).endVertex();
               var10.pos(this.posX + (double)((float)var11), this.posY + (double)((float)this.FONT_HEIGHT) - 1.0D, 0.0D).endVertex();
               var9.draw();
               GlStateManager.enableTexture2D();
            }

            this.posX += (double)var8;
         }
      }

   }

   public int func_175065_a(String var1, double var2, double var4, int var6, boolean var7) {
      GlStateManager.enableAlpha();
      this.resetStyles();
      int var8;
      if (var7) {
         var8 = this.func_180455_b(var1, var2 + 0.800000011920929D, var4 + 0.800000011920929D, var6, true);
         var8 = Math.max(var8, this.func_180455_b(var1, var2, var4, var6, false));
      } else {
         var8 = this.func_180455_b(var1, var2, var4, var6, false);
      }

      return var8;
   }

   public void setUnicodeFlag(boolean var1) {
      this.unicodeFlag = var1;
   }

   private void resetStyles() {
      this.randomStyle = false;
      this.boldStyle = false;
      this.italicStyle = false;
      this.underlineStyle = false;
      this.strikethroughStyle = false;
   }

   private int func_180455_b(String var1, double var2, double var4, int var6, boolean var7) {
      if (var1 == null) {
         return 0;
      } else {
         if (this.bidiFlag) {
            var1 = this.bidiReorder(var1);
         }

         if ((var6 & -67108864) == 0) {
            var6 |= -16777216;
         }

         if (var7) {
            var6 = (var6 & 16579836) >> 2 | var6 & -16777216;
         }

         this.red = (float)(var6 >> 16 & 255) / 255.0F;
         this.blue = (float)(var6 >> 8 & 255) / 255.0F;
         this.green = (float)(var6 & 255) / 255.0F;
         this.alpha = (float)(var6 >> 24 & 255) / 255.0F;
         GlStateManager.color(this.red, this.blue, this.green, this.alpha);
         this.posX = var2;
         this.posY = var4;
         this.renderStringAtPos(var1, var7);
         return (int)this.posX;
      }
   }

   public int getStringWidth(String var1) {
      if (var1 == null) {
         return 0;
      } else {
         float var2 = 0.0F;
         boolean var3 = false;

         for(int var4 = 0; var4 < var1.length(); ++var4) {
            char var5 = var1.charAt(var4);
            float var6 = this.getCharWidthFloat(var5);
            if (var6 < 0.0F && var4 < var1.length() - 1) {
               ++var4;
               var5 = var1.charAt(var4);
               if (var5 != 'l' && var5 != 'L') {
                  if (var5 == 'r' || var5 == 'R') {
                     var3 = false;
                  }
               } else {
                  var3 = true;
               }

               var6 = 0.0F;
            }

            var2 += var6;
            if (var3 && var6 > 0.0F) {
               ++var2;
            }
         }

         return (int)var2;
      }
   }

   private int renderStringAligned(String var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (this.bidiFlag) {
         int var7 = this.getStringWidth(this.bidiReorder(var1));
         var2 = var2 + var4 - var7;
      }

      return this.func_180455_b(var1, (double)var2, (double)var3, var5, var6);
   }

   public int getCharWidth(char var1) {
      return Math.round(this.getCharWidthFloat(var1));
   }

   public int drawStringWithShadow(String var1, double var2, double var4, int var6) {
      return this.func_175065_a(var1, var2, var4, var6, true);
   }

   public String trimStringToWidth(String var1, int var2, boolean var3) {
      StringBuilder var4 = new StringBuilder();
      float var5 = 0.0F;
      int var6 = var3 ? var1.length() - 1 : 0;
      int var7 = var3 ? -1 : 1;
      boolean var8 = false;
      boolean var9 = false;

      for(int var10 = var6; var10 >= 0 && var10 < var1.length() && var5 < (float)var2; var10 += var7) {
         char var11 = var1.charAt(var10);
         float var12 = this.getCharWidthFloat(var11);
         if (var8) {
            var8 = false;
            if (var11 != 'l' && var11 != 'L') {
               if (var11 == 'r' || var11 == 'R') {
                  var9 = false;
               }
            } else {
               var9 = true;
            }
         } else if (var12 < 0.0F) {
            var8 = true;
         } else {
            var5 += var12;
            if (var9) {
               ++var5;
            }
         }

         if (var5 > (float)var2) {
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

   public int splitStringWidth(String var1, int var2) {
      return this.FONT_HEIGHT * this.listFormattedStringToWidth(var1, var2).size();
   }

   private void renderSplitString(String var1, int var2, int var3, int var4, boolean var5) {
      List var6 = this.listFormattedStringToWidth(var1, var4);

      for(Iterator var7 = var6.iterator(); var7.hasNext(); var3 += this.FONT_HEIGHT) {
         String var8 = (String)var7.next();
         this.renderStringAligned(var8, var2, var3, var4, this.textColor, var5);
         var3 += this.FONT_HEIGHT;
      }

   }

   public void onResourceManagerReload(IResourceManager var1) {
      locationFontTexture = getHdFontLocation(this.locationFontTextureBase);

      for(int var2 = 0; var2 < unicodePageLocations.length; ++var2) {
         unicodePageLocations[var2] = null;
      }

      this.readFontTexture();
   }

   private static void readCustomCharWidths() {
      String var0 = locationFontTexture.getResourcePath();
      String var1 = ".png";
      if (var0.endsWith(var1)) {
         String var2 = var0.substring(0, var0.length() - var1.length()) + ".properties";

         try {
            ResourceLocation var3 = new ResourceLocation(locationFontTexture.getResourceDomain(), var2);
            InputStream var4 = Config.getResourceStream(Config.getResourceManager(), var3);
            if (var4 == null) {
               return;
            }

            Config.log("Loading " + var2);
            Properties var5 = new Properties();
            var5.load(var4);
            Set var6 = var5.keySet();
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               String var8 = (String)var7.next();
               String var9 = "width.";
               if (var8.startsWith(var9)) {
                  String var10 = var8.substring(var9.length());
                  int var11 = Config.parseInt(var10, -1);
                  if (var11 >= 0 && var11 < charWidth.length) {
                     String var12 = var5.getProperty(var8);
                     float var13 = Config.parseFloat(var12, -1.0F);
                     if (var13 >= 0.0F) {
                        charWidth[var11] = var13;
                     }
                  }
               }
            }
         } catch (FileNotFoundException var15) {
         } catch (IOException var16) {
            var16.printStackTrace();
         }
      }

   }

   protected String wrapFormattedStringToWidth(String var1, int var2) {
      int var3 = this.sizeStringToWidth(var1, var2);
      if (var1.length() <= var3) {
         return var1;
      } else {
         String var4 = var1.substring(0, var3);
         char var5 = var1.charAt(var3);
         boolean var6 = var5 == ' ' || var5 == '\n';
         String var7 = String.valueOf(getFormatFromString(var4)) + var1.substring(var3 + (var6 ? 1 : 0));
         return String.valueOf(var4) + "\n" + this.wrapFormattedStringToWidth(var7, var2);
      }
   }

   public List listFormattedStringToWidth(String var1, int var2) {
      return Arrays.asList(this.wrapFormattedStringToWidth(var1, var2).split("\n"));
   }

   public String trimStringToWidth(String var1, int var2) {
      return this.trimStringToWidth(var1, var2, false);
   }

   public boolean getBidiFlag() {
      return this.bidiFlag;
   }

   private int sizeStringToWidth(String param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   private float renderUnicodeChar(char var1, boolean var2) {
      if (this.glyphWidth[var1] == 0) {
         return 0.0F;
      } else {
         int var3 = var1 / 256;
         this.loadGlyphTexture(var3);
         int var4 = this.glyphWidth[var1] >>> 4;
         int var5 = this.glyphWidth[var1] & 15;
         float var6 = (float)var4;
         float var7 = (float)(var5 + 1);
         float var8 = (float)(var1 % 16 * 16) + var6;
         float var9 = (float)((var1 & 255) / 16 * 16);
         float var10 = var7 - var6 - 0.02F;
         float var11 = var2 ? 1.0F : 0.0F;
         GL11.glBegin(5);
         GL11.glTexCoord2f(var8 / 256.0F, var9 / 256.0F);
         GL11.glVertex3d(this.posX + (double)var11, this.posY, 0.0D);
         GL11.glTexCoord2f(var8 / 256.0F, (var9 + 15.98F) / 256.0F);
         GL11.glVertex3d(this.posX - (double)var11, this.posY + 7.989999771118164D, 0.0D);
         GL11.glTexCoord2f((var8 + var10) / 256.0F, var9 / 256.0F);
         GL11.glVertex3d(this.posX + (double)(var10 / 2.0F) + (double)var11, this.posY, 0.0D);
         GL11.glTexCoord2f((var8 + var10) / 256.0F, (var9 + 15.98F) / 256.0F);
         GL11.glVertex3d(this.posX + (double)(var10 / 2.0F) - (double)var11, this.posY + 7.989999771118164D, 0.0D);
         GL11.glEnd();
         return (var7 - var6) / 2.0F + 1.0F;
      }
   }

   private float renderCharAtPos(int var1, char var2, boolean var3) {
      return var2 == ' ' ? charWidth[var2] : (var2 == ' ' ? 4.0F : ("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜøÂ£Ø×ƒáíóúñÑÂªÂºÂ¿Â®Â¬Â½Â¼Â¡Â«Â»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡Â±≥≤⌠⌡÷≈Â°∙Â·√ⁿÂ²■\u0000".indexOf(var2) != -1 && !this.unicodeFlag ? this.renderDefaultChar(var1, var3) : this.renderUnicodeChar(var2, var3)));
   }

   private void readGlyphSizes() {
      InputStream var1 = null;

      try {
         var1 = ClientUtils.mc().getResourceManager().getResource(new ResourceLocation("font/glyph_sizes.bin")).getInputStream();
         var1.read(this.glyphWidth);
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }

      IOUtils.closeQuietly(var1);
      IOUtils.closeQuietly(var1);
   }
}
