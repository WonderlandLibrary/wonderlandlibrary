package space.lunaclient.luna.impl.gui.notifications;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import space.lunaclient.luna.util.Timer;

public class ClientNotification
{
  private String message;
  private Timer timer;
  private double lastY;
  private double posY;
  private double width;
  private double height;
  private double animationX;
  private int color;
  private int imageWidth;
  private ResourceLocation image;
  private long stayTime;
  
  ClientNotification(String message, int time, Type type)
  {
    this.message = message;
    this.timer = new Timer();
    this.timer.reset();
    Minecraft.getMinecraft();this.width = (Minecraft.fontRendererObj.getStringWidth(message) + 35);
    this.height = 19.0D;
    this.animationX = this.width;
    this.stayTime = time;
    this.imageWidth = 13;
    this.posY = -1.0D;
    this.image = new ResourceLocation("luna/icon/" + type.name().toLowerCase() + ".png");
    if (type.equals(Type.INFO)) {
      this.color = new Color(29, 29, 29).getRGB();
    } else if (type.equals(Type.ERROR)) {
      this.color = new Color(29, 29, 29).getRGB();
    } else if (type.equals(Type.SUCCESS)) {
      this.color = new Color(29, 29, 29).getRGB();
    } else if (type.equals(Type.WARNING)) {
      this.color = new Color(29, 29, 29).getRGB();
    }
  }
  
  public void draw(double getY, double lastY)
  {
    this.lastY = lastY;
    this.animationX = NotificationUtil.getAnimationState(this.animationX, isFinished() ? this.width : 0.0D, Math.max(isFinished() ? 200.0D : 30.0D, Math.abs(this.animationX - (isFinished() ? this.width : 0.0D)) * 5.0D));
    if (this.posY == -1.0D) {
      this.posY = getY;
    } else {
      this.posY = NotificationUtil.getAnimationState(this.posY, getY, 200.0D);
    }
    ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    int x1 = (int)(res.getScaledWidth() - this.width + this.animationX);int x2 = (int)(res.getScaledWidth() + this.animationX);int y1 = (int)this.posY;int y2 = (int)(y1 + this.height);
    Gui.drawRect(x1, y1, x2, y2, this.color);
    Gui.drawRect(x1, y2, x2, y2 + 0.5F, this.color);
    Gui.drawRect(x1, y2, x2, y2 + 0.5F, NotificationUtil.INSTANCE.reAlpha(1, 0.5F));
    
    Gui.drawRect(x1, y1, (int)(x1 + this.height), y2, NotificationUtil.INSTANCE.reAlpha(-1, 0.1F));
    NotificationUtil.INSTANCE.drawImage(this.image, (int)(x1 + (this.height - this.imageWidth) / 2.0D), y1 + (int)((this.height - this.imageWidth) / 2.0D), this.imageWidth, this.imageWidth);
    
    NotificationUtil.INSTANCE.drawCenteredString(this.message, (float)(x1 + this.width / 2.0D) + 10.0F, (float)(y1 + this.height / 3.5D), -1);
  }
  
  boolean shouldDelete()
  {
    return (isFinished()) && (this.animationX >= this.width);
  }
  
  private boolean isFinished()
  {
    return (this.timer.hasReached(this.stayTime)) && (this.posY == this.lastY);
  }
  
  public double getHeight()
  {
    return this.height;
  }
  
  public static enum Type
  {
    SUCCESS,  INFO,  WARNING,  ERROR;
    
    private Type() {}
  }
}
