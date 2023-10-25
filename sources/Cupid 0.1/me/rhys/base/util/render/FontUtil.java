package me.rhys.base.util.render;

import me.rhys.base.Lite;
import me.rhys.base.font.FontRenderer;
import me.rhys.base.font.Fonts;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.render.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class FontUtil {
  private static final FontRenderer fontRenderer = (Minecraft.getMinecraft()).fontRendererObj;
  
  public static void drawString(String str, float x, float y, int color) {
    if (getType() == HUD.Fonts.MINECRAFT) {
      fontRenderer.drawString(str, x, y, color);
    } else {
      typeToFont().drawString(str, new Vec2f(x, y), color);
    } 
  }
  
  public static void drawString(String str, Vec2f pos, int color) {
    drawString(str, pos.x, pos.y, color);
  }
  
  public static void drawCenteredString(String str, float x, float y, int color) {
    drawString(str, x - getStringWidth(str) / 2.0F, y - getFontHeight() / 2.0F, color);
  }
  
  public static void drawCenteredString(String str, Vec2f pos, int color) {
    drawCenteredString(str, pos.x, pos.y, color);
  }
  
  public static void drawStringWithShadow(String str, float x, float y, int color) {
    if (getType() == HUD.Fonts.MINECRAFT) {
      fontRenderer.drawStringWithShadow(str, x, y, color);
    } else {
      typeToFont().drawStringWithShadow(str, x, y, color);
    } 
  }
  
  public static void drawStringWithShadow(String str, Vec2f pos, int color) {
    drawStringWithShadow(str, pos.x, pos.y, color);
  }
  
  public static void drawCenteredStringWithShadow(String str, float x, float y, int color) {
    drawStringWithShadow(str, x - getStringWidth(str) / 2.0F, y - getFontHeight() / 2.0F, color);
  }
  
  public static void drawCenteredStringWithShadow(String str, Vec2f pos, int color) {
    drawCenteredStringWithShadow(str, pos.x, pos.y, color);
  }
  
  public static float getStringWidth(String str) {
    if (getType() == HUD.Fonts.MINECRAFT)
      return fontRenderer.getStringWidth(str); 
    return typeToFont().getStringWidth(str);
  }
  
  public static float getFontHeight() {
    if (getType() == HUD.Fonts.MINECRAFT)
      return fontRenderer.FONT_HEIGHT; 
    return typeToFont().getHeight();
  }
  
  public static FontRenderer typeToFont() {
    switch (getType()) {
      case APPLE:
        return Fonts.INSTANCE.getApple();
      case RISE:
        return Fonts.INSTANCE.getRise();
    } 
    return Fonts.INSTANCE.getApple();
  }
  
  private static HUD.Fonts getType() {
    return ((HUD)Lite.MODULE_FACTORY.findByClass(HUD.class)).font;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\render\FontUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */