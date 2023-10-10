package net.minecraft.network.status.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class C00PacketServerQuery implements Packet {
   public void readPacketData(PacketBuffer var1) throws IOException {
   }

   public void processPacket(INetHandlerStatusServer var1) {
      var1.processServerQuery(this);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerStatusServer)var1);
   }
}
