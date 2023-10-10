package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S35PacketUpdateTileEntity implements Packet {
   private int metadata;
   private BlockPos blockPos;
   private NBTTagCompound nbt;

   public S35PacketUpdateTileEntity() {
   }

   public S35PacketUpdateTileEntity(BlockPos var1, int var2, NBTTagCompound var3) {
      this.blockPos = var1;
      this.metadata = var2;
      this.nbt = var3;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeBlockPos(this.blockPos);
      var1.writeByte((byte)this.metadata);
      var1.writeNBTTagCompoundToBuffer(this.nbt);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.blockPos = var1.readBlockPos();
      this.metadata = var1.readUnsignedByte();
      this.nbt = var1.readNBTTagCompoundFromBuffer();
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public BlockPos getPos() {
      return this.blockPos;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleUpdateTileEntity(this);
   }

   public int getTileEntityType() {
      return this.metadata;
   }

   public NBTTagCompound getNbtCompound() {
      return this.nbt;
   }
}
