/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPane
extends Block {
    private final boolean canDrop;
    public static final PropertyBool WEST;
    public static final PropertyBool EAST;
    public static final PropertyBool NORTH;
    public static final PropertyBool SOUTH;

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos blockPos, IBlockState iBlockState, AxisAlignedBB axisAlignedBB, List<AxisAlignedBB> list, Entity entity) {
        boolean bl = this.canPaneConnectToBlock(world.getBlockState(blockPos.north()).getBlock());
        boolean bl2 = this.canPaneConnectToBlock(world.getBlockState(blockPos.south()).getBlock());
        boolean bl3 = this.canPaneConnectToBlock(world.getBlockState(blockPos.west()).getBlock());
        boolean bl4 = this.canPaneConnectToBlock(world.getBlockState(blockPos.east()).getBlock());
        if ((!bl3 || !bl4) && (bl3 || bl4 || bl || bl2)) {
            if (bl3) {
                this.setBlockBounds(0.0f, 0.0f, 0.4375f, 0.5f, 1.0f, 0.5625f);
                super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
            } else if (bl4) {
                this.setBlockBounds(0.5f, 0.0f, 0.4375f, 1.0f, 1.0f, 0.5625f);
                super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
            }
        } else {
            this.setBlockBounds(0.0f, 0.0f, 0.4375f, 1.0f, 1.0f, 0.5625f);
            super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
        }
        if ((!bl || !bl2) && (bl3 || bl4 || bl || bl2)) {
            if (bl) {
                this.setBlockBounds(0.4375f, 0.0f, 0.0f, 0.5625f, 1.0f, 0.5f);
                super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
            } else if (bl2) {
                this.setBlockBounds(0.4375f, 0.0f, 0.5f, 0.5625f, 1.0f, 1.0f);
                super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
            }
        } else {
            this.setBlockBounds(0.4375f, 0.0f, 0.0f, 0.5625f, 1.0f, 1.0f);
            super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
        }
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    static {
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        float f = 0.4375f;
        float f2 = 0.5625f;
        float f3 = 0.4375f;
        float f4 = 0.5625f;
        boolean bl = this.canPaneConnectToBlock(iBlockAccess.getBlockState(blockPos.north()).getBlock());
        boolean bl2 = this.canPaneConnectToBlock(iBlockAccess.getBlockState(blockPos.south()).getBlock());
        boolean bl3 = this.canPaneConnectToBlock(iBlockAccess.getBlockState(blockPos.west()).getBlock());
        boolean bl4 = this.canPaneConnectToBlock(iBlockAccess.getBlockState(blockPos.east()).getBlock());
        if ((!bl3 || !bl4) && (bl3 || bl4 || bl || bl2)) {
            if (bl3) {
                f = 0.0f;
            } else if (bl4) {
                f2 = 1.0f;
            }
        } else {
            f = 0.0f;
            f2 = 1.0f;
        }
        if ((!bl || !bl2) && (bl3 || bl4 || bl || bl2)) {
            if (bl) {
                f3 = 0.0f;
            } else if (bl2) {
                f4 = 1.0f;
            }
        } else {
            f3 = 0.0f;
            f4 = 1.0f;
        }
        this.setBlockBounds(f, 0.0f, f3, f2, 1.0f, f4);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        return iBlockState.withProperty(NORTH, this.canPaneConnectToBlock(iBlockAccess.getBlockState(blockPos.north()).getBlock())).withProperty(SOUTH, this.canPaneConnectToBlock(iBlockAccess.getBlockState(blockPos.south()).getBlock())).withProperty(WEST, this.canPaneConnectToBlock(iBlockAccess.getBlockState(blockPos.west()).getBlock())).withProperty(EAST, this.canPaneConnectToBlock(iBlockAccess.getBlockState(blockPos.east()).getBlock()));
    }

    protected BlockPane(Material material, boolean bl) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
        this.canDrop = bl;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return !this.canDrop ? null : super.getItemDropped(iBlockState, random, n);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, NORTH, EAST, WEST, SOUTH);
    }

    public final boolean canPaneConnectToBlock(Block block) {
        return block.isFullBlock() || block == this || block == Blocks.glass || block == Blocks.stained_glass || block == Blocks.stained_glass_pane || block instanceof BlockPane;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return iBlockAccess.getBlockState(blockPos).getBlock() == this ? false : super.shouldSideBeRendered(iBlockAccess, blockPos, enumFacing);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}

