/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
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
    @Override
    public int getColorFromItemStack(ItemStack itemStack, int n) {
        BlockDoublePlant.EnumPlantType enumPlantType = BlockDoublePlant.EnumPlantType.byMetadata(itemStack.getMetadata());
        return enumPlantType != BlockDoublePlant.EnumPlantType.GRASS && enumPlantType != BlockDoublePlant.EnumPlantType.FERN ? super.getColorFromItemStack(itemStack, n) : ColorizerGrass.getGrassColor(0.5, 1.0);
    }

    public ItemDoublePlant(Block block, Block block2, Function<ItemStack, String> function) {
        super(block, block2, function);
    }
}

