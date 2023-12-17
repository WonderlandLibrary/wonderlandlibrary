// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyBool;

public class BlockFenceGate extends BlockHorizontal
{
    public static final PropertyBool OPEN;
    public static final PropertyBool POWERED;
    public static final PropertyBool IN_WALL;
    protected static final AxisAlignedBB AABB_HITBOX_ZAXIS;
    protected static final AxisAlignedBB AABB_HITBOX_XAXIS;
    protected static final AxisAlignedBB AABB_HITBOX_ZAXIS_INWALL;
    protected static final AxisAlignedBB AABB_HITBOX_XAXIS_INWALL;
    protected static final AxisAlignedBB AABB_COLLISION_BOX_ZAXIS;
    protected static final AxisAlignedBB AABB_COLLISION_BOX_XAXIS;
    
    public BlockFenceGate(final BlockPlanks.EnumType p_i46394_1_) {
        super(Material.WOOD, p_i46394_1_.getMapColor());
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, false).withProperty((IProperty<Comparable>)BlockFenceGate.POWERED, false).withProperty((IProperty<Comparable>)BlockFenceGate.IN_WALL, false));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, final IBlockAccess source, final BlockPos pos) {
        state = this.getActualState(state, source, pos);
        if (state.getValue((IProperty<Boolean>)BlockFenceGate.IN_WALL)) {
            return (state.getValue((IProperty<EnumFacing>)BlockFenceGate.FACING).getAxis() == EnumFacing.Axis.X) ? BlockFenceGate.AABB_HITBOX_XAXIS_INWALL : BlockFenceGate.AABB_HITBOX_ZAXIS_INWALL;
        }
        return (state.getValue((IProperty<EnumFacing>)BlockFenceGate.FACING).getAxis() == EnumFacing.Axis.X) ? BlockFenceGate.AABB_HITBOX_XAXIS : BlockFenceGate.AABB_HITBOX_ZAXIS;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final EnumFacing.Axis enumfacing$axis = state.getValue((IProperty<EnumFacing>)BlockFenceGate.FACING).getAxis();
        if ((enumfacing$axis == EnumFacing.Axis.Z && (worldIn.getBlockState(pos.west()).getBlock() == Blocks.COBBLESTONE_WALL || worldIn.getBlockState(pos.east()).getBlock() == Blocks.COBBLESTONE_WALL)) || (enumfacing$axis == EnumFacing.Axis.X && (worldIn.getBlockState(pos.north()).getBlock() == Blocks.COBBLESTONE_WALL || worldIn.getBlockState(pos.south()).getBlock() == Blocks.COBBLESTONE_WALL))) {
            state = state.withProperty((IProperty<Comparable>)BlockFenceGate.IN_WALL, true);
        }
        return state;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockFenceGate.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockFenceGate.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockFenceGate.FACING)));
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getMaterial().isSolid() && super.canPlaceBlockAt(worldIn, pos);
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        if (blockState.getValue((IProperty<Boolean>)BlockFenceGate.OPEN)) {
            return BlockFenceGate.NULL_AABB;
        }
        return (blockState.getValue((IProperty<EnumFacing>)BlockFenceGate.FACING).getAxis() == EnumFacing.Axis.Z) ? BlockFenceGate.AABB_COLLISION_BOX_ZAXIS : BlockFenceGate.AABB_COLLISION_BOX_XAXIS;
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
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockFenceGate.OPEN);
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final boolean flag = worldIn.isBlockPowered(pos);
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFenceGate.FACING, placer.getHorizontalFacing()).withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, flag).withProperty((IProperty<Comparable>)BlockFenceGate.POWERED, flag).withProperty((IProperty<Comparable>)BlockFenceGate.IN_WALL, false);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (state.getValue((IProperty<Boolean>)BlockFenceGate.OPEN)) {
            state = state.withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, false);
            worldIn.setBlockState(pos, state, 10);
        }
        else {
            final EnumFacing enumfacing = EnumFacing.fromAngle(playerIn.rotationYaw);
            if (state.getValue((IProperty<Comparable>)BlockFenceGate.FACING) == enumfacing.getOpposite()) {
                state = state.withProperty((IProperty<Comparable>)BlockFenceGate.FACING, enumfacing);
            }
            state = state.withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, true);
            worldIn.setBlockState(pos, state, 10);
        }
        worldIn.playEvent(playerIn, ((boolean)state.getValue((IProperty<Boolean>)BlockFenceGate.OPEN)) ? 1008 : 1014, pos, 0);
        return true;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.isRemote) {
            final boolean flag = worldIn.isBlockPowered(pos);
            if (state.getValue((IProperty<Boolean>)BlockFenceGate.POWERED) != flag) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFenceGate.POWERED, flag).withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, flag), 2);
                if (state.getValue((IProperty<Boolean>)BlockFenceGate.OPEN) != flag) {
                    worldIn.playEvent(null, flag ? 1008 : 1014, pos, 0);
                }
            }
        }
    }
    
    @Override
    @Deprecated
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFenceGate.FACING, EnumFacing.byHorizontalIndex(meta)).withProperty((IProperty<Comparable>)BlockFenceGate.OPEN, (meta & 0x4) != 0x0).withProperty((IProperty<Comparable>)BlockFenceGate.POWERED, (meta & 0x8) != 0x0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockFenceGate.FACING).getHorizontalIndex();
        if (state.getValue((IProperty<Boolean>)BlockFenceGate.POWERED)) {
            i |= 0x8;
        }
        if (state.getValue((IProperty<Boolean>)BlockFenceGate.OPEN)) {
            i |= 0x4;
        }
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockFenceGate.FACING, BlockFenceGate.OPEN, BlockFenceGate.POWERED, BlockFenceGate.IN_WALL });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        if (face != EnumFacing.UP && face != EnumFacing.DOWN) {
            return (state.getValue((IProperty<EnumFacing>)BlockFenceGate.FACING).getAxis() == face.rotateY().getAxis()) ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.UNDEFINED;
        }
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        OPEN = PropertyBool.create("open");
        POWERED = PropertyBool.create("powered");
        IN_WALL = PropertyBool.create("in_wall");
        AABB_HITBOX_ZAXIS = new AxisAlignedBB(0.0, 0.0, 0.375, 1.0, 1.0, 0.625);
        AABB_HITBOX_XAXIS = new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.0, 1.0);
        AABB_HITBOX_ZAXIS_INWALL = new AxisAlignedBB(0.0, 0.0, 0.375, 1.0, 0.8125, 0.625);
        AABB_HITBOX_XAXIS_INWALL = new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 0.8125, 1.0);
        AABB_COLLISION_BOX_ZAXIS = new AxisAlignedBB(0.0, 0.0, 0.375, 1.0, 1.5, 0.625);
        AABB_COLLISION_BOX_XAXIS = new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.5, 1.0);
    }
}
