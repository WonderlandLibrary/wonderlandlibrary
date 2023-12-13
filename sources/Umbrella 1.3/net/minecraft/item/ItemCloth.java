/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemCloth
extends ItemBlock {
    private static final String __OBFID = "CL_00000075";

    public ItemCloth(Block p_i45358_1_) {
        super(p_i45358_1_);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return String.valueOf(super.getUnlocalizedName()) + "." + EnumDyeColor.func_176764_b(stack.getMetadata()).func_176762_d();
    }
}

