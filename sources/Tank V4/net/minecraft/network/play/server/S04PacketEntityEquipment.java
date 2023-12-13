package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S04PacketEntityEquipment implements Packet {
   private int equipmentSlot;
   private ItemStack itemStack;
   private int entityID;

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleEntityEquipment(this);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.entityID);
      var1.writeShort(this.equipmentSlot);
      var1.writeItemStackToBuffer(this.itemStack);
   }

   public int getEquipmentSlot() {
      return this.equipmentSlot;
   }

   public S04PacketEntityEquipment(int var1, int var2, ItemStack var3) {
      this.entityID = var1;
      this.equipmentSlot = var2;
      this.itemStack = var3 == null ? null : var3.copy();
   }

   public S04PacketEntityEquipment() {
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityID = var1.readVarIntFromBuffer();
      this.equipmentSlot = var1.readShort();
      this.itemStack = var1.readItemStackFromBuffer();
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public int getEntityID() {
      return this.entityID;
   }
}
