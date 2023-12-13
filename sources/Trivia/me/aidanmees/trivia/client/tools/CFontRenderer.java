package me.aidanmees.trivia.client.tools;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class CFontRenderer
  implements FontObject
{
  private MinecraftFontRenderer fontRenderer;
  private Font theFont;
  
  public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics)
  {
    this.theFont = font;
    this.fontRenderer = new MinecraftFontRenderer(this.theFont, antiAlias, fractionalMetrics);
  }
  
  public CFontRenderer(InputStream font, float size, boolean antiAlias, boolean fractionalMetrics)
  {
    try
    {
      this.theFont = Font.createFont(0, font).deriveFont(size);
    }
    catch (FontFormatException|IOException e)
    {
      e.printStackTrace();
    }
    this.fontRenderer = new MinecraftFontRenderer(this.theFont, antiAlias, fractionalMetrics);
  }
  
  public void drawString(String text, float x, float y, int color)
  {
    this.fontRenderer.drawString(text, x, y, color);
  }
  
  public void drawString(String text, float x, float y, int color, boolean dropShadow)
  {
    this.fontRenderer.drawString(text, x, y, color, dropShadow);
  }
  
  public void drawStringWithShadow(String text, float x, float y, int color)
  {
    this.fontRenderer.drawStringWithShadow(text, x, y, color);
  }
  
  public int getStringWith(String text)
  {
    return this.fontRenderer.getStringWidth(text);
  }
  
  public int getStringHeight(String text)
  {
    return this.fontRenderer.getStringHeight(text);
  }
}
