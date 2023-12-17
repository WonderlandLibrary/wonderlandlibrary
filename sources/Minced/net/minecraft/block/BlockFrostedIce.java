// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.item.ItemStack;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;

public class BlockFrostedIce extends BlockIce
{
    public static final PropertyInteger AGE;
    
    public BlockFrostedIce() {
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockFrostedIce.AGE, 0));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockFrostedIce.AGE);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFrostedIce.AGE, MathHelper.clamp(meta, 0, 3));
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if ((rand.nextInt(3) == 0 || this.countNeighbors(worldIn, pos) < 4) && worldIn.getLightFromNeighbors(pos) > 11 - state.getValue((IProperty<Integer>)BlockFrostedIce.AGE) - state.getLightOpacity()) {
            this.slightlyMelt(worldIn, pos, state, rand, true);
        }
        else {
            worldIn.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
        }
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (blockIn == this) {
            final int i = this.countNeighbors(worldIn, pos);
            if (i < 2) {
                this.turnIntoWater(worldIn, pos);
            }
        }
    }
    
    private int countNeighbors(final World worldIn, final BlockPos pos) {
        int i = 0;
        for (final EnumFacing enumfacing : EnumFacing.values()) {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this && ++i >= 4) {
                return i;
            }
        }
        return i;
    }
    
    protected void slightlyMelt(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand, final boolean meltNeighbors) {
        final int i = state.getValue((IProperty<Integer>)BlockFrostedIce.AGE);
        if (i < 3) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFrostedIce.AGE, i + 1), 2);
            worldIn.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
        }
        else {
            this.turnIntoWater(worldIn, pos);
            if (meltNeighbors) {
                for (final EnumFacing enumfacing : EnumFacing.values()) {
                    final BlockPos blockpos = pos.offset(enumfacing);
                    final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    if (iblockstate.getBlock() == this) {
                        this.slightlyMelt(worldIn, blockpos, iblockstate, rand, false);
                    }
                }
            }
        }
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockFrostedIce.AGE });
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return ItemStack.EMPTY;
    }
    
    static {
        AGE = PropertyInteger.create("age", 0, 3);
    }
}
