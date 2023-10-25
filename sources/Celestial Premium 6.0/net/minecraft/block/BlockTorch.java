/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTorch
extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>(){

        @Override
        public boolean apply(@Nullable EnumFacing p_apply_1_) {
            return p_apply_1_ != EnumFacing.DOWN;
        }
    });
    protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.4f, 0.0, 0.4f, 0.6f, 0.6f, 0.6f);
    protected static final AxisAlignedBB TORCH_NORTH_AABB = new AxisAlignedBB(0.35f, 0.2f, 0.7f, 0.65f, 0.8f, 1.0);
    protected static final AxisAlignedBB TORCH_SOUTH_AABB = new AxisAlignedBB(0.35f, 0.2f, 0.0, 0.65f, 0.8f, 0.3f);
    protected static final AxisAlignedBB TORCH_WEST_AABB = new AxisAlignedBB(0.7f, 0.2f, 0.35f, 1.0, 0.8f, 0.65f);
    protected static final AxisAlignedBB TORCH_EAST_AABB = new AxisAlignedBB(0.0, 0.2f, 0.35f, 0.3f, 0.8f, 0.65f);

    protected BlockTorch() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case EAST: {
                return TORCH_EAST_AABB;
            }
            case WEST: {
                return TORCH_WEST_AABB;
            }
            case SOUTH: {
                return TORCH_SOUTH_AABB;
            }
            case NORTH: {
                return TORCH_NORTH_AABB;
            }
        }
        return STANDING_AABB;
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

    private boolean canPlaceOn(World worldIn, BlockPos pos) {
        boolean flag;
        Block block = worldIn.getBlockState(pos).getBlock();
        boolean bl = flag = block == Blocks.END_GATEWAY || block == Blocks.LIT_PUMPKIN;
        if (worldIn.getBlockState(pos).isFullyOpaque()) {
            return !flag;
        }
        boolean flag1 = block instanceof BlockFence || block == Blocks.GLASS || block == Blocks.COBBLESTONE_WALL || block == Blocks.STAINED_GLASS;
        return flag1 && !flag;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : FACING.getAllowedValues()) {
            if (!this.canPlaceAt(worldIn, pos, enumfacing)) continue;
            return true;
        }
        return false;
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing) {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        BlockFaceShape blockfaceshape = iblockstate.func_193401_d(worldIn, blockpos, facing);
        if (facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos)) {
            return true;
        }
        if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
            return !BlockTorch.func_193382_c(block) && blockfaceshape == BlockFaceShape.SOLID;
        }
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (this.canPlaceAt(worldIn, pos, facing)) {
            return this.getDefaultState().withProperty(FACING, facing);
        }
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (!this.canPlaceAt(worldIn, pos, enumfacing)) continue;
            return this.getDefaultState().withProperty(FACING, enumfacing);
        }
        return this.getDefaultState();
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.checkForDrop(worldIn, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        this.onNeighborChangeInternal(worldIn, pos, state);
    }

    protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.checkForDrop(worldIn, pos, state)) {
            return true;
        }
        EnumFacing enumfacing = state.getValue(FACING);
        EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
        EnumFacing enumfacing1 = enumfacing.getOpposite();
        BlockPos blockpos = pos.offset(enumfacing1);
        boolean flag = false;
        if (enumfacing$axis.isHorizontal() && worldIn.getBlockState(blockpos).func_193401_d(worldIn, blockpos, enumfacing) != BlockFaceShape.SOLID) {
            flag = true;
        } else if (enumfacing$axis.isVertical() && !this.canPlaceOn(worldIn, blockpos)) {
            flag = true;
        }
        if (flag) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return true;
        }
        return false;
    }

    protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, state.getValue(FACING))) {
            return true;
        }
        if (worldIn.getBlockState(pos).getBlock() == this) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        return false;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        EnumFacing enumfacing = stateIn.getValue(FACING);
        double d0 = (double)pos.getX() + 0.5;
        double d1 = (double)pos.getY() + 0.7;
        double d2 = (double)pos.getZ() + 0.5;
        double d3 = 0.22;
        double d4 = 0.27;
        if (enumfacing.getAxis().isHorizontal()) {
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.27 * (double)enumfacing1.getXOffset(), d1 + 0.22, d2 + 0.27 * (double)enumfacing1.getZOffset(), 0.0, 0.0, 0.0, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.27 * (double)enumfacing1.getXOffset(), d1 + 0.22, d2 + 0.27 * (double)enumfacing1.getZOffset(), 0.0, 0.0, 0.0, new int[0]);
        } else {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0, 0.0, 0.0, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState();
        switch (meta) {
            case 1: {
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.EAST);
                break;
            }
            case 2: {
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.WEST);
                break;
            }
            case 3: {
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.SOUTH);
                break;
            }
            case 4: {
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.NORTH);
                break;
            }
            default: {
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.UP);
            }
        }
        return iblockstate;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        switch (state.getValue(FACING)) {
            case EAST: {
                i |= 1;
                break;
            }
            case WEST: {
                i |= 2;
                break;
            }
            case SOUTH: {
                i |= 3;
                break;
            }
            case NORTH: {
                i |= 4;
                break;
            }
            default: {
                i |= 5;
            }
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
        return new BlockStateContainer((Block)this, FACING);
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }
}

