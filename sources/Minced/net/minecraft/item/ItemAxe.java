// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import java.util.Set;

public class ItemAxe extends ItemTool
{
    private static final Set<Block> EFFECTIVE_ON;
    private static final float[] ATTACK_DAMAGES;
    private static final float[] ATTACK_SPEEDS;
    
    protected ItemAxe(final ToolMaterial material) {
        super(material, ItemAxe.EFFECTIVE_ON);
        this.attackDamage = ItemAxe.ATTACK_DAMAGES[material.ordinal()];
        this.attackSpeed = ItemAxe.ATTACK_SPEEDS[material.ordinal()];
    }
    
    @Override
    public float getDestroySpeed(final ItemStack stack, final IBlockState state) {
        final Material material = state.getMaterial();
        return (material != Material.WOOD && material != Material.PLANTS && material != Material.VINE) ? super.getDestroySpeed(stack, state) : this.efficiency;
    }
    
    static {
        EFFECTIVE_ON = Sets.newHashSet((Object[])new Block[] { Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE });
        ATTACK_DAMAGES = new float[] { 6.0f, 8.0f, 8.0f, 8.0f, 6.0f };
        ATTACK_SPEEDS = new float[] { -3.2f, -3.2f, -3.1f, -3.0f, -3.0f };
    }
}
