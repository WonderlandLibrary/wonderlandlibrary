package de.violence.packet;

import net.minecraft.client.Minecraft;

public class Packet {
   private String name;
   public Minecraft mc = Minecraft.getMinecraft();

   public Packet(String packetName) {
      this.name = packetName;
   }

   public boolean dispatchPacket(Object... objects) {
      return false;
   }

   public String getName() {
      return this.name;
   }
}
