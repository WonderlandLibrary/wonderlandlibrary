// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.world.Explosion;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Random;
import net.minecraft.block.state.BlockFaceShape;
import com.google.common.collect.Lists;
import java.util.Iterator;
import net.minecraft.world.IBlockAccess;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyDirection;

public class BlockStairs extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyEnum<EnumHalf> HALF;
    public static final PropertyEnum<EnumShape> SHAPE;
    protected static final AxisAlignedBB AABB_SLAB_TOP;
    protected static final AxisAlignedBB AABB_QTR_TOP_WEST;
    protected static final AxisAlignedBB AABB_QTR_TOP_EAST;
    protected static final AxisAlignedBB AABB_QTR_TOP_NORTH;
    protected static final AxisAlignedBB AABB_QTR_TOP_SOUTH;
    protected static final AxisAlignedBB AABB_OCT_TOP_NW;
    protected static final AxisAlignedBB AABB_OCT_TOP_NE;
    protected static final AxisAlignedBB AABB_OCT_TOP_SW;
    protected static final AxisAlignedBB AABB_OCT_TOP_SE;
    protected static final AxisAlignedBB AABB_SLAB_BOTTOM;
    protected static final AxisAlignedBB AABB_QTR_BOT_WEST;
    protected static final AxisAlignedBB AABB_QTR_BOT_EAST;
    protected static final AxisAlignedBB AABB_QTR_BOT_NORTH;
    protected static final AxisAlignedBB AABB_QTR_BOT_SOUTH;
    protected static final AxisAlignedBB AABB_OCT_BOT_NW;
    protected static final AxisAlignedBB AABB_OCT_BOT_NE;
    protected static final AxisAlignedBB AABB_OCT_BOT_SW;
    protected static final AxisAlignedBB AABB_OCT_BOT_SE;
    private final Block modelBlock;
    private final IBlockState modelState;
    
    protected BlockStairs(final IBlockState modelState) {
        super(modelState.getBlock().material);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT));
        this.modelBlock = modelState.getBlock();
        this.modelState = modelState;
        this.setHardness(this.modelBlock.blockHardness);
        this.setResistance(this.modelBlock.blockResistance / 3.0f);
        this.setSoundType(this.modelBlock.blockSoundType);
        this.setLightOpacity(255);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        if (!isActualState) {
            state = this.getActualState(state, worldIn, pos);
        }
        for (final AxisAlignedBB axisalignedbb : getCollisionBoxList(state)) {
            Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
        }
    }
    
    private static List<AxisAlignedBB> getCollisionBoxList(final IBlockState bstate) {
        final List<AxisAlignedBB> list = (List<AxisAlignedBB>)Lists.newArrayList();
        final boolean flag = bstate.getValue(BlockStairs.HALF) == EnumHalf.TOP;
        list.add(flag ? BlockStairs.AABB_SLAB_TOP : BlockStairs.AABB_SLAB_BOTTOM);
        final EnumShape blockstairs$enumshape = bstate.getValue(BlockStairs.SHAPE);
        if (blockstairs$enumshape == EnumShape.STRAIGHT || blockstairs$enumshape == EnumShape.INNER_LEFT || blockstairs$enumshape == EnumShape.INNER_RIGHT) {
            list.add(getCollQuarterBlock(bstate));
        }
        if (blockstairs$enumshape != EnumShape.STRAIGHT) {
            list.add(getCollEighthBlock(bstate));
        }
        return list;
    }
    
    private static AxisAlignedBB getCollQuarterBlock(final IBlockState bstate) {
        final boolean flag = bstate.getValue(BlockStairs.HALF) == EnumHalf.TOP;
        switch (bstate.getValue((IProperty<EnumFacing>)BlockStairs.FACING)) {
            default: {
                return flag ? BlockStairs.AABB_QTR_BOT_NORTH : BlockStairs.AABB_QTR_TOP_NORTH;
            }
            case SOUTH: {
                return flag ? BlockStairs.AABB_QTR_BOT_SOUTH : BlockStairs.AABB_QTR_TOP_SOUTH;
            }
            case WEST: {
                return flag ? BlockStairs.AABB_QTR_BOT_WEST : BlockStairs.AABB_QTR_TOP_WEST;
            }
            case EAST: {
                return flag ? BlockStairs.AABB_QTR_BOT_EAST : BlockStairs.AABB_QTR_TOP_EAST;
            }
        }
    }
    
    private static AxisAlignedBB getCollEighthBlock(final IBlockState bstate) {
        final EnumFacing enumfacing = bstate.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
        EnumFacing enumfacing2 = null;
        switch (bstate.getValue(BlockStairs.SHAPE)) {
            default: {
                enumfacing2 = enumfacing;
                break;
            }
            case OUTER_RIGHT: {
                enumfacing2 = enumfacing.rotateY();
                break;
            }
            case INNER_RIGHT: {
                enumfacing2 = enumfacing.getOpposite();
                break;
            }
            case INNER_LEFT: {
                enumfacing2 = enumfacing.rotateYCCW();
                break;
            }
        }
        final boolean flag = bstate.getValue(BlockStairs.HALF) == EnumHalf.TOP;
        switch (enumfacing2) {
            default: {
                return flag ? BlockStairs.AABB_OCT_BOT_NW : BlockStairs.AABB_OCT_TOP_NW;
            }
            case SOUTH: {
                return flag ? BlockStairs.AABB_OCT_BOT_SE : BlockStairs.AABB_OCT_TOP_SE;
            }
            case WEST: {
                return flag ? BlockStairs.AABB_OCT_BOT_SW : BlockStairs.AABB_OCT_TOP_SW;
            }
            case EAST: {
                return flag ? BlockStairs.AABB_OCT_BOT_NE : BlockStairs.AABB_OCT_TOP_NE;
            }
        }
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, IBlockState state, final BlockPos pos, final EnumFacing face) {
        state = this.getActualState(state, worldIn, pos);
        if (face.getAxis() == EnumFacing.Axis.Y) {
            return (face == EnumFacing.UP == (state.getValue(BlockStairs.HALF) == EnumHalf.TOP)) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
        }
        final EnumShape blockstairs$enumshape = state.getValue(BlockStairs.SHAPE);
        if (blockstairs$enumshape == EnumShape.OUTER_LEFT || blockstairs$enumshape == EnumShape.OUTER_RIGHT) {
            return BlockFaceShape.UNDEFINED;
        }
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
        switch (blockstairs$enumshape) {
            case INNER_RIGHT: {
                return (enumfacing != face && enumfacing != face.rotateYCCW()) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
            }
            case INNER_LEFT: {
                return (enumfacing != face && enumfacing != face.rotateY()) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
            }
            case STRAIGHT: {
                return (enumfacing == face) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
            }
            default: {
                return BlockFaceShape.UNDEFINED;
            }
        }
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
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        this.modelBlock.randomDisplayTick(stateIn, worldIn, pos, rand);
    }
    
    @Override
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
        this.modelBlock.onBlockClicked(worldIn, pos, playerIn);
    }
    
    @Override
    public void onPlayerDestroy(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.modelBlock.onPlayerDestroy(worldIn, pos, state);
    }
    
    @Override
    @Deprecated
    public int getPackedLightmapCoords(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return this.modelState.getPackedLightmapCoords(source, pos);
    }
    
    @Override
    public float getExplosionResistance(final Entity exploder) {
        return this.modelBlock.getExplosionResistance(exploder);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return this.modelBlock.getRenderLayer();
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return this.modelBlock.tickRate(worldIn);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getSelectedBoundingBox(final IBlockState state, final World worldIn, final BlockPos pos) {
        return this.modelState.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public Vec3d modifyAcceleration(final World worldIn, final BlockPos pos, final Entity entityIn, final Vec3d motion) {
        return this.modelBlock.modifyAcceleration(worldIn, pos, entityIn, motion);
    }
    
    @Override
    public boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }
    
    @Override
    public boolean canCollideCheck(final IBlockState state, final boolean hitIfLiquid) {
        return this.modelBlock.canCollideCheck(state, hitIfLiquid);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return this.modelBlock.canPlaceBlockAt(worldIn, pos);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.modelState.neighborChanged(worldIn, pos, Blocks.AIR, pos);
        this.modelBlock.onBlockAdded(worldIn, pos, this.modelState);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.modelBlock.breakBlock(worldIn, pos, this.modelState);
    }
    
    @Override
    public void onEntityWalk(final World worldIn, final BlockPos pos, final Entity entityIn) {
        this.modelBlock.onEntityWalk(worldIn, pos, entityIn);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        this.modelBlock.updateTick(worldIn, pos, state, rand);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        return this.modelBlock.onBlockActivated(worldIn, pos, this.modelState, playerIn, hand, EnumFacing.DOWN, 0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void onExplosionDestroy(final World worldIn, final BlockPos pos, final Explosion explosionIn) {
        this.modelBlock.onExplosionDestroy(worldIn, pos, explosionIn);
    }
    
    @Override
    @Deprecated
    public boolean isTopSolid(final IBlockState state) {
        return state.getValue(BlockStairs.HALF) == EnumHalf.TOP;
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return this.modelBlock.getMapColor(this.modelState, worldIn, pos);
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        IBlockState iblockstate = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockStairs.FACING, placer.getHorizontalFacing()).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT);
        return (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5)) ? iblockstate.withProperty(BlockStairs.HALF, EnumHalf.BOTTOM) : iblockstate.withProperty(BlockStairs.HALF, EnumHalf.TOP);
    }
    
    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(final IBlockState blockState, final World worldIn, final BlockPos pos, final Vec3d start, final Vec3d end) {
        final List<RayTraceResult> list = (List<RayTraceResult>)Lists.newArrayList();
        for (final AxisAlignedBB axisalignedbb : getCollisionBoxList(this.getActualState(blockState, worldIn, pos))) {
            list.add(this.rayTrace(pos, start, end, axisalignedbb));
        }
        RayTraceResult raytraceresult1 = null;
        double d1 = 0.0;
        for (final RayTraceResult raytraceresult2 : list) {
            if (raytraceresult2 != null) {
                final double d2 = raytraceresult2.hitVec.squareDistanceTo(end);
                if (d2 <= d1) {
                    continue;
                }
                raytraceresult1 = raytraceresult2;
                d1 = d2;
            }
        }
        return raytraceresult1;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(BlockStairs.HALF, ((meta & 0x4) > 0) ? EnumHalf.TOP : EnumHalf.BOTTOM);
        iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.byIndex(5 - (meta & 0x3)));
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        if (state.getValue(BlockStairs.HALF) == EnumHalf.TOP) {
            i |= 0x4;
        }
        i |= 5 - state.getValue((IProperty<EnumFacing>)BlockStairs.FACING).getIndex();
        return i;
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty(BlockStairs.SHAPE, getStairsShape(state, worldIn, pos));
    }
    
    private static EnumShape getStairsShape(final IBlockState p_185706_0_, final IBlockAccess p_185706_1_, final BlockPos p_185706_2_) {
        final EnumFacing enumfacing = p_185706_0_.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
        final IBlockState iblockstate = p_185706_1_.getBlockState(p_185706_2_.offset(enumfacing));
        if (isBlockStairs(iblockstate) && p_185706_0_.getValue(BlockStairs.HALF) == iblockstate.getValue(BlockStairs.HALF)) {
            final EnumFacing enumfacing2 = iblockstate.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
            if (enumfacing2.getAxis() != p_185706_0_.getValue((IProperty<EnumFacing>)BlockStairs.FACING).getAxis() && isDifferentStairs(p_185706_0_, p_185706_1_, p_185706_2_, enumfacing2.getOpposite())) {
                if (enumfacing2 == enumfacing.rotateYCCW()) {
                    return EnumShape.OUTER_LEFT;
                }
                return EnumShape.OUTER_RIGHT;
            }
        }
        final IBlockState iblockstate2 = p_185706_1_.getBlockState(p_185706_2_.offset(enumfacing.getOpposite()));
        if (isBlockStairs(iblockstate2) && p_185706_0_.getValue(BlockStairs.HALF) == iblockstate2.getValue(BlockStairs.HALF)) {
            final EnumFacing enumfacing3 = iblockstate2.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
            if (enumfacing3.getAxis() != p_185706_0_.getValue((IProperty<EnumFacing>)BlockStairs.FACING).getAxis() && isDifferentStairs(p_185706_0_, p_185706_1_, p_185706_2_, enumfacing3)) {
                if (enumfacing3 == enumfacing.rotateYCCW()) {
                    return EnumShape.INNER_LEFT;
                }
                return EnumShape.INNER_RIGHT;
            }
        }
        return EnumShape.STRAIGHT;
    }
    
    private static boolean isDifferentStairs(final IBlockState p_185704_0_, final IBlockAccess p_185704_1_, final BlockPos p_185704_2_, final EnumFacing p_185704_3_) {
        final IBlockState iblockstate = p_185704_1_.getBlockState(p_185704_2_.offset(p_185704_3_));
        return !isBlockStairs(iblockstate) || iblockstate.getValue((IProperty<Comparable>)BlockStairs.FACING) != p_185704_0_.getValue((IProperty<Comparable>)BlockStairs.FACING) || iblockstate.getValue(BlockStairs.HALF) != p_185704_0_.getValue(BlockStairs.HALF);
    }
    
    public static boolean isBlockStairs(final IBlockState state) {
        return state.getBlock() instanceof BlockStairs;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockStairs.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockStairs.FACING)));
    }
    
    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
        final EnumShape blockstairs$enumshape = state.getValue(BlockStairs.SHAPE);
        Label_0350: {
            switch (mirrorIn) {
                case LEFT_RIGHT: {
                    if (enumfacing.getAxis() != EnumFacing.Axis.Z) {
                        break;
                    }
                    switch (blockstairs$enumshape) {
                        case OUTER_LEFT: {
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, EnumShape.OUTER_RIGHT);
                        }
                        case OUTER_RIGHT: {
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, EnumShape.OUTER_LEFT);
                        }
                        case INNER_RIGHT: {
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, EnumShape.INNER_LEFT);
                        }
                        case INNER_LEFT: {
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, EnumShape.INNER_RIGHT);
                        }
                        default: {
                            return state.withRotation(Rotation.CLOCKWISE_180);
                        }
                    }
                    break;
                }
                case FRONT_BACK: {
                    if (enumfacing.getAxis() != EnumFacing.Axis.X) {
                        break;
                    }
                    switch (blockstairs$enumshape) {
                        case OUTER_LEFT: {
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, EnumShape.OUTER_RIGHT);
                        }
                        case OUTER_RIGHT: {
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, EnumShape.OUTER_LEFT);
                        }
                        case INNER_RIGHT: {
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, EnumShape.INNER_RIGHT);
                        }
                        case INNER_LEFT: {
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, EnumShape.INNER_LEFT);
                        }
                        case STRAIGHT: {
                            return state.withRotation(Rotation.CLOCKWISE_180);
                        }
                        default: {
                            break Label_0350;
                        }
                    }
                    break;
                }
            }
        }
        return super.withMirror(state, mirrorIn);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockStairs.FACING, BlockStairs.HALF, BlockStairs.SHAPE });
    }
    
    static {
        FACING = BlockHorizontal.FACING;
        HALF = PropertyEnum.create("half", EnumHalf.class);
        SHAPE = PropertyEnum.create("shape", EnumShape.class);
        AABB_SLAB_TOP = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);
        AABB_QTR_TOP_WEST = new AxisAlignedBB(0.0, 0.5, 0.0, 0.5, 1.0, 1.0);
        AABB_QTR_TOP_EAST = new AxisAlignedBB(0.5, 0.5, 0.0, 1.0, 1.0, 1.0);
        AABB_QTR_TOP_NORTH = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 0.5);
        AABB_QTR_TOP_SOUTH = new AxisAlignedBB(0.0, 0.5, 0.5, 1.0, 1.0, 1.0);
        AABB_OCT_TOP_NW = new AxisAlignedBB(0.0, 0.5, 0.0, 0.5, 1.0, 0.5);
        AABB_OCT_TOP_NE = new AxisAlignedBB(0.5, 0.5, 0.0, 1.0, 1.0, 0.5);
        AABB_OCT_TOP_SW = new AxisAlignedBB(0.0, 0.5, 0.5, 0.5, 1.0, 1.0);
        AABB_OCT_TOP_SE = new AxisAlignedBB(0.5, 0.5, 0.5, 1.0, 1.0, 1.0);
        AABB_SLAB_BOTTOM = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
        AABB_QTR_BOT_WEST = new AxisAlignedBB(0.0, 0.0, 0.0, 0.5, 0.5, 1.0);
        AABB_QTR_BOT_EAST = new AxisAlignedBB(0.5, 0.0, 0.0, 1.0, 0.5, 1.0);
        AABB_QTR_BOT_NORTH = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 0.5);
        AABB_QTR_BOT_SOUTH = new AxisAlignedBB(0.0, 0.0, 0.5, 1.0, 0.5, 1.0);
        AABB_OCT_BOT_NW = new AxisAlignedBB(0.0, 0.0, 0.0, 0.5, 0.5, 0.5);
        AABB_OCT_BOT_NE = new AxisAlignedBB(0.5, 0.0, 0.0, 1.0, 0.5, 0.5);
        AABB_OCT_BOT_SW = new AxisAlignedBB(0.0, 0.0, 0.5, 0.5, 0.5, 1.0);
        AABB_OCT_BOT_SE = new AxisAlignedBB(0.5, 0.0, 0.5, 1.0, 0.5, 1.0);
    }
    
    public enum EnumHalf implements IStringSerializable
    {
        TOP("top"), 
        BOTTOM("bottom");
        
        private final String name;
        
        private EnumHalf(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
    
    public enum EnumShape implements IStringSerializable
    {
        STRAIGHT("straight"), 
        INNER_LEFT("inner_left"), 
        INNER_RIGHT("inner_right"), 
        OUTER_LEFT("outer_left"), 
        OUTER_RIGHT("outer_right");
        
        private final String name;
        
        private EnumShape(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
