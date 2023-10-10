package me.AveReborn.util;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;
import me.AveReborn.Client;
import me.AveReborn.ui.ClientNotification;
import me.AveReborn.ui.ClientNotification.Type;
import me.AveReborn.util.ChatType;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import me.AveReborn.util.fontRenderer.CFont.CFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;

public enum ClientUtil {
   INSTANCE;

   private static ArrayList notifications = new ArrayList();
   public CFontRenderer sansation19 = new CFontRenderer(this.getSansation(19.0F), true, true);
   public CFontRenderer tahoma19 = new CFontRenderer(this.getTahoma(19.0F), true, true);

   public Font getTahoma(float size) {
      Font f;
      try {
         InputStream ex = UnicodeFontRenderer.class.getResourceAsStream("fonts/tahoma.ttf");
         f = Font.createFont(0, ex);
         f = f.deriveFont(0, size);
      } catch (Exception var4) {
         var4.printStackTrace();
         System.out.println("Error loading font");
         f = new Font("Arial", 0, (int)size);
      }

      return f;
   }

   public Font getSansation(float size) {
      Font f;
      try {
         InputStream ex = UnicodeFontRenderer.class.getResourceAsStream("fonts/sansation.ttf");
         f = Font.createFont(0, ex);
         f = f.deriveFont(0, size);
      } catch (Exception var4) {
         var4.printStackTrace();
         System.out.println("Error loading font");
         f = new Font("Arial", 0, (int)size);
      }

      return f;
   }

   public void drawNotifications() {
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      double startY = (double)(res.getScaledHeight() - 25);
      double lastY = startY;

      for(int i = 0; i < notifications.size(); ++i) {
         ClientNotification not = (ClientNotification)notifications.get(i);
         if(not.shouldDelete()) {
            notifications.remove(i);
         }

         not.draw(startY, lastY);
         startY -= not.getHeight() + 1.0D;
      }

   }

   public static void sendClientMessage(String message, Type type) {
      notifications.add(new ClientNotification(message, type));
   }

   public static int reAlpha(int color, float alpha) {
      Color c = new Color(color);
      float r = 0.003921569F * (float)c.getRed();
      float g = 0.003921569F * (float)c.getGreen();
      float b = 0.003921569F * (float)c.getBlue();
      return (new Color(r, g, b, alpha)).getRGB();
   }

   public static void sendChatMessage(String message, ChatType type) {
      if(type == ChatType.INFO) {
         Minecraft.getMinecraft();
         Minecraft.thePlayer.addChatMessage(new ChatComponentText("\u00a7c\u00a7l[" + Client.CLIENT_NAME + "]\u00a7r\u00a77\u00a7l " + message));
      } else if(type == ChatType.WARN) {
         Minecraft.getMinecraft();
         Minecraft.thePlayer.addChatMessage(new ChatComponentText("\u00a7c\u00a7l[" + Client.CLIENT_NAME + "]\u00a7r\u00a7e\u00a7l " + message));
      } else if(type == ChatType.ERROR) {
         Minecraft.getMinecraft();
         Minecraft.thePlayer.addChatMessage(new ChatComponentText("\u00a7b\u00a7l[" + Client.CLIENT_NAME + "]\u00a7r\u00a74\u00a7l " + message));
      }

   }
}
