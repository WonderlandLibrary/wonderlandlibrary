package net.minecraft.item;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemLead extends Item {
   public static boolean attachToFence(EntityPlayer var0, World var1, BlockPos var2) {
      EntityLeashKnot var3 = EntityLeashKnot.getKnotForPosition(var1, var2);
      boolean var4 = false;
      double var5 = 7.0D;
      int var7 = var2.getX();
      int var8 = var2.getY();
      int var9 = var2.getZ();
      Iterator var11 = var1.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double)var7 - var5, (double)var8 - var5, (double)var9 - var5, (double)var7 + var5, (double)var8 + var5, (double)var9 + var5)).iterator();

      while(var11.hasNext()) {
         EntityLiving var10 = (EntityLiving)var11.next();
         if (var10.getLeashed() && var10.getLeashedToEntity() == var0) {
            if (var3 == null) {
               var3 = EntityLeashKnot.createKnot(var1, var2);
            }

            var10.setLeashedToEntity(var3, true);
            var4 = true;
         }
      }

      return var4;
   }

   public ItemLead() {
      this.setCreativeTab(CreativeTabs.tabTools);
   }

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      Block var9 = var3.getBlockState(var4).getBlock();
      if (var9 instanceof BlockFence) {
         if (var3.isRemote) {
            return true;
         } else {
            attachToFence(var2, var3, var4);
            return true;
         }
      } else {
         return false;
      }
   }
}
