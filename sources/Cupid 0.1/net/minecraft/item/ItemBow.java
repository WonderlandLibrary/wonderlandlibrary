package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemBow extends Item {
  public static final String[] bowPullIconNameArray = new String[] { "pulling_0", "pulling_1", "pulling_2" };
  
  public ItemBow() {
    this.maxStackSize = 1;
    setMaxDamage(384);
    setCreativeTab(CreativeTabs.tabCombat);
  }
  
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
    boolean flag = (playerIn.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0);
    if (flag || playerIn.inventory.hasItem(Items.arrow)) {
      int i = getMaxItemUseDuration(stack) - timeLeft;
      float f = i / 20.0F;
      f = (f * f + f * 2.0F) / 3.0F;
      if (f < 0.1D)
        return; 
      if (f > 1.0F)
        f = 1.0F; 
      EntityArrow entityarrow = new EntityArrow(worldIn, (EntityLivingBase)playerIn, f * 2.0F);
      if (f == 1.0F)
        entityarrow.setIsCritical(true); 
      int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
      if (j > 0)
        entityarrow.setDamage(entityarrow.getDamage() + j * 0.5D + 0.5D); 
      int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
      if (k > 0)
        entityarrow.setKnockbackStrength(k); 
      if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
        entityarrow.setFire(100); 
      stack.damageItem(1, (EntityLivingBase)playerIn);
      worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
      if (flag) {
        entityarrow.canBePickedUp = 2;
      } else {
        playerIn.inventory.consumeInventoryItem(Items.arrow);
      } 
      playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
      if (!worldIn.isRemote)
        worldIn.spawnEntityInWorld((Entity)entityarrow); 
    } 
  }
  
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    return stack;
  }
  
  public int getMaxItemUseDuration(ItemStack stack) {
    return 72000;
  }
  
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BOW;
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    if (playerIn.capabilities.isCreativeMode || playerIn.inventory.hasItem(Items.arrow))
      playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn)); 
    return itemStackIn;
  }
  
  public int getItemEnchantability() {
    return 1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemBow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */