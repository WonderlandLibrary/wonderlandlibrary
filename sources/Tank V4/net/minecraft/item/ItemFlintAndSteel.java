package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemFlintAndSteel extends Item {
   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      var4 = var4.offset(var5);
      if (!var2.canPlayerEdit(var4, var5, var1)) {
         return false;
      } else {
         if (var3.getBlockState(var4).getBlock().getMaterial() == Material.air) {
            var3.playSoundEffect((double)var4.getX() + 0.5D, (double)var4.getY() + 0.5D, (double)var4.getZ() + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
            var3.setBlockState(var4, Blocks.fire.getDefaultState());
         }

         var1.damageItem(1, var2);
         return true;
      }
   }

   public ItemFlintAndSteel() {
      this.maxStackSize = 1;
      this.setMaxDamage(64);
      this.setCreativeTab(CreativeTabs.tabTools);
   }
}
