package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockAir extends Block {
   public int getRenderType() {
      return -1;
   }

   public boolean canCollideCheck(IBlockState var1, boolean var2) {
      return false;
   }

   protected BlockAir() {
      super(Material.air);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isReplaceable(World var1, BlockPos var2) {
      return true;
   }

   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return null;
   }
}
