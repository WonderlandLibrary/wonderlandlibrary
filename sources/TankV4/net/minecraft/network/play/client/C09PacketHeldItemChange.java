package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C09PacketHeldItemChange implements Packet {
   private int slotId;

   public void processPacket(INetHandlerPlayServer var1) {
      var1.processHeldItemChange(this);
   }

   public int getSlotId() {
      return this.slotId;
   }

   public C09PacketHeldItemChange() {
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeShort(this.slotId);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayServer)var1);
   }

   public C09PacketHeldItemChange(int var1) {
      this.slotId = var1;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.slotId = var1.readShort();
   }
}
