package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;

public class C08PacketPlayerBlockPlacement implements Packet {
   private float facingY;
   private float facingZ;
   private ItemStack stack;
   private static final BlockPos field_179726_a = new BlockPos(-1, -1, -1);
   private BlockPos position;
   private float facingX;
   private int placedBlockDirection;

   public BlockPos getPosition() {
      return this.position;
   }

   public C08PacketPlayerBlockPlacement(ItemStack var1) {
      this(field_179726_a, 255, var1, 0.0F, 0.0F, 0.0F);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeBlockPos(this.position);
      var1.writeByte(this.placedBlockDirection);
      var1.writeItemStackToBuffer(this.stack);
      var1.writeByte((int)(this.facingX * 16.0F));
      var1.writeByte((int)(this.facingY * 16.0F));
      var1.writeByte((int)(this.facingZ * 16.0F));
   }

   public float getPlacedBlockOffsetX() {
      return this.facingX;
   }

   public void processPacket(INetHandlerPlayServer var1) {
      var1.processPlayerBlockPlacement(this);
   }

   public C08PacketPlayerBlockPlacement(BlockPos var1, int var2, ItemStack var3, float var4, float var5, float var6) {
      this.position = var1;
      this.placedBlockDirection = var2;
      this.stack = var3 != null ? var3.copy() : null;
      this.facingX = var4;
      this.facingY = var5;
      this.facingZ = var6;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.position = var1.readBlockPos();
      this.placedBlockDirection = var1.readUnsignedByte();
      this.stack = var1.readItemStackFromBuffer();
      this.facingX = (float)var1.readUnsignedByte() / 16.0F;
      this.facingY = (float)var1.readUnsignedByte() / 16.0F;
      this.facingZ = (float)var1.readUnsignedByte() / 16.0F;
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public float getPlacedBlockOffsetZ() {
      return this.facingZ;
   }

   public int getPlacedBlockDirection() {
      return this.placedBlockDirection;
   }

   public C08PacketPlayerBlockPlacement() {
   }

   public float getPlacedBlockOffsetY() {
      return this.facingY;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayServer)var1);
   }
}
