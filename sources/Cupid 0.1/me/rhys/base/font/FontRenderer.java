package me.rhys.base.font;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class FontRenderer extends CFont {
  private final int[] colorCode = new int[32];
  
  protected CFont.CharData[] boldChars = new CFont.CharData[256];
  
  protected CFont.CharData[] italicChars = new CFont.CharData[256];
  
  protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
  
  protected DynamicTexture texBold;
  
  protected DynamicTexture texItalic;
  
  protected DynamicTexture texItalicBold;
  
  public FontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
    super(font, antiAlias, fractionalMetrics);
    setupMinecraftColorcodes();
    setupBoldItalicIDs();
  }
  
  public void drawCenteredScaledStringWithShadow(String str, Vec2f pos, float scale, int color) {
    drawScaledStringWithShadow(str, new Vec2f(pos.x - getStringWidth(str) * scale / 2.0F, pos.y - getHeight() * scale / 2.0F), scale, color);
  }
  
  public float drawString(String text, Vec2f vec2, int color) {
    return drawString(text, new Vec2f(vec2.x, vec2.y), color, false);
  }
  
  public void drawScaledStringWithShadow(String str, Vec2f pos, float scale, int color) {
    GlStateManager.pushMatrix();
    GlStateManager.scale(scale, scale, 0.0F);
    drawStringWithShadow(str, pos.clone().div(scale, scale), color);
    GlStateManager.scale(1.0F, 1.0F, 1.0F);
    GlStateManager.popMatrix();
  }
  
  public float drawStringWithShadow(String text, Vec2f vec2, int color) {
    float shadowWidth = drawString(text, new Vec2f(vec2.x + 0.7F, vec2.y + 0.75F), color, true);
    return Math.max(shadowWidth, drawString(text, new Vec2f(vec2.x, vec2.y), color, false));
  }
  
  public float drawStringWithShadow(String text, int x, int y, int color) {
    float shadowWidth = drawString(text, new Vec2f(x + 0.7F, y + 0.75F), color, true);
    return Math.max(shadowWidth, drawString(text, new Vec2f(x, y), color, false));
  }
  
  public float drawStringWithShadow(String text, float x, float y, int color) {
    float shadowWidth = drawString(text, new Vec2f(x + 0.7F, y + 0.75F), color, true);
    return Math.max(shadowWidth, drawString(text, new Vec2f(x, y), color, false));
  }
  
  public float drawCenteredString(String text, Vec2f vec2, int color) {
    return drawString(text, new Vec2f(vec2.x - (getStringWidth(text) / 2), vec2.y), color);
  }
  
  public float drawCenteredStringWithShadow(String text, Vec2f vec2, int color) {
    return drawString(text, new Vec2f(vec2.x - (getStringWidth(text) / 2), vec2.y), color);
  }
  
  public float drawString(String text, Vec2f vec2, int color, boolean shadow) {
    vec2.x--;
    if (text == null)
      return 0.0F; 
    if (color == 553648127)
      color = 16777215; 
    if ((color & 0xFC000000) == 0)
      color |= 0xFF000000; 
    if (shadow)
      color = (color & 0xFCFCFC) >> 2 | color & (new Color(20, 20, 20, 200)).getRGB(); 
    CFont.CharData[] currentData = this.charData;
    float alpha = (color >> 24 & 0xFF) / 255.0F;
    boolean bold = false;
    boolean italic = false;
    boolean strikethrough = false;
    boolean underline = false;
    vec2.x = (float)(vec2.x * 2.0D);
    vec2.y = (float)((vec2.y - 3.0D) * 2.0D);
    GL11.glPushMatrix();
    GlStateManager.scale(0.5D, 0.5D, 0.5D);
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(770, 771);
    GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
    int size = text.length();
    GlStateManager.enableTexture2D();
    GlStateManager.bindTexture(this.tex.getGlTextureId());
    GL11.glBindTexture(3553, this.tex.getGlTextureId());
    for (int i = 0; i < size; i++) {
      char character = text.charAt(i);
      if (String.valueOf(character).equals("§")) {
        int colorIndex = 21;
        try {
          colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
        } catch (Exception e) {
          e.printStackTrace();
        } 
        if (colorIndex < 16) {
          bold = false;
          italic = false;
          underline = false;
          strikethrough = false;
          GlStateManager.bindTexture(this.tex.getGlTextureId());
          currentData = this.charData;
          if (colorIndex < 0)
            colorIndex = 15; 
          if (shadow)
            colorIndex += 16; 
          int colorcode = this.colorCode[colorIndex];
          GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
        } else if (colorIndex == 17) {
          bold = true;
          if (italic) {
            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
            currentData = this.boldItalicChars;
          } else {
            GlStateManager.bindTexture(this.texBold.getGlTextureId());
            currentData = this.boldChars;
          } 
        } else if (colorIndex == 18) {
          strikethrough = true;
        } else if (colorIndex == 19) {
          underline = true;
        } else if (colorIndex == 20) {
          italic = true;
          if (bold) {
            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
            currentData = this.boldItalicChars;
          } else {
            GlStateManager.bindTexture(this.texItalic.getGlTextureId());
            currentData = this.italicChars;
          } 
        } else if (colorIndex != 16) {
          bold = false;
          italic = false;
          underline = false;
          strikethrough = false;
          GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
          GlStateManager.bindTexture(this.tex.getGlTextureId());
          currentData = this.charData;
        } 
        i++;
      } else if (character < currentData.length) {
        GL11.glBegin(4);
        drawChar(currentData, character, new Vec2f(vec2.x, vec2.y));
        GL11.glEnd();
        if (strikethrough)
          drawLine(new Vec2f(vec2.x, vec2.y + ((currentData[character]).height / 2)), new Vec2f(vec2.x + (currentData[character]).width - 8.0F, vec2.y + ((currentData[character]).height / 2))); 
        if (underline)
          drawLine(new Vec2f(vec2.x, vec2.y + (currentData[character]).height - 2.0F), new Vec2f(vec2.x + (currentData[character]).width - 8.0F, vec2.y + (currentData[character]).height - 2.0F)); 
        vec2.x += ((currentData[character]).width - 8 + this.charOffset);
      } 
    } 
    GL11.glHint(3155, 4352);
    GL11.glPopMatrix();
    return vec2.x / 2.0F;
  }
  
  public int getStringWidth(String text) {
    if (text == null)
      return 0; 
    int width = 0;
    CFont.CharData[] currentData = this.charData;
    boolean bold = false;
    boolean italic = false;
    int size = text.length();
    for (int i = 0; i < size; i++) {
      char character = text.charAt(i);
      if (String.valueOf(character).equals("§")) {
        int colorIndex = "0123456789abcdefklmnor".indexOf(character);
        if (colorIndex < 16) {
          bold = false;
          italic = false;
        } else if (colorIndex == 17) {
          bold = true;
          if (italic) {
            currentData = this.boldItalicChars;
          } else {
            currentData = this.boldChars;
          } 
        } else if (colorIndex == 20) {
          italic = true;
          if (bold) {
            currentData = this.boldItalicChars;
          } else {
            currentData = this.italicChars;
          } 
        } else if (colorIndex == 21) {
          bold = false;
          italic = false;
          currentData = this.charData;
        } 
        i++;
      } else if (character < currentData.length) {
        width += (currentData[character]).width - 8 + this.charOffset;
      } 
    } 
    return width / 2;
  }
  
  public int getStringWidthCust(String text) {
    if (text == null)
      return 0; 
    int width = 0;
    CFont.CharData[] currentData = this.charData;
    boolean bold = false;
    boolean italic = false;
    int size = text.length();
    for (int i = 0; i < size; i++) {
      char character = text.charAt(i);
      if (String.valueOf(character).equals("§")) {
        int colorIndex = "0123456789abcdefklmnor".indexOf(character);
        if (colorIndex < 16) {
          bold = false;
          italic = false;
        } else if (colorIndex == 17) {
          bold = true;
          if (italic) {
            currentData = this.boldItalicChars;
          } else {
            currentData = this.boldChars;
          } 
        } else if (colorIndex == 20) {
          italic = true;
          if (bold) {
            currentData = this.boldItalicChars;
          } else {
            currentData = this.italicChars;
          } 
        } else if (colorIndex == 21) {
          bold = false;
          italic = false;
          currentData = this.charData;
        } 
        i++;
      } else if (character < currentData.length) {
        width += (currentData[character]).width - 8 + this.charOffset;
      } 
    } 
    return (width - this.charOffset) / 2;
  }
  
  public void setFont(Font font) {
    super.setFont(font);
    setupBoldItalicIDs();
  }
  
  public void setAntiAlias(boolean antiAlias) {
    super.setAntiAlias(antiAlias);
    setupBoldItalicIDs();
  }
  
  public void setFractionalMetrics(boolean fractionalMetrics) {
    super.setFractionalMetrics(fractionalMetrics);
    setupBoldItalicIDs();
  }
  
  private void setupBoldItalicIDs() {
    this.texBold = setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
    this.texItalic = setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
    this.texItalicBold = setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
  }
  
  private void drawLine(Vec2f vec2, Vec2f vec1) {
    GL11.glDisable(3553);
    GL11.glLineWidth(1.0F);
    GL11.glBegin(1);
    GL11.glVertex2d(vec2.x, vec2.y);
    GL11.glVertex2d(vec1.x, vec1.y);
    GL11.glEnd();
    GL11.glEnable(3553);
  }
  
  public List<String> wrapWords(String text, double width) {
    List<String> finalWords = new ArrayList<>();
    if (getStringWidth(text) > width) {
      String[] words = text.split(" ");
      String currentWord = "";
      char lastColorCode = Character.MAX_VALUE;
      for (String word : words) {
        for (int i = 0; i < (word.toCharArray()).length; i++) {
          char c = word.toCharArray()[i];
          if (String.valueOf(c).equals("§") && i < (word.toCharArray()).length - 1)
            lastColorCode = word.toCharArray()[i + 1]; 
        } 
        if (getStringWidth(currentWord + word + " ") < width) {
          currentWord = currentWord + word + " ";
        } else {
          finalWords.add(currentWord);
          currentWord = "" + lastColorCode + word + " ";
        } 
      } 
      if (currentWord.length() > 0)
        if (getStringWidth(currentWord) < width) {
          finalWords.add("" + lastColorCode + currentWord + " ");
        } else {
          finalWords.addAll(formatString(currentWord, width));
        }  
    } else {
      finalWords.add(text);
    } 
    return finalWords;
  }
  
  public List<String> formatString(String string, double width) {
    List<String> finalWords = new ArrayList<>();
    StringBuilder currentWord = new StringBuilder();
    char lastColorCode = Character.MAX_VALUE;
    char[] chars = string.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (String.valueOf(c).equals("§") && i < chars.length - 1)
        lastColorCode = chars[i + 1]; 
      if (getStringWidth(currentWord.toString() + c) < width) {
        currentWord.append(c);
      } else {
        finalWords.add(currentWord.toString());
        currentWord = new StringBuilder("" + lastColorCode + c);
      } 
    } 
    if (currentWord.length() > 0)
      finalWords.add(currentWord.toString()); 
    return finalWords;
  }
  
  private void setupMinecraftColorcodes() {
    for (int index = 0; index < 32; index++) {
      int noClue = (index >> 3 & 0x1) * 85;
      int red = (index >> 2 & 0x1) * 170 + noClue;
      int green = (index >> 1 & 0x1) * 170 + noClue;
      int blue = (index & 0x1) * 170 + noClue;
      if (index == 6)
        red += 85; 
      if (index >= 16) {
        red /= 4;
        green /= 4;
        blue /= 4;
      } 
      this.colorCode[index] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\font\FontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */