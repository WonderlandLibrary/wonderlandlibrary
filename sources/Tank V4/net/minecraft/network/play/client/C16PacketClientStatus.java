package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C16PacketClientStatus implements Packet {
   private C16PacketClientStatus.EnumState status;

   public C16PacketClientStatus() {
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeEnumValue(this.status);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.status = (C16PacketClientStatus.EnumState)var1.readEnumValue(C16PacketClientStatus.EnumState.class);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      var1.processClientStatus(this);
   }

   public C16PacketClientStatus.EnumState getStatus() {
      return this.status;
   }

   public C16PacketClientStatus(C16PacketClientStatus.EnumState var1) {
      this.status = var1;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayServer)var1);
   }

   public static enum EnumState {
      PERFORM_RESPAWN,
      OPEN_INVENTORY_ACHIEVEMENT;

      private static final C16PacketClientStatus.EnumState[] ENUM$VALUES = new C16PacketClientStatus.EnumState[]{PERFORM_RESPAWN, REQUEST_STATS, OPEN_INVENTORY_ACHIEVEMENT};
      REQUEST_STATS;
   }
}
