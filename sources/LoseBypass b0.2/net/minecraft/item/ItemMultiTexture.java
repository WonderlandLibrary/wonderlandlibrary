/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMultiTexture
extends ItemBlock {
    protected final Block theBlock;
    protected final Function<ItemStack, String> nameFunction;

    public ItemMultiTexture(Block block, Block block2, Function<ItemStack, String> nameFunction) {
        super(block);
        this.theBlock = block2;
        this.nameFunction = nameFunction;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public ItemMultiTexture(Block block, Block block2, final String[] namesByMeta) {
        this(block, block2, new Function<ItemStack, String>(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                int i = p_apply_1_.getMetadata();
                if (i >= 0) {
                    if (i < namesByMeta.length) return namesByMeta[i];
                }
                i = 0;
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
}

