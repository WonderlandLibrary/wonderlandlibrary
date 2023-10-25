/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemCloth
extends ItemBlock {
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.valueOf(super.getUnlocalizedName()) + "." + EnumDyeColor.byMetadata(itemStack.getMetadata()).getUnlocalizedName();
    }

    @Override
    public int getMetadata(int n) {
        return n;
    }

    public ItemCloth(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
}

