/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMultiTexture
extends ItemBlock {
    protected final Block theBlock;
    protected final Mapper nameFunction;

    public ItemMultiTexture(Block p_i47262_1_, Block p_i47262_2_, Mapper p_i47262_3_) {
        super(p_i47262_1_);
        this.theBlock = p_i47262_2_;
        this.nameFunction = p_i47262_3_;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public ItemMultiTexture(Block block, Block block2, final String[] namesByMeta) {
        this(block, block2, new Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                int i = p_apply_1_.getMetadata();
                if (i < 0 || i >= namesByMeta.length) {
                    i = 0;
                }
                return namesByMeta[i];
            }
        });
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + this.nameFunction.apply(stack);
    }

    public static interface Mapper {
        public String apply(ItemStack var1);
    }
}

