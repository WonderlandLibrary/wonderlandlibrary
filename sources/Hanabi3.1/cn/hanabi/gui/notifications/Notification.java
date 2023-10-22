package cn.hanabi.gui.notifications;

import cn.hanabi.Hanabi;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.utils.Colors;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.utils.fontmanager.HanabiFonts;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Notification {
   private final Type t;
   private final long stayTime;
   public String message;
   public TimeHelper timer;
   private double lastY;
   private double posY;
   private double width;
   private double height;
   private double animationX;
   private int color;
   private int imageWidth;

   public Notification(String message, Type type) {
      this.message = message;
      this.timer = new TimeHelper();
      this.timer.reset();
      if (Minecraft.getMinecraft().thePlayer != null) {
         this.width = (double)(Hanabi.INSTANCE.fontManager.comfortaa16.getStringWidth(message) + 35);
      } else {
         this.width = 0.0;
      }

      this.height = 20.0;
      this.animationX = this.width;
      this.stayTime = 1200L;
      this.imageWidth = 16;
      this.posY = -1.0;
      this.t = type;
      if (type.equals(Notification.Type.INFO)) {
         this.color = Colors.DARKGREY.c;
      } else if (type.equals(Notification.Type.ERROR)) {
         this.color = (new Color(36, 36, 36)).getRGB();
      } else if (type.equals(Notification.Type.SUCCESS)) {
         this.color = (new Color(36, 36, 36)).getRGB();
      } else if (type.equals(Notification.Type.WARNING)) {
         this.color = Colors.DARKGREY.c;
      }

   }

   public void draw(double getY, double lastY) {
      this.width = (double)(Hanabi.INSTANCE.fontManager.comfortaa15.getStringWidth(this.message) + 45);
      this.height = 22.0;
      this.imageWidth = 11;
      this.lastY = lastY;
      this.animationX = this.getAnimationState(this.animationX, this.isFinished() ? this.width : 0.0, Math.max(this.isFinished() ? 200.0 : 30.0, Math.abs(this.animationX - (this.isFinished() ? this.width : 0.0)) * 20.0) * 0.3);
      if (this.posY == -1.0) {
         this.posY = getY;
      } else {
         this.posY = this.getAnimationState(this.posY, getY, 200.0);
      }

      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      int x1 = (int)((double)res.getScaledWidth() - this.width + this.animationX);
      int x2 = (int)((double)res.getScaledWidth() + this.animationX);
      int y1 = (int)this.posY - 22;
      int y2 = (int)((double)y1 + this.height);
      RenderUtil.drawRect((float)x1, (float)y1, (float)x2, (float)y2, ClientUtil.reAlpha(this.color, 0.85F));
      RenderUtil.drawRect((float)x1, (float)(y2 - 1), (float)((long)x1 + Math.min((long)(x2 - x1) * (System.currentTimeMillis() - this.timer.getLastMs()) / this.stayTime, (long)(x2 - x1))), (float)y2, ClientUtil.reAlpha(-1, 0.85F));
      switch (this.t) {
         case ERROR:
            Hanabi.INSTANCE.fontManager.icon25.drawString(HanabiFonts.ICON_NOTIFY_ERROR, (float)(x1 + 3), (float)(y1 + 8), Colors.WHITE.c);
            break;
         case INFO:
            Hanabi.INSTANCE.fontManager.icon25.drawString(HanabiFonts.ICON_NOTIFY_INFO, (float)(x1 + 3), (float)(y1 + 8), Colors.WHITE.c);
            break;
         case SUCCESS:
            Hanabi.INSTANCE.fontManager.icon25.drawString(HanabiFonts.ICON_NOTIFY_SUCCESS, (float)(x1 + 3), (float)(y1 + 8), Colors.WHITE.c);
            break;
         case WARNING:
            Hanabi.INSTANCE.fontManager.icon25.drawString(HanabiFonts.ICON_NOTIFY_WARN, (float)(x1 + 3), (float)(y1 + 8), Colors.WHITE.c);
      }

      ++y1;
      if (this.message.contains(" Enabled")) {
         Hanabi.INSTANCE.fontManager.comfortaa15.drawString(this.message.replace(" Enabled", ""), (float)(x1 + 19), (float)((double)y1 + this.height / 4.0), -1);
         Hanabi.INSTANCE.fontManager.comfortaa15.drawString(" Enabled", (float)(x1 + 20 + Hanabi.INSTANCE.fontManager.comfortaa15.getStringWidth(this.message.replace(" Enabled", ""))), (float)((double)y1 + this.height / 4.0), Colors.GREY.c);
      } else if (this.message.contains(" Disabled")) {
         Hanabi.INSTANCE.fontManager.comfortaa15.drawString(this.message.replace(" Disabled", ""), (float)(x1 + 19), (float)((double)y1 + this.height / 4.0), -1);
         Hanabi.INSTANCE.fontManager.comfortaa15.drawString(" Disabled", (float)(x1 + 20 + Hanabi.INSTANCE.fontManager.comfortaa15.getStringWidth(this.message.replace(" Disabled", ""))), (float)((double)y1 + this.height / 4.0), Colors.GREY.c);
      } else {
         Hanabi.INSTANCE.fontManager.comfortaa15.drawString(this.message, (float)(x1 + 20), (float)((double)y1 + this.height / 4.0), -1);
      }

   }

   public boolean shouldDelete() {
      return this.isFinished() && this.animationX >= this.width;
   }

   private boolean isFinished() {
      return this.timer.isDelayComplete(this.stayTime) && this.posY == this.lastY;
   }

   public double getHeight() {
      return this.height;
   }

   public double getAnimationState(double animation, double finalState, double speed) {
      float add = (float)((double)(RenderUtil.delta / 1000.0F) * speed);
      if (animation < finalState) {
         if (animation + (double)add < finalState) {
            animation += (double)add;
         } else {
            animation = finalState;
         }
      } else if (animation - (double)add > finalState) {
         animation -= (double)add;
      } else {
         animation = finalState;
      }

      return animation;
   }

   public void drawImage(ResourceLocation image, int x, int y, int width, int height) {
      new ScaledResolution(Minecraft.getMinecraft());
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float)width, (float)height);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public static enum Type {
      SUCCESS,
      INFO,
      WARNING,
      ERROR;
   }
}
