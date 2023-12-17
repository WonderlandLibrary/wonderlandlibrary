// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.MathHelper;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockSeaLantern extends Block
{
    public BlockSeaLantern(final Material materialIn) {
        super(materialIn);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 2 + random.nextInt(2);
    }
    
    @Override
    public int quantityDroppedWithBonus(final int fortune, final Random random) {
        return MathHelper.clamp(this.quantityDropped(random) + random.nextInt(fortune + 1), 1, 5);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.PRISMARINE_CRYSTALS;
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return MapColor.QUARTZ;
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
}
