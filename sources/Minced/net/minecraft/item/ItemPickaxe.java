// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import java.util.Set;

public class ItemPickaxe extends ItemTool
{
    private static final Set<Block> EFFECTIVE_ON;
    
    protected ItemPickaxe(final ToolMaterial material) {
        super(1.0f, -2.8f, material, ItemPickaxe.EFFECTIVE_ON);
    }
    
    @Override
    public boolean canHarvestBlock(final IBlockState blockIn) {
        final Block block = blockIn.getBlock();
        if (block == Blocks.OBSIDIAN) {
            return this.toolMaterial.getHarvestLevel() == 3;
        }
        if (block == Blocks.DIAMOND_BLOCK || block == Blocks.DIAMOND_ORE) {
            return this.toolMaterial.getHarvestLevel() >= 2;
        }
        if (block == Blocks.EMERALD_ORE || block == Blocks.EMERALD_BLOCK) {
            return this.toolMaterial.getHarvestLevel() >= 2;
        }
        if (block == Blocks.GOLD_BLOCK || block == Blocks.GOLD_ORE) {
            return this.toolMaterial.getHarvestLevel() >= 2;
        }
        if (block == Blocks.IRON_BLOCK || block == Blocks.IRON_ORE) {
            return this.toolMaterial.getHarvestLevel() >= 1;
        }
        if (block == Blocks.LAPIS_BLOCK || block == Blocks.LAPIS_ORE) {
            return this.toolMaterial.getHarvestLevel() >= 1;
        }
        if (block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE) {
            final Material material = blockIn.getMaterial();
            return material == Material.ROCK || material == Material.IRON || material == Material.ANVIL;
        }
        return this.toolMaterial.getHarvestLevel() >= 2;
    }
    
    @Override
    public float getDestroySpeed(final ItemStack stack, final IBlockState state) {
        final Material material = state.getMaterial();
        return (material != Material.IRON && material != Material.ANVIL && material != Material.ROCK) ? super.getDestroySpeed(stack, state) : this.efficiency;
    }
    
    static {
        EFFECTIVE_ON = Sets.newHashSet((Object[])new Block[] { Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE });
    }
}
