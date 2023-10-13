/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemPiston
extends ItemBlock {
    private static final String __OBFID = "CL_00000054";

    public ItemPiston(Block p_i45348_1_) {
        super(p_i45348_1_);
    }

    @Override
    public int getMetadata(int damage) {
        return 7;
    }
}

