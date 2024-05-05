/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class ItemAxe
extends ItemTool {
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder);

    protected ItemAxe(Item.ToolMaterial material) {
        super(3.0f, material, EFFECTIVE_ON);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {
        float f;
        if (block.getMaterial() != Material.wood && block.getMaterial() != Material.plants && block.getMaterial() != Material.vine) {
            f = super.getStrVsBlock(stack, block);
            return f;
        }
        f = this.efficiencyOnProperMaterial;
        return f;
    }
}

