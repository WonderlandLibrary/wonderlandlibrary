// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate
{
    public static final PropertyInteger POWER;
    private final int maxWeight;
    
    protected BlockPressurePlateWeighted(final Material materialIn, final int p_i46379_2_) {
        this(materialIn, p_i46379_2_, materialIn.getMaterialMapColor());
    }
    
    protected BlockPressurePlateWeighted(final Material materialIn, final int p_i46380_2_, final MapColor color) {
        super(materialIn, color);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPressurePlateWeighted.POWER, 0));
        this.maxWeight = p_i46380_2_;
    }
    
    @Override
    protected int computeRedstoneStrength(final World worldIn, final BlockPos pos) {
        final int i = Math.min(worldIn.getEntitiesWithinAABB((Class<? extends Entity>)Entity.class, BlockPressurePlateWeighted.PRESSURE_AABB.offset(pos)).size(), this.maxWeight);
        if (i > 0) {
            final float f = Math.min(this.maxWeight, i) / (float)this.maxWeight;
            return MathHelper.ceil(f * 15.0f);
        }
        return 0;
    }
    
    @Override
    protected void playClickOnSound(final World worldIn, final BlockPos color) {
        worldIn.playSound(null, color, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3f, 0.90000004f);
    }
    
    @Override
    protected void playClickOffSound(final World worldIn, final BlockPos pos) {
        worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3f, 0.75f);
    }
    
    @Override
    protected int getRedstoneStrength(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockPressurePlateWeighted.POWER);
    }
    
    @Override
    protected IBlockState setRedstoneStrength(final IBlockState state, final int strength) {
        return state.withProperty((IProperty<Comparable>)BlockPressurePlateWeighted.POWER, strength);
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 10;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPressurePlateWeighted.POWER, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockPressurePlateWeighted.POWER);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockPressurePlateWeighted.POWER });
    }
    
    static {
        POWER = PropertyInteger.create("power", 0, 15);
    }
}
