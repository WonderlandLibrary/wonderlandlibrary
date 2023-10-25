/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemAppleGold
extends ItemFood {
    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return itemStack.getMetadata() == 0 ? EnumRarity.RARE : EnumRarity.EPIC;
    }

    @Override
    protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (!world.isRemote) {
            entityPlayer.addPotionEffect(new PotionEffect(Potion.absorption.id, 2400, 0));
        }
        if (itemStack.getMetadata() > 0) {
            if (!world.isRemote) {
                entityPlayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600, 4));
                entityPlayer.addPotionEffect(new PotionEffect(Potion.resistance.id, 6000, 0));
                entityPlayer.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6000, 0));
            }
        } else {
            super.onFoodEaten(itemStack, world, entityPlayer);
        }
    }

    @Override
    public boolean hasEffect(ItemStack itemStack) {
        return itemStack.getMetadata() > 0;
    }

    public ItemAppleGold(int n, float f, boolean bl) {
        super(n, f, bl);
        this.setHasSubtypes(true);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }
}

