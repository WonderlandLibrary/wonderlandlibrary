/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPane
extends Block {
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[]{new AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 1.0, 0.5625), new AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.4375, 0.5625, 1.0, 0.5625), new AxisAlignedBB(0.0, 0.0, 0.4375, 0.5625, 1.0, 1.0), new AxisAlignedBB(0.4375, 0.0, 0.0, 0.5625, 1.0, 0.5625), new AxisAlignedBB(0.4375, 0.0, 0.0, 0.5625, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 0.5625, 1.0, 0.5625), new AxisAlignedBB(0.0, 0.0, 0.0, 0.5625, 1.0, 1.0), new AxisAlignedBB(0.4375, 0.0, 0.4375, 1.0, 1.0, 0.5625), new AxisAlignedBB(0.4375, 0.0, 0.4375, 1.0, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.4375, 1.0, 1.0, 0.5625), new AxisAlignedBB(0.0, 0.0, 0.4375, 1.0, 1.0, 1.0), new AxisAlignedBB(0.4375, 0.0, 0.0, 1.0, 1.0, 0.5625), new AxisAlignedBB(0.4375, 0.0, 0.0, 1.0, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.5625), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)};
    private final boolean canDrop;

    protected BlockPane(Material materialIn, boolean canDrop) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
        this.canDrop = canDrop;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        if (!p_185477_7_) {
            state = this.getActualState(state, worldIn, pos);
        }
        BlockPane.addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BY_INDEX[0]);
        if (state.getValue(NORTH).booleanValue()) {
            BlockPane.addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BY_INDEX[BlockPane.getBoundingBoxIndex(EnumFacing.NORTH)]);
        }
        if (state.getValue(SOUTH).booleanValue()) {
            BlockPane.addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BY_INDEX[BlockPane.getBoundingBoxIndex(EnumFacing.SOUTH)]);
        }
        if (state.getValue(EAST).booleanValue()) {
            BlockPane.addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BY_INDEX[BlockPane.getBoundingBoxIndex(EnumFacing.EAST)]);
        }
        if (state.getValue(WEST).booleanValue()) {
            BlockPane.addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BY_INDEX[BlockPane.getBoundingBoxIndex(EnumFacing.WEST)]);
        }
    }

    private static int getBoundingBoxIndex(EnumFacing p_185729_0_) {
        return 1 << p_185729_0_.getHorizontalIndex();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = this.getActualState(state, source, pos);
        return AABB_BY_INDEX[BlockPane.getBoundingBoxIndex(state)];
    }

    private static int getBoundingBoxIndex(IBlockState state) {
        int i = 0;
        if (state.getValue(NORTH).booleanValue()) {
            i |= BlockPane.getBoundingBoxIndex(EnumFacing.NORTH);
        }
        if (state.getValue(EAST).booleanValue()) {
            i |= BlockPane.getBoundingBoxIndex(EnumFacing.EAST);
        }
        if (state.getValue(SOUTH).booleanValue()) {
            i |= BlockPane.getBoundingBoxIndex(EnumFacing.SOUTH);
        }
        if (state.getValue(WEST).booleanValue()) {
            i |= BlockPane.getBoundingBoxIndex(EnumFacing.WEST);
        }
        return i;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(NORTH, this.func_193393_b(worldIn, worldIn.getBlockState(pos.north()), pos.north(), EnumFacing.SOUTH)).withProperty(SOUTH, this.func_193393_b(worldIn, worldIn.getBlockState(pos.south()), pos.south(), EnumFacing.NORTH)).withProperty(WEST, this.func_193393_b(worldIn, worldIn.getBlockState(pos.west()), pos.west(), EnumFacing.EAST)).withProperty(EAST, this.func_193393_b(worldIn, worldIn.getBlockState(pos.east()), pos.east(), EnumFacing.WEST));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return !this.canDrop ? Items.field_190931_a : super.getItemDropped(state, rand, fortune);
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
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    public final boolean func_193393_b(IBlockAccess p_193393_1_, IBlockState p_193393_2_, BlockPos p_193393_3_, EnumFacing p_193393_4_) {
        Block block = p_193393_2_.getBlock();
        BlockFaceShape blockfaceshape = p_193393_2_.func_193401_d(p_193393_1_, p_193393_3_, p_193393_4_);
        return !BlockPane.func_193394_e(block) && blockfaceshape == BlockFaceShape.SOLID || blockfaceshape == BlockFaceShape.MIDDLE_POLE_THIN;
    }

    protected static boolean func_193394_e(Block p_193394_0_) {
        return p_193394_0_ instanceof BlockShulkerBox || p_193394_0_ instanceof BlockLeaves || p_193394_0_ == Blocks.BEACON || p_193394_0_ == Blocks.CAULDRON || p_193394_0_ == Blocks.GLOWSTONE || p_193394_0_ == Blocks.ICE || p_193394_0_ == Blocks.SEA_LANTERN || p_193394_0_ == Blocks.PISTON || p_193394_0_ == Blocks.STICKY_PISTON || p_193394_0_ == Blocks.PISTON_HEAD || p_193394_0_ == Blocks.MELON_BLOCK || p_193394_0_ == Blocks.PUMPKIN || p_193394_0_ == Blocks.LIT_PUMPKIN || p_193394_0_ == Blocks.BARRIER;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180: {
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
            }
            case COUNTERCLOCKWISE_90: {
                return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
            }
            case CLOCKWISE_90: {
                return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
            }
        }
        return state;
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        switch (mirrorIn) {
            case LEFT_RIGHT: {
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
            }
            case FRONT_BACK: {
                return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
            }
        }
        return super.withMirror(state, mirrorIn);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, NORTH, EAST, WEST, SOUTH);
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return p_193383_4_ != EnumFacing.UP && p_193383_4_ != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THIN : BlockFaceShape.CENTER_SMALL;
    }
}

