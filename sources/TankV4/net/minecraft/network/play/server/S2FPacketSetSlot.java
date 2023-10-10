package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S2FPacketSetSlot implements Packet {
   private int slot;
   private int windowId;
   private ItemStack item;

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = var1.readByte();
      this.slot = var1.readShort();
      this.item = var1.readItemStackFromBuffer();
   }

   public ItemStack func_149174_e() {
      return this.item;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeByte(this.windowId);
      var1.writeShort(this.slot);
      var1.writeItemStackToBuffer(this.item);
   }

   public S2FPacketSetSlot(int var1, int var2, ItemStack var3) {
      this.windowId = var1;
      this.slot = var2;
      this.item = var3 == null ? null : var3.copy();
   }

   public int func_149173_d() {
      return this.slot;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleSetSlot(this);
   }

   public int func_149175_c() {
      return this.windowId;
   }

   public S2FPacketSetSlot() {
   }
}
