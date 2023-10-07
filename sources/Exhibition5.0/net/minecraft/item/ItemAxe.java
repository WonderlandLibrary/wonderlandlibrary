// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import java.util.Set;

public class ItemAxe extends ItemTool
{
    private static final Set field_150917_c;
    private static final String __OBFID = "CL_00001770";
    
    protected ItemAxe(final ToolMaterial p_i45327_1_) {
        super(3.0f, p_i45327_1_, ItemAxe.field_150917_c);
    }
    
    @Override
    public float getStrVsBlock(final ItemStack stack, final Block p_150893_2_) {
        return (p_150893_2_.getMaterial() != Material.wood && p_150893_2_.getMaterial() != Material.plants && p_150893_2_.getMaterial() != Material.vine) ? super.getStrVsBlock(stack, p_150893_2_) : this.efficiencyOnProperMaterial;
    }
    
    static {
        field_150917_c = Sets.newHashSet((Object[])new Block[] { Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder });
    }
}
