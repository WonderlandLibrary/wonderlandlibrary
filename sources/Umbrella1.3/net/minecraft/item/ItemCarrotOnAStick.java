/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemCarrotOnAStick
extends Item {
    private static final String __OBFID = "CL_00000001";

    public ItemCarrotOnAStick() {
        this.setCreativeTab(CreativeTabs.tabTransport);
        this.setMaxStackSize(1);
        this.setMaxDamage(25);
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        EntityPig var4;
        if (playerIn.isRiding() && playerIn.ridingEntity instanceof EntityPig && (var4 = (EntityPig)playerIn.ridingEntity).getAIControlledByPlayer().isControlledByPlayer() && itemStackIn.getMaxDamage() - itemStackIn.getMetadata() >= 7) {
            var4.getAIControlledByPlayer().boostSpeed();
            itemStackIn.damageItem(7, playerIn);
            if (itemStackIn.stackSize == 0) {
                ItemStack var5 = new ItemStack(Items.fishing_rod);
                var5.setTagCompound(itemStackIn.getTagCompound());
                return var5;
            }
        }
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
}

