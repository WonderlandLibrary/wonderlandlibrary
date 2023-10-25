/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemNameTag
extends Item {
    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase) {
        if (!itemStack.hasDisplayName()) {
            return false;
        }
        if (entityLivingBase instanceof EntityLiving) {
            EntityLiving entityLiving = (EntityLiving)entityLivingBase;
            entityLiving.setCustomNameTag(itemStack.getDisplayName());
            entityLiving.enablePersistence();
            --itemStack.stackSize;
            return true;
        }
        return super.itemInteractionForEntity(itemStack, entityPlayer, entityLivingBase);
    }

    public ItemNameTag() {
        this.setCreativeTab(CreativeTabs.tabTools);
    }
}

