// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.entity.Entity;
import java.util.Random;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public abstract class BlockBasePressurePlate extends Block
{
    private static final String __OBFID = "CL_00000194";
    
    protected BlockBasePressurePlate(final Material materialIn) {
        super(materialIn);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setTickRandomly(true);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        this.func_180668_d(access.getBlockState(pos));
    }
    
    protected void func_180668_d(final IBlockState p_180668_1_) {
        final boolean var2 = this.getRedstoneStrength(p_180668_1_) > 0;
        final float var3 = 0.0625f;
        if (var2) {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.03125f, 0.9375f);
        }
        else {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.0625f, 0.9375f);
        }
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 20;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isPassable(final IBlockAccess blockAccess, final BlockPos pos) {
        return true;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return this.canBePlacedOn(worldIn, pos.offsetDown());
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!this.canBePlacedOn(worldIn, pos.offsetDown())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    private boolean canBePlacedOn(final World worldIn, final BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos) || worldIn.getBlockState(pos).getBlock() instanceof BlockFence;
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            final int var5 = this.getRedstoneStrength(state);
            if (var5 > 0) {
                this.updateState(worldIn, pos, state, var5);
            }
        }
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote) {
            final int var5 = this.getRedstoneStrength(state);
            if (var5 == 0) {
                this.updateState(worldIn, pos, state, var5);
            }
        }
    }
    
    protected void updateState(final World worldIn, final BlockPos pos, IBlockState state, final int oldRedstoneStrength) {
        final int var5 = this.computeRedstoneStrength(worldIn, pos);
        final boolean var6 = oldRedstoneStrength > 0;
        final boolean var7 = var5 > 0;
        if (oldRedstoneStrength != var5) {
            state = this.setRedstoneStrength(state, var5);
            worldIn.setBlockState(pos, state, 2);
            this.updateNeighbors(worldIn, pos);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
        if (!var7 && var6) {
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5, "random.click", 0.3f, 0.5f);
        }
        else if (var7 && !var6) {
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        }
        if (var7) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }
    
    protected AxisAlignedBB getSensitiveAABB(final BlockPos pos) {
        final float var2 = 0.125f;
        return new AxisAlignedBB(pos.getX() + 0.125f, pos.getY(), pos.getZ() + 0.125f, pos.getX() + 1 - 0.125f, pos.getY() + 0.25, pos.getZ() + 1 - 0.125f);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.getRedstoneStrength(state) > 0) {
            this.updateNeighbors(worldIn, pos);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    protected void updateNeighbors(final World worldIn, final BlockPos pos) {
        worldIn.notifyNeighborsOfStateChange(pos, this);
        worldIn.notifyNeighborsOfStateChange(pos.offsetDown(), this);
    }
    
    @Override
    public int isProvidingWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return this.getRedstoneStrength(state);
    }
    
    @Override
    public int isProvidingStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return (side == EnumFacing.UP) ? this.getRedstoneStrength(state) : 0;
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        final float var1 = 0.5f;
        final float var2 = 0.125f;
        final float var3 = 0.5f;
        this.setBlockBounds(0.0f, 0.375f, 0.0f, 1.0f, 0.625f, 1.0f);
    }
    
    @Override
    public int getMobilityFlag() {
        return 1;
    }
    
    protected abstract int computeRedstoneStrength(final World p0, final BlockPos p1);
    
    protected abstract int getRedstoneStrength(final IBlockState p0);
    
    protected abstract IBlockState setRedstoneStrength(final IBlockState p0, final int p1);
}
