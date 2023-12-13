package cn.hanabi.gui.font.noway.ttfr;

import cn.hanabi.Wrapper;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class HFontRenderer implements IResourceManagerReloadListener {
   private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
   protected int[] charWidth = new int[256];
   public int FONT_HEIGHT = 9;
   public Random fontRandom = new Random();
   protected byte[] glyphWidth = new byte[65536];
   private int[] colorCode = new int[32];
   protected ResourceLocation locationFontTexture;
   private final TextureManager renderEngine;
   protected float posX;
   protected float posY;
   private boolean unicodeFlag;
   private boolean bidiFlag;
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
   private HStringCache stringCache;

   public HFontRenderer(Font font, int size, boolean antiAlias) {
      this.renderEngine = Wrapper.getMinecraft().renderEngine;
      GameSettings p_i1035_1_ = Wrapper.getGameSettings();
      ResourceLocation p_i1035_2_ = new ResourceLocation("textures/font/ascii.png");
      this.locationFontTexture = p_i1035_2_;
      this.unicodeFlag = false;
      this.renderEngine.bindTexture(this.locationFontTexture);

      for(int i = 0; i < 32; ++i) {
         int j = (i >> 3 & 1) * 85;
         int k = (i >> 2 & 1) * 170 + j;
         int l = (i >> 1 & 1) * 170 + j;
         int i1 = (i & 1) * 170 + j;
         if (i == 6) {
            k += 85;
         }

         if (p_i1035_1_.anaglyph) {
            int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
            int k1 = (k * 30 + l * 70) / 100;
            int l1 = (k * 30 + i1 * 70) / 100;
            k = j1;
            l = k1;
            i1 = l1;
         }

         if (i >= 16) {
            k /= 4;
            l /= 4;
            i1 /= 4;
         }

         this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
      }

      if (this.locationFontTexture.getResourcePath().equalsIgnoreCase("textures/font/ascii.png") && this.getStringCache() == null) {
         this.setStringCache(new HStringCache(this.colorCode));
         this.getStringCache().setDefaultFont(font, size, antiAlias);
      }

      this.readGlyphSizes();
   }

   public HStringCache getStringCache() {
      return this.stringCache;
   }

   public void setStringCache(HStringCache value) {
      this.stringCache = value;
   }

   public void onResourceManagerReload(IResourceManager p_onResourceManagerReload_1_) {
      this.readFontTexture();
      this.readGlyphSizes();
   }

   private void readFontTexture() {
      BufferedImage bufferedimage;
      try {
         bufferedimage = TextureUtil.readBufferedImage(this.getResourceInputStream(this.locationFontTexture));
      } catch (IOException var17) {
         throw new RuntimeException(var17);
      }

      int i = bufferedimage.getWidth();
      int j = bufferedimage.getHeight();
      int[] aint = new int[i * j];
      bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
      int k = j / 16;
      int l = i / 16;
      int i1 = 1;
      float f = 8.0F / (float)l;

      for(int j1 = 0; j1 < 256; ++j1) {
         int k1 = j1 % 16;
         int l1 = j1 / 16;
         if (j1 == 32) {
            this.charWidth[j1] = 3 + i1;
         }

         int i2;
         for(i2 = l - 1; i2 >= 0; --i2) {
            int j2 = k1 * l + i2;
            boolean flag = true;

            for(int k2 = 0; k2 < k && flag; ++k2) {
               int l2 = (l1 * l + k2) * i;
               if ((aint[j2 + l2] >> 24 & 255) != 0) {
                  flag = false;
               }
            }

            if (!flag) {
               break;
            }
         }

         ++i2;
         this.charWidth[j1] = (int)(0.5 + (double)((float)i2 * f)) + i1;
      }

   }

   private void readGlyphSizes() {
      InputStream inputstream = null;

      try {
         inputstream = this.getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
         inputstream.read(this.glyphWidth);
      } catch (IOException var6) {
         throw new RuntimeException(var6);
      } finally {
         IOUtils.closeQuietly(inputstream);
      }

   }

   private float renderChar(char p_renderChar_1_, boolean p_renderChar_2_) {
      if (p_renderChar_1_ == ' ') {
         return 4.0F;
      } else {
         int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(p_renderChar_1_);
         return i != -1 && !this.unicodeFlag ? this.renderDefaultChar(i, p_renderChar_2_) : this.renderUnicodeChar(p_renderChar_1_, p_renderChar_2_);
      }
   }

   protected float renderDefaultChar(int p_renderDefaultChar_1_, boolean p_renderDefaultChar_2_) {
      int i = p_renderDefaultChar_1_ % 16 * 8;
      int j = p_renderDefaultChar_1_ / 16 * 8;
      int k = p_renderDefaultChar_2_ ? 1 : 0;
      this.renderEngine.bindTexture(this.locationFontTexture);
      int l = this.charWidth[p_renderDefaultChar_1_];
      float f = (float)l - 0.01F;
      GL11.glBegin(5);
      GL11.glTexCoord2f((float)i / 128.0F, (float)j / 128.0F);
      GL11.glVertex3f(this.posX + (float)k, this.posY, 0.0F);
      GL11.glTexCoord2f((float)i / 128.0F, ((float)j + 7.99F) / 128.0F);
      GL11.glVertex3f(this.posX - (float)k, this.posY + 7.99F, 0.0F);
      GL11.glTexCoord2f(((float)i + f - 1.0F) / 128.0F, (float)j / 128.0F);
      GL11.glVertex3f(this.posX + f - 1.0F + (float)k, this.posY, 0.0F);
      GL11.glTexCoord2f(((float)i + f - 1.0F) / 128.0F, ((float)j + 7.99F) / 128.0F);
      GL11.glVertex3f(this.posX + f - 1.0F - (float)k, this.posY + 7.99F, 0.0F);
      GL11.glEnd();
      return (float)l;
   }

   private ResourceLocation getUnicodePageLocation(int p_getUnicodePageLocation_1_) {
      if (unicodePageLocations[p_getUnicodePageLocation_1_] == null) {
         unicodePageLocations[p_getUnicodePageLocation_1_] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", p_getUnicodePageLocation_1_));
      }

      return unicodePageLocations[p_getUnicodePageLocation_1_];
   }

   private void loadGlyphTexture(int p_loadGlyphTexture_1_) {
      this.renderEngine.bindTexture(this.getUnicodePageLocation(p_loadGlyphTexture_1_));
   }

   protected float renderUnicodeChar(char p_renderUnicodeChar_1_, boolean p_renderUnicodeChar_2_) {
      if (this.glyphWidth[p_renderUnicodeChar_1_] == 0) {
         return 0.0F;
      } else {
         int i = p_renderUnicodeChar_1_ / 256;
         this.loadGlyphTexture(i);
         int j = this.glyphWidth[p_renderUnicodeChar_1_] >>> 4;
         int k = this.glyphWidth[p_renderUnicodeChar_1_] & 15;
         float f = (float)j;
         float f1 = (float)(k + 1);
         float f2 = (float)(p_renderUnicodeChar_1_ % 16 * 16) + f;
         float f3 = (float)((p_renderUnicodeChar_1_ & 255) / 16 * 16);
         float f4 = f1 - f - 0.02F;
         float f5 = p_renderUnicodeChar_2_ ? 1.0F : 0.0F;
         GL11.glBegin(5);
         GL11.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
         GL11.glVertex3f(this.posX + f5, this.posY, 0.0F);
         GL11.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
         GL11.glVertex3f(this.posX - f5, this.posY + 7.99F, 0.0F);
         GL11.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
         GL11.glVertex3f(this.posX + f4 / 2.0F + f5, this.posY, 0.0F);
         GL11.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
         GL11.glVertex3f(this.posX + f4 / 2.0F - f5, this.posY + 7.99F, 0.0F);
         GL11.glEnd();
         return (f1 - f) / 2.0F + 1.0F;
      }
   }

   public int drawStringWithShadow(String p_drawStringWithShadow_1_, float v, float p_drawStringWithShadow_2_, float p_drawStringWithShadow_3_, int p_drawStringWithShadow_4_) {
      return this.drawString(p_drawStringWithShadow_1_, p_drawStringWithShadow_2_, p_drawStringWithShadow_3_, p_drawStringWithShadow_4_, true);
   }

   public int drawString(String p_drawString_1_, float p_drawString_2_, float p_drawString_3_, int p_drawString_4_) {
      return this.drawString(p_drawString_1_, p_drawString_2_, p_drawString_3_, p_drawString_4_, false);
   }

   public int drawString(String p_drawString_1_, float p_drawString_2_, float p_drawString_3_, int p_drawString_4_, boolean p_drawString_5_) {
      this.enableAlpha();
      this.resetStyles();
      int i;
      if (p_drawString_5_) {
         i = this.renderString(p_drawString_1_, p_drawString_2_ + 1.0F, p_drawString_3_ + 1.0F, p_drawString_4_, true);
         i = Math.max(i, this.renderString(p_drawString_1_, p_drawString_2_, p_drawString_3_, p_drawString_4_, false));
      } else {
         i = this.renderString(p_drawString_1_, p_drawString_2_, p_drawString_3_, p_drawString_4_, false);
      }

      return i;
   }

   private String bidiReorder(String p_bidiReorder_1_) {
      try {
         Bidi bidi = new Bidi((new ArabicShaping(8)).shape(p_bidiReorder_1_), 127);
         bidi.setReorderingMode(0);
         return bidi.writeReordered(2);
      } catch (ArabicShapingException var3) {
         return p_bidiReorder_1_;
      }
   }

   private void resetStyles() {
      this.randomStyle = false;
      this.boldStyle = false;
      this.italicStyle = false;
      this.underlineStyle = false;
      this.strikethroughStyle = false;
   }

   private void renderStringAtPos(String p_renderStringAtPos_1_, boolean p_renderStringAtPos_2_) {
      for(int i = 0; i < p_renderStringAtPos_1_.length(); ++i) {
         char c0 = p_renderStringAtPos_1_.charAt(i);
         int i1;
         int j1;
         if (c0 == 167 && i + 1 < p_renderStringAtPos_1_.length()) {
            i1 = "0123456789abcdefklmnor".indexOf(p_renderStringAtPos_1_.toLowerCase(Locale.ENGLISH).charAt(i + 1));
            if (i1 < 16) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               if (i1 < 0 || i1 > 15) {
                  i1 = 15;
               }

               if (p_renderStringAtPos_2_) {
                  i1 += 16;
               }

               j1 = this.colorCode[i1];
               this.textColor = j1;
               this.setColor((float)(j1 >> 16) / 255.0F, (float)(j1 >> 8 & 255) / 255.0F, (float)(j1 & 255) / 255.0F, this.alpha);
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
            } else if (i1 == 21) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               this.setColor(this.red, this.blue, this.green, this.alpha);
            }

            ++i;
         } else {
            i1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(c0);
            if (this.randomStyle && i1 != -1) {
               j1 = this.getCharWidth(c0);

               char c1;
               do {
                  i1 = this.fontRandom.nextInt("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".length());
                  c1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".charAt(i1);
               } while(j1 != this.getCharWidth(c1));

               c0 = c1;
            }

            float f1 = i1 != -1 && !this.unicodeFlag ? 1.0F : 0.5F;
            boolean flag = (c0 == 0 || i1 == -1 || this.unicodeFlag) && p_renderStringAtPos_2_;
            if (flag) {
               this.posX -= f1;
               this.posY -= f1;
            }

            float f = this.renderChar(c0, this.italicStyle);
            if (flag) {
               this.posX += f1;
               this.posY += f1;
            }

            if (this.boldStyle) {
               this.posX += f1;
               if (flag) {
                  this.posX -= f1;
                  this.posY -= f1;
               }

               this.renderChar(c0, this.italicStyle);
               this.posX -= f1;
               if (flag) {
                  this.posX += f1;
                  this.posY += f1;
               }

               ++f;
            }

            this.doDraw(f);
         }
      }

   }

   protected void doDraw(float p_doDraw_1_) {
      Tessellator tessellator1;
      WorldRenderer worldrenderer1;
      if (this.strikethroughStyle) {
         tessellator1 = Tessellator.getInstance();
         worldrenderer1 = tessellator1.getWorldRenderer();
         GlStateManager.disableTexture2D();
         worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
         worldrenderer1.pos((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0).endVertex();
         worldrenderer1.pos((double)(this.posX + p_doDraw_1_), (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0).endVertex();
         worldrenderer1.pos((double)(this.posX + p_doDraw_1_), (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F), 0.0).endVertex();
         worldrenderer1.pos((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F), 0.0).endVertex();
         tessellator1.draw();
         GlStateManager.enableTexture2D();
      }

      if (this.underlineStyle) {
         tessellator1 = Tessellator.getInstance();
         worldrenderer1 = tessellator1.getWorldRenderer();
         GlStateManager.disableTexture2D();
         worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
         int l = this.underlineStyle ? -1 : 0;
         worldrenderer1.pos((double)(this.posX + (float)l), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0).endVertex();
         worldrenderer1.pos((double)(this.posX + p_doDraw_1_), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0).endVertex();
         worldrenderer1.pos((double)(this.posX + p_doDraw_1_), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0F), 0.0).endVertex();
         worldrenderer1.pos((double)(this.posX + (float)l), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0F), 0.0).endVertex();
         tessellator1.draw();
         GlStateManager.enableTexture2D();
      }

      this.posX += (float)((int)p_doDraw_1_);
   }

   private int renderStringAligned(String p_renderStringAligned_1_, int p_renderStringAligned_2_, int p_renderStringAligned_3_, int p_renderStringAligned_4_, int p_renderStringAligned_5_, boolean p_renderStringAligned_6_) {
      if (this.bidiFlag) {
         int i = this.getStringWidth(this.bidiReorder(p_renderStringAligned_1_));
         p_renderStringAligned_2_ = p_renderStringAligned_2_ + p_renderStringAligned_4_ - i;
      }

      return this.renderString(p_renderStringAligned_1_, (float)p_renderStringAligned_2_, (float)p_renderStringAligned_3_, p_renderStringAligned_5_, p_renderStringAligned_6_);
   }

   private int renderString(String p_renderString_1_, float p_renderString_2_, float p_renderString_3_, int p_renderString_4_, boolean p_renderString_5_) {
      if (p_renderString_1_ == null) {
         return 0;
      } else {
         if (this.bidiFlag) {
            p_renderString_1_ = this.bidiReorder(p_renderString_1_);
         }

         if ((p_renderString_4_ & -67108864) == 0) {
            p_renderString_4_ |= -16777216;
         }

         if (p_renderString_5_) {
            p_renderString_4_ = (p_renderString_4_ & 16579836) >> 2 | p_renderString_4_ & -16777216;
         }

         this.red = (float)(p_renderString_4_ >> 16 & 255) / 255.0F;
         this.blue = (float)(p_renderString_4_ >> 8 & 255) / 255.0F;
         this.green = (float)(p_renderString_4_ & 255) / 255.0F;
         this.alpha = (float)(p_renderString_4_ >> 24 & 255) / 255.0F;
         this.setColor(this.red, this.blue, this.green, this.alpha);
         this.posX = p_renderString_2_;
         this.posY = p_renderString_3_;
         this.getStringCache().renderString(p_renderString_1_, p_renderString_2_, p_renderString_3_, p_renderString_4_, p_renderString_5_);
         return (int)this.posX;
      }
   }

   public int getStringWidth(String p_getStringWidth_1_) {
      return p_getStringWidth_1_ == null ? 0 : this.stringCache.getStringWidth(p_getStringWidth_1_);
   }

   public int getCharWidth(char p_getCharWidth_1_) {
      if (p_getCharWidth_1_ == 167) {
         return -1;
      } else if (p_getCharWidth_1_ == ' ') {
         return 4;
      } else {
         int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(p_getCharWidth_1_);
         if (p_getCharWidth_1_ > 0 && i != -1 && !this.unicodeFlag) {
            return this.charWidth[i];
         } else if (this.glyphWidth[p_getCharWidth_1_] != 0) {
            int j = this.glyphWidth[p_getCharWidth_1_] >>> 4;
            int k = this.glyphWidth[p_getCharWidth_1_] & 15;
            ++k;
            return (k - j) / 2 + 1;
         } else {
            return 0;
         }
      }
   }

   public String trimStringToWidth(String p_trimStringToWidth_1_, int p_trimStringToWidth_2_) {
      return this.stringCache.trimStringToWidth(p_trimStringToWidth_1_, p_trimStringToWidth_2_, false);
   }

   private String trimStringNewline(String p_trimStringNewline_1_) {
      while(p_trimStringNewline_1_ != null && p_trimStringNewline_1_.endsWith("\n")) {
         p_trimStringNewline_1_ = p_trimStringNewline_1_.substring(0, p_trimStringNewline_1_.length() - 1);
      }

      return p_trimStringNewline_1_;
   }

   public void drawSplitString(String p_drawSplitString_1_, int p_drawSplitString_2_, int p_drawSplitString_3_, int p_drawSplitString_4_, int p_drawSplitString_5_) {
      this.resetStyles();
      this.textColor = p_drawSplitString_5_;
      p_drawSplitString_1_ = this.trimStringNewline(p_drawSplitString_1_);
      this.renderSplitString(p_drawSplitString_1_, p_drawSplitString_2_, p_drawSplitString_3_, p_drawSplitString_4_, false);
   }

   private void renderSplitString(String p_renderSplitString_1_, int p_renderSplitString_2_, int p_renderSplitString_3_, int p_renderSplitString_4_, boolean p_renderSplitString_5_) {
      for(Iterator i$ = this.listFormattedStringToWidth(p_renderSplitString_1_, p_renderSplitString_4_).iterator(); i$.hasNext(); p_renderSplitString_3_ += this.FONT_HEIGHT) {
         String s = (String)i$.next();
         this.renderStringAligned(s, p_renderSplitString_2_, p_renderSplitString_3_, p_renderSplitString_4_, this.textColor, p_renderSplitString_5_);
      }

   }

   public int splitStringWidth(String p_splitStringWidth_1_, int p_splitStringWidth_2_) {
      return this.FONT_HEIGHT * this.listFormattedStringToWidth(p_splitStringWidth_1_, p_splitStringWidth_2_).size();
   }

   public void setUnicodeFlag(boolean p_setUnicodeFlag_1_) {
      this.unicodeFlag = p_setUnicodeFlag_1_;
   }

   public boolean getUnicodeFlag() {
      return this.unicodeFlag;
   }

   public void setBidiFlag(boolean p_setBidiFlag_1_) {
      this.bidiFlag = p_setBidiFlag_1_;
   }

   public List listFormattedStringToWidth(String p_listFormattedStringToWidth_1_, int p_listFormattedStringToWidth_2_) {
      return Arrays.asList(this.wrapFormattedStringToWidth(p_listFormattedStringToWidth_1_, p_listFormattedStringToWidth_2_).split("\n"));
   }

   String wrapFormattedStringToWidth(String p_wrapFormattedStringToWidth_1_, int p_wrapFormattedStringToWidth_2_) {
      int i = this.sizeStringToWidth(p_wrapFormattedStringToWidth_1_, p_wrapFormattedStringToWidth_2_);
      if (p_wrapFormattedStringToWidth_1_.length() <= i) {
         return p_wrapFormattedStringToWidth_1_;
      } else {
         String s = p_wrapFormattedStringToWidth_1_.substring(0, i);
         char c0 = p_wrapFormattedStringToWidth_1_.charAt(i);
         boolean flag = c0 == ' ' || c0 == '\n';
         String s1 = getFormatFromString(s) + p_wrapFormattedStringToWidth_1_.substring(i + (flag ? 1 : 0));
         return s + "\n" + this.wrapFormattedStringToWidth(s1, p_wrapFormattedStringToWidth_2_);
      }
   }

   private int sizeStringToWidth(String p_sizeStringToWidth_1_, int p_sizeStringToWidth_2_) {
      int i = p_sizeStringToWidth_1_.length();
      int j = 0;
      int k = 0;
      int l = -1;

      for(boolean flag = false; k < i; ++k) {
         char c0 = p_sizeStringToWidth_1_.charAt(k);
         switch (c0) {
            case '\n':
               --k;
               break;
            case ' ':
               l = k;
            default:
               j += this.getCharWidth(c0);
               if (flag) {
                  ++j;
               }
               break;
            case '§':
               if (k < i - 1) {
                  ++k;
                  char c1 = p_sizeStringToWidth_1_.charAt(k);
                  if (c1 != 'l' && c1 != 'L') {
                     if (c1 == 'r' || c1 == 'R' || isFormatColor(c1)) {
                        flag = false;
                     }
                  } else {
                     flag = true;
                  }
               }
         }

         if (c0 == '\n') {
            ++k;
            l = k;
            break;
         }

         if (j > p_sizeStringToWidth_2_) {
            break;
         }
      }

      return k != i && l != -1 && l < k ? l : k;
   }

   private static boolean isFormatColor(char p_isFormatColor_0_) {
      return p_isFormatColor_0_ >= '0' && p_isFormatColor_0_ <= '9' || p_isFormatColor_0_ >= 'a' && p_isFormatColor_0_ <= 'f' || p_isFormatColor_0_ >= 'A' && p_isFormatColor_0_ <= 'F';
   }

   private static boolean isFormatSpecial(char p_isFormatSpecial_0_) {
      return p_isFormatSpecial_0_ >= 'k' && p_isFormatSpecial_0_ <= 'o' || p_isFormatSpecial_0_ >= 'K' && p_isFormatSpecial_0_ <= 'O' || p_isFormatSpecial_0_ == 'r' || p_isFormatSpecial_0_ == 'R';
   }

   public static String getFormatFromString(String p_getFormatFromString_0_) {
      String s = "";
      int i = -1;
      int j = p_getFormatFromString_0_.length();

      while((i = p_getFormatFromString_0_.indexOf(167, i + 1)) != -1) {
         if (i < j - 1) {
            char c0 = p_getFormatFromString_0_.charAt(i + 1);
            if (isFormatColor(c0)) {
               s = "§" + c0;
            } else if (isFormatSpecial(c0)) {
               s = s + "§" + c0;
            }
         }
      }

      return s;
   }

   public boolean getBidiFlag() {
      return this.bidiFlag;
   }

   protected void setColor(float p_setColor_1_, float p_setColor_2_, float p_setColor_3_, float p_setColor_4_) {
      GlStateManager.color(p_setColor_1_, p_setColor_2_, p_setColor_3_, p_setColor_4_);
   }

   protected void enableAlpha() {
      GlStateManager.enableAlpha();
   }

   protected InputStream getResourceInputStream(ResourceLocation p_getResourceInputStream_1_) throws IOException {
      return Minecraft.getMinecraft().getResourceManager().getResource(p_getResourceInputStream_1_).getInputStream();
   }

   public int getColorCode(char p_getColorCode_1_) {
      return this.colorCode["0123456789abcdef".indexOf(p_getColorCode_1_)];
   }

   public int drawStringWithShadow(String text, float x, float y, int color) {
      this.drawString(text, x + 0.5F, y + 0.5F, (new Color(0, 0, 0, 100)).getRGB());
      return this.drawString(text, x, y, color);
   }

   public int drawStringWithShadow(String text, float x, float y, int color, int shadowAlpha) {
      this.drawString(text, x + 0.5F, y + 0.5F, (new Color(0, 0, 0, shadowAlpha)).getRGB());
      return this.drawString(text, x, y, color);
   }

   public int drawStringWithShadowShifted(String text, float x, float y, int color, int shadowAlpha) {
      this.drawString(text, x + 1.0F, y + 1.0F, (new Color(0, 0, 0, shadowAlpha)).getRGB());
      return this.drawString(text, x, y, color);
   }

   public int drawCenteredString(String text, float x, float y, int color) {
      return this.drawString(text, x - (float)this.getStringWidth(text) / 2.0F, y, color);
   }
}
