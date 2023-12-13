package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSign extends BlockContainer {
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.sign;
   }

   public AxisAlignedBB getSelectedBoundingBox(World var1, BlockPos var2) {
      this.setBlockBoundsBasedOnState(var1, var2);
      return super.getSelectedBoundingBox(var1, var2);
   }

   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return true;
   }

   public Item getItem(World var1, BlockPos var2) {
      return Items.sign;
   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return !this.func_181087_e(var1, var2) && super.canPlaceBlockAt(var1, var2);
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntitySign();
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean func_181623_g() {
      return true;
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return null;
   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var1.isRemote) {
         return true;
      } else {
         TileEntity var9 = var1.getTileEntity(var2);
         return var9 instanceof TileEntitySign ? ((TileEntitySign)var9).executeCommand(var4) : false;
      }
   }

   protected BlockSign() {
      super(Material.wood);
      float var1 = 0.25F;
      float var2 = 1.0F;
      this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var2, 0.5F + var1);
   }
}
