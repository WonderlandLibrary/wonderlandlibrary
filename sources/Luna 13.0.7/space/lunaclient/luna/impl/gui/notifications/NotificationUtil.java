package space.lunaclient.luna.impl.gui.notifications;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import space.lunaclient.luna.impl.gui.LunaFontRenderer;
import space.lunaclient.luna.impl.managers.FontManager;

public enum NotificationUtil
{
  INSTANCE;
  
  public static ArrayList<ClientNotification> notifications = new ArrayList();
  
  private NotificationUtil() {}
  
  public int reAlpha(int color, float alpha)
  {
    Color c = new Color(color);
    float r = 0.003921569F * c.getRed();
    float g = 0.003921569F * c.getGreen();
    float b = 0.003921569F * c.getBlue();
    return new Color(r, g, b, alpha).getRGB();
  }
  
  public void drawNotifications()
  {
    ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    double startY = res.getScaledHeight() - 45;
    double lastY = startY;
    for (int i = 0; i < notifications.size(); i++)
    {
      ClientNotification not = (ClientNotification)notifications.get(i);
      if (not.shouldDelete()) {
        notifications.remove(i);
      }
      not.draw(startY, lastY);
      startY -= not.getHeight() + 1.0D;
    }
  }
  
  public static void sendClientMessage(String message, int time, ClientNotification.Type type)
  {
    notifications.add(new ClientNotification(message, time, type));
  }
  
  public static double getAnimationState(double animation, double finalState, double speed)
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
    ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
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
  
  public void drawCenteredString(String text, float x, float y, int color)
  {
    drawString(text, x - (Minecraft.fontRendererObj.getStringWidth(text) >> 1), y, color);
  }
  
  public int drawString(String text, float x, float y, int color)
  {
    return FontManager.fontRendererBebasMinGUI.drawStringWithShadow(text, x, y - 1.5F, color);
  }
}
