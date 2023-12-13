package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockSourceImpl implements IBlockSource {
   private final BlockPos pos;
   private final World worldObj;

   public double getZ() {
      return (double)this.pos.getZ() + 0.5D;
   }

   public World getWorld() {
      return this.worldObj;
   }

   public TileEntity getBlockTileEntity() {
      return this.worldObj.getTileEntity(this.pos);
   }

   public BlockSourceImpl(World var1, BlockPos var2) {
      this.worldObj = var1;
      this.pos = var2;
   }

   public double getY() {
      return (double)this.pos.getY() + 0.5D;
   }

   public int getBlockMetadata() {
      IBlockState var1 = this.worldObj.getBlockState(this.pos);
      return var1.getBlock().getMetaFromState(var1);
   }

   public BlockPos getBlockPos() {
      return this.pos;
   }

   public double getX() {
      return (double)this.pos.getX() + 0.5D;
   }
}
