/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStairs
extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyEnum<EnumHalf> HALF = PropertyEnum.create("half", EnumHalf.class);
    public static final PropertyEnum<EnumShape> SHAPE = PropertyEnum.create("shape", EnumShape.class);
    protected static final AxisAlignedBB AABB_SLAB_TOP = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB AABB_QTR_TOP_WEST = new AxisAlignedBB(0.0, 0.5, 0.0, 0.5, 1.0, 1.0);
    protected static final AxisAlignedBB AABB_QTR_TOP_EAST = new AxisAlignedBB(0.5, 0.5, 0.0, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB AABB_QTR_TOP_NORTH = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 0.5);
    protected static final AxisAlignedBB AABB_QTR_TOP_SOUTH = new AxisAlignedBB(0.0, 0.5, 0.5, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB AABB_OCT_TOP_NW = new AxisAlignedBB(0.0, 0.5, 0.0, 0.5, 1.0, 0.5);
    protected static final AxisAlignedBB AABB_OCT_TOP_NE = new AxisAlignedBB(0.5, 0.5, 0.0, 1.0, 1.0, 0.5);
    protected static final AxisAlignedBB AABB_OCT_TOP_SW = new AxisAlignedBB(0.0, 0.5, 0.5, 0.5, 1.0, 1.0);
    protected static final AxisAlignedBB AABB_OCT_TOP_SE = new AxisAlignedBB(0.5, 0.5, 0.5, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB AABB_SLAB_BOTTOM = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
    protected static final AxisAlignedBB AABB_QTR_BOT_WEST = new AxisAlignedBB(0.0, 0.0, 0.0, 0.5, 0.5, 1.0);
    protected static final AxisAlignedBB AABB_QTR_BOT_EAST = new AxisAlignedBB(0.5, 0.0, 0.0, 1.0, 0.5, 1.0);
    protected static final AxisAlignedBB AABB_QTR_BOT_NORTH = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 0.5);
    protected static final AxisAlignedBB AABB_QTR_BOT_SOUTH = new AxisAlignedBB(0.0, 0.0, 0.5, 1.0, 0.5, 1.0);
    protected static final AxisAlignedBB AABB_OCT_BOT_NW = new AxisAlignedBB(0.0, 0.0, 0.0, 0.5, 0.5, 0.5);
    protected static final AxisAlignedBB AABB_OCT_BOT_NE = new AxisAlignedBB(0.5, 0.0, 0.0, 1.0, 0.5, 0.5);
    protected static final AxisAlignedBB AABB_OCT_BOT_SW = new AxisAlignedBB(0.0, 0.0, 0.5, 0.5, 0.5, 1.0);
    protected static final AxisAlignedBB AABB_OCT_BOT_SE = new AxisAlignedBB(0.5, 0.0, 0.5, 1.0, 0.5, 1.0);
    private final Block modelBlock;
    private final IBlockState modelState;

    protected BlockStairs(IBlockState modelState) {
        super(modelState.getBlock().blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, EnumHalf.BOTTOM).withProperty(SHAPE, EnumShape.STRAIGHT));
        this.modelBlock = modelState.getBlock();
        this.modelState = modelState;
        this.setHardness(this.modelBlock.blockHardness);
        this.setResistance(this.modelBlock.blockResistance / 3.0f);
        this.setSoundType(this.modelBlock.blockSoundType);
        this.setLightOpacity(255);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        if (!p_185477_7_) {
            state = this.getActualState(state, worldIn, pos);
        }
        for (AxisAlignedBB axisalignedbb : BlockStairs.getCollisionBoxList(state)) {
            BlockStairs.addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
        }
    }

    private static List<AxisAlignedBB> getCollisionBoxList(IBlockState bstate) {
        ArrayList<AxisAlignedBB> list = Lists.newArrayList();
        boolean flag = bstate.getValue(HALF) == EnumHalf.TOP;
        list.add(flag ? AABB_SLAB_TOP : AABB_SLAB_BOTTOM);
        EnumShape blockstairs$enumshape = bstate.getValue(SHAPE);
        if (blockstairs$enumshape == EnumShape.STRAIGHT || blockstairs$enumshape == EnumShape.INNER_LEFT || blockstairs$enumshape == EnumShape.INNER_RIGHT) {
            list.add(BlockStairs.getCollQuarterBlock(bstate));
        }
        if (blockstairs$enumshape != EnumShape.STRAIGHT) {
            list.add(BlockStairs.getCollEighthBlock(bstate));
        }
        return list;
    }

    private static AxisAlignedBB getCollQuarterBlock(IBlockState bstate) {
        boolean flag = bstate.getValue(HALF) == EnumHalf.TOP;
        switch (bstate.getValue(FACING)) {
            default: {
                return flag ? AABB_QTR_BOT_NORTH : AABB_QTR_TOP_NORTH;
            }
            case SOUTH: {
                return flag ? AABB_QTR_BOT_SOUTH : AABB_QTR_TOP_SOUTH;
            }
            case WEST: {
                return flag ? AABB_QTR_BOT_WEST : AABB_QTR_TOP_WEST;
            }
            case EAST: 
        }
        return flag ? AABB_QTR_BOT_EAST : AABB_QTR_TOP_EAST;
    }

    private static AxisAlignedBB getCollEighthBlock(IBlockState bstate) {
        EnumFacing enumfacing1;
        EnumFacing enumfacing = bstate.getValue(FACING);
        switch (bstate.getValue(SHAPE)) {
            default: {
                enumfacing1 = enumfacing;
                break;
            }
            case OUTER_RIGHT: {
                enumfacing1 = enumfacing.rotateY();
                break;
            }
            case INNER_RIGHT: {
                enumfacing1 = enumfacing.getOpposite();
                break;
            }
            case INNER_LEFT: {
                enumfacing1 = enumfacing.rotateYCCW();
            }
        }
        boolean flag = bstate.getValue(HALF) == EnumHalf.TOP;
        switch (enumfacing1) {
            default: {
                return flag ? AABB_OCT_BOT_NW : AABB_OCT_TOP_NW;
            }
            case SOUTH: {
                return flag ? AABB_OCT_BOT_SE : AABB_OCT_TOP_SE;
            }
            case WEST: {
                return flag ? AABB_OCT_BOT_SW : AABB_OCT_TOP_SW;
            }
            case EAST: 
        }
        return flag ? AABB_OCT_BOT_NE : AABB_OCT_TOP_NE;
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        p_193383_2_ = this.getActualState(p_193383_2_, p_193383_1_, p_193383_3_);
        if (p_193383_4_.getAxis() == EnumFacing.Axis.Y) {
            return p_193383_4_ == EnumFacing.UP == (p_193383_2_.getValue(HALF) == EnumHalf.TOP) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
        }
        EnumShape blockstairs$enumshape = p_193383_2_.getValue(SHAPE);
        if (blockstairs$enumshape != EnumShape.OUTER_LEFT && blockstairs$enumshape != EnumShape.OUTER_RIGHT) {
            EnumFacing enumfacing = p_193383_2_.getValue(FACING);
            switch (blockstairs$enumshape) {
                case INNER_RIGHT: {
                    return enumfacing != p_193383_4_ && enumfacing != p_193383_4_.rotateYCCW() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
                }
                case INNER_LEFT: {
                    return enumfacing != p_193383_4_ && enumfacing != p_193383_4_.rotateY() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
                }
                case STRAIGHT: {
                    return enumfacing == p_193383_4_ ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
                }
            }
            return BlockFaceShape.UNDEFINED;
        }
        return BlockFaceShape.UNDEFINED;
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
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        this.modelBlock.randomDisplayTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        this.modelBlock.onBlockClicked(worldIn, pos, playerIn);
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        this.modelBlock.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    @Override
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos) {
        return this.modelState.getPackedLightmapCoords(source, pos);
    }

    @Override
    public float getExplosionResistance(Entity exploder) {
        return this.modelBlock.getExplosionResistance(exploder);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return this.modelBlock.getBlockLayer();
    }

    @Override
    public int tickRate(World worldIn) {
        return this.modelBlock.tickRate(worldIn);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return this.modelState.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion) {
        return this.modelBlock.modifyAcceleration(worldIn, pos, entityIn, motion);
    }

    @Override
    public boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return this.modelBlock.canCollideCheck(state, hitIfLiquid);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return this.modelBlock.canPlaceBlockAt(worldIn, pos);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.modelState.neighborChanged(worldIn, pos, Blocks.AIR, pos);
        this.modelBlock.onBlockAdded(worldIn, pos, this.modelState);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        this.modelBlock.breakBlock(worldIn, pos, this.modelState);
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        this.modelBlock.onEntityWalk(worldIn, pos, entityIn);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.modelBlock.updateTick(worldIn, pos, state, rand);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        return this.modelBlock.onBlockActivated(worldIn, pos, this.modelState, playerIn, hand, EnumFacing.DOWN, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        this.modelBlock.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
    }

    @Override
    public boolean isFullyOpaque(IBlockState state) {
        return state.getValue(HALF) == EnumHalf.TOP;
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
        return this.modelBlock.getMapColor(this.modelState, p_180659_2_, p_180659_3_);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing()).withProperty(SHAPE, EnumShape.STRAIGHT);
        return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5) ? iblockstate.withProperty(HALF, EnumHalf.BOTTOM) : iblockstate.withProperty(HALF, EnumHalf.TOP);
    }

    @Override
    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        ArrayList<RayTraceResult> list = Lists.newArrayList();
        for (AxisAlignedBB axisalignedbb : BlockStairs.getCollisionBoxList(this.getActualState(blockState, worldIn, pos))) {
            list.add(this.rayTrace(pos, start, end, axisalignedbb));
        }
        RayTraceResult raytraceresult1 = null;
        double d1 = 0.0;
        for (RayTraceResult raytraceresult : list) {
            double d0;
            if (raytraceresult == null || !((d0 = raytraceresult.hitVec.squareDistanceTo(end)) > d1)) continue;
            raytraceresult1 = raytraceresult;
            d1 = d0;
        }
        return raytraceresult1;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(HALF, (meta & 4) > 0 ? EnumHalf.TOP : EnumHalf.BOTTOM);
        iblockstate = iblockstate.withProperty(FACING, EnumFacing.getFront(5 - (meta & 3)));
        return iblockstate;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(HALF) == EnumHalf.TOP) {
            i |= 4;
        }
        return i |= 5 - state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(SHAPE, BlockStairs.getStairsShape(state, worldIn, pos));
    }

    private static EnumShape getStairsShape(IBlockState p_185706_0_, IBlockAccess p_185706_1_, BlockPos p_185706_2_) {
        EnumFacing enumfacing2;
        EnumFacing enumfacing1;
        EnumFacing enumfacing = p_185706_0_.getValue(FACING);
        IBlockState iblockstate = p_185706_1_.getBlockState(p_185706_2_.offset(enumfacing));
        if (BlockStairs.isBlockStairs(iblockstate) && p_185706_0_.getValue(HALF) == iblockstate.getValue(HALF) && (enumfacing1 = iblockstate.getValue(FACING)).getAxis() != p_185706_0_.getValue(FACING).getAxis() && BlockStairs.isDifferentStairs(p_185706_0_, p_185706_1_, p_185706_2_, enumfacing1.getOpposite())) {
            if (enumfacing1 == enumfacing.rotateYCCW()) {
                return EnumShape.OUTER_LEFT;
            }
            return EnumShape.OUTER_RIGHT;
        }
        IBlockState iblockstate1 = p_185706_1_.getBlockState(p_185706_2_.offset(enumfacing.getOpposite()));
        if (BlockStairs.isBlockStairs(iblockstate1) && p_185706_0_.getValue(HALF) == iblockstate1.getValue(HALF) && (enumfacing2 = iblockstate1.getValue(FACING)).getAxis() != p_185706_0_.getValue(FACING).getAxis() && BlockStairs.isDifferentStairs(p_185706_0_, p_185706_1_, p_185706_2_, enumfacing2)) {
            if (enumfacing2 == enumfacing.rotateYCCW()) {
                return EnumShape.INNER_LEFT;
            }
            return EnumShape.INNER_RIGHT;
        }
        return EnumShape.STRAIGHT;
    }

    private static boolean isDifferentStairs(IBlockState p_185704_0_, IBlockAccess p_185704_1_, BlockPos p_185704_2_, EnumFacing p_185704_3_) {
        IBlockState iblockstate = p_185704_1_.getBlockState(p_185704_2_.offset(p_185704_3_));
        return !BlockStairs.isBlockStairs(iblockstate) || iblockstate.getValue(FACING) != p_185704_0_.getValue(FACING) || iblockstate.getValue(HALF) != p_185704_0_.getValue(HALF);
    }

    public static boolean isBlockStairs(IBlockState state) {
        return state.getBlock() instanceof BlockStairs;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        EnumFacing enumfacing = state.getValue(FACING);
        EnumShape blockstairs$enumshape = state.getValue(SHAPE);
        switch (mirrorIn) {
            case LEFT_RIGHT: {
                if (enumfacing.getAxis() != EnumFacing.Axis.Z) break;
                switch (blockstairs$enumshape) {
                    case OUTER_LEFT: {
                        return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, EnumShape.OUTER_RIGHT);
                    }
                    case OUTER_RIGHT: {
                        return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, EnumShape.OUTER_LEFT);
                    }
                    case INNER_RIGHT: {
                        return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, EnumShape.INNER_LEFT);
                    }
                    case INNER_LEFT: {
                        return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, EnumShape.INNER_RIGHT);
                    }
                }
                return state.withRotation(Rotation.CLOCKWISE_180);
            }
            case FRONT_BACK: {
                if (enumfacing.getAxis() != EnumFacing.Axis.X) break;
                switch (blockstairs$enumshape) {
                    case OUTER_LEFT: {
                        return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, EnumShape.OUTER_RIGHT);
                    }
                    case OUTER_RIGHT: {
                        return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, EnumShape.OUTER_LEFT);
                    }
                    case INNER_RIGHT: {
                        return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, EnumShape.INNER_RIGHT);
                    }
                    case INNER_LEFT: {
                        return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, EnumShape.INNER_LEFT);
                    }
                    case STRAIGHT: {
                        return state.withRotation(Rotation.CLOCKWISE_180);
                    }
                }
            }
        }
        return super.withMirror(state, mirrorIn);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, FACING, HALF, SHAPE);
    }

    public static enum EnumShape implements IStringSerializable
    {
        STRAIGHT("straight"),
        INNER_LEFT("inner_left"),
        INNER_RIGHT("inner_right"),
        OUTER_LEFT("outer_left"),
        OUTER_RIGHT("outer_right");

        private final String name;

        private EnumShape(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    public static enum EnumHalf implements IStringSerializable
    {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        private EnumHalf(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

