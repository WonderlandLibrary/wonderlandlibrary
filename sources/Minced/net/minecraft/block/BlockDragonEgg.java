// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;

public class BlockDragonEgg extends Block
{
    protected static final AxisAlignedBB DRAGON_EGG_AABB;
    
    public BlockDragonEgg() {
        super(Material.DRAGON_EGG, MapColor.BLACK);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockDragonEgg.DRAGON_EGG_AABB;
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        this.checkFall(worldIn, pos);
    }
    
    private void checkFall(final World worldIn, final BlockPos pos) {
        if (BlockFalling.canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
            final int i = 32;
            if (!BlockFalling.fallInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
                worldIn.spawnEntity(new EntityFallingBlock(worldIn, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, this.getDefaultState()));
            }
            else {
                worldIn.setBlockToAir(pos);
                BlockPos blockpos;
                for (blockpos = pos; BlockFalling.canFallThrough(worldIn.getBlockState(blockpos)) && blockpos.getY() > 0; blockpos = blockpos.down()) {}
                if (blockpos.getY() > 0) {
                    worldIn.setBlockState(blockpos, this.getDefaultState(), 2);
                }
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        this.teleport(worldIn, pos);
        return true;
    }
    
    @Override
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
        this.teleport(worldIn, pos);
    }
    
    private void teleport(final World worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this) {
            for (int i = 0; i < 1000; ++i) {
                final BlockPos blockpos = pos.add(worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16), worldIn.rand.nextInt(8) - worldIn.rand.nextInt(8), worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16));
                if (worldIn.getBlockState(blockpos).getBlock().material == Material.AIR) {
                    if (worldIn.isRemote) {
                        for (int j = 0; j < 128; ++j) {
                            final double d0 = worldIn.rand.nextDouble();
                            final float f = (worldIn.rand.nextFloat() - 0.5f) * 0.2f;
                            final float f2 = (worldIn.rand.nextFloat() - 0.5f) * 0.2f;
                            final float f3 = (worldIn.rand.nextFloat() - 0.5f) * 0.2f;
                            final double d2 = blockpos.getX() + (pos.getX() - blockpos.getX()) * d0 + (worldIn.rand.nextDouble() - 0.5) + 0.5;
                            final double d3 = blockpos.getY() + (pos.getY() - blockpos.getY()) * d0 + worldIn.rand.nextDouble() - 0.5;
                            final double d4 = blockpos.getZ() + (pos.getZ() - blockpos.getZ()) * d0 + (worldIn.rand.nextDouble() - 0.5) + 0.5;
                            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d2, d3, d4, f, f2, f3, new int[0]);
                        }
                    }
                    else {
                        worldIn.setBlockState(blockpos, iblockstate, 2);
                        worldIn.setBlockToAir(pos);
                    }
                    return;
                }
            }
        }
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 5;
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
    @Deprecated
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        DRAGON_EGG_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375);
    }
}
