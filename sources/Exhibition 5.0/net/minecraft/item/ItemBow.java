// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemBow extends Item
{
    public static final String[] bowPullIconNameArray;
    private static final String __OBFID = "CL_00001777";
    
    public ItemBow() {
        this.maxStackSize = 1;
        this.setMaxDamage(384);
        this.setCreativeTab(CreativeTabs.tabCombat);
    }
    
    @Override
    public void onPlayerStoppedUsing(final ItemStack stack, final World worldIn, final EntityPlayer playerIn, final int timeLeft) {
        final boolean var5 = playerIn.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;
        if (var5 || playerIn.inventory.hasItem(Items.arrow)) {
            final int var6 = this.getMaxItemUseDuration(stack) - timeLeft;
            float var7 = var6 / 20.0f;
            var7 = (var7 * var7 + var7 * 2.0f) / 3.0f;
            if (var7 < 0.1) {
                return;
            }
            if (var7 > 1.0f) {
                var7 = 1.0f;
            }
            final EntityArrow var8 = new EntityArrow(worldIn, playerIn, var7 * 2.0f);
            if (var7 == 1.0f) {
                var8.setIsCritical(true);
            }
            final int var9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            if (var9 > 0) {
                var8.setDamage(var8.getDamage() + var9 * 0.5 + 0.5);
            }
            final int var10 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
            if (var10 > 0) {
                var8.setKnockbackStrength(var10);
            }
            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0) {
                var8.setFire(100);
            }
            stack.damageItem(1, playerIn);
            worldIn.playSoundAtEntity(playerIn, "random.bow", 1.0f, 1.0f / (Item.itemRand.nextFloat() * 0.4f + 1.2f) + var7 * 0.5f);
            if (var5) {
                var8.canBePickedUp = 2;
            }
            else {
                playerIn.inventory.consumeInventoryItem(Items.arrow);
            }
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld(var8);
            }
        }
    }
    
    @Override
    public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityPlayer playerIn) {
        return stack;
    }
    
    @Override
    public int getMaxItemUseDuration(final ItemStack stack) {
        return 72000;
    }
    
    @Override
    public EnumAction getItemUseAction(final ItemStack stack) {
        return EnumAction.BOW;
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        if (playerIn.capabilities.isCreativeMode || playerIn.inventory.hasItem(Items.arrow)) {
            playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        }
        return itemStackIn;
    }
    
    @Override
    public int getItemEnchantability() {
        return 1;
    }
    
    static {
        bowPullIconNameArray = new String[] { "pulling_0", "pulling_1", "pulling_2" };
    }
}
