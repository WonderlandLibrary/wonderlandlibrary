package my.NewSnake.event.events;

import my.NewSnake.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class BoundingBoxEvent extends Event {
   private Block block;
   private BlockPos blockPos;
   private AxisAlignedBB boundingBox;

   public BoundingBoxEvent(Block var1, BlockPos var2, AxisAlignedBB var3) {
      this.block = var1;
      this.blockPos = var2;
      this.boundingBox = var3;
   }

   public void setBoundingBox(AxisAlignedBB var1) {
      this.boundingBox = var1;
   }

   public void setBlockPos(BlockPos var1) {
      this.blockPos = var1;
   }

   public Block getBlock() {
      return this.block;
   }

   public void setBlock(Block var1) {
      this.block = var1;
   }

   public BlockPos getBlockPos() {
      return this.blockPos;
   }

   public AxisAlignedBB getBoundingBox() {
      return this.boundingBox;
   }
}
