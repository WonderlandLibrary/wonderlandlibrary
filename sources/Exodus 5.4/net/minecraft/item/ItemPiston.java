/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemPiston
extends ItemBlock {
    @Override
    public int getMetadata(int n) {
        return 7;
    }

    public ItemPiston(Block block) {
        super(block);
    }
}

