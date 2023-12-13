package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemBow extends Item {
   public static final String[] bowPullIconNameArray = new String[]{"pulling_0", "pulling_1", "pulling_2"};

   public ItemBow() {
      this.maxStackSize = 1;
      this.setMaxDamage(384);
      this.setCreativeTab(CreativeTabs.tabCombat);
   }

   public int getMaxItemUseDuration(ItemStack var1) {
      return 72000;
   }

   public void onPlayerStoppedUsing(ItemStack var1, World var2, EntityPlayer var3, int var4) {
      boolean var5 = var3.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, var1) > 0;
      if (var5 || var3.inventory.hasItem(Items.arrow)) {
         int var6 = this.getMaxItemUseDuration(var1) - var4;
         float var7 = (float)var6 / 20.0F;
         var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;
         if ((double)var7 < 0.1D) {
            return;
         }

         if (var7 > 1.0F) {
            var7 = 1.0F;
         }

         EntityArrow var8 = new EntityArrow(var2, var3, var7 * 2.0F);
         if (var7 == 1.0F) {
            var8.setIsCritical(true);
         }

         int var9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, var1);
         if (var9 > 0) {
            var8.setDamage(var8.getDamage() + (double)var9 * 0.5D + 0.5D);
         }

         int var10 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, var1);
         if (var10 > 0) {
            var8.setKnockbackStrength(var10);
         }

         if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, var1) > 0) {
            var8.setFire(100);
         }

         var1.damageItem(1, var3);
         var2.playSoundAtEntity(var3, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + var7 * 0.5F);
         if (var5) {
            var8.canBePickedUp = 2;
         } else {
            var3.inventory.consumeInventoryItem(Items.arrow);
         }

         var3.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
         if (!var2.isRemote) {
            var2.spawnEntityInWorld(var8);
         }
      }

   }

   public int getItemEnchantability() {
      return 1;
   }

   public ItemStack onItemUseFinish(ItemStack var1, World var2, EntityPlayer var3) {
      return var1;
   }

   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      if (var3.capabilities.isCreativeMode || var3.inventory.hasItem(Items.arrow)) {
         var3.setItemInUse(var1, this.getMaxItemUseDuration(var1));
      }

      return var1;
   }

   public EnumAction getItemUseAction(ItemStack var1) {
      return EnumAction.BOW;
   }
}
