package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S30PacketWindowItems implements Packet {
   private ItemStack[] itemStacks;
   private int windowId;

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleWindowItems(this);
   }

   public S30PacketWindowItems() {
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeByte(this.windowId);
      var1.writeShort(this.itemStacks.length);
      ItemStack[] var5;
      int var4 = (var5 = this.itemStacks).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         ItemStack var2 = var5[var3];
         var1.writeItemStackToBuffer(var2);
      }

   }

   public int func_148911_c() {
      return this.windowId;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = var1.readUnsignedByte();
      short var2 = var1.readShort();
      this.itemStacks = new ItemStack[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.itemStacks[var3] = var1.readItemStackFromBuffer();
      }

   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public S30PacketWindowItems(int var1, List var2) {
      this.windowId = var1;
      this.itemStacks = new ItemStack[var2.size()];

      for(int var3 = 0; var3 < this.itemStacks.length; ++var3) {
         ItemStack var4 = (ItemStack)var2.get(var3);
         this.itemStacks[var3] = var4 == null ? null : var4.copy();
      }

   }

   public ItemStack[] getItemStacks() {
      return this.itemStacks;
   }
}
