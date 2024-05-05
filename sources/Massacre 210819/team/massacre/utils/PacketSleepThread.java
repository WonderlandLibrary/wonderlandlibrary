package team.massacre.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketSleepThread extends Thread {
   public PacketSleepThread(Packet packet, long delay) {
      super(() -> {
         sleep_ms(delay);
         if (Minecraft.getMinecraft().thePlayer != null) {
            PacketUtil.sendPacketNoEvent(packet);
         }

      });
   }

   static void sleep_ms(long delay) {
      try {
         sleep(delay);
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }

   }

   public static void delayPacket(Packet packet, long delay) {
      (new PacketSleepThread(packet, delay)).start();
   }
}
