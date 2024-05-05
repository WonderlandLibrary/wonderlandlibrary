package net.minecraft.item;

import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class ItemBow
  extends Item
{
  public static final String[] bowPullIconNameArray = { "pulling_0", "pulling_1", "pulling_2" };
  private static final String __OBFID = "CL_00001777";
  
  public ItemBow()
  {
    this.maxStackSize = 1;
    setMaxDamage(384);
    setCreativeTab(CreativeTabs.tabCombat);
  }
  
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft)
  {
    boolean var5 = (playerIn.capabilities.isCreativeMode) || (EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0);
    if ((var5) || (playerIn.inventory.hasItem(Items.arrow)))
    {
      int var6 = getMaxItemUseDuration(stack) - timeLeft;
      float var7 = var6 / 20.0F;
      var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;
      if (var7 < 0.1D) {
        return;
      }
      if (var7 > 1.0F) {
        var7 = 1.0F;
      }
      EntityArrow var8 = new EntityArrow(worldIn, playerIn, var7 * 2.0F);
      if (var7 == 1.0F) {
        var8.setIsCritical(true);
      }
      int var9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
      if (var9 > 0) {
        var8.setDamage(var8.getDamage() + var9 * 0.5D + 0.5D);
      }
      int var10 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
      if (var10 > 0) {
        var8.setKnockbackStrength(var10);
      }
      if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0) {
        var8.setFire(100);
      }
      stack.damageItem(1, playerIn);
      worldIn.playSoundAtEntity(playerIn, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + var7 * 0.5F);
      if (var5) {
        var8.canBePickedUp = 2;
      } else {
        playerIn.inventory.consumeInventoryItem(Items.arrow);
      }
      playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
      if (!worldIn.isRemote) {
        worldIn.spawnEntityInWorld(var8);
      }
    }
  }
  
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
  {
    return stack;
  }
  
  public int getMaxItemUseDuration(ItemStack stack)
  {
    return 72000;
  }
  
  public EnumAction getItemUseAction(ItemStack stack)
  {
    return EnumAction.BOW;
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
  {
    if ((playerIn.capabilities.isCreativeMode) || (playerIn.inventory.hasItem(Items.arrow))) {
      playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
    }
    return itemStackIn;
  }
  
  public int getItemEnchantability()
  {
    return 1;
  }
}
