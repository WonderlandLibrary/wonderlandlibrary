// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IBlockAccess;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockObserver extends BlockDirectional
{
    public static final PropertyBool POWERED;
    
    public BlockObserver() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockObserver.FACING, EnumFacing.SOUTH).withProperty((IProperty<Comparable>)BlockObserver.POWERED, false));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockObserver.FACING, BlockObserver.POWERED });
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockObserver.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockObserver.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockObserver.FACING)));
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (state.getValue((IProperty<Boolean>)BlockObserver.POWERED)) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockObserver.POWERED, false), 2);
        }
        else {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockObserver.POWERED, true), 2);
            worldIn.scheduleUpdate(pos, this, 2);
        }
        this.updateNeighborsInFront(worldIn, pos, state);
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
    }
    
    public void observedNeighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.isRemote && pos.offset(state.getValue((IProperty<EnumFacing>)BlockObserver.FACING)).equals(fromPos)) {
            this.startSignal(state, worldIn, pos);
        }
    }
    
    private void startSignal(final IBlockState p_190960_1_, final World p_190960_2_, final BlockPos pos) {
        if (!p_190960_1_.getValue((IProperty<Boolean>)BlockObserver.POWERED) && !p_190960_2_.isUpdateScheduled(pos, this)) {
            p_190960_2_.scheduleUpdate(pos, this, 2);
        }
    }
    
    protected void updateNeighborsInFront(final World worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockObserver.FACING);
        final BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        worldIn.neighborChanged(blockpos, this, pos);
        worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
    }
    
    @Override
    @Deprecated
    public boolean canProvidePower(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public int getStrongPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return blockState.getWeakPower(blockAccess, pos, side);
    }
    
    @Override
    @Deprecated
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return (blockState.getValue((IProperty<Boolean>)BlockObserver.POWERED) && blockState.getValue((IProperty<Comparable>)BlockObserver.FACING) == side) ? 15 : 0;
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            if (state.getValue((IProperty<Boolean>)BlockObserver.POWERED)) {
                this.updateTick(worldIn, pos, state, worldIn.rand);
            }
            this.startSignal(state, worldIn, pos);
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue((IProperty<Boolean>)BlockObserver.POWERED) && worldIn.isUpdateScheduled(pos, this)) {
            this.updateNeighborsInFront(worldIn, pos, state.withProperty((IProperty<Comparable>)BlockObserver.POWERED, false));
        }
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockObserver.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite());
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockObserver.FACING).getIndex();
        if (state.getValue((IProperty<Boolean>)BlockObserver.POWERED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockObserver.FACING, EnumFacing.byIndex(meta & 0x7));
    }
    
    static {
        POWERED = PropertyBool.create("powered");
    }
}
