package net.minecraft.item;

import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemSign extends Item {
   public ItemSign() {
      this.maxStackSize = 16;
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var5 == EnumFacing.DOWN) {
         return false;
      } else if (!var3.getBlockState(var4).getBlock().getMaterial().isSolid()) {
         return false;
      } else {
         var4 = var4.offset(var5);
         if (!var2.canPlayerEdit(var4, var5, var1)) {
            return false;
         } else if (!Blocks.standing_sign.canPlaceBlockAt(var3, var4)) {
            return false;
         } else if (var3.isRemote) {
            return true;
         } else {
            if (var5 == EnumFacing.UP) {
               int var9 = MathHelper.floor_double((double)((var2.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
               var3.setBlockState(var4, Blocks.standing_sign.getDefaultState().withProperty(BlockStandingSign.ROTATION, var9), 3);
            } else {
               var3.setBlockState(var4, Blocks.wall_sign.getDefaultState().withProperty(BlockWallSign.FACING, var5), 3);
            }

            --var1.stackSize;
            TileEntity var10 = var3.getTileEntity(var4);
            if (var10 instanceof TileEntitySign && !ItemBlock.setTileEntityNBT(var3, var2, var4, var1)) {
               var2.openEditSign((TileEntitySign)var10);
            }

            return true;
         }
      }
   }
}
