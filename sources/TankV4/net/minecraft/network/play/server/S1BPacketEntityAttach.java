package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S1BPacketEntityAttach implements Packet {
   private int entityId;
   private int leash;
   private int vehicleEntityId;

   public int getVehicleEntityId() {
      return this.vehicleEntityId;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleEntityAttach(this);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = var1.readInt();
      this.vehicleEntityId = var1.readInt();
      this.leash = var1.readUnsignedByte();
   }

   public S1BPacketEntityAttach(int var1, Entity var2, Entity var3) {
      this.leash = var1;
      this.entityId = var2.getEntityId();
      this.vehicleEntityId = var3 != null ? var3.getEntityId() : -1;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeInt(this.entityId);
      var1.writeInt(this.vehicleEntityId);
      var1.writeByte(this.leash);
   }

   public S1BPacketEntityAttach() {
   }

   public int getLeash() {
      return this.leash;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public int getEntityId() {
      return this.entityId;
   }
}
