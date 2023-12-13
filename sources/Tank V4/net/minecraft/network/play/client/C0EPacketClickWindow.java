package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0EPacketClickWindow implements Packet {
   private int windowId;
   private int slotId;
   private int mode;
   private short actionNumber;
   private int usedButton;
   private ItemStack clickedItem;

   public C0EPacketClickWindow(int var1, int var2, int var3, int var4, ItemStack var5, short var6) {
      this.windowId = var1;
      this.slotId = var2;
      this.usedButton = var3;
      this.clickedItem = var5 != null ? var5.copy() : null;
      this.actionNumber = var6;
      this.mode = var4;
   }

   public C0EPacketClickWindow() {
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeByte(this.windowId);
      var1.writeShort(this.slotId);
      var1.writeByte(this.usedButton);
      var1.writeShort(this.actionNumber);
      var1.writeByte(this.mode);
      var1.writeItemStackToBuffer(this.clickedItem);
   }

   public ItemStack getClickedItem() {
      return this.clickedItem;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = var1.readByte();
      this.slotId = var1.readShort();
      this.usedButton = var1.readByte();
      this.actionNumber = var1.readShort();
      this.mode = var1.readByte();
      this.clickedItem = var1.readItemStackFromBuffer();
   }

   public int getMode() {
      return this.mode;
   }

   public short getActionNumber() {
      return this.actionNumber;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayServer)var1);
   }

   public int getWindowId() {
      return this.windowId;
   }

   public void processPacket(INetHandlerPlayServer var1) {
      var1.processClickWindow(this);
   }

   public int getSlotId() {
      return this.slotId;
   }

   public int getUsedButton() {
      return this.usedButton;
   }
}
