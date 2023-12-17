// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCactus extends Block
{
    public static final PropertyInteger AGE;
    protected static final AxisAlignedBB CACTUS_COLLISION_AABB;
    protected static final AxisAlignedBB CACTUS_AABB;
    
    protected BlockCactus() {
        super(Material.CACTUS);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockCactus.AGE, 0));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final BlockPos blockpos = pos.up();
        if (worldIn.isAirBlock(blockpos)) {
            int i;
            for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; ++i) {}
            if (i < 3) {
                final int j = state.getValue((IProperty<Integer>)BlockCactus.AGE);
                if (j == 15) {
                    worldIn.setBlockState(blockpos, this.getDefaultState());
                    final IBlockState iblockstate = state.withProperty((IProperty<Comparable>)BlockCactus.AGE, 0);
                    worldIn.setBlockState(pos, iblockstate, 4);
                    iblockstate.neighborChanged(worldIn, blockpos, this, pos);
                }
                else {
                    worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCactus.AGE, j + 1), 4);
                }
            }
        }
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockCactus.CACTUS_COLLISION_AABB;
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getSelectedBoundingBox(final IBlockState state, final World worldIn, final BlockPos pos) {
        return BlockCactus.CACTUS_AABB.offset(pos);
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!this.canBlockStay(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }
    
    public boolean canBlockStay(final World worldIn, final BlockPos pos) {
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final Material material = worldIn.getBlockState(pos.offset(enumfacing)).getMaterial();
            if (material.isSolid() || material == Material.LAVA) {
                return false;
            }
        }
        final Block block = worldIn.getBlockState(pos.down()).getBlock();
        return block == Blocks.CACTUS || (block == Blocks.SAND && !worldIn.getBlockState(pos.up()).getMaterial().isLiquid());
    }
    
    @Override
    public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        entityIn.attackEntityFrom(DamageSource.CACTUS, 1.0f);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCactus.AGE, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockCactus.AGE);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockCactus.AGE });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        AGE = PropertyInteger.create("age", 0, 15);
        CACTUS_COLLISION_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.9375, 0.9375);
        CACTUS_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375);
    }
}
