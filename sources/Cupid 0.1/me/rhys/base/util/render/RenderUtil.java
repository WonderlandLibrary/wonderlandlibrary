package me.rhys.base.util.render;

import java.util.UUID;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderUtil {
  public static boolean renderAltSkin(String name, int x, int y, int size) {
    try {
      Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, 8.0F, 8, 8, size, size, 64.0F, 64.0F);
    } catch (Exception e) {
      return false;
    } 
    return true;
  }
  
  public static boolean renderAltSkin(EntityPlayer player, Vec2f pos, int size) {
    if (((AbstractClientPlayer)player).getLocationSkin() == null)
      return false; 
    try {
      GL11.glPushMatrix();
      Minecraft.getMinecraft().getTextureManager().bindTexture(((AbstractClientPlayer)player).getLocationSkin());
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Gui.drawScaledCustomSizeModalRect((int)pos.getX(), (int)pos.getY(), 8.0F, 8.0F, 8, 8, size, size, 64.0F, 64.0F);
      GL11.glPopMatrix();
    } catch (Exception e) {
      return false;
    } 
    return true;
  }
  
  public static boolean renderAltSkin(EntityPlayer player, float x, float y, int size) {
    if (((AbstractClientPlayer)player).getLocationSkin() == null)
      return false; 
    try {
      GL11.glPushMatrix();
      Minecraft.getMinecraft().getTextureManager().bindTexture(((AbstractClientPlayer)player).getLocationSkin());
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Gui.drawScaledCustomSizeModalRect((int)x, (int)y, 8.0F, 8.0F, 8, 8, size, size, 64.0F, 64.0F);
      GL11.glPopMatrix();
    } catch (Exception e) {
      return false;
    } 
    return true;
  }
  
  public static boolean renderAltSkin(ResourceLocation location, Vec2f pos, int size) {
    try {
      Minecraft.getMinecraft().getTextureManager().bindTexture(location);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Gui.drawScaledCustomSizeModalRect((int)pos.x, (int)pos.y, 8.0F, 8.0F, 8, 8, size, size, 64.0F, 64.0F);
    } catch (Exception e) {
      return false;
    } 
    return true;
  }
  
  public static boolean renderAltSkin(Vec2f pos, int size) {
    try {
      Minecraft.getMinecraft().getTextureManager().bindTexture(DefaultPlayerSkin.getDefaultSkin(new UUID(0L, 10L)));
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Gui.drawScaledCustomSizeModalRect((int)pos.getX(), (int)pos.getY(), 8.0F, 8.0F, 8, 8, size, size, 64.0F, 64.0F);
    } catch (Exception e) {
      return false;
    } 
    return true;
  }
  
  public static boolean renderAltSkin(String name, Vec2f pos, int size, UUID uuid) {
    try {
      Minecraft.getMinecraft().getTextureManager().bindTexture(DefaultPlayerSkin.getDefaultSkin(uuid));
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Gui.drawScaledCustomSizeModalRect((int)pos.getX(), (int)pos.getY(), 8.0F, 8.0F, 8, 8, size, size, 64.0F, 64.0F);
    } catch (Exception e) {
      return false;
    } 
    return true;
  }
  
  public static void drawOutlineRect(Vec2f start, Vec2f end, int color) {
    GlStateManager.pushMatrix();
    float f3 = (color >> 24 & 0xFF) / 255.0F;
    float f = (color >> 16 & 0xFF) / 255.0F;
    float f1 = (color >> 8 & 0xFF) / 255.0F;
    float f2 = (color & 0xFF) / 255.0F;
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    GlStateManager.enableBlend();
    GlStateManager.disableTexture2D();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.color(f, f1, f2, f3);
    worldrenderer.begin(2, DefaultVertexFormats.POSITION);
    worldrenderer.pos(start.x, end.y, 0.0D).endVertex();
    worldrenderer.pos(end.x, end.y, 0.0D).endVertex();
    worldrenderer.pos(end.x, start.y, 0.0D).endVertex();
    worldrenderer.pos(start.x, start.y, 0.0D).endVertex();
    tessellator.draw();
    GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();
    GlStateManager.popMatrix();
  }
  
  public static void drawBorderedRectangle(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    GlStateManager.disableTexture2D();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    if (left < right) {
      double i = left;
      left = right;
      right = i;
    } 
    if (top < bottom) {
      double j = top;
      top = bottom;
      bottom = j;
    } 
    float insideAlpha = (insideColor >> 24 & 0xFF) / 255.0F;
    float insideRed = (insideColor >> 16 & 0xFF) / 255.0F;
    float insideGreen = (insideColor >> 8 & 0xFF) / 255.0F;
    float insieBlue = (insideColor & 0xFF) / 255.0F;
    float borderAlpha = (borderColor >> 24 & 0xFF) / 255.0F;
    float borderRed = (borderColor >> 16 & 0xFF) / 255.0F;
    float borderGreen = (borderColor >> 8 & 0xFF) / 255.0F;
    float borderBlue = (borderColor & 0xFF) / 255.0F;
    if (insideAlpha > 0.0F) {
      GlStateManager.color(insideRed, insideGreen, insieBlue, insideAlpha);
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(left, bottom, 0.0D).endVertex();
      worldrenderer.pos(right, bottom, 0.0D).endVertex();
      worldrenderer.pos(right, top, 0.0D).endVertex();
      worldrenderer.pos(left, top, 0.0D).endVertex();
      tessellator.draw();
    } 
    if (borderAlpha > 0.0F) {
      GlStateManager.color(borderRed, borderGreen, borderBlue, borderAlpha);
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(left - (borderIncludedInBounds ? 0.0D : borderWidth), bottom, 0.0D).endVertex();
      worldrenderer.pos(left - (borderIncludedInBounds ? borderWidth : 0.0D), bottom, 0.0D).endVertex();
      worldrenderer.pos(left - (borderIncludedInBounds ? borderWidth : 0.0D), top, 0.0D).endVertex();
      worldrenderer.pos(left - (borderIncludedInBounds ? 0.0D : borderWidth), top, 0.0D).endVertex();
      tessellator.draw();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(left, top - (borderIncludedInBounds ? borderWidth : 0.0D), 0.0D).endVertex();
      worldrenderer.pos(right, top - (borderIncludedInBounds ? borderWidth : 0.0D), 0.0D).endVertex();
      worldrenderer.pos(right, top - (borderIncludedInBounds ? 0.0D : borderWidth), 0.0D).endVertex();
      worldrenderer.pos(left, top - (borderIncludedInBounds ? 0.0D : borderWidth), 0.0D).endVertex();
      tessellator.draw();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(right + (borderIncludedInBounds ? borderWidth : 0.0D), bottom, 0.0D).endVertex();
      worldrenderer.pos(right + (borderIncludedInBounds ? 0.0D : borderWidth), bottom, 0.0D).endVertex();
      worldrenderer.pos(right + (borderIncludedInBounds ? 0.0D : borderWidth), top, 0.0D).endVertex();
      worldrenderer.pos(right + (borderIncludedInBounds ? borderWidth : 0.0D), top, 0.0D).endVertex();
      tessellator.draw();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(left, bottom - (borderIncludedInBounds ? 0.0D : borderWidth), 0.0D).endVertex();
      worldrenderer.pos(right, bottom + (borderIncludedInBounds ? 0.0D : borderWidth), 0.0D).endVertex();
      worldrenderer.pos(right, bottom + (borderIncludedInBounds ? borderWidth : 0.0D), 0.0D).endVertex();
      worldrenderer.pos(left, bottom + (borderIncludedInBounds ? borderWidth : 0.0D), 0.0D).endVertex();
      tessellator.draw();
    } 
    GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();
    GlStateManager.popMatrix();
  }
  
  public static void drawRect(Vec2f pos, Vec2f end, int color) {
    Gui.drawRect((int)pos.getX(), (int)pos.getY(), (int)end.getX(), (int)end.getY(), color);
  }
  
  public static void drawRect(float x, float y, int width, int height, int color) {
    Gui.drawRect((int)x, (int)y, (int)x + width, (int)y + height, color);
  }
  
  public static void drawRect(Vec2f pos, int width, int height, int color) {
    drawRect(pos.x, pos.y, width, height, color);
  }
  
  public static double rainbow(int delay) {
    double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 13.0D);
    rainbowState %= 360.0D;
    return rainbowState;
  }
  
  public static void color(int color) {
    float alpha = (color >> 24 & 0xFF) / 255.0F;
    float red = (color >> 16 & 0xFF) / 255.0F;
    float green = (color >> 8 & 0xFF) / 255.0F;
    float blue = (color & 0xFF) / 255.0F;
    GlStateManager.color(red, green, blue, alpha);
  }
  
  public static float[] getColors(int color) {
    float alpha = (color >> 24 & 0xFF) / 255.0F;
    float red = (color >> 16 & 0xFF) / 255.0F;
    float green = (color >> 8 & 0xFF) / 255.0F;
    float blue = (color & 0xFF) / 255.0F;
    return new float[] { red, green, blue, alpha };
  }
  
  public static void drawImage(ResourceLocation location, float x, float y, int width, int height) {
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.enableTexture2D();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    Minecraft.getMinecraft().getTextureManager().bindTexture(location);
    Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, 0.0F, 0.0F, width, height, width, height);
    GlStateManager.popMatrix();
  }
  
  public static void drawImage(ResourceLocation location, Vec2f pos, int width, int height) {
    drawImage(location, pos.x, pos.y, width, height);
  }
  
  public static void clipRect(Vec2f pos, Vec2f endPos, int scale) {
    ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft(), scale);
    int factor = resolution.getScaleFactor();
    GL11.glScissor((int)(pos.x * factor), (int)((resolution.getScaledHeight() - endPos.y) * factor), (int)((endPos.x - pos.x) * factor), (int)((endPos.y - pos.y) * factor));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\render\RenderUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */