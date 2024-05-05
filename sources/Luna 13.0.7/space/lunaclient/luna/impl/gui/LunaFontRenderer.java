package space.lunaclient.luna.impl.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LunaFontRenderer
  extends FontRenderer
{
  private Random fontRandom = new Random();
  private Color[] customColorCodes = new Color['Ā'];
  private int[] colorCode = new int[32];
  private LunaFont font;
  private LunaFont boldFont;
  private LunaFont italicFont;
  private LunaFont boldItalicFont;
  private String colorCodeIdentifiers = "0123456789abcdefklmnor";
  private boolean bidi;
  
  public LunaFontRenderer(Font font, boolean antiAlias, int charOffset)
  {
    super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), 
    
      Minecraft.getMinecraft().getTextureManager(), false);
    setFont(font, antiAlias, charOffset);
    this.customColorCodes[113] = new Color(0, 90, 163);
    this.colorCodeIdentifiers = setupColorCodeIdentifier();
    setupMinecraftColorcodes();
    this.FONT_HEIGHT = getHeight();
  }
  
  public int drawString(String s, float x, float y, int color)
  {
    return drawString(s, x, y, color, false);
  }
  
  public int drawStringWithShadow(String s, float x, float y, int color)
  {
    return drawString(s, x, y, color, false);
  }
  
  public void drawCenteredString(String s, int x, int y, int color, boolean shadow)
  {
    if (shadow) {
      drawStringWithShadow(s, x - (getStringWidth(s) >> 1), y, color);
    } else {
      drawString(s, x - getStringWidth(s) / 2, y, color);
    }
  }
  
  public void drawCenteredString(String s, int x, int y, int color)
  {
    drawStringWithShadow(s, x - (getStringWidth(s) >> 1), y, color);
  }
  
  public int drawString(String text, float x, float y, int color, boolean shadow)
  {
    int result = 0;
    
    String[] lines = text.split("\n");
    for (int i = 0; i < lines.length; i++) {
      result = drawLine(lines[i], x, y + i * getHeight(), color, shadow);
    }
    return result;
  }
  
  private int drawLine(String text, float x, float y, int color, boolean shadow)
  {
    if (text == null) {
      return 0;
    }
    GL11.glPushMatrix();
    GL11.glTranslated(x - 1.5D, y + 0.5D, 0.0D);
    boolean wasBlend = GL11.glGetBoolean(3042);
    GlStateManager.enableAlpha();
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(3553);
    if ((color & 0xFC000000) == 0) {
      color |= 0xFF000000;
    }
    if (shadow) {
      color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
    }
    float red = (color >> 16 & 0xFF) / 255.0F;
    float green = (color >> 8 & 0xFF) / 255.0F;
    float blue = (color & 0xFF) / 255.0F;
    float alpha = (color >> 24 & 0xFF) / 255.0F;
    Color c = new Color(red, green, blue, alpha);
    if (text.contains("§"))
    {
      String[] parts = text.split("§");
      
      Color currentColor = c;
      LunaFont currentFont = this.font;
      int width = 0;
      boolean randomCase = false;boolean bold = false;boolean italic = false;
      boolean strikethrough = false;boolean underline = false;
      for (int index = 0; index < parts.length; index++) {
        if (parts[index].length() > 0) {
          if (index == 0)
          {
            currentFont.drawString(parts[index], width, 0.0D, currentColor, shadow);
            
            width += currentFont.getStringWidth(parts[index]);
          }
          else
          {
            String words = parts[index].substring(1);
            char type = parts[index].charAt(0);
            int colorIndex = this.colorCodeIdentifiers.indexOf(type);
            if (colorIndex != -1) {
              if (colorIndex < 16)
              {
                int colorcode = this.colorCode[colorIndex];
                currentColor = getColor(colorcode, alpha);
                bold = false;
                italic = false;
                randomCase = false;
                underline = false;
                strikethrough = false;
              }
              else if (colorIndex == 16)
              {
                randomCase = true;
              }
              else if (colorIndex == 17)
              {
                bold = true;
              }
              else if (colorIndex == 18)
              {
                strikethrough = true;
              }
              else if (colorIndex == 19)
              {
                underline = true;
              }
              else if (colorIndex == 20)
              {
                italic = true;
              }
              else if (colorIndex == 21)
              {
                bold = false;
                italic = false;
                randomCase = false;
                underline = false;
                strikethrough = false;
                currentColor = c;
              }
              else if (colorIndex > 21)
              {
                Color customColor = this.customColorCodes[type];
                
                currentColor = new Color(customColor.getRed() / 255.0F, customColor.getGreen() / 255.0F, customColor.getBlue() / 255.0F, alpha);
              }
            }
            if ((bold & italic))
            {
              this.boldItalicFont.drawString(randomCase ? 
                toRandom(words) : words, width, 0.0D, currentColor, shadow);
              
              currentFont = this.boldItalicFont;
            }
            else if (bold)
            {
              this.boldFont.drawString(randomCase ? 
                toRandom(words) : words, width, 0.0D, currentColor, shadow);
              
              currentFont = this.boldFont;
            }
            else if (italic)
            {
              this.italicFont.drawString(randomCase ? 
                toRandom(words) : words, width, 0.0D, currentColor, shadow);
              
              currentFont = this.italicFont;
            }
            else
            {
              this.font.drawString(randomCase ? 
                toRandom(words) : words, width, 0.0D, currentColor, shadow);
              
              currentFont = this.font;
            }
            float u = this.font.getHeight() / 16.0F;
            int h = currentFont.getStringHeight(words);
            if (strikethrough) {
              drawLine(width / 2.0D + 1.0D, h / 3, 
                (width + currentFont.getStringWidth(words)) / 2.0D + 1.0D, h / 3, u);
            }
            if (underline) {
              drawLine(width / 2.0D + 1.0D, h / 2, 
                (width + currentFont.getStringWidth(words)) / 2.0D + 1.0D, h / 2, u);
            }
            width += currentFont.getStringWidth(words);
          }
        }
      }
    }
    else
    {
      this.font.drawString(text, 0.0D, 0.0D, c, shadow);
    }
    if (!wasBlend) {
      GL11.glDisable(3042);
    }
    GL11.glPopMatrix();
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    Gui.drawRect(0.0D, 0.0D, 0.0D, 0.0D, 0);
    return (int)(x + getStringWidth(text));
  }
  
  private String toRandom(String text)
  {
    StringBuilder newText = new StringBuilder();
    String allowedCharacters = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000";
    for (char c : text.toCharArray()) {
      if (ChatAllowedCharacters.isAllowedCharacter(c))
      {
        int index = this.fontRandom.nextInt(allowedCharacters.length());
        newText.append(allowedCharacters.toCharArray()[index]);
      }
    }
    return newText.toString();
  }
  
  public int getHeight()
  {
    return this.font.getHeight() / 2;
  }
  
  public static String getFormatFromString(String p_78282_0_)
  {
    StringBuilder var1 = new StringBuilder();
    int var2 = -1;
    int var3 = p_78282_0_.length();
    while ((var2 = p_78282_0_.indexOf('§', var2 + 1)) != -1) {
      if (var2 < var3 - 1)
      {
        char var4 = p_78282_0_.charAt(var2 + 1);
        if (isFormatColor(var4)) {
          var1 = new StringBuilder("§" + var4);
        } else if (isFormatSpecial(var4)) {
          var1.append("§").append(var4);
        }
      }
    }
    return var1.toString();
  }
  
  private static boolean isFormatSpecial(char formatChar)
  {
    return (formatChar >= 'k' ? 1 : 0) & (formatChar <= 'o' ? 1 : 0) | (formatChar >= 'K' ? 1 : 0) & (formatChar <= 'O' ? 1 : 0) | (formatChar == 'r' ? 1 : 0) | (formatChar == 'R' ? 1 : 0);
  }
  
  public void setBidiFlag(boolean state)
  {
    this.bidi = state;
  }
  
  public boolean getBidiFlag()
  {
    return this.bidi;
  }
  
  private int sizeStringToWidth(String str, int wrapWidth)
  {
    int var3 = str.length();
    int var4 = 0;
    int var5 = 0;
    int var6 = -1;
    for (boolean var7 = false; var5 < var3; var5++)
    {
      char var8 = str.charAt(var5);
      switch (var8)
      {
      case '\n': 
        var5--;
        break;
      case '§': 
        if (var5 < var3 - 1)
        {
          var5++;
          char var9 = str.charAt(var5);
          if (((var9 != 'l' ? 1 : 0) & (var9 != 'L' ? 1 : 0)) != 0)
          {
            if (((var9 == 'r' ? 1 : 0) | (var9 == 'R' ? 1 : 0) | isFormatColor(var9)) != 0) {
              var7 = false;
            }
          }
          else {
            var7 = true;
          }
        }
        break;
      case ' ': 
        var6 = var5;
      default: 
        var4 += getStringWidth(Character.toString(var8));
        if (var7) {
          var4++;
        }
        break;
      }
      if (var8 == '\n')
      {
        var5++;
        var6 = var5;
      }
      else
      {
        if (var4 > wrapWidth) {
          break;
        }
      }
    }
    return ((var5 != var3 ? 1 : 0) & (var6 != -1 ? 1 : 0) & (var6 < var5 ? 1 : 0)) != 0 ? var6 : var5;
  }
  
  private static boolean isFormatColor(char colorChar)
  {
    return (colorChar >= '0' ? 1 : 0) & (colorChar <= '9' ? 1 : 0) | (colorChar >= 'a' ? 1 : 0) & (colorChar <= 'f' ? 1 : 0) | (colorChar >= 'A' ? 1 : 0) & (colorChar <= 'F' ? 1 : 0);
  }
  
  public int getCharWidth(char c)
  {
    return getStringWidth(Character.toString(c));
  }
  
  public int getStringWidth(String text)
  {
    if (text == null) {
      return 0;
    }
    if (text.contains("§"))
    {
      String[] parts = text.split("§");
      LunaFont currentFont = this.font;
      int width = 0;
      boolean bold = false;boolean italic = false;
      for (int index = 0; index < parts.length; index++) {
        if (parts[index].length() > 0) {
          if (index == 0)
          {
            width += currentFont.getStringWidth(parts[index]);
          }
          else
          {
            String words = parts[index].substring(1);
            char type = parts[index].charAt(0);
            int colorIndex = this.colorCodeIdentifiers.indexOf(type);
            if (colorIndex != -1) {
              if (colorIndex < 16)
              {
                bold = false;
                italic = false;
              }
              else if (colorIndex == 17)
              {
                bold = true;
              }
              else if (colorIndex == 20)
              {
                italic = true;
              }
              else if (colorIndex == 21)
              {
                bold = false;
                italic = false;
              }
            }
            if ((bold & italic)) {
              currentFont = this.boldItalicFont;
            } else if (bold) {
              currentFont = this.boldFont;
            } else if (italic) {
              currentFont = this.italicFont;
            } else {
              currentFont = this.font;
            }
            width += currentFont.getStringWidth(words);
          }
        }
      }
      return width / 2;
    }
    return this.font.getStringWidth(text) / 2;
  }
  
  private void setFont(Font font, boolean antiAlias, int charOffset)
  {
    synchronized (this)
    {
      this.font = new LunaFont(font, antiAlias, charOffset);
      this.boldFont = new LunaFont(font.deriveFont(1), antiAlias, charOffset);
      
      this.italicFont = new LunaFont(font.deriveFont(2), antiAlias, charOffset);
      
      this.boldItalicFont = new LunaFont(font.deriveFont(3), antiAlias, charOffset);
      
      this.FONT_HEIGHT = getHeight();
    }
  }
  
  public LunaFont getFont()
  {
    return this.font;
  }
  
  public int getSize()
  {
    return this.font.getFont().getSize();
  }
  
  public List<String> wrapWords(String text, double width)
  {
    List<String> Words = new ArrayList();
    if (getStringWidth(text) > width)
    {
      String[] words = text.split(" ");
      StringBuilder currentWord = new StringBuilder();
      char lastColorCode = 65535;
      for (String word : words)
      {
        for (int i = 0; i < word.toCharArray().length; i++)
        {
          char c = word.toCharArray()[i];
          if (((c == '§' ? 1 : 0) & (i < word.toCharArray().length - 1 ? 1 : 0)) != 0) {
            lastColorCode = word.toCharArray()[(i + 1)];
          }
        }
        if (getStringWidth(currentWord + word + " ") < width)
        {
          currentWord.append(word).append(" ");
        }
        else
        {
          Words.add(currentWord.toString());
          currentWord = new StringBuilder("§" + lastColorCode + word + " ");
        }
      }
      if (!currentWord.toString().equals("")) {
        if (getStringWidth(currentWord.toString()) < width) {
          Words.add("§" + lastColorCode + currentWord + " ");
        } else {
          Words.addAll(formatString(currentWord.toString(), width));
        }
      }
    }
    else
    {
      Words.add(text);
    }
    return Words;
  }
  
  private List<String> formatString(String s, double width)
  {
    List<String> Words = new ArrayList();
    StringBuilder currentWord = new StringBuilder();
    char lastColorCode = 65535;
    for (int i = 0; i < s.toCharArray().length; i++)
    {
      char c = s.toCharArray()[i];
      if (((c == '§' ? 1 : 0) & (i < s.toCharArray().length - 1 ? 1 : 0)) != 0) {
        lastColorCode = s.toCharArray()[(i + 1)];
      }
      if (getStringWidth(currentWord.toString() + c) < width)
      {
        currentWord.append(c);
      }
      else
      {
        Words.add(currentWord.toString());
        currentWord = new StringBuilder("§" + lastColorCode + String.valueOf(c));
      }
    }
    if (!currentWord.toString().equals("")) {
      Words.add(currentWord.toString());
    }
    return Words;
  }
  
  private void drawLine(double x, double y, double x1, double y1, float width)
  {
    GL11.glDisable(3553);
    GL11.glLineWidth(width);
    GL11.glBegin(1);
    GL11.glVertex2d(x, y);
    GL11.glVertex2d(x1, y1);
    GL11.glEnd();
    GL11.glEnable(3553);
  }
  
  private void setupMinecraftColorcodes()
  {
    for (int index = 0; index < 32; index++)
    {
      int var6 = (index >> 3 & 0x1) * 85;
      int var7 = (index >> 2 & 0x1) * 170 + var6;
      int var8 = (index >> 1 & 0x1) * 170 + var6;
      int var9 = (index & 0x1) * 170 + var6;
      if (index == 6) {
        var7 += 85;
      }
      if (index >= 16)
      {
        var7 /= 4;
        var8 /= 4;
        var9 /= 4;
      }
      this.colorCode[index] = ((var7 & 0xFF) << 16 | (var8 & 0xFF) << 8 | var9 & 0xFF);
    }
  }
  
  public String trimStringToWidth(String p_78269_1_, int p_78269_2_)
  {
    return trimStringToWidth(p_78269_1_, p_78269_2_, false);
  }
  
  public String trimStringToWidth(String p_78262_1_, int p_78262_2_, boolean p_78262_3_)
  {
    StringBuilder var4 = new StringBuilder();
    int var5 = 0;
    int var6 = p_78262_3_ ? p_78262_1_.length() - 1 : 0;
    int var7 = p_78262_3_ ? -1 : 1;
    boolean var8 = false;
    boolean var9 = false;
    for (int var10 = var6; (
          (var10 >= 0 ? 1 : 0) & (var10 < p_78262_1_.length() ? 1 : 0) & (var5 < p_78262_2_ ? 1 : 0)) != 0; var10 += var7)
    {
      char var11 = p_78262_1_.charAt(var10);
      int var12 = getStringWidth(Character.toString(var11));
      if (var8)
      {
        var8 = false;
        if (((var11 != 'l' ? 1 : 0) & (var11 != 'L' ? 1 : 0)) != 0)
        {
          if (((var11 == 'r' ? 1 : 0) | (var11 == 'R' ? 1 : 0)) != 0) {
            var9 = false;
          }
        }
        else {
          var9 = true;
        }
      }
      else if (var12 < 0)
      {
        var8 = true;
      }
      else
      {
        var5 += var12;
        if (var9) {
          var5++;
        }
      }
      if (var5 > p_78262_2_) {
        break;
      }
      if (p_78262_3_) {
        var4.insert(0, var11);
      } else {
        var4.append(var11);
      }
    }
    return var4.toString();
  }
  
  public List<String> listFormattedStringToWidth(String str, int wrapWidth)
  {
    return 
      Arrays.asList(wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
  }
  
  private String wrapFormattedStringToWidth(String str, int wrapWidth)
  {
    int var3 = sizeStringToWidth(str, wrapWidth);
    if (str.length() <= var3) {
      return str;
    }
    String var4 = str.substring(0, var3);
    char var5 = str.charAt(var3);
    boolean var6 = (var5 == ' ' ? 1 : 0) | (var5 == '\n' ? 1 : 0);
    
    String var7 = getFormatFromString(var4) + str.substring(var3 + (var6 ? 1 : 0));
    return var4 + "\n" + wrapFormattedStringToWidth(var7, wrapWidth);
  }
  
  public Color getColor(int colorCode, float alpha)
  {
    return new Color((colorCode >> 16) / 255.0F, (colorCode >> 8 & 0xFF) / 255.0F, (colorCode & 0xFF) / 255.0F, alpha);
  }
  
  private String setupColorCodeIdentifier()
  {
    StringBuilder colorCodes = new StringBuilder("0123456789abcdefklmnor");
    for (int i = 0; i < this.customColorCodes.length; i++) {
      if (this.customColorCodes[i] != null) {
        colorCodes.append((char)i);
      }
    }
    return colorCodes.toString();
  }
  
  public void onResourceManagerReload(IResourceManager p_110549_1_) {}
}
