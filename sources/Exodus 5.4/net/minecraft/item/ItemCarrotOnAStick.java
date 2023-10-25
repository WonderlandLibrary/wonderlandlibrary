/*
 * Decompiled with CFR 0.152.
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
    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

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
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        EntityPig entityPig;
        if (entityPlayer.isRiding() && entityPlayer.ridingEntity instanceof EntityPig && (entityPig = (EntityPig)entityPlayer.ridingEntity).getAIControlledByPlayer().isControlledByPlayer() && itemStack.getMaxDamage() - itemStack.getMetadata() >= 7) {
            entityPig.getAIControlledByPlayer().boostSpeed();
            itemStack.damageItem(7, entityPlayer);
            if (itemStack.stackSize == 0) {
                ItemStack itemStack2 = new ItemStack(Items.fishing_rod);
                itemStack2.setTagCompound(itemStack.getTagCompound());
                return itemStack2;
            }
        }
        entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStack;
    }
}

