package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S09PacketHeldItemChange implements Packet {
   private int heldItemHotbarIndex;

   public int getHeldItemHotbarIndex() {
      return this.heldItemHotbarIndex;
   }

   public S09PacketHeldItemChange(int var1) {
      this.heldItemHotbarIndex = var1;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleHeldItemChange(this);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public S09PacketHeldItemChange() {
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.heldItemHotbarIndex = var1.readByte();
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeByte(this.heldItemHotbarIndex);
   }
}
