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
import net.minecraft.util.EnumHand;

public class ItemNameTag
extends Item {
    public ItemNameTag() {
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (stack.hasDisplayName() && !(target instanceof EntityPlayer)) {
            target.setCustomNameTag(stack.getDisplayName());
            if (target instanceof EntityLiving) {
                ((EntityLiving)target).enablePersistence();
            }
            stack.func_190918_g(1);
            return true;
        }
        return false;
    }
}

