/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockButton
extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    private final boolean wooden;

    protected BlockButton(boolean wooden) {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.wooden = wooden;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public int tickRate(World worldIn) {
        if (!this.wooden) return 20;
        return 30;
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
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return BlockButton.func_181088_a(worldIn, pos, side.getOpposite());
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumfacing = enumFacingArray[n2];
            if (BlockButton.func_181088_a(worldIn, pos, enumfacing)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    protected static boolean func_181088_a(World p_181088_0_, BlockPos p_181088_1_, EnumFacing p_181088_2_) {
        boolean bl;
        BlockPos blockpos = p_181088_1_.offset(p_181088_2_);
        if (p_181088_2_ == EnumFacing.DOWN) {
            bl = World.doesBlockHaveSolidTopSurface(p_181088_0_, blockpos);
            return bl;
        }
        bl = p_181088_0_.getBlockState(blockpos).getBlock().isNormalCube();
        return bl;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iBlockState;
        if (BlockButton.func_181088_a(worldIn, pos, facing.getOpposite())) {
            iBlockState = this.getDefaultState().withProperty(FACING, facing).withProperty(POWERED, false);
            return iBlockState;
        }
        iBlockState = this.getDefaultState().withProperty(FACING, EnumFacing.DOWN).withProperty(POWERED, false);
        return iBlockState;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!this.checkForDrop(worldIn, pos, state)) return;
        if (BlockButton.func_181088_a(worldIn, pos, state.getValue(FACING).getOpposite())) return;
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (this.canPlaceBlockAt(worldIn, pos)) {
            return true;
        }
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.updateBlockBounds(worldIn.getBlockState(pos));
    }

    private void updateBlockBounds(IBlockState state) {
        EnumFacing enumfacing = state.getValue(FACING);
        boolean flag = state.getValue(POWERED);
        float f = 0.25f;
        float f1 = 0.375f;
        float f2 = (float)(flag ? 1 : 2) / 16.0f;
        float f3 = 0.125f;
        float f4 = 0.1875f;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[enumfacing.ordinal()]) {
            case 1: {
                this.setBlockBounds(0.0f, 0.375f, 0.3125f, f2, 0.625f, 0.6875f);
                return;
            }
            case 2: {
                this.setBlockBounds(1.0f - f2, 0.375f, 0.3125f, 1.0f, 0.625f, 0.6875f);
                return;
            }
            case 3: {
                this.setBlockBounds(0.3125f, 0.375f, 0.0f, 0.6875f, 0.625f, f2);
                return;
            }
            case 4: {
                this.setBlockBounds(0.3125f, 0.375f, 1.0f - f2, 0.6875f, 0.625f, 1.0f);
                return;
            }
            case 5: {
                this.setBlockBounds(0.3125f, 0.0f, 0.375f, 0.6875f, 0.0f + f2, 0.625f);
                return;
            }
            case 6: {
                this.setBlockBounds(0.3125f, 1.0f - f2, 0.375f, 0.6875f, 1.0f, 0.625f);
                return;
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (state.getValue(POWERED).booleanValue()) {
            return true;
        }
        worldIn.setBlockState(pos, state.withProperty(POWERED, true), 3);
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
        worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        this.notifyNeighbors(worldIn, pos, state.getValue(FACING));
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getValue(POWERED).booleanValue()) {
            this.notifyNeighbors(worldIn, pos, state.getValue(FACING));
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (state.getValue(POWERED) == false) return 0;
        return 15;
    }

    @Override
    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (!state.getValue(POWERED).booleanValue()) {
            return 0;
        }
        if (state.getValue(FACING) != side) return 0;
        return 15;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.isRemote) return;
        if (state.getValue(POWERED) == false) return;
        if (this.wooden) {
            this.checkForArrows(worldIn, pos, state);
            return;
        }
        worldIn.setBlockState(pos, state.withProperty(POWERED, false));
        this.notifyNeighbors(worldIn, pos, state.getValue(FACING));
        worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, 0.5f);
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float f = 0.1875f;
        float f1 = 0.125f;
        float f2 = 0.125f;
        this.setBlockBounds(0.5f - f, 0.5f - f1, 0.5f - f2, 0.5f + f, 0.5f + f1, 0.5f + f2);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (worldIn.isRemote) return;
        if (!this.wooden) return;
        if (state.getValue(POWERED) != false) return;
        this.checkForArrows(worldIn, pos, state);
    }

    private void checkForArrows(World worldIn, BlockPos pos, IBlockState state) {
        this.updateBlockBounds(state);
        List<EntityArrow> list = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ));
        boolean flag = !list.isEmpty();
        boolean flag1 = state.getValue(POWERED);
        if (flag && !flag1) {
            worldIn.setBlockState(pos, state.withProperty(POWERED, true));
            this.notifyNeighbors(worldIn, pos, state.getValue(FACING));
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, 0.6f);
        }
        if (!flag && flag1) {
            worldIn.setBlockState(pos, state.withProperty(POWERED, false));
            this.notifyNeighbors(worldIn, pos, state.getValue(FACING));
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, 0.5f);
        }
        if (!flag) return;
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing facing) {
        worldIn.notifyNeighborsOfStateChange(pos, this);
        worldIn.notifyNeighborsOfStateChange(pos.offset(facing.getOpposite()), this);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean bl;
        EnumFacing enumfacing;
        switch (meta & 7) {
            case 0: {
                enumfacing = EnumFacing.DOWN;
                break;
            }
            case 1: {
                enumfacing = EnumFacing.EAST;
                break;
            }
            case 2: {
                enumfacing = EnumFacing.WEST;
                break;
            }
            case 3: {
                enumfacing = EnumFacing.SOUTH;
                break;
            }
            case 4: {
                enumfacing = EnumFacing.NORTH;
                break;
            }
            default: {
                enumfacing = EnumFacing.UP;
            }
        }
        IBlockState iBlockState = this.getDefaultState().withProperty(FACING, enumfacing);
        if ((meta & 8) > 0) {
            bl = true;
            return iBlockState.withProperty(POWERED, bl);
        }
        bl = false;
        return iBlockState.withProperty(POWERED, bl);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[state.getValue(FACING).ordinal()]) {
            case 1: {
                i = 1;
                break;
            }
            case 2: {
                i = 2;
                break;
            }
            case 3: {
                i = 3;
                break;
            }
            case 4: {
                i = 4;
                break;
            }
            default: {
                i = 5;
                break;
            }
            case 6: {
                i = 0;
            }
        }
        if (state.getValue(POWERED) == false) return i;
        i |= 8;
        return i;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, POWERED);
    }
}

