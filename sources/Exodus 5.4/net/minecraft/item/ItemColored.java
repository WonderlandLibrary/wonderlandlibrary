/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemColored
extends ItemBlock {
    private final Block coloredBlock;
    private String[] subtypeNames;

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        if (this.subtypeNames == null) {
            return super.getUnlocalizedName(itemStack);
        }
        int n = itemStack.getMetadata();
        return n >= 0 && n < this.subtypeNames.length ? String.valueOf(super.getUnlocalizedName(itemStack)) + "." + this.subtypeNames[n] : super.getUnlocalizedName(itemStack);
    }

    public ItemColored setSubtypeNames(String[] stringArray) {
        this.subtypeNames = stringArray;
        return this;
    }

    public ItemColored(Block block, boolean bl) {
        super(block);
        this.coloredBlock = block;
        if (bl) {
            this.setMaxDamage(0);
            this.setHasSubtypes(true);
        }
    }

    @Override
    public int getMetadata(int n) {
        return n;
    }

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int n) {
        return this.coloredBlock.getRenderColor(this.coloredBlock.getStateFromMeta(itemStack.getMetadata()));
    }
}

