/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
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
    private static final Set field_150917_c = Sets.newHashSet((Object[])new Block[]{Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder});
    private static final String __OBFID = "CL_00001770";

    protected ItemAxe(Item.ToolMaterial p_i45327_1_) {
        super(3.0f, p_i45327_1_, field_150917_c);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block p_150893_2_) {
        return p_150893_2_.getMaterial() != Material.wood && p_150893_2_.getMaterial() != Material.plants && p_150893_2_.getMaterial() != Material.vine ? super.getStrVsBlock(stack, p_150893_2_) : this.efficiencyOnProperMaterial;
    }
}

