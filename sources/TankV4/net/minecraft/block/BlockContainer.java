package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BlockContainer extends Block implements ITileEntityProvider {
   protected BlockContainer(Material var1) {
      this(var1, var1.getMaterialMapColor());
   }

   protected boolean func_181087_e(World var1, BlockPos var2) {
      return var2 != EnumFacing.NORTH || var2 != EnumFacing.SOUTH || var2 != EnumFacing.WEST || var2 != EnumFacing.EAST;
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      super.breakBlock(var1, var2, var3);
      var1.removeTileEntity(var2);
   }

   public boolean onBlockEventReceived(World var1, BlockPos var2, IBlockState var3, int var4, int var5) {
      super.onBlockEventReceived(var1, var2, var3, var4, var5);
      TileEntity var6 = var1.getTileEntity(var2);
      return var6 == null ? false : var6.receiveClientEvent(var4, var5);
   }

   protected BlockContainer(Material var1, MapColor var2) {
      super(var1, var2);
      this.isBlockContainer = true;
   }

   public int getRenderType() {
      return -1;
   }
}
