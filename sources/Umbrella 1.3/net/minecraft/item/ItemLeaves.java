/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.block.BlockLeaves;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemLeaves
extends ItemBlock {
    private final BlockLeaves field_150940_b;
    private static final String __OBFID = "CL_00000046";

    public ItemLeaves(BlockLeaves p_i45344_1_) {
        super(p_i45344_1_);
        this.field_150940_b = p_i45344_1_;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage | 4;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return this.field_150940_b.getRenderColor(this.field_150940_b.getStateFromMeta(stack.getMetadata()));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return String.valueOf(super.getUnlocalizedName()) + "." + this.field_150940_b.func_176233_b(stack.getMetadata()).func_176840_c();
    }
}

