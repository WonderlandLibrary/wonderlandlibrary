package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockBarrier extends Block {
   public float getAmbientOcclusionLightValue() {
      return 1.0F;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public int getRenderType() {
      return -1;
   }

   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
   }

   protected BlockBarrier() {
      super(Material.barrier);
      this.setBlockUnbreakable();
      this.setResistance(6000001.0F);
      this.disableStats();
      this.translucent = true;
   }
}
