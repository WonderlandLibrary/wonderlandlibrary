package my.NewSnake.utils;

import java.util.ArrayList;
import java.util.List;
import my.NewSnake.event.events.MoveEvent;
import my.NewSnake.utils.minecraft.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;

public final class ClientUtils {
   public static FontRenderer clientFont;

   public static void offsetPosition(double var0) {
      double var2 = (double)movementInput().moveForward;
      double var4 = (double)movementInput().moveStrafe;
      float var6 = yaw();
      if (var2 != 0.0D || var4 != 0.0D) {
         if (var2 != 0.0D) {
            if (var4 > 0.0D) {
               var6 += (float)(var2 > 0.0D ? -45 : 45);
            } else if (var4 < 0.0D) {
               var6 += (float)(var2 > 0.0D ? 45 : -45);
            }

            var4 = 0.0D;
            if (var2 > 0.0D) {
               var2 = 1.0D;
            } else if (var2 < 0.0D) {
               var2 = -1.0D;
            }
         }

         player().setPositionAndUpdate(x() + var2 * var0 * Math.cos(Math.toRadians((double)(var6 + 90.0F))) + var4 * var0 * Math.sin(Math.toRadians((double)(var6 + 90.0F))), y(), z() + (var2 * var0 * Math.sin(Math.toRadians((double)(var6 + 90.0F))) - var4 * var0 * Math.cos(Math.toRadians((double)(var6 + 90.0F)))));
      }
   }

   public static void packet(Packet var0) {
      mc().getNetHandler().addToSendQueue(var0);
   }

   public static void setMoveSpeed(MoveEvent var0, double var1) {
      double var3 = (double)movementInput().moveForward;
      double var5 = (double)movementInput().moveStrafe;
      float var7 = yaw();
      if (var3 == 0.0D && var5 == 0.0D) {
         var0.setX(0.0D);
         var0.setZ(0.0D);
      } else {
         if (var3 != 0.0D) {
            if (var5 > 0.0D) {
               var7 += (float)(var3 > 0.0D ? -45 : 45);
            } else if (var5 < 0.0D) {
               var7 += (float)(var3 > 0.0D ? 45 : -45);
            }

            var5 = 0.0D;
            if (var3 > 0.0D) {
               var3 = 1.0D;
            } else if (var3 < 0.0D) {
               var3 = -1.0D;
            }
         }

         var0.setX(var3 * var1 * Math.cos(Math.toRadians((double)(var7 + 90.0F))) + var5 * var1 * Math.sin(Math.toRadians((double)(var7 + 90.0F))));
         var0.setZ(var3 * var1 * Math.sin(Math.toRadians((double)(var7 + 90.0F))) - var5 * var1 * Math.cos(Math.toRadians((double)(var7 + 90.0F))));
      }

   }

   public static void y(double var0) {
      player().posY = var0;
   }

   public static float pitch() {
      return player().rotationPitch;
   }

   public static void yaw(float var0) {
      player().rotationYaw = var0;
   }

   public static void pitch(float var0) {
      player().rotationPitch = var0;
   }

   public static MovementInput movementInput() {
      return player().movementInput;
   }

   public static void sendMessage(String var0) {
      (new ChatMessage.ChatMessageBuilder(true, true)).appendText(var0).setColor(EnumChatFormatting.GRAY).build().displayClientSided();
   }

   public static float yaw() {
      return player().rotationYaw;
   }

   public static void loadClientFont() {
      clientFont = new FontRenderer(mc().gameSettings, new ResourceLocation("client/font/ascii.png"), mc().renderEngine, false);
      if (mc().gameSettings.language != null) {
         mc();
         Minecraft.fontRendererObj.setUnicodeFlag(mc().isUnicode());
         mc();
         Minecraft.fontRendererObj.setBidiFlag(mc().mcLanguageManager.isCurrentLanguageBidirectional());
      }

      mc().mcResourceManager.registerReloadListener(clientFont);
   }

   public static EntityPlayerSP player() {
      mc();
      return Minecraft.thePlayer;
   }

   public static Minecraft mc() {
      return Minecraft.getMinecraft();
   }

   public static void x(double var0) {
      player().posX = var0;
   }

   public static WorldClient world() {
      mc();
      return Minecraft.theWorld;
   }

   public static double y() {
      return player().posY;
   }

   public static void sendMessage(String var0, boolean var1) {
      (new ChatMessage.ChatMessageBuilder(var1, true)).appendText(var0).setColor(EnumChatFormatting.GRAY).build().displayClientSided();
   }

   public static double x() {
      return player().posX;
   }

   public static void z(double var0) {
      player().posZ = var0;
   }

   public static FontRenderer clientFont() {
      return clientFont;
   }

   public static PlayerControllerMP playerController() {
      mc();
      return Minecraft.playerController;
   }

   public static List loadedEntityList() {
      ArrayList var0 = new ArrayList(world().loadedEntityList);
      var0.remove(player());
      return var0;
   }

   public static double z() {
      return player().posZ;
   }

   public static GameSettings gamesettings() {
      return mc().gameSettings;
   }
}
