package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class S23PacketBlockChange implements Packet {
   private IBlockState blockState;
   private BlockPos blockPosition;

   public S23PacketBlockChange(World var1, BlockPos var2) {
      this.blockPosition = var2;
      this.blockState = var1.getBlockState(var2);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeBlockPos(this.blockPosition);
      var1.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(this.blockState));
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleBlockChange(this);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public BlockPos getBlockPosition() {
      return this.blockPosition;
   }

   public IBlockState getBlockState() {
      return this.blockState;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.blockPosition = var1.readBlockPos();
      this.blockState = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(var1.readVarIntFromBuffer());
   }

   public S23PacketBlockChange() {
   }
}
