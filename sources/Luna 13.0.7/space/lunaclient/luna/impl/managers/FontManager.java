package space.lunaclient.luna.impl.managers;

import java.awt.Font;
import java.io.InputStream;
import java.io.PrintStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import space.lunaclient.luna.impl.gui.LunaFontRenderer;

public class FontManager
{
  public static final LunaFontRenderer fontRenderer = new LunaFontRenderer(getFont(36), true, 8);
  public static final LunaFontRenderer fontRendererMAIN = new LunaFontRenderer(getFont3(250), true, 8);
  public static final LunaFontRenderer fontRendererBebasRegMed = new LunaFontRenderer(getFontBebas(48), true, 8);
  public static final LunaFontRenderer fontRendererBebasRegSmall = new LunaFontRenderer(getFontBebas(34), true, 8);
  public static final LunaFontRenderer fontRendererBebasRegGUI = new LunaFontRenderer(getFontBebas(43), true, 8);
  public static final LunaFontRenderer fontRendererBebasMinGUI = new LunaFontRenderer(getFontBebas(45), true, 8);
  public static final LunaFontRenderer fontRendererComfortaaRegMed = new LunaFontRenderer(getFont5(43), true, 8);
  public static final LunaFontRenderer fontRendererComfortaaReg = new LunaFontRenderer(getFont5(70), true, 8);
  public static final LunaFontRenderer fontRendererLuna = new LunaFontRenderer(getFont4(85), true, 8);
  public static final LunaFontRenderer fontRendererSFNormal = new LunaFontRenderer(getFontSFNormal(85), true, 8);
  public static final LunaFontRenderer fontRendererSFMedium = new LunaFontRenderer(getFontSFNormal(65), true, 8);
  public static final LunaFontRenderer fontRendererSFSmall = new LunaFontRenderer(getFontSFNormal(39), true, 8);
  public static final LunaFontRenderer fontRendererBOLD = new LunaFontRenderer(getFont2(60), true, 8);
  public static final LunaFontRenderer ComfortaaSmall = new LunaFontRenderer(getFont5(26), true, 8);
  public static final LunaFontRenderer ComfortaaMedium = new LunaFontRenderer(getFont5(36), true, 8);
  public static final LunaFontRenderer ComfortaaLarge = new LunaFontRenderer(getFont5(60), true, 8);
  
  public FontManager() {}
  
  private static Font getFont(int size)
  {
    Font font;
    try
    {
      InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("luna/Comfortaa_Regular.ttf")).getInputStream();
      Font font = Font.createFont(0, is);
      font = font.deriveFont(0, size);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      System.out.println("Error loading font");
      font = new Font("default", 0, size);
    }
    return font;
  }
  
  private static Font getFontSFNormal(int size)
  {
    Font font;
    try
    {
      InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("luna/sfnorm.otf")).getInputStream();
      Font font = Font.createFont(0, is);
      font = font.deriveFont(0, size);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      System.out.println("Error loading font");
      font = new Font("default", 0, size);
    }
    return font;
  }
  
  private static Font getFont2(int size)
  {
    Font font;
    try
    {
      InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("luna/Comfortaa_Regular.ttf")).getInputStream();
      Font font = Font.createFont(0, is);
      font = font.deriveFont(0, size);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      System.out.println("Error loading font");
      font = new Font("default", 0, size);
    }
    return font;
  }
  
  private static Font getFont3(int size)
  {
    Font font;
    try
    {
      InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("luna/theboldfont.ttf")).getInputStream();
      Font font = Font.createFont(0, is);
      font = font.deriveFont(0, size);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      System.out.println("Error loading font");
      font = new Font("default", 0, size);
    }
    return font;
  }
  
  private static Font getFont4(int size)
  {
    Font font;
    try
    {
      InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("luna/Lies.ttf")).getInputStream();
      Font font = Font.createFont(0, is);
      font = font.deriveFont(0, size);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      System.out.println("Error loading font");
      font = new Font("default", 0, size);
    }
    return font;
  }
  
  private static Font getFont5(int size)
  {
    Font font;
    try
    {
      InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("C.ttf")).getInputStream();
      Font font = Font.createFont(0, is);
      font = font.deriveFont(0, size);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      System.out.println("Error loading font");
      font = new Font("default", 0, size);
    }
    return font;
  }
  
  private static Font getFontBebas(int size)
  {
    Font font;
    try
    {
      InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("luna/bebas.ttf")).getInputStream();
      Font font = Font.createFont(0, is);
      font = font.deriveFont(0, size);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      System.out.println("Error loading font");
      font = new Font("default", 0, size);
    }
    return font;
  }
}
