package me.rhys.base.font;

import java.awt.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

public enum Fonts {
  INSTANCE;
  
  private FontRenderer bit;
  
  private FontRenderer bigRise;
  
  private FontRenderer rise;
  
  private FontRenderer bigApple;
  
  private FontRenderer apple;
  
  public FontRenderer getApple() {
    return this.apple;
  }
  
  public FontRenderer getBigApple() {
    return this.bigApple;
  }
  
  public FontRenderer getRise() {
    return this.rise;
  }
  
  public FontRenderer getBigRise() {
    return this.bigRise;
  }
  
  public FontRenderer getBit() {
    return this.bit;
  }
  
  public void setup() {
    this.apple = new FontRenderer(fontFromTTF(new ResourceLocation("Lite/fonts/apple.ttf"), 18.0F, 0), true, true);
    this.bigApple = new FontRenderer(fontFromTTF(new ResourceLocation("Lite/fonts/apple.ttf"), 26.0F, 0), true, true);
    this.rise = new FontRenderer(fontFromTTF(new ResourceLocation("Lite/fonts/comfortaa.ttf"), 18.0F, 0), true, true);
    this.bigRise = new FontRenderer(fontFromTTF(new ResourceLocation("Lite/fonts/comfortaa.ttf"), 26.0F, 0), true, true);
    loadBitFont();
  }
  
  public void loadBitFont() {
    this
      
      .bit = new FontRenderer((Minecraft.getMinecraft()).gameSettings, new ResourceLocation("Lite/fonts/Bit.png"), (Minecraft.getMinecraft()).renderEngine, false);
    if ((Minecraft.getMinecraft()).gameSettings.language != null) {
      this.bit.setUnicodeFlag(Minecraft.getMinecraft().isUnicode());
      this.bit.setBidiFlag((Minecraft.getMinecraft()).mcLanguageManager.isCurrentLanguageBidirectional());
    } 
    (Minecraft.getMinecraft()).mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.bit);
  }
  
  public static Font fontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
    Font output = null;
    try {
      output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
      output = output.deriveFont(fontSize);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return output;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\font\Fonts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */