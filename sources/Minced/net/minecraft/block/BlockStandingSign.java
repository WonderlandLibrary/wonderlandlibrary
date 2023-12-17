// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;

public class BlockStandingSign extends BlockSign
{
    public static final PropertyInteger ROTATION;
    
    public BlockStandingSign() {
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockStandingSign.ROTATION, 0));
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.getBlockState(pos.down()).getMaterial().isSolid()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockStandingSign.ROTATION, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockStandingSign.ROTATION);
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockStandingSign.ROTATION, rot.rotate(state.getValue((IProperty<Integer>)BlockStandingSign.ROTATION), 16));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withProperty((IProperty<Comparable>)BlockStandingSign.ROTATION, mirrorIn.mirrorRotation(state.getValue((IProperty<Integer>)BlockStandingSign.ROTATION), 16));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockStandingSign.ROTATION });
    }
    
    static {
        ROTATION = PropertyInteger.create("rotation", 0, 15);
    }
}
