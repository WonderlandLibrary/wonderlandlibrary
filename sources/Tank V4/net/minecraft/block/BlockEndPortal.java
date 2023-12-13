package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEndPortal extends BlockContainer {
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityEndPortal();
   }

   protected BlockEndPortal(Material var1) {
      super(var1);
      this.setLightLevel(1.0F);
   }

   public int quantityDropped(Random var1) {
      return 0;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      float var3 = 0.0625F;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var3, 1.0F);
   }

   public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      return var3 == EnumFacing.DOWN ? super.shouldSideBeRendered(var1, var2, var3) : false;
   }

   public MapColor getMapColor(IBlockState var1) {
      return MapColor.blackColor;
   }

   public void addCollisionBoxesToList(World var1, BlockPos var2, IBlockState var3, AxisAlignedBB var4, List var5, Entity var6) {
   }

   public Item getItem(World var1, BlockPos var2) {
      return null;
   }

   public void onEntityCollidedWithBlock(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      if (var4.ridingEntity == null && var4.riddenByEntity == null && !var1.isRemote) {
         var4.travelToDimension(1);
      }

   }

   public boolean isFullCube() {
      return false;
   }

   public void randomDisplayTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      double var5 = (double)((float)var2.getX() + var4.nextFloat());
      double var7 = (double)((float)var2.getY() + 0.8F);
      double var9 = (double)((float)var2.getZ() + var4.nextFloat());
      double var11 = 0.0D;
      double var13 = 0.0D;
      double var15 = 0.0D;
      var1.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var5, var7, var9, var11, var13, var15);
   }
}
