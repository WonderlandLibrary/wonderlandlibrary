package me.AveReborn.ui;

import me.AveReborn.Client;
import me.AveReborn.util.ClientUtil;
import me.AveReborn.util.RenderUtil;
import me.AveReborn.util.fontRenderer.FontManager;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import me.AveReborn.util.timeUtils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ClientNotification
{
  private String message;
  private TimeHelper timer;
  private double lastY;
  private double posY;
  private double width;
  private double height;
  private double animationX;
  private int color;
  private int imageWidth;
  private ResourceLocation image;
  private long stayTime;
  
  public ClientNotification(String message, Type type)
  {
    this.message = message;
    this.timer = new TimeHelper();
    this.timer.reset();
    this.width = (Client.instance.fontMgr.consolasbold14.getStringWidth(message) + 35);
    this.height = 20.0D;
    this.animationX = this.width;
    this.stayTime = 3000L;
    this.imageWidth = 13;
    this.posY = -1.0D;
    this.image = new ResourceLocation("Ave/notification/" + type.name().toLowerCase() + ".png");
    if (type.equals(Type.INFO)) {
      this.color = -2894893;
    } else if (type.equals(Type.ERROR)) {
      this.color = -1618884;
    } else if (type.equals(Type.SUCCESS)) {
      this.color = -13710223;
    } else if (type.equals(Type.WARNING)) {
      this.color = -932849;
    }
  }
  
  public void draw(double getY, double lastY)
  {
    this.lastY = lastY;
    this.animationX = getAnimationState(this.animationX, isFinished() ? this.width : 0.0D, Math.max(isFinished() ? 200 : 30, Math.abs(this.animationX - (isFinished() ? this.width : 0.0D)) * 5.0D));
    if (this.posY == -1.0D) {
      this.posY = getY;
    } else {
      this.posY = getAnimationState(this.posY, getY, 200.0D);
    }
    ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
    int x1 = (int)(res.getScaledWidth() - this.width + this.animationX);int x2 = (int)(res.getScaledWidth() + this.animationX);int y1 = (int)this.posY;int y2 = (int)(y1 + this.height);
    RenderUtil.drawRect(x1, y1, x2, y2, this.color);
    RenderUtil.drawRect(x1, y2, x2, y2 + 0.5F, this.color);
    RenderUtil.drawRect(x1, y2, x2, y2 + 0.5F, ClientUtil.reAlpha(1, 0.5F));
    RenderUtil.drawRect(x1, y1, (int)(x1 + this.height), y2, ClientUtil.reAlpha(-1, 0.1F));
    drawImage(this.image, (int)(x1 + (this.height - this.imageWidth) / 2.0D), y1 + (int)((this.height - this.imageWidth) / 2.0D), this.imageWidth, this.imageWidth);
    Client.instance.fontMgr.consolasbold14.drawCenteredString(this.message, (float)(x1 + this.width / 2.0D) + 10.0F, (float)(y1 + this.height / 3.0D), -1);
  }
  
  public boolean shouldDelete()
  {
    return (isFinished()) && (this.animationX >= this.width);
  }
  
  private boolean isFinished()
  {
    return (this.timer.isDelayComplete(this.stayTime)) && (this.posY == this.lastY);
  }
  
  public double getHeight()
  {
    return this.height;
  }
  
  public static enum Type
  {
    SUCCESS,  INFO,  WARNING,  ERROR;
  }
  
  public double getAnimationState(double animation, double finalState, double speed)
  {
    float add = (float)(0.01D * speed);
    if (animation < finalState)
    {
      if (animation + add < finalState) {
        animation += add;
      } else {
        animation = finalState;
      }
    }
    else if (animation - add > finalState) {
      animation -= add;
    } else {
      animation = finalState;
    }
    return animation;
  }
  
  public void drawImage(ResourceLocation image, int x, int y, int width, int height)
  {
    ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    GL11.glDisable(2929);
    GL11.glEnable(3042);
    GL11.glDepthMask(false);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    Minecraft.getMinecraft().getTextureManager().bindTexture(image);
    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
    GL11.glDepthMask(true);
    GL11.glDisable(3042);
    GL11.glEnable(2929);
  }
}
