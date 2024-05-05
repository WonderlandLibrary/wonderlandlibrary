package team.massacre.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtil {
   public static Minecraft mc = Minecraft.getMinecraft();
   public static double xC;
   public static double yC;
   public static double zC;
   public static double cX;
   public static double cY;
   public static double cZ;
   public static boolean isOnGround;
   public static boolean state;

   public static void interceptC03Pos(double x, double y, double z, boolean onGround) {
      xC = x;
      yC = y;
      zC = z;
      isOnGround = onGround;
   }

   public static void sendPacket(Packet packet) {
      mc.getNetHandler().addToSendQueue(packet);
   }

   public static void sendPacketNoEvent(Packet packet) {
      mc.getNetHandler().getNetworkManager().sendPacket(packet);
   }

   public static void sendPacketSilent(Packet packet) {
      mc.getNetHandler().getNetworkManager().sendPacket(packet);
   }

   public static void reset() {
      zC = 0.0D;
      yC = 0.0D;
      xC = 0.0D;
   }

   public static boolean getState() {
      return state;
   }

   public static void setState(boolean allow) {
      state = allow;
   }

   public static double getX() {
      return cX;
   }

   public static double getY() {
      return cY;
   }

   public static double getZ() {
      return cZ;
   }
}
