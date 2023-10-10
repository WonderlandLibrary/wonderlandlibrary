/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ColorizerGrass;

public class ItemDoublePlant
extends ItemMultiTexture {
    public ItemDoublePlant(Block block, Block block2, Function<ItemStack, String> nameFunction) {
        super(block, block2, nameFunction);
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        int n;
        BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.byMetadata(stack.getMetadata());
        if (blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS && blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN) {
            n = super.getColorFromItemStack(stack, renderPass);
            return n;
        }
        n = ColorizerGrass.getGrassColor(0.5, 1.0);
        return n;
    }
}

