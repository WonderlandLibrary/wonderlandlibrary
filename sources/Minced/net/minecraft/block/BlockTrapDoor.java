// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockTrapDoor extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyBool OPEN;
    public static final PropertyEnum<DoorHalf> HALF;
    protected static final AxisAlignedBB EAST_OPEN_AABB;
    protected static final AxisAlignedBB WEST_OPEN_AABB;
    protected static final AxisAlignedBB SOUTH_OPEN_AABB;
    protected static final AxisAlignedBB NORTH_OPEN_AABB;
    protected static final AxisAlignedBB BOTTOM_AABB;
    protected static final AxisAlignedBB TOP_AABB;
    
    protected BlockTrapDoor(final Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockTrapDoor.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockTrapDoor.OPEN, false).withProperty(BlockTrapDoor.HALF, DoorHalf.BOTTOM));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        AxisAlignedBB axisalignedbb = null;
        if (state.getValue((IProperty<Boolean>)BlockTrapDoor.OPEN)) {
            switch (state.getValue((IProperty<EnumFacing>)BlockTrapDoor.FACING)) {
                default: {
                    axisalignedbb = BlockTrapDoor.NORTH_OPEN_AABB;
                    break;
                }
                case SOUTH: {
                    axisalignedbb = BlockTrapDoor.SOUTH_OPEN_AABB;
                    break;
                }
                case WEST: {
                    axisalignedbb = BlockTrapDoor.WEST_OPEN_AABB;
                    break;
                }
                case EAST: {
                    axisalignedbb = BlockTrapDoor.EAST_OPEN_AABB;
                    break;
                }
            }
        }
        else if (state.getValue(BlockTrapDoor.HALF) == DoorHalf.TOP) {
            axisalignedbb = BlockTrapDoor.TOP_AABB;
        }
        else {
            axisalignedbb = BlockTrapDoor.BOTTOM_AABB;
        }
        return axisalignedbb;
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
        return !worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockTrapDoor.OPEN);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (this.material == Material.IRON) {
            return false;
        }
        state = state.cycleProperty((IProperty<Comparable>)BlockTrapDoor.OPEN);
        worldIn.setBlockState(pos, state, 2);
        this.playSound(playerIn, worldIn, pos, state.getValue((IProperty<Boolean>)BlockTrapDoor.OPEN));
        return true;
    }
    
    protected void playSound(@Nullable final EntityPlayer player, final World worldIn, final BlockPos pos, final boolean p_185731_4_) {
        if (p_185731_4_) {
            final int i = (this.material == Material.IRON) ? 1037 : 1007;
            worldIn.playEvent(player, i, pos, 0);
        }
        else {
            final int j = (this.material == Material.IRON) ? 1036 : 1013;
            worldIn.playEvent(player, j, pos, 0);
        }
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.isRemote) {
            final boolean flag = worldIn.isBlockPowered(pos);
            if (flag || blockIn.getDefaultState().canProvidePower()) {
                final boolean flag2 = state.getValue((IProperty<Boolean>)BlockTrapDoor.OPEN);
                if (flag2 != flag) {
                    worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockTrapDoor.OPEN, flag), 2);
                    this.playSound(null, worldIn, pos, flag);
                }
            }
        }
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState();
        if (facing.getAxis().isHorizontal()) {
            iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTrapDoor.FACING, facing).withProperty((IProperty<Comparable>)BlockTrapDoor.OPEN, false);
            iblockstate = iblockstate.withProperty(BlockTrapDoor.HALF, (hitY > 0.5f) ? DoorHalf.TOP : DoorHalf.BOTTOM);
        }
        else {
            iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTrapDoor.FACING, placer.getHorizontalFacing().getOpposite()).withProperty((IProperty<Comparable>)BlockTrapDoor.OPEN, false);
            iblockstate = iblockstate.withProperty(BlockTrapDoor.HALF, (facing == EnumFacing.UP) ? DoorHalf.BOTTOM : DoorHalf.TOP);
        }
        if (worldIn.isBlockPowered(pos)) {
            iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTrapDoor.OPEN, true);
        }
        return iblockstate;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    protected static EnumFacing getFacing(final int meta) {
        switch (meta & 0x3) {
            case 0: {
                return EnumFacing.NORTH;
            }
            case 1: {
                return EnumFacing.SOUTH;
            }
            case 2: {
                return EnumFacing.WEST;
            }
            default: {
                return EnumFacing.EAST;
            }
        }
    }
    
    protected static int getMetaForFacing(final EnumFacing facing) {
        switch (facing) {
            case NORTH: {
                return 0;
            }
            case SOUTH: {
                return 1;
            }
            case WEST: {
                return 2;
            }
            default: {
                return 3;
            }
        }
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockTrapDoor.FACING, getFacing(meta)).withProperty((IProperty<Comparable>)BlockTrapDoor.OPEN, (meta & 0x4) != 0x0).withProperty(BlockTrapDoor.HALF, ((meta & 0x8) == 0x0) ? DoorHalf.BOTTOM : DoorHalf.TOP);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= getMetaForFacing(state.getValue((IProperty<EnumFacing>)BlockTrapDoor.FACING));
        if (state.getValue((IProperty<Boolean>)BlockTrapDoor.OPEN)) {
            i |= 0x4;
        }
        if (state.getValue(BlockTrapDoor.HALF) == DoorHalf.TOP) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockTrapDoor.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockTrapDoor.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockTrapDoor.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockTrapDoor.FACING, BlockTrapDoor.OPEN, BlockTrapDoor.HALF });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return (((face == EnumFacing.UP && state.getValue(BlockTrapDoor.HALF) == DoorHalf.TOP) || (face == EnumFacing.DOWN && state.getValue(BlockTrapDoor.HALF) == DoorHalf.BOTTOM)) && !state.getValue((IProperty<Boolean>)BlockTrapDoor.OPEN)) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
    
    static {
        FACING = BlockHorizontal.FACING;
        OPEN = PropertyBool.create("open");
        HALF = PropertyEnum.create("half", DoorHalf.class);
        EAST_OPEN_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.1875, 1.0, 1.0);
        WEST_OPEN_AABB = new AxisAlignedBB(0.8125, 0.0, 0.0, 1.0, 1.0, 1.0);
        SOUTH_OPEN_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.1875);
        NORTH_OPEN_AABB = new AxisAlignedBB(0.0, 0.0, 0.8125, 1.0, 1.0, 1.0);
        BOTTOM_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.1875, 1.0);
        TOP_AABB = new AxisAlignedBB(0.0, 0.8125, 0.0, 1.0, 1.0, 1.0);
    }
    
    public enum DoorHalf implements IStringSerializable
    {
        TOP("top"), 
        BOTTOM("bottom");
        
        private final String name;
        
        private DoorHalf(final String name) {
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
