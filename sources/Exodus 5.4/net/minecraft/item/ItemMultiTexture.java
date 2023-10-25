/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
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

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.valueOf(super.getUnlocalizedName()) + "." + (String)this.nameFunction.apply((Object)itemStack);
    }

    @Override
    public int getMetadata(int n) {
        return n;
    }

    public ItemMultiTexture(Block block, Block block2, final String[] stringArray) {
        this(block, block2, new Function<ItemStack, String>(){

            public String apply(ItemStack itemStack) {
                int n = itemStack.getMetadata();
                if (n < 0 || n >= stringArray.length) {
                    n = 0;
                }
                return stringArray[n];
            }
        });
    }

    public ItemMultiTexture(Block block, Block block2, Function<ItemStack, String> function) {
        super(block);
        this.theBlock = block2;
        this.nameFunction = function;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
}

