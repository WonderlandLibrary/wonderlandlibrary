// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.item.ItemStack;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockPistonExtension extends BlockDirectional
{
    public static final PropertyEnum<EnumPistonType> TYPE;
    public static final PropertyBool SHORT;
    protected static final AxisAlignedBB PISTON_EXTENSION_EAST_AABB;
    protected static final AxisAlignedBB PISTON_EXTENSION_WEST_AABB;
    protected static final AxisAlignedBB PISTON_EXTENSION_SOUTH_AABB;
    protected static final AxisAlignedBB PISTON_EXTENSION_NORTH_AABB;
    protected static final AxisAlignedBB PISTON_EXTENSION_UP_AABB;
    protected static final AxisAlignedBB PISTON_EXTENSION_DOWN_AABB;
    protected static final AxisAlignedBB UP_ARM_AABB;
    protected static final AxisAlignedBB DOWN_ARM_AABB;
    protected static final AxisAlignedBB SOUTH_ARM_AABB;
    protected static final AxisAlignedBB NORTH_ARM_AABB;
    protected static final AxisAlignedBB EAST_ARM_AABB;
    protected static final AxisAlignedBB WEST_ARM_AABB;
    protected static final AxisAlignedBB SHORT_UP_ARM_AABB;
    protected static final AxisAlignedBB SHORT_DOWN_ARM_AABB;
    protected static final AxisAlignedBB SHORT_SOUTH_ARM_AABB;
    protected static final AxisAlignedBB SHORT_NORTH_ARM_AABB;
    protected static final AxisAlignedBB SHORT_EAST_ARM_AABB;
    protected static final AxisAlignedBB SHORT_WEST_ARM_AABB;
    
    public BlockPistonExtension() {
        super(Material.PISTON);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, EnumFacing.NORTH).withProperty(BlockPistonExtension.TYPE, EnumPistonType.DEFAULT).withProperty((IProperty<Comparable>)BlockPistonExtension.SHORT, false));
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.5f);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        switch (state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING)) {
            default: {
                return BlockPistonExtension.PISTON_EXTENSION_DOWN_AABB;
            }
            case UP: {
                return BlockPistonExtension.PISTON_EXTENSION_UP_AABB;
            }
            case NORTH: {
                return BlockPistonExtension.PISTON_EXTENSION_NORTH_AABB;
            }
            case SOUTH: {
                return BlockPistonExtension.PISTON_EXTENSION_SOUTH_AABB;
            }
            case WEST: {
                return BlockPistonExtension.PISTON_EXTENSION_WEST_AABB;
            }
            case EAST: {
                return BlockPistonExtension.PISTON_EXTENSION_EAST_AABB;
            }
        }
    }
    
    @Override
    public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getBoundingBox(worldIn, pos));
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getArmShape(state));
    }
    
    private AxisAlignedBB getArmShape(final IBlockState state) {
        final boolean flag = state.getValue((IProperty<Boolean>)BlockPistonExtension.SHORT);
        switch (state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING)) {
            default: {
                return flag ? BlockPistonExtension.SHORT_DOWN_ARM_AABB : BlockPistonExtension.DOWN_ARM_AABB;
            }
            case UP: {
                return flag ? BlockPistonExtension.SHORT_UP_ARM_AABB : BlockPistonExtension.UP_ARM_AABB;
            }
            case NORTH: {
                return flag ? BlockPistonExtension.SHORT_NORTH_ARM_AABB : BlockPistonExtension.NORTH_ARM_AABB;
            }
            case SOUTH: {
                return flag ? BlockPistonExtension.SHORT_SOUTH_ARM_AABB : BlockPistonExtension.SOUTH_ARM_AABB;
            }
            case WEST: {
                return flag ? BlockPistonExtension.SHORT_WEST_ARM_AABB : BlockPistonExtension.WEST_ARM_AABB;
            }
            case EAST: {
                return flag ? BlockPistonExtension.SHORT_EAST_ARM_AABB : BlockPistonExtension.EAST_ARM_AABB;
            }
        }
    }
    
    @Override
    @Deprecated
    public boolean isTopSolid(final IBlockState state) {
        return state.getValue((IProperty<Comparable>)BlockPistonExtension.FACING) == EnumFacing.UP;
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            final BlockPos blockpos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING).getOpposite());
            final Block block = worldIn.getBlockState(blockpos).getBlock();
            if (block == Blocks.PISTON || block == Blocks.STICKY_PISTON) {
                worldIn.setBlockToAir(blockpos);
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }
    
    @Override
    public void breakBlock(final World worldIn, BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING).getOpposite();
        pos = pos.offset(enumfacing);
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if ((iblockstate.getBlock() == Blocks.PISTON || iblockstate.getBlock() == Blocks.STICKY_PISTON) && iblockstate.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
            iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
            worldIn.setBlockToAir(pos);
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
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return false;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING);
        final BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getBlock() != Blocks.PISTON && iblockstate.getBlock() != Blocks.STICKY_PISTON) {
            worldIn.setBlockToAir(pos);
        }
        else {
            iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
        }
    }
    
    @Override
    @Deprecated
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    @Nullable
    public static EnumFacing getFacing(final int meta) {
        final int i = meta & 0x7;
        return (i > 5) ? null : EnumFacing.byIndex(i);
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack((state.getValue(BlockPistonExtension.TYPE) == EnumPistonType.STICKY) ? Blocks.STICKY_PISTON : Blocks.PISTON);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, getFacing(meta)).withProperty(BlockPistonExtension.TYPE, ((meta & 0x8) > 0) ? EnumPistonType.STICKY : EnumPistonType.DEFAULT);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING).getIndex();
        if (state.getValue(BlockPistonExtension.TYPE) == EnumPistonType.STICKY) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockPistonExtension.FACING, BlockPistonExtension.TYPE, BlockPistonExtension.SHORT });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return (face == state.getValue((IProperty<EnumFacing>)BlockPistonExtension.FACING)) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
    
    static {
        TYPE = PropertyEnum.create("type", EnumPistonType.class);
        SHORT = PropertyBool.create("short");
        PISTON_EXTENSION_EAST_AABB = new AxisAlignedBB(0.75, 0.0, 0.0, 1.0, 1.0, 1.0);
        PISTON_EXTENSION_WEST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.25, 1.0, 1.0);
        PISTON_EXTENSION_SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.75, 1.0, 1.0, 1.0);
        PISTON_EXTENSION_NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.25);
        PISTON_EXTENSION_UP_AABB = new AxisAlignedBB(0.0, 0.75, 0.0, 1.0, 1.0, 1.0);
        PISTON_EXTENSION_DOWN_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0);
        UP_ARM_AABB = new AxisAlignedBB(0.375, -0.25, 0.375, 0.625, 0.75, 0.625);
        DOWN_ARM_AABB = new AxisAlignedBB(0.375, 0.25, 0.375, 0.625, 1.25, 0.625);
        SOUTH_ARM_AABB = new AxisAlignedBB(0.375, 0.375, -0.25, 0.625, 0.625, 0.75);
        NORTH_ARM_AABB = new AxisAlignedBB(0.375, 0.375, 0.25, 0.625, 0.625, 1.25);
        EAST_ARM_AABB = new AxisAlignedBB(-0.25, 0.375, 0.375, 0.75, 0.625, 0.625);
        WEST_ARM_AABB = new AxisAlignedBB(0.25, 0.375, 0.375, 1.25, 0.625, 0.625);
        SHORT_UP_ARM_AABB = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.75, 0.625);
        SHORT_DOWN_ARM_AABB = new AxisAlignedBB(0.375, 0.25, 0.375, 0.625, 1.0, 0.625);
        SHORT_SOUTH_ARM_AABB = new AxisAlignedBB(0.375, 0.375, 0.0, 0.625, 0.625, 0.75);
        SHORT_NORTH_ARM_AABB = new AxisAlignedBB(0.375, 0.375, 0.25, 0.625, 0.625, 1.0);
        SHORT_EAST_ARM_AABB = new AxisAlignedBB(0.0, 0.375, 0.375, 0.75, 0.625, 0.625);
        SHORT_WEST_ARM_AABB = new AxisAlignedBB(0.25, 0.375, 0.375, 1.0, 0.625, 0.625);
    }
    
    public enum EnumPistonType implements IStringSerializable
    {
        DEFAULT("normal"), 
        STICKY("sticky");
        
        private final String VARIANT;
        
        private EnumPistonType(final String name) {
            this.VARIANT = name;
        }
        
        @Override
        public String toString() {
            return this.VARIANT;
        }
        
        @Override
        public String getName() {
            return this.VARIANT;
        }
    }
}
