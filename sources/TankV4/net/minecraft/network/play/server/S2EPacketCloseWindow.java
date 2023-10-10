package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S2EPacketCloseWindow implements Packet {
   private int windowId;

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = var1.readUnsignedByte();
   }

   public S2EPacketCloseWindow(int var1) {
      this.windowId = var1;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeByte(this.windowId);
   }

   public S2EPacketCloseWindow() {
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleCloseWindow(this);
   }
}
