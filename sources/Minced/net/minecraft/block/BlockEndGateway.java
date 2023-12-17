// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BlockEndGateway extends BlockContainer
{
    protected BlockEndGateway(final Material p_i46687_1_) {
        super(p_i46687_1_);
        this.setLightLevel(1.0f);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityEndGateway();
    }
    
    @Override
    @Deprecated
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        final IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        final Block block = iblockstate.getBlock();
        return !iblockstate.isOpaqueCube() && block != Blocks.END_GATEWAY;
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockEndGateway.NULL_AABB;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityEndGateway) {
            for (int i = ((TileEntityEndGateway)tileentity).getParticleAmount(), j = 0; j < i; ++j) {
                double d0 = pos.getX() + rand.nextFloat();
                final double d2 = pos.getY() + rand.nextFloat();
                double d3 = pos.getZ() + rand.nextFloat();
                double d4 = (rand.nextFloat() - 0.5) * 0.5;
                final double d5 = (rand.nextFloat() - 0.5) * 0.5;
                double d6 = (rand.nextFloat() - 0.5) * 0.5;
                final int k = rand.nextInt(2) * 2 - 1;
                if (rand.nextBoolean()) {
                    d3 = pos.getZ() + 0.5 + 0.25 * k;
                    d6 = rand.nextFloat() * 2.0f * k;
                }
                else {
                    d0 = pos.getX() + 0.5 + 0.25 * k;
                    d4 = rand.nextFloat() * 2.0f * k;
                }
                worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d2, d3, d4, d5, d6, new int[0]);
            }
        }
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return ItemStack.EMPTY;
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return MapColor.BLACK;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
