/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000052";

    public ItemNameTag() {
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
        if (!stack.hasDisplayName()) {
            return false;
        }
        if (target instanceof EntityLiving) {
            EntityLiving var4 = (EntityLiving)target;
            var4.setCustomNameTag(stack.getDisplayName());
            var4.enablePersistence();
            --stack.stackSize;
            return true;
        }
        return super.itemInteractionForEntity(stack, playerIn, target);
    }
}

