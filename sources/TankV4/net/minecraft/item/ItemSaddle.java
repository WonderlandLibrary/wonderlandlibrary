package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;

public class ItemSaddle extends Item {
   public boolean itemInteractionForEntity(ItemStack var1, EntityPlayer var2, EntityLivingBase var3) {
      if (var3 instanceof EntityPig) {
         EntityPig var4 = (EntityPig)var3;
         if (!var4.getSaddled() && !var4.isChild()) {
            var4.setSaddled(true);
            var4.worldObj.playSoundAtEntity(var4, "mob.horse.leather", 0.5F, 1.0F);
            --var1.stackSize;
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean hitEntity(ItemStack var1, EntityLivingBase var2, EntityLivingBase var3) {
      this.itemInteractionForEntity(var1, (EntityPlayer)null, var2);
      return true;
   }

   public ItemSaddle() {
      this.maxStackSize = 1;
      this.setCreativeTab(CreativeTabs.tabTransport);
   }
}
