package cn.hanabi;

import cn.hanabi.injection.interfaces.IFontRenderer;
import cn.hanabi.injection.interfaces.IMinecraft;
import cn.hanabi.injection.interfaces.INetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.util.Timer;

public class Wrapper {
   public static final Minecraft mc = Minecraft.getMinecraft();
   public static boolean canSendMotionPacket = true;
   private static boolean notificationsAllowed = false;

   public static EntityRenderer getEntityRenderer() {
      return getMinecraft().entityRenderer;
   }

   public static Minecraft getMinecraft() {
      return Minecraft.getMinecraft();
   }

   public static EntityPlayerSP getPlayer() {
      return getMinecraft().thePlayer;
   }

   public static WorldClient getWorld() {
      return getMinecraft().theWorld;
   }

   public static PlayerControllerMP getPlayerController() {
      return getMinecraft().playerController;
   }

   public static NetHandlerPlayClient getNetHandler() {
      return getMinecraft().getNetHandler();
   }

   public static GameSettings getGameSettings() {
      return getMinecraft().gameSettings;
   }

   public static Timer getTimer() {
      return ((IMinecraft)Minecraft.getMinecraft()).getTimer();
   }

   public static void sendPacketNoEvent(Packet i) {
      ((INetworkManager)mc.getNetHandler().getNetworkManager()).sendPacketNoEvent(i);
   }

   public static boolean notificationsAllowed() {
      return notificationsAllowed;
   }

   public static void notificationsAllowed(boolean value) {
      notificationsAllowed = value;
   }

   public static ItemStack getStackInSlot(int index) {
      return getPlayer().inventoryContainer.getSlot(index).getStack();
   }

   public static IFontRenderer getFontRender() {
      return getFontRender();
   }
}
