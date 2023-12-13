package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemCarrotOnAStick extends Item {
   public ItemCarrotOnAStick() {
      this.setCreativeTab(CreativeTabs.tabTransport);
      this.setMaxStackSize(1);
      this.setMaxDamage(25);
   }

   public boolean isFull3D() {
      return true;
   }

   public boolean shouldRotateAroundWhenRendering() {
      return true;
   }

   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      if (var3.isRiding() && var3.ridingEntity instanceof EntityPig) {
         EntityPig var4 = (EntityPig)var3.ridingEntity;
         if (var4.getAIControlledByPlayer().isControlledByPlayer() && var1.getMaxDamage() - var1.getMetadata() >= 7) {
            var4.getAIControlledByPlayer().boostSpeed();
            var1.damageItem(7, var3);
            if (var1.stackSize == 0) {
               ItemStack var5 = new ItemStack(Items.fishing_rod);
               var5.setTagCompound(var1.getTagCompound());
               return var5;
            }
         }
      }

      var3.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
      return var1;
   }
}
