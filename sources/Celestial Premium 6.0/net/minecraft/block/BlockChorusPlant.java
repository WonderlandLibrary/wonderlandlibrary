/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChorusPlant
extends Block {
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    protected BlockChorusPlant() {
        super(Material.PLANTS, MapColor.PURPLE);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(UP, false).withProperty(DOWN, false));
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos.down()).getBlock();
        Block block1 = worldIn.getBlockState(pos.up()).getBlock();
        Block block2 = worldIn.getBlockState(pos.north()).getBlock();
        Block block3 = worldIn.getBlockState(pos.east()).getBlock();
        Block block4 = worldIn.getBlockState(pos.south()).getBlock();
        Block block5 = worldIn.getBlockState(pos.west()).getBlock();
        return state.withProperty(DOWN, block == this || block == Blocks.CHORUS_FLOWER || block == Blocks.END_STONE).withProperty(UP, block1 == this || block1 == Blocks.CHORUS_FLOWER).withProperty(NORTH, block2 == this || block2 == Blocks.CHORUS_FLOWER).withProperty(EAST, block3 == this || block3 == Blocks.CHORUS_FLOWER).withProperty(SOUTH, block4 == this || block4 == Blocks.CHORUS_FLOWER).withProperty(WEST, block5 == this || block5 == Blocks.CHORUS_FLOWER);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = state.getActualState(source, pos);
        float f = 0.1875f;
        float f1 = state.getValue(WEST) != false ? 0.0f : 0.1875f;
        float f2 = state.getValue(DOWN) != false ? 0.0f : 0.1875f;
        float f3 = state.getValue(NORTH) != false ? 0.0f : 0.1875f;
        float f4 = state.getValue(EAST) != false ? 1.0f : 0.8125f;
        float f5 = state.getValue(UP) != false ? 1.0f : 0.8125f;
        float f6 = state.getValue(SOUTH) != false ? 1.0f : 0.8125f;
        return new AxisAlignedBB(f1, f2, f3, f4, f5, f6);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        if (!p_185477_7_) {
            state = state.getActualState(worldIn, pos);
        }
        float f = 0.1875f;
        float f1 = 0.8125f;
        BlockChorusPlant.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875, 0.1875, 0.1875, 0.8125, 0.8125, 0.8125));
        if (state.getValue(WEST).booleanValue()) {
            BlockChorusPlant.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.0, 0.1875, 0.1875, 0.1875, 0.8125, 0.8125));
        }
        if (state.getValue(EAST).booleanValue()) {
            BlockChorusPlant.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.8125, 0.1875, 0.1875, 1.0, 0.8125, 0.8125));
        }
        if (state.getValue(UP).booleanValue()) {
            BlockChorusPlant.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875, 0.8125, 0.1875, 0.8125, 1.0, 0.8125));
        }
        if (state.getValue(DOWN).booleanValue()) {
            BlockChorusPlant.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875, 0.0, 0.1875, 0.8125, 0.1875, 0.8125));
        }
        if (state.getValue(NORTH).booleanValue()) {
            BlockChorusPlant.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875, 0.1875, 0.0, 0.8125, 0.8125, 0.1875));
        }
        if (state.getValue(SOUTH).booleanValue()) {
            BlockChorusPlant.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875, 0.1875, 0.8125, 0.8125, 0.8125, 1.0));
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.CHORUS_FRUIT;
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(2);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) ? this.canSurviveAt(worldIn, pos) : false;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        if (!this.canSurviveAt(worldIn, pos)) {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    public boolean canSurviveAt(World wordIn, BlockPos pos) {
        boolean flag = wordIn.isAirBlock(pos.up());
        boolean flag1 = wordIn.isAirBlock(pos.down());
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.offset(enumfacing);
            Block block = wordIn.getBlockState(blockpos).getBlock();
            if (block != this) continue;
            if (!flag && !flag1) {
                return false;
            }
            Block block1 = wordIn.getBlockState(blockpos.down()).getBlock();
            if (block1 != this && block1 != Blocks.END_STONE) continue;
            return true;
        }
        Block block2 = wordIn.getBlockState(pos.down()).getBlock();
        return block2 == this || block2 == Blocks.END_STONE;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        Block block = blockAccess.getBlockState(pos.offset(side)).getBlock();
        return block != this && block != Blocks.CHORUS_FLOWER && (side != EnumFacing.DOWN || block != Blocks.END_STONE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }
}

