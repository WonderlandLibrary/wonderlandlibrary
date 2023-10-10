package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

public class S22PacketMultiBlockChange implements Packet {
   private ChunkCoordIntPair chunkPosCoord;
   private S22PacketMultiBlockChange.BlockUpdateData[] changedBlocks;

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleMultiBlockChange(this);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeInt(this.chunkPosCoord.chunkXPos);
      var1.writeInt(this.chunkPosCoord.chunkZPos);
      var1.writeVarIntToBuffer(this.changedBlocks.length);
      S22PacketMultiBlockChange.BlockUpdateData[] var5;
      int var4 = (var5 = this.changedBlocks).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         S22PacketMultiBlockChange.BlockUpdateData var2 = var5[var3];
         var1.writeShort(var2.func_180089_b());
         var1.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(var2.getBlockState()));
      }

   }

   public S22PacketMultiBlockChange.BlockUpdateData[] getChangedBlocks() {
      return this.changedBlocks;
   }

   static ChunkCoordIntPair access$1(S22PacketMultiBlockChange var0) {
      return var0.chunkPosCoord;
   }

   public S22PacketMultiBlockChange() {
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.chunkPosCoord = new ChunkCoordIntPair(var1.readInt(), var1.readInt());
      this.changedBlocks = new S22PacketMultiBlockChange.BlockUpdateData[var1.readVarIntFromBuffer()];

      for(int var2 = 0; var2 < this.changedBlocks.length; ++var2) {
         this.changedBlocks[var2] = new S22PacketMultiBlockChange.BlockUpdateData(this, var1.readShort(), (IBlockState)Block.BLOCK_STATE_IDS.getByValue(var1.readVarIntFromBuffer()));
      }

   }

   public S22PacketMultiBlockChange(int var1, short[] var2, Chunk var3) {
      this.chunkPosCoord = new ChunkCoordIntPair(var3.xPosition, var3.zPosition);
      this.changedBlocks = new S22PacketMultiBlockChange.BlockUpdateData[var1];

      for(int var4 = 0; var4 < this.changedBlocks.length; ++var4) {
         this.changedBlocks[var4] = new S22PacketMultiBlockChange.BlockUpdateData(this, var2[var4], var3);
      }

   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public class BlockUpdateData {
      private final IBlockState blockState;
      private final short chunkPosCrammed;
      final S22PacketMultiBlockChange this$0;

      public BlockUpdateData(S22PacketMultiBlockChange var1, short var2, Chunk var3) {
         this.this$0 = var1;
         this.chunkPosCrammed = var2;
         this.blockState = var3.getBlockState(this.getPos());
      }

      public IBlockState getBlockState() {
         return this.blockState;
      }

      public short func_180089_b() {
         return this.chunkPosCrammed;
      }

      public BlockPos getPos() {
         return new BlockPos(S22PacketMultiBlockChange.access$1(this.this$0).getBlock(this.chunkPosCrammed >> 12 & 15, this.chunkPosCrammed & 255, this.chunkPosCrammed >> 8 & 15));
      }

      public BlockUpdateData(S22PacketMultiBlockChange var1, short var2, IBlockState var3) {
         this.this$0 = var1;
         this.chunkPosCrammed = var2;
         this.blockState = var3;
      }
   }
}
