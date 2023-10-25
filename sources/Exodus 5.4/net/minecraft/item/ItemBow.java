/*
 * Decompiled with CFR 0.152.
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

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 72000;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (entityPlayer.capabilities.isCreativeMode || entityPlayer.inventory.hasItem(Items.arrow)) {
            entityPlayer.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        }
        return itemStack;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.BOW;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        return itemStack;
    }

    public ItemBow() {
        this.maxStackSize = 1;
        this.setMaxDamage(384);
        this.setCreativeTab(CreativeTabs.tabCombat);
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer entityPlayer, int n) {
        boolean bl;
        boolean bl2 = bl = entityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemStack) > 0;
        if (bl || entityPlayer.inventory.hasItem(Items.arrow)) {
            int n2;
            int n3;
            int n4 = this.getMaxItemUseDuration(itemStack) - n;
            float f = (float)n4 / 20.0f;
            if ((double)(f = (f * f + f * 2.0f) / 3.0f) < 0.1) {
                return;
            }
            if (f > 1.0f) {
                f = 1.0f;
            }
            EntityArrow entityArrow = new EntityArrow(world, entityPlayer, f * 2.0f);
            if (f == 1.0f) {
                entityArrow.setIsCritical(true);
            }
            if ((n3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack)) > 0) {
                entityArrow.setDamage(entityArrow.getDamage() + (double)n3 * 0.5 + 0.5);
            }
            if ((n2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack)) > 0) {
                entityArrow.setKnockbackStrength(n2);
            }
            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack) > 0) {
                entityArrow.setFire(100);
            }
            itemStack.damageItem(1, entityPlayer);
            world.playSoundAtEntity(entityPlayer, "random.bow", 1.0f, 1.0f / (itemRand.nextFloat() * 0.4f + 1.2f) + f * 0.5f);
            if (bl) {
                entityArrow.canBePickedUp = 2;
            } else {
                entityPlayer.inventory.consumeInventoryItem(Items.arrow);
            }
            entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            if (!world.isRemote) {
                world.spawnEntityInWorld(entityArrow);
            }
        }
    }
}

