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

    public ItemColored(Block block, boolean hasSubtypes) {
        super(block);
        this.coloredBlock = block;
        if (!hasSubtypes) return;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return this.coloredBlock.getRenderColor(this.coloredBlock.getStateFromMeta(stack.getMetadata()));
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    public ItemColored setSubtypeNames(String[] names) {
        this.subtypeNames = names;
        return this;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String string;
        if (this.subtypeNames == null) {
            return super.getUnlocalizedName(stack);
        }
        int i = stack.getMetadata();
        if (i >= 0 && i < this.subtypeNames.length) {
            string = super.getUnlocalizedName(stack) + "." + this.subtypeNames[i];
            return string;
        }
        string = super.getUnlocalizedName(stack);
        return string;
    }
}

