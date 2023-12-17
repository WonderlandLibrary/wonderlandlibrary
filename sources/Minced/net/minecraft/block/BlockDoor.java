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
import net.minecraft.item.ItemStack;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockDoor extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyBool OPEN;
    public static final PropertyEnum<EnumHingePosition> HINGE;
    public static final PropertyBool POWERED;
    public static final PropertyEnum<EnumDoorHalf> HALF;
    protected static final AxisAlignedBB SOUTH_AABB;
    protected static final AxisAlignedBB NORTH_AABB;
    protected static final AxisAlignedBB WEST_AABB;
    protected static final AxisAlignedBB EAST_AABB;
    
    protected BlockDoor(final Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockDoor.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockDoor.OPEN, false).withProperty(BlockDoor.HINGE, EnumHingePosition.LEFT).withProperty((IProperty<Comparable>)BlockDoor.POWERED, false).withProperty(BlockDoor.HALF, EnumDoorHalf.LOWER));
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, final IBlockAccess source, final BlockPos pos) {
        state = state.getActualState(source, pos);
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockDoor.FACING);
        final boolean flag = !state.getValue((IProperty<Boolean>)BlockDoor.OPEN);
        final boolean flag2 = state.getValue(BlockDoor.HINGE) == EnumHingePosition.RIGHT;
        switch (enumfacing) {
            default: {
                return flag ? BlockDoor.EAST_AABB : (flag2 ? BlockDoor.NORTH_AABB : BlockDoor.SOUTH_AABB);
            }
            case SOUTH: {
                return flag ? BlockDoor.SOUTH_AABB : (flag2 ? BlockDoor.EAST_AABB : BlockDoor.WEST_AABB);
            }
            case WEST: {
                return flag ? BlockDoor.WEST_AABB : (flag2 ? BlockDoor.SOUTH_AABB : BlockDoor.NORTH_AABB);
            }
            case NORTH: {
                return flag ? BlockDoor.NORTH_AABB : (flag2 ? BlockDoor.WEST_AABB : BlockDoor.EAST_AABB);
            }
        }
    }
    
    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal((this.getTranslationKey() + ".name").replaceAll("tile", "item"));
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return isOpen(combineMetadata(worldIn, pos));
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    private int getCloseSound() {
        return (this.material == Material.IRON) ? 1011 : 1012;
    }
    
    private int getOpenSound() {
        return (this.material == Material.IRON) ? 1005 : 1006;
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (state.getBlock() == Blocks.IRON_DOOR) {
            return MapColor.IRON;
        }
        if (state.getBlock() == Blocks.OAK_DOOR) {
            return BlockPlanks.EnumType.OAK.getMapColor();
        }
        if (state.getBlock() == Blocks.SPRUCE_DOOR) {
            return BlockPlanks.EnumType.SPRUCE.getMapColor();
        }
        if (state.getBlock() == Blocks.BIRCH_DOOR) {
            return BlockPlanks.EnumType.BIRCH.getMapColor();
        }
        if (state.getBlock() == Blocks.JUNGLE_DOOR) {
            return BlockPlanks.EnumType.JUNGLE.getMapColor();
        }
        if (state.getBlock() == Blocks.ACACIA_DOOR) {
            return BlockPlanks.EnumType.ACACIA.getMapColor();
        }
        return (state.getBlock() == Blocks.DARK_OAK_DOOR) ? BlockPlanks.EnumType.DARK_OAK.getMapColor() : super.getMapColor(state, worldIn, pos);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (this.material == Material.IRON) {
            return false;
        }
        final BlockPos blockpos = (state.getValue(BlockDoor.HALF) == EnumDoorHalf.LOWER) ? pos : pos.down();
        final IBlockState iblockstate = pos.equals(blockpos) ? state : worldIn.getBlockState(blockpos);
        if (iblockstate.getBlock() != this) {
            return false;
        }
        state = iblockstate.cycleProperty((IProperty<Comparable>)BlockDoor.OPEN);
        worldIn.setBlockState(blockpos, state, 10);
        worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
        worldIn.playEvent(playerIn, ((boolean)state.getValue((IProperty<Boolean>)BlockDoor.OPEN)) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
        return true;
    }
    
    public void toggleDoor(final World worldIn, final BlockPos pos, final boolean open) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this) {
            final BlockPos blockpos = (iblockstate.getValue(BlockDoor.HALF) == EnumDoorHalf.LOWER) ? pos : pos.down();
            final IBlockState iblockstate2 = (pos == blockpos) ? iblockstate : worldIn.getBlockState(blockpos);
            if (iblockstate2.getBlock() == this && iblockstate2.getValue((IProperty<Boolean>)BlockDoor.OPEN) != open) {
                worldIn.setBlockState(blockpos, iblockstate2.withProperty((IProperty<Comparable>)BlockDoor.OPEN, open), 10);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.playEvent(null, open ? this.getOpenSound() : this.getCloseSound(), pos, 0);
            }
        }
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (state.getValue(BlockDoor.HALF) == EnumDoorHalf.UPPER) {
            final BlockPos blockpos = pos.down();
            final IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getBlock() != this) {
                worldIn.setBlockToAir(pos);
            }
            else if (blockIn != this) {
                iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
            }
        }
        else {
            boolean flag1 = false;
            final BlockPos blockpos2 = pos.up();
            final IBlockState iblockstate2 = worldIn.getBlockState(blockpos2);
            if (iblockstate2.getBlock() != this) {
                worldIn.setBlockToAir(pos);
                flag1 = true;
            }
            if (!worldIn.getBlockState(pos.down()).isTopSolid()) {
                worldIn.setBlockToAir(pos);
                flag1 = true;
                if (iblockstate2.getBlock() == this) {
                    worldIn.setBlockToAir(blockpos2);
                }
            }
            if (flag1) {
                if (!worldIn.isRemote) {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                }
            }
            else {
                final boolean flag2 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos2);
                if (blockIn != this && (flag2 || blockIn.getDefaultState().canProvidePower()) && flag2 != iblockstate2.getValue((IProperty<Boolean>)BlockDoor.POWERED)) {
                    worldIn.setBlockState(blockpos2, iblockstate2.withProperty((IProperty<Comparable>)BlockDoor.POWERED, flag2), 2);
                    if (flag2 != state.getValue((IProperty<Boolean>)BlockDoor.OPEN)) {
                        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockDoor.OPEN, flag2), 2);
                        worldIn.markBlockRangeForRenderUpdate(pos, pos);
                        worldIn.playEvent(null, flag2 ? this.getOpenSound() : this.getCloseSound(), pos, 0);
                    }
                }
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return (state.getValue(BlockDoor.HALF) == EnumDoorHalf.UPPER) ? Items.AIR : this.getItem();
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return pos.getY() < 255 && worldIn.getBlockState(pos.down()).isTopSolid() && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
    }
    
    @Override
    @Deprecated
    public EnumPushReaction getPushReaction(final IBlockState state) {
        return EnumPushReaction.DESTROY;
    }
    
    public static int combineMetadata(final IBlockAccess worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final int i = iblockstate.getBlock().getMetaFromState(iblockstate);
        final boolean flag = isTop(i);
        final IBlockState iblockstate2 = worldIn.getBlockState(pos.down());
        final int j = iblockstate2.getBlock().getMetaFromState(iblockstate2);
        final int k = flag ? j : i;
        final IBlockState iblockstate3 = worldIn.getBlockState(pos.up());
        final int l = iblockstate3.getBlock().getMetaFromState(iblockstate3);
        final int i2 = flag ? i : l;
        final boolean flag2 = (i2 & 0x1) != 0x0;
        final boolean flag3 = (i2 & 0x2) != 0x0;
        return removeHalfBit(k) | (flag ? 8 : 0) | (flag2 ? 16 : 0) | (flag3 ? 32 : 0);
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(this.getItem());
    }
    
    private Item getItem() {
        if (this == Blocks.IRON_DOOR) {
            return Items.IRON_DOOR;
        }
        if (this == Blocks.SPRUCE_DOOR) {
            return Items.SPRUCE_DOOR;
        }
        if (this == Blocks.BIRCH_DOOR) {
            return Items.BIRCH_DOOR;
        }
        if (this == Blocks.JUNGLE_DOOR) {
            return Items.JUNGLE_DOOR;
        }
        if (this == Blocks.ACACIA_DOOR) {
            return Items.ACACIA_DOOR;
        }
        return (this == Blocks.DARK_OAK_DOOR) ? Items.DARK_OAK_DOOR : Items.OAK_DOOR;
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        final BlockPos blockpos = pos.down();
        final BlockPos blockpos2 = pos.up();
        if (player.capabilities.isCreativeMode && state.getValue(BlockDoor.HALF) == EnumDoorHalf.UPPER && worldIn.getBlockState(blockpos).getBlock() == this) {
            worldIn.setBlockToAir(blockpos);
        }
        if (state.getValue(BlockDoor.HALF) == EnumDoorHalf.LOWER && worldIn.getBlockState(blockpos2).getBlock() == this) {
            if (player.capabilities.isCreativeMode) {
                worldIn.setBlockToAir(pos);
            }
            worldIn.setBlockToAir(blockpos2);
        }
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (state.getValue(BlockDoor.HALF) == EnumDoorHalf.LOWER) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.up());
            if (iblockstate.getBlock() == this) {
                state = state.withProperty(BlockDoor.HINGE, (Comparable)iblockstate.getValue((IProperty<V>)BlockDoor.HINGE)).withProperty((IProperty<Comparable>)BlockDoor.POWERED, (Comparable)iblockstate.getValue((IProperty<V>)BlockDoor.POWERED));
            }
        }
        else {
            final IBlockState iblockstate2 = worldIn.getBlockState(pos.down());
            if (iblockstate2.getBlock() == this) {
                state = state.withProperty((IProperty<Comparable>)BlockDoor.FACING, (Comparable)iblockstate2.getValue((IProperty<V>)BlockDoor.FACING)).withProperty((IProperty<Comparable>)BlockDoor.OPEN, (Comparable)iblockstate2.getValue((IProperty<V>)BlockDoor.OPEN));
            }
        }
        return state;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return (state.getValue(BlockDoor.HALF) != EnumDoorHalf.LOWER) ? state : state.withProperty((IProperty<Comparable>)BlockDoor.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockDoor.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return (mirrorIn == Mirror.NONE) ? state : state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockDoor.FACING))).cycleProperty(BlockDoor.HINGE);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return ((meta & 0x8) > 0) ? this.getDefaultState().withProperty(BlockDoor.HALF, EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGE, ((meta & 0x1) > 0) ? EnumHingePosition.RIGHT : EnumHingePosition.LEFT).withProperty((IProperty<Comparable>)BlockDoor.POWERED, (meta & 0x2) > 0) : this.getDefaultState().withProperty(BlockDoor.HALF, EnumDoorHalf.LOWER).withProperty((IProperty<Comparable>)BlockDoor.FACING, EnumFacing.byHorizontalIndex(meta & 0x3).rotateYCCW()).withProperty((IProperty<Comparable>)BlockDoor.OPEN, (meta & 0x4) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        if (state.getValue(BlockDoor.HALF) == EnumDoorHalf.UPPER) {
            i |= 0x8;
            if (state.getValue(BlockDoor.HINGE) == EnumHingePosition.RIGHT) {
                i |= 0x1;
            }
            if (state.getValue((IProperty<Boolean>)BlockDoor.POWERED)) {
                i |= 0x2;
            }
        }
        else {
            i |= state.getValue((IProperty<EnumFacing>)BlockDoor.FACING).rotateY().getHorizontalIndex();
            if (state.getValue((IProperty<Boolean>)BlockDoor.OPEN)) {
                i |= 0x4;
            }
        }
        return i;
    }
    
    protected static int removeHalfBit(final int meta) {
        return meta & 0x7;
    }
    
    public static boolean isOpen(final IBlockAccess worldIn, final BlockPos pos) {
        return isOpen(combineMetadata(worldIn, pos));
    }
    
    public static EnumFacing getFacing(final IBlockAccess worldIn, final BlockPos pos) {
        return getFacing(combineMetadata(worldIn, pos));
    }
    
    public static EnumFacing getFacing(final int combinedMeta) {
        return EnumFacing.byHorizontalIndex(combinedMeta & 0x3).rotateYCCW();
    }
    
    protected static boolean isOpen(final int combinedMeta) {
        return (combinedMeta & 0x4) != 0x0;
    }
    
    protected static boolean isTop(final int meta) {
        return (meta & 0x8) != 0x0;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockDoor.HALF, BlockDoor.FACING, BlockDoor.OPEN, BlockDoor.HINGE, BlockDoor.POWERED });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        FACING = BlockHorizontal.FACING;
        OPEN = PropertyBool.create("open");
        HINGE = PropertyEnum.create("hinge", EnumHingePosition.class);
        POWERED = PropertyBool.create("powered");
        HALF = PropertyEnum.create("half", EnumDoorHalf.class);
        SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.1875);
        NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.8125, 1.0, 1.0, 1.0);
        WEST_AABB = new AxisAlignedBB(0.8125, 0.0, 0.0, 1.0, 1.0, 1.0);
        EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.1875, 1.0, 1.0);
    }
    
    public enum EnumDoorHalf implements IStringSerializable
    {
        UPPER, 
        LOWER;
        
        @Override
        public String toString() {
            return this.getName();
        }
        
        @Override
        public String getName() {
            return (this == EnumDoorHalf.UPPER) ? "upper" : "lower";
        }
    }
    
    public enum EnumHingePosition implements IStringSerializable
    {
        LEFT, 
        RIGHT;
        
        @Override
        public String toString() {
            return this.getName();
        }
        
        @Override
        public String getName() {
            return (this == EnumHingePosition.LEFT) ? "left" : "right";
        }
    }
}
