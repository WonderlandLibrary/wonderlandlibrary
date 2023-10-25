/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import com.google.common.base.MoreObjects;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWireHook
extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool ATTACHED = PropertyBool.create("attached");
    protected static final AxisAlignedBB HOOK_NORTH_AABB = new AxisAlignedBB(0.3125, 0.0, 0.625, 0.6875, 0.625, 1.0);
    protected static final AxisAlignedBB HOOK_SOUTH_AABB = new AxisAlignedBB(0.3125, 0.0, 0.0, 0.6875, 0.625, 0.375);
    protected static final AxisAlignedBB HOOK_WEST_AABB = new AxisAlignedBB(0.625, 0.0, 0.3125, 1.0, 0.625, 0.6875);
    protected static final AxisAlignedBB HOOK_EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.3125, 0.375, 0.625, 0.6875);

    public BlockTripWireHook() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false).withProperty(ATTACHED, false));
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setTickRandomly(true);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            default: {
                return HOOK_EAST_AABB;
            }
            case WEST: {
                return HOOK_WEST_AABB;
            }
            case SOUTH: {
                return HOOK_SOUTH_AABB;
            }
            case NORTH: 
        }
        return HOOK_NORTH_AABB;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        EnumFacing enumfacing = side.getOpposite();
        BlockPos blockpos = pos.offset(enumfacing);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        boolean flag = BlockTripWireHook.func_193382_c(iblockstate.getBlock());
        return !flag && side.getAxis().isHorizontal() && iblockstate.func_193401_d(worldIn, blockpos, side) == BlockFaceShape.SOLID && !iblockstate.canProvidePower();
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (!this.canPlaceBlockOnSide(worldIn, pos, enumfacing)) continue;
            return true;
        }
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState().withProperty(POWERED, false).withProperty(ATTACHED, false);
        if (facing.getAxis().isHorizontal()) {
            iblockstate = iblockstate.withProperty(FACING, facing);
        }
        return iblockstate;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        this.calculateState(worldIn, pos, state, false, false, -1, null);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        EnumFacing enumfacing;
        if (blockIn != this && this.checkForDrop(worldIn, pos, state) && !this.canPlaceBlockOnSide(worldIn, pos, enumfacing = state.getValue(FACING))) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    public void calculateState(World worldIn, BlockPos pos, IBlockState hookState, boolean p_176260_4_, boolean p_176260_5_, int p_176260_6_, @Nullable IBlockState p_176260_7_) {
        EnumFacing enumfacing = hookState.getValue(FACING);
        boolean flag = hookState.getValue(ATTACHED);
        boolean flag1 = hookState.getValue(POWERED);
        boolean flag2 = !p_176260_4_;
        boolean flag3 = false;
        int i = 0;
        IBlockState[] aiblockstate = new IBlockState[42];
        for (int j = 1; j < 42; ++j) {
            BlockPos blockpos = pos.offset(enumfacing, j);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getBlock() == Blocks.TRIPWIRE_HOOK) {
                if (iblockstate.getValue(FACING) != enumfacing.getOpposite()) break;
                i = j;
                break;
            }
            if (iblockstate.getBlock() != Blocks.TRIPWIRE && j != p_176260_6_) {
                aiblockstate[j] = null;
                flag2 = false;
                continue;
            }
            if (j == p_176260_6_) {
                iblockstate = MoreObjects.firstNonNull(p_176260_7_, iblockstate);
            }
            boolean flag4 = iblockstate.getValue(BlockTripWire.DISARMED) == false;
            boolean flag5 = iblockstate.getValue(BlockTripWire.POWERED);
            flag3 |= flag4 && flag5;
            aiblockstate[j] = iblockstate;
            if (j != p_176260_6_) continue;
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            flag2 &= flag4;
        }
        IBlockState iblockstate1 = this.getDefaultState().withProperty(ATTACHED, flag2).withProperty(POWERED, flag3 &= (flag2 &= i > 1));
        if (i > 0) {
            BlockPos blockpos1 = pos.offset(enumfacing, i);
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            worldIn.setBlockState(blockpos1, iblockstate1.withProperty(FACING, enumfacing1), 3);
            this.notifyNeighbors(worldIn, blockpos1, enumfacing1);
            this.playSound(worldIn, blockpos1, flag2, flag3, flag, flag1);
        }
        this.playSound(worldIn, pos, flag2, flag3, flag, flag1);
        if (!p_176260_4_) {
            worldIn.setBlockState(pos, iblockstate1.withProperty(FACING, enumfacing), 3);
            if (p_176260_5_) {
                this.notifyNeighbors(worldIn, pos, enumfacing);
            }
        }
        if (flag != flag2) {
            for (int k = 1; k < i; ++k) {
                BlockPos blockpos2 = pos.offset(enumfacing, k);
                IBlockState iblockstate2 = aiblockstate[k];
                if (iblockstate2 == null || worldIn.getBlockState(blockpos2).getMaterial() == Material.AIR) continue;
                worldIn.setBlockState(blockpos2, iblockstate2.withProperty(ATTACHED, flag2), 3);
            }
        }
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.calculateState(worldIn, pos, state, false, true, -1, null);
    }

    private void playSound(World worldIn, BlockPos pos, boolean p_180694_3_, boolean p_180694_4_, boolean p_180694_5_, boolean p_180694_6_) {
        if (p_180694_4_ && !p_180694_6_) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_TRIPWIRE_CLICK_ON, SoundCategory.BLOCKS, 0.4f, 0.6f);
        } else if (!p_180694_4_ && p_180694_6_) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, SoundCategory.BLOCKS, 0.4f, 0.5f);
        } else if (p_180694_3_ && !p_180694_5_) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_TRIPWIRE_ATTACH, SoundCategory.BLOCKS, 0.4f, 0.7f);
        } else if (!p_180694_3_ && p_180694_5_) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_TRIPWIRE_DETACH, SoundCategory.BLOCKS, 0.4f, 1.2f / (worldIn.rand.nextFloat() * 0.2f + 0.9f));
        }
    }

    private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing side) {
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        worldIn.notifyNeighborsOfStateChange(pos.offset(side.getOpposite()), this, false);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        boolean flag = state.getValue(ATTACHED);
        boolean flag1 = state.getValue(POWERED);
        if (flag || flag1) {
            this.calculateState(worldIn, pos, state, true, false, -1, null);
        }
        if (flag1) {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            worldIn.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this, false);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getValue(POWERED) != false ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (!blockState.getValue(POWERED).booleanValue()) {
            return 0;
        }
        return blockState.getValue(FACING) == side ? 15 : 0;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(POWERED, (meta & 8) > 0).withProperty(ATTACHED, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(POWERED).booleanValue()) {
            i |= 8;
        }
        if (state.getValue(ATTACHED).booleanValue()) {
            i |= 4;
        }
        return i;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, FACING, POWERED, ATTACHED);
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }
}

