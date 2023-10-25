/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSaddle
extends Item {
    public ItemSaddle() {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase) {
        if (entityLivingBase instanceof EntityPig) {
            EntityPig entityPig = (EntityPig)entityLivingBase;
            if (!entityPig.getSaddled() && !entityPig.isChild()) {
                entityPig.setSaddled(true);
                entityPig.worldObj.playSoundAtEntity(entityPig, "mob.horse.leather", 0.5f, 1.0f);
                --itemStack.stackSize;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityLivingBase, EntityLivingBase entityLivingBase2) {
        this.itemInteractionForEntity(itemStack, null, entityLivingBase);
        return true;
    }
}

