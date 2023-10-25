package net.minecraft.client.gui;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import me.rhys.base.util.vec.Vec2f;
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
import optifine.Config;
import optifine.CustomColors;
import optifine.FontUtils;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

public class FontRenderer implements IResourceManagerReloadListener {
  private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
  
  private float[] charWidth = new float[256];
  
  public int FONT_HEIGHT = 9;
  
  public Random fontRandom = new Random();
  
  private byte[] glyphWidth = new byte[65536];
  
  private int[] colorCode = new int[32];
  
  private ResourceLocation locationFontTexture;
  
  private final TextureManager renderEngine;
  
  private float posX;
  
  private float posY;
  
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
  
  private static final String __OBFID = "CL_00000660";
  
  public GameSettings gameSettings;
  
  public ResourceLocation locationFontTextureBase;
  
  public boolean enabled = true;
  
  public float offsetBold = 1.0F;
  
  public FontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
    this.gameSettings = gameSettingsIn;
    this.locationFontTextureBase = location;
    this.locationFontTexture = location;
    this.renderEngine = textureManagerIn;
    this.unicodeFlag = unicode;
    this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
    bindTexture(this.locationFontTexture);
    for (int i = 0; i < 32; i++) {
      int j = (i >> 3 & 0x1) * 85;
      int k = (i >> 2 & 0x1) * 170 + j;
      int l = (i >> 1 & 0x1) * 170 + j;
      int i1 = (i >> 0 & 0x1) * 170 + j;
      if (i == 6)
        k += 85; 
      if (gameSettingsIn.anaglyph) {
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
      this.colorCode[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
    } 
    readGlyphSizes();
  }
  
  public void onResourceManagerReload(IResourceManager resourceManager) {
    this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
    for (int i = 0; i < unicodePageLocations.length; i++)
      unicodePageLocations[i] = null; 
    readFontTexture();
    readGlyphSizes();
  }
  
  private void readFontTexture() {
    BufferedImage bufferedimage;
    try {
      bufferedimage = TextureUtil.readBufferedImage(getResourceInputStream(this.locationFontTexture));
    } catch (IOException ioexception) {
      throw new RuntimeException(ioexception);
    } 
    Properties properties = FontUtils.readFontProperties(this.locationFontTexture);
    int i = bufferedimage.getWidth();
    int j = bufferedimage.getHeight();
    int k = i / 16;
    int l = j / 16;
    float f = i / 128.0F;
    float f1 = Config.limit(f, 1.0F, 2.0F);
    this.offsetBold = 1.0F / f1;
    float f2 = FontUtils.readFloat(properties, "offsetBold", -1.0F);
    if (f2 >= 0.0F)
      this.offsetBold = f2; 
    int[] aint = new int[i * j];
    bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
    for (int i1 = 0; i1 < 256; i1++) {
      int j1 = i1 % 16;
      int k1 = i1 / 16;
      int l1 = 0;
      for (l1 = k - 1; l1 >= 0; l1--) {
        int i2 = j1 * k + l1;
        boolean flag = true;
        for (int j2 = 0; j2 < l && flag; j2++) {
          int k2 = (k1 * l + j2) * i;
          int l2 = aint[i2 + k2];
          int i3 = l2 >> 24 & 0xFF;
          if (i3 > 16)
            flag = false; 
        } 
        if (!flag)
          break; 
      } 
      if (i1 == 65)
        i1 = i1; 
      if (i1 == 32)
        if (k <= 8) {
          l1 = (int)(2.0F * f);
        } else {
          l1 = (int)(1.5F * f);
        }  
      this.charWidth[i1] = (l1 + 1) / f + 1.0F;
    } 
    FontUtils.readCustomCharWidths(properties, this.charWidth);
  }
  
  private void readGlyphSizes() {
    InputStream inputstream = null;
    try {
      inputstream = getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
      inputstream.read(this.glyphWidth);
    } catch (IOException ioexception) {
      throw new RuntimeException(ioexception);
    } finally {
      IOUtils.closeQuietly(inputstream);
    } 
  }
  
  private float func_181559_a(char p_181559_1_, boolean p_181559_2_) {
    if (p_181559_1_ == ' ')
      return !this.unicodeFlag ? this.charWidth[p_181559_1_] : 4.0F; 
    int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(p_181559_1_);
    return (i != -1 && !this.unicodeFlag) ? renderDefaultChar(i, p_181559_2_) : renderUnicodeChar(p_181559_1_, p_181559_2_);
  }
  
  private float renderDefaultChar(int p_78266_1_, boolean p_78266_2_) {
    int i = p_78266_1_ % 16 * 8;
    int j = p_78266_1_ / 16 * 8;
    int k = p_78266_2_ ? 1 : 0;
    bindTexture(this.locationFontTexture);
    float f = this.charWidth[p_78266_1_];
    float f1 = 7.99F;
    GL11.glBegin(5);
    GL11.glTexCoord2f(i / 128.0F, j / 128.0F);
    GL11.glVertex3f(this.posX + k, this.posY, 0.0F);
    GL11.glTexCoord2f(i / 128.0F, (j + 7.99F) / 128.0F);
    GL11.glVertex3f(this.posX - k, this.posY + 7.99F, 0.0F);
    GL11.glTexCoord2f((i + f1 - 1.0F) / 128.0F, j / 128.0F);
    GL11.glVertex3f(this.posX + f1 - 1.0F + k, this.posY, 0.0F);
    GL11.glTexCoord2f((i + f1 - 1.0F) / 128.0F, (j + 7.99F) / 128.0F);
    GL11.glVertex3f(this.posX + f1 - 1.0F - k, this.posY + 7.99F, 0.0F);
    GL11.glEnd();
    return f;
  }
  
  private ResourceLocation getUnicodePageLocation(int p_111271_1_) {
    if (unicodePageLocations[p_111271_1_] == null) {
      unicodePageLocations[p_111271_1_] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", new Object[] { Integer.valueOf(p_111271_1_) }));
      unicodePageLocations[p_111271_1_] = FontUtils.getHdFontLocation(unicodePageLocations[p_111271_1_]);
    } 
    return unicodePageLocations[p_111271_1_];
  }
  
  private void loadGlyphTexture(int p_78257_1_) {
    bindTexture(getUnicodePageLocation(p_78257_1_));
  }
  
  private float renderUnicodeChar(char p_78277_1_, boolean p_78277_2_) {
    if (this.glyphWidth[p_78277_1_] == 0)
      return 0.0F; 
    int i = p_78277_1_ / 256;
    loadGlyphTexture(i);
    int j = this.glyphWidth[p_78277_1_] >>> 4;
    int k = this.glyphWidth[p_78277_1_] & 0xF;
    j &= 0xF;
    float f = j;
    float f1 = (k + 1);
    float f2 = (p_78277_1_ % 16 * 16) + f;
    float f3 = ((p_78277_1_ & 0xFF) / 16 * 16);
    float f4 = f1 - f - 0.02F;
    float f5 = p_78277_2_ ? 1.0F : 0.0F;
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
  
  public int drawStringWithShadow(String text, float x, float y, int color) {
    return drawString(text, x, y, color, true);
  }
  
  public int drawString(String text, float x, float y, int color) {
    return !this.enabled ? 0 : drawString(text, x, y, color, false);
  }
  
  public int drawStringWithShadow(String s, Vec2f pos, int color) {
    return drawStringWithShadow(s, pos.x, pos.y, color);
  }
  
  public void drawCenteredString(String s, int x, int y, int color) {
    drawString(s, (x - getStringWidth(s) / 2), (y - this.FONT_HEIGHT / 2), color);
  }
  
  public int drawString(String text, float x, float y, int color, boolean dropShadow) {
    int i;
    enableAlpha();
    resetStyles();
    if (dropShadow) {
      i = renderString(text, x + 1.0F, y + 1.0F, color, true);
      i = Math.max(i, renderString(text, x, y, color, false));
    } else {
      i = renderString(text, x, y, color, false);
    } 
    return i;
  }
  
  private String bidiReorder(String p_147647_1_) {
    try {
      Bidi bidi = new Bidi((new ArabicShaping(8)).shape(p_147647_1_), 127);
      bidi.setReorderingMode(0);
      return bidi.writeReordered(2);
    } catch (ArabicShapingException var3) {
      return p_147647_1_;
    } 
  }
  
  private void resetStyles() {
    this.randomStyle = false;
    this.boldStyle = false;
    this.italicStyle = false;
    this.underlineStyle = false;
    this.strikethroughStyle = false;
  }
  
  private void renderStringAtPos(String p_78255_1_, boolean p_78255_2_) {
    for (int i = 0; i < p_78255_1_.length(); i++) {
      char c0 = p_78255_1_.charAt(i);
      if (c0 == '§' && i + 1 < p_78255_1_.length()) {
        int i1 = "0123456789abcdefklmnor".indexOf(p_78255_1_.toLowerCase().charAt(i + 1));
        if (i1 < 16) {
          this.randomStyle = false;
          this.boldStyle = false;
          this.strikethroughStyle = false;
          this.underlineStyle = false;
          this.italicStyle = false;
          if (i1 < 0 || i1 > 15)
            i1 = 15; 
          if (p_78255_2_)
            i1 += 16; 
          int j1 = this.colorCode[i1];
          if (Config.isCustomColors())
            j1 = CustomColors.getTextColor(i1, j1); 
          this.textColor = j1;
          setColor((j1 >> 16) / 255.0F, (j1 >> 8 & 0xFF) / 255.0F, (j1 & 0xFF) / 255.0F, this.alpha);
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
          setColor(this.red, this.blue, this.green, this.alpha);
        } 
        i++;
      } else {
        int j = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(c0);
        if (this.randomStyle && j != -1) {
          char c1;
          int k = getCharWidth(c0);
          do {
            j = this.fontRandom.nextInt("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".length());
            c1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".charAt(j);
          } while (k != getCharWidth(c1));
          c0 = c1;
        } 
        float f1 = (j != -1 && !this.unicodeFlag) ? this.offsetBold : 0.5F;
        boolean flag = ((c0 == '\000' || j == -1 || this.unicodeFlag) && p_78255_2_);
        if (flag) {
          this.posX -= f1;
          this.posY -= f1;
        } 
        float f = func_181559_a(c0, this.italicStyle);
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
          func_181559_a(c0, this.italicStyle);
          this.posX -= f1;
          if (flag) {
            this.posX += f1;
            this.posY += f1;
          } 
          f += f1;
        } 
        if (this.strikethroughStyle) {
          Tessellator tessellator = Tessellator.getInstance();
          WorldRenderer worldrenderer = tessellator.getWorldRenderer();
          GlStateManager.disableTexture2D();
          worldrenderer.begin(7, DefaultVertexFormats.POSITION);
          worldrenderer.pos(this.posX, (this.posY + (this.FONT_HEIGHT / 2)), 0.0D).endVertex();
          worldrenderer.pos((this.posX + f), (this.posY + (this.FONT_HEIGHT / 2)), 0.0D).endVertex();
          worldrenderer.pos((this.posX + f), (this.posY + (this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
          worldrenderer.pos(this.posX, (this.posY + (this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
          tessellator.draw();
          GlStateManager.enableTexture2D();
        } 
        if (this.underlineStyle) {
          Tessellator tessellator1 = Tessellator.getInstance();
          WorldRenderer worldrenderer1 = tessellator1.getWorldRenderer();
          GlStateManager.disableTexture2D();
          worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
          int l = this.underlineStyle ? -1 : 0;
          worldrenderer1.pos((this.posX + l), (this.posY + this.FONT_HEIGHT), 0.0D).endVertex();
          worldrenderer1.pos((this.posX + f), (this.posY + this.FONT_HEIGHT), 0.0D).endVertex();
          worldrenderer1.pos((this.posX + f), (this.posY + this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
          worldrenderer1.pos((this.posX + l), (this.posY + this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
          tessellator1.draw();
          GlStateManager.enableTexture2D();
        } 
        this.posX += f;
      } 
    } 
  }
  
  private int renderStringAligned(String text, int x, int y, int p_78274_4_, int color, boolean dropShadow) {
    if (this.bidiFlag) {
      int i = getStringWidth(bidiReorder(text));
      x = x + p_78274_4_ - i;
    } 
    return renderString(text, x, y, color, dropShadow);
  }
  
  private int renderString(String text, float x, float y, int color, boolean dropShadow) {
    if (text == null)
      return 0; 
    if (this.bidiFlag)
      text = bidiReorder(text); 
    if ((color & 0xFC000000) == 0)
      color |= 0xFF000000; 
    if (dropShadow)
      color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000; 
    this.red = (color >> 16 & 0xFF) / 255.0F;
    this.blue = (color >> 8 & 0xFF) / 255.0F;
    this.green = (color & 0xFF) / 255.0F;
    this.alpha = (color >> 24 & 0xFF) / 255.0F;
    setColor(this.red, this.blue, this.green, this.alpha);
    this.posX = x;
    this.posY = y;
    renderStringAtPos(text, dropShadow);
    return (int)this.posX;
  }
  
  public int getStringWidth(String text) {
    if (text == null)
      return 0; 
    float f = 0.0F;
    boolean flag = false;
    for (int i = 0; i < text.length(); i++) {
      char c0 = text.charAt(i);
      float f1 = getCharWidthFloat(c0);
      if (f1 < 0.0F && i < text.length() - 1) {
        i++;
        c0 = text.charAt(i);
        if (c0 != 'l' && c0 != 'L') {
          if (c0 == 'r' || c0 == 'R')
            flag = false; 
        } else {
          flag = true;
        } 
        f1 = 0.0F;
      } 
      f += f1;
      if (flag && f1 > 0.0F)
        f += this.unicodeFlag ? 1.0F : this.offsetBold; 
    } 
    return (int)f;
  }
  
  public int getCharWidth(char character) {
    return Math.round(getCharWidthFloat(character));
  }
  
  private float getCharWidthFloat(char p_getCharWidthFloat_1_) {
    if (p_getCharWidthFloat_1_ == '§')
      return -1.0F; 
    if (p_getCharWidthFloat_1_ == ' ')
      return this.charWidth[32]; 
    int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(p_getCharWidthFloat_1_);
    if (p_getCharWidthFloat_1_ > '\000' && i != -1 && !this.unicodeFlag)
      return this.charWidth[i]; 
    if (this.glyphWidth[p_getCharWidthFloat_1_] != 0) {
      int j = this.glyphWidth[p_getCharWidthFloat_1_] >>> 4;
      int k = this.glyphWidth[p_getCharWidthFloat_1_] & 0xF;
      j &= 0xF;
      k++;
      return ((k - j) / 2 + 1);
    } 
    return 0.0F;
  }
  
  public String trimStringToWidth(String text, int width) {
    return trimStringToWidth(text, width, false);
  }
  
  public String trimStringToWidth(String text, int width, boolean reverse) {
    StringBuilder stringbuilder = new StringBuilder();
    float f = 0.0F;
    int i = reverse ? (text.length() - 1) : 0;
    int j = reverse ? -1 : 1;
    boolean flag = false;
    boolean flag1 = false;
    int k;
    for (k = i; k >= 0 && k < text.length() && f < width; k += j) {
      char c0 = text.charAt(k);
      float f1 = getCharWidthFloat(c0);
      if (flag) {
        flag = false;
        if (c0 != 'l' && c0 != 'L') {
          if (c0 == 'r' || c0 == 'R')
            flag1 = false; 
        } else {
          flag1 = true;
        } 
      } else if (f1 < 0.0F) {
        flag = true;
      } else {
        f += f1;
        if (flag1)
          f++; 
      } 
      if (f > width)
        break; 
      if (reverse) {
        stringbuilder.insert(0, c0);
      } else {
        stringbuilder.append(c0);
      } 
    } 
    return stringbuilder.toString();
  }
  
  private String trimStringNewline(String text) {
    while (text != null && text.endsWith("\n"))
      text = text.substring(0, text.length() - 1); 
    return text;
  }
  
  public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
    resetStyles();
    this.textColor = textColor;
    str = trimStringNewline(str);
    renderSplitString(str, x, y, wrapWidth, false);
  }
  
  private void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow) {
    for (Object s : listFormattedStringToWidth(str, wrapWidth)) {
      renderStringAligned((String)s, x, y, wrapWidth, this.textColor, addShadow);
      y += this.FONT_HEIGHT;
    } 
  }
  
  public int splitStringWidth(String p_78267_1_, int p_78267_2_) {
    return this.FONT_HEIGHT * listFormattedStringToWidth(p_78267_1_, p_78267_2_).size();
  }
  
  public void setUnicodeFlag(boolean unicodeFlagIn) {
    this.unicodeFlag = unicodeFlagIn;
  }
  
  public boolean getUnicodeFlag() {
    return this.unicodeFlag;
  }
  
  public void setBidiFlag(boolean bidiFlagIn) {
    this.bidiFlag = bidiFlagIn;
  }
  
  public List listFormattedStringToWidth(String str, int wrapWidth) {
    return Arrays.asList(wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
  }
  
  String wrapFormattedStringToWidth(String str, int wrapWidth) {
    int i = sizeStringToWidth(str, wrapWidth);
    if (str.length() <= i)
      return str; 
    String s = str.substring(0, i);
    char c0 = str.charAt(i);
    boolean flag = (c0 == ' ' || c0 == '\n');
    String s1 = getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
    return s + "\n" + wrapFormattedStringToWidth(s1, wrapWidth);
  }
  
  private int sizeStringToWidth(String str, int wrapWidth) {
    int i = str.length();
    float f = 0.0F;
    int j = 0;
    int k = -1;
    for (boolean flag = false; j < i; j++) {
      char c0 = str.charAt(j);
      switch (c0) {
        case '\n':
          j--;
          break;
        case ' ':
          k = j;
        default:
          f += getCharWidthFloat(c0);
          if (flag)
            f++; 
          break;
        case '§':
          if (j < i - 1) {
            j++;
            char c1 = str.charAt(j);
            if (c1 != 'l' && c1 != 'L') {
              if (c1 == 'r' || c1 == 'R' || isFormatColor(c1))
                flag = false; 
              break;
            } 
            flag = true;
          } 
          break;
      } 
      if (c0 == '\n') {
        k = ++j;
        break;
      } 
      if (f > wrapWidth)
        break; 
    } 
    return (j != i && k != -1 && k < j) ? k : j;
  }
  
  private static boolean isFormatColor(char colorChar) {
    return ((colorChar >= '0' && colorChar <= '9') || (colorChar >= 'a' && colorChar <= 'f') || (colorChar >= 'A' && colorChar <= 'F'));
  }
  
  private static boolean isFormatSpecial(char formatChar) {
    return ((formatChar >= 'k' && formatChar <= 'o') || (formatChar >= 'K' && formatChar <= 'O') || formatChar == 'r' || formatChar == 'R');
  }
  
  public static String getFormatFromString(String text) {
    String s = "";
    int i = -1;
    int j = text.length();
    while ((i = text.indexOf('§', i + 1)) != -1) {
      if (i < j - 1) {
        char c0 = text.charAt(i + 1);
        if (isFormatColor(c0)) {
          s = "§" + c0;
          continue;
        } 
        if (isFormatSpecial(c0))
          s = s + "§" + c0; 
      } 
    } 
    return s;
  }
  
  public boolean getBidiFlag() {
    return this.bidiFlag;
  }
  
  public int getColorCode(char character) {
    int i = "0123456789abcdef".indexOf(character);
    if (i >= 0 && i < this.colorCode.length) {
      int j = this.colorCode[i];
      if (Config.isCustomColors())
        j = CustomColors.getTextColor(i, j); 
      return j;
    } 
    return 16777215;
  }
  
  protected void setColor(float p_setColor_1_, float p_setColor_2_, float p_setColor_3_, float p_setColor_4_) {
    GlStateManager.color(p_setColor_1_, p_setColor_2_, p_setColor_3_, p_setColor_4_);
  }
  
  protected void enableAlpha() {
    GlStateManager.enableAlpha();
  }
  
  protected void bindTexture(ResourceLocation p_bindTexture_1_) {
    this.renderEngine.bindTexture(p_bindTexture_1_);
  }
  
  protected InputStream getResourceInputStream(ResourceLocation p_getResourceInputStream_1_) throws IOException {
    return Minecraft.getMinecraft().getResourceManager().getResource(p_getResourceInputStream_1_).getInputStream();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\gui\FontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */