/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemBow
extends Item {
    public static final String[] bowPullIconNameArray = new String[]{"pulling_0", "pulling_1", "pulling_2"};
    private static final String __OBFID = "CL_00001777";

    public ItemBow() {
        this.maxStackSize = 1;
        this.setMaxDamage(384);
        this.setCreativeTab(CreativeTabs.tabCombat);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
        boolean var5;
        boolean bl = var5 = playerIn.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;
        if (var5 || playerIn.inventory.hasItem(Items.arrow)) {
            int var10;
            int var9;
            int var6 = this.getMaxItemUseDuration(stack) - timeLeft;
            float var7 = (float)var6 / 20.0f;
            if ((double)(var7 = (var7 * var7 + var7 * 2.0f) / 3.0f) < 0.1) {
                return;
            }
            if (var7 > 1.0f) {
                var7 = 1.0f;
            }
            EntityArrow var8 = new EntityArrow(worldIn, playerIn, var7 * 2.0f);
            if (var7 == 1.0f) {
                var8.setIsCritical(true);
            }
            if ((var9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack)) > 0) {
                var8.setDamage(var8.getDamage() + (double)var9 * 0.5 + 0.5);
            }
            if ((var10 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack)) > 0) {
                var8.setKnockbackStrength(var10);
            }
            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0) {
                var8.setFire(100);
            }
            stack.damageItem(1, playerIn);
            worldIn.playSoundAtEntity(playerIn, "random.bow", 1.0f, 1.0f / (itemRand.nextFloat() * 0.4f + 1.2f) + var7 * 0.5f);
            if (var5) {
                var8.canBePickedUp = 2;
            } else {
                playerIn.inventory.consumeInventoryItem(Items.arrow);
            }
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld(var8);
            }
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (playerIn.capabilities.isCreativeMode || playerIn.inventory.hasItem(Items.arrow)) {
            playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        }
        return itemStackIn;
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }
}

