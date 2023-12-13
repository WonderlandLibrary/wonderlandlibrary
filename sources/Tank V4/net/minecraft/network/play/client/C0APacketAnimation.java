package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0APacketAnimation implements Packet {
   public void writePacketData(PacketBuffer var1) throws IOException {
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayServer)var1);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      var1.handleAnimation(this);
   }
}
