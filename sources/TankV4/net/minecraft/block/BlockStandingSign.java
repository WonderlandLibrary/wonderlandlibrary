package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockStandingSign extends BlockSign {
   public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{ROTATION});
   }

   public int getMetaFromState(IBlockState var1) {
      return (Integer)var1.getValue(ROTATION);
   }

   public BlockStandingSign() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, 0));
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (!var1.getBlockState(var2.down()).getBlock().getMaterial().isSolid()) {
         this.dropBlockAsItem(var1, var2, var3, 0);
         var1.setBlockToAir(var2);
      }

      super.onNeighborBlockChange(var1, var2, var3, var4);
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(ROTATION, var1);
   }
}
