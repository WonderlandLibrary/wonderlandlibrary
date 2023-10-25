/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoor
extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum<EnumHingePosition> HINGE = PropertyEnum.create("hinge", EnumHingePosition.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyEnum<EnumDoorHalf> HALF = PropertyEnum.create("half", EnumDoorHalf.class);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.1875);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.8125, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.8125, 0.0, 0.0, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.1875, 1.0, 1.0);

    protected BlockDoor(Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, false).withProperty(HINGE, EnumHingePosition.LEFT).withProperty(POWERED, false).withProperty(HALF, EnumDoorHalf.LOWER));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = state.getActualState(source, pos);
        EnumFacing enumfacing = state.getValue(FACING);
        boolean flag = state.getValue(OPEN) == false;
        boolean flag1 = state.getValue(HINGE) == EnumHingePosition.RIGHT;
        switch (enumfacing) {
            default: {
                return flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);
            }
            case SOUTH: {
                return flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);
            }
            case WEST: {
                return flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);
            }
            case NORTH: 
        }
        return flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
    }

    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal((this.getUnlocalizedName() + ".name").replaceAll("tile", "item"));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return BlockDoor.isOpen(BlockDoor.combineMetadata(worldIn, pos));
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    private int getCloseSound() {
        return this.blockMaterial == Material.IRON ? 1011 : 1012;
    }

    private int getOpenSound() {
        return this.blockMaterial == Material.IRON ? 1005 : 1006;
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
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
        return state.getBlock() == Blocks.DARK_OAK_DOOR ? BlockPlanks.EnumType.DARK_OAK.getMapColor() : super.getMapColor(state, p_180659_2_, p_180659_3_);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        IBlockState iblockstate;
        if (this.blockMaterial == Material.IRON) {
            return false;
        }
        BlockPos blockpos = state.getValue(HALF) == EnumDoorHalf.LOWER ? pos : pos.down();
        IBlockState iBlockState = iblockstate = pos.equals(blockpos) ? state : worldIn.getBlockState(blockpos);
        if (iblockstate.getBlock() != this) {
            return false;
        }
        state = iblockstate.cycleProperty(OPEN);
        worldIn.setBlockState(blockpos, state, 10);
        worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
        worldIn.playEvent(playerIn, state.getValue(OPEN) != false ? this.getOpenSound() : this.getCloseSound(), pos, 0);
        return true;
    }

    public void toggleDoor(World worldIn, BlockPos pos, boolean open) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this) {
            IBlockState iblockstate1;
            BlockPos blockpos = iblockstate.getValue(HALF) == EnumDoorHalf.LOWER ? pos : pos.down();
            IBlockState iBlockState = iblockstate1 = pos == blockpos ? iblockstate : worldIn.getBlockState(blockpos);
            if (iblockstate1.getBlock() == this && iblockstate1.getValue(OPEN) != open) {
                worldIn.setBlockState(blockpos, iblockstate1.withProperty(OPEN, open), 10);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.playEvent(null, open ? this.getOpenSound() : this.getCloseSound(), pos, 0);
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        if (state.getValue(HALF) == EnumDoorHalf.UPPER) {
            BlockPos blockpos = pos.down();
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getBlock() != this) {
                worldIn.setBlockToAir(pos);
            } else if (blockIn != this) {
                iblockstate.neighborChanged(worldIn, blockpos, blockIn, p_189540_5_);
            }
        } else {
            boolean flag1 = false;
            BlockPos blockpos1 = pos.up();
            IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);
            if (iblockstate1.getBlock() != this) {
                worldIn.setBlockToAir(pos);
                flag1 = true;
            }
            if (!worldIn.getBlockState(pos.down()).isFullyOpaque()) {
                worldIn.setBlockToAir(pos);
                flag1 = true;
                if (iblockstate1.getBlock() == this) {
                    worldIn.setBlockToAir(blockpos1);
                }
            }
            if (flag1) {
                if (!worldIn.isRemote) {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                }
            } else {
                boolean flag;
                boolean bl = flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos1);
                if (blockIn != this && (flag || blockIn.getDefaultState().canProvidePower()) && flag != iblockstate1.getValue(POWERED)) {
                    worldIn.setBlockState(blockpos1, iblockstate1.withProperty(POWERED, flag), 2);
                    if (flag != state.getValue(OPEN)) {
                        worldIn.setBlockState(pos, state.withProperty(OPEN, flag), 2);
                        worldIn.markBlockRangeForRenderUpdate(pos, pos);
                        worldIn.playEvent(null, flag ? this.getOpenSound() : this.getCloseSound(), pos, 0);
                    }
                }
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(HALF) == EnumDoorHalf.UPPER ? Items.field_190931_a : this.getItem();
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        if (pos.getY() >= 255) {
            return false;
        }
        return worldIn.getBlockState(pos.down()).isFullyOpaque() && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.DESTROY;
    }

    public static int combineMetadata(IBlockAccess worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        int i = iblockstate.getBlock().getMetaFromState(iblockstate);
        boolean flag = BlockDoor.isTop(i);
        IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
        int j = iblockstate1.getBlock().getMetaFromState(iblockstate1);
        int k = flag ? j : i;
        IBlockState iblockstate2 = worldIn.getBlockState(pos.up());
        int l = iblockstate2.getBlock().getMetaFromState(iblockstate2);
        int i1 = flag ? i : l;
        boolean flag1 = (i1 & 1) != 0;
        boolean flag2 = (i1 & 2) != 0;
        return BlockDoor.removeHalfBit(k) | (flag ? 8 : 0) | (flag1 ? 16 : 0) | (flag2 ? 32 : 0);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
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
        return this == Blocks.DARK_OAK_DOOR ? Items.DARK_OAK_DOOR : Items.OAK_DOOR;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        BlockPos blockpos = pos.down();
        BlockPos blockpos1 = pos.up();
        if (player.capabilities.isCreativeMode && state.getValue(HALF) == EnumDoorHalf.UPPER && worldIn.getBlockState(blockpos).getBlock() == this) {
            worldIn.setBlockToAir(blockpos);
        }
        if (state.getValue(HALF) == EnumDoorHalf.LOWER && worldIn.getBlockState(blockpos1).getBlock() == this) {
            if (player.capabilities.isCreativeMode) {
                worldIn.setBlockToAir(pos);
            }
            worldIn.setBlockToAir(blockpos1);
        }
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (state.getValue(HALF) == EnumDoorHalf.LOWER) {
            IBlockState iblockstate = worldIn.getBlockState(pos.up());
            if (iblockstate.getBlock() == this) {
                state = state.withProperty(HINGE, iblockstate.getValue(HINGE)).withProperty(POWERED, iblockstate.getValue(POWERED));
            }
        } else {
            IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
            if (iblockstate1.getBlock() == this) {
                state = state.withProperty(FACING, iblockstate1.getValue(FACING)).withProperty(OPEN, iblockstate1.getValue(OPEN));
            }
        }
        return state;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.getValue(HALF) != EnumDoorHalf.LOWER ? state : state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return mirrorIn == Mirror.NONE ? state : state.withRotation(mirrorIn.toRotation(state.getValue(FACING))).cycleProperty(HINGE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(HALF, EnumDoorHalf.UPPER).withProperty(HINGE, (meta & 1) > 0 ? EnumHingePosition.RIGHT : EnumHingePosition.LEFT).withProperty(POWERED, (meta & 2) > 0) : this.getDefaultState().withProperty(HALF, EnumDoorHalf.LOWER).withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3).rotateYCCW()).withProperty(OPEN, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(HALF) == EnumDoorHalf.UPPER) {
            i |= 8;
            if (state.getValue(HINGE) == EnumHingePosition.RIGHT) {
                i |= 1;
            }
            if (state.getValue(POWERED).booleanValue()) {
                i |= 2;
            }
        } else {
            i |= state.getValue(FACING).rotateY().getHorizontalIndex();
            if (state.getValue(OPEN).booleanValue()) {
                i |= 4;
            }
        }
        return i;
    }

    protected static int removeHalfBit(int meta) {
        return meta & 7;
    }

    public static boolean isOpen(IBlockAccess worldIn, BlockPos pos) {
        return BlockDoor.isOpen(BlockDoor.combineMetadata(worldIn, pos));
    }

    public static EnumFacing getFacing(IBlockAccess worldIn, BlockPos pos) {
        return BlockDoor.getFacing(BlockDoor.combineMetadata(worldIn, pos));
    }

    public static EnumFacing getFacing(int combinedMeta) {
        return EnumFacing.byHorizontalIndex(combinedMeta & 3).rotateYCCW();
    }

    protected static boolean isOpen(int combinedMeta) {
        return (combinedMeta & 4) != 0;
    }

    protected static boolean isTop(int meta) {
        return (meta & 8) != 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, HALF, FACING, OPEN, HINGE, POWERED);
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }

    public static enum EnumHingePosition implements IStringSerializable
    {
        LEFT,
        RIGHT;


        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this == LEFT ? "left" : "right";
        }
    }

    public static enum EnumDoorHalf implements IStringSerializable
    {
        UPPER,
        LOWER;


        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this == UPPER ? "upper" : "lower";
        }
    }
}

