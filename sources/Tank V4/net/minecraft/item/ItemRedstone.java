package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemRedstone extends Item {
   public ItemRedstone() {
      this.setCreativeTab(CreativeTabs.tabRedstone);
   }

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      boolean var9 = var3.getBlockState(var4).getBlock().isReplaceable(var3, var4);
      BlockPos var10 = var9 ? var4 : var4.offset(var5);
      if (!var2.canPlayerEdit(var10, var5, var1)) {
         return false;
      } else {
         Block var11 = var3.getBlockState(var10).getBlock();
         if (!var3.canBlockBePlaced(var11, var10, false, var5, (Entity)null, var1)) {
            return false;
         } else if (Blocks.redstone_wire.canPlaceBlockAt(var3, var10)) {
            --var1.stackSize;
            var3.setBlockState(var10, Blocks.redstone_wire.getDefaultState());
            return true;
         } else {
            return false;
         }
      }
   }
}
