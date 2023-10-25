/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockBush
extends Block {
    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        return null;
    }

    protected void checkAndDropBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (!this.canBlockStay(world, blockPos, iBlockState)) {
            this.dropBlockAsItem(world, blockPos, iBlockState, 0);
            world.setBlockState(blockPos, Blocks.air.getDefaultState(), 3);
        }
    }

    protected boolean canPlaceBlockOn(Block block) {
        return block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland;
    }

    protected BlockBush(Material material, MapColor mapColor) {
        super(material, mapColor);
        this.setTickRandomly(true);
        float f = 0.2f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f * 3.0f, 0.5f + f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        this.checkAndDropBlock(world, blockPos, iBlockState);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        super.onNeighborBlockChange(world, blockPos, iBlockState, block);
        this.checkAndDropBlock(world, blockPos, iBlockState);
    }

    protected BlockBush() {
        this(Material.plants);
    }

    protected BlockBush(Material material) {
        this(material, material.getMaterialMapColor());
    }

    public boolean canBlockStay(World world, BlockPos blockPos, IBlockState iBlockState) {
        return this.canPlaceBlockOn(world.getBlockState(blockPos.down()).getBlock());
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return super.canPlaceBlockAt(world, blockPos) && this.canPlaceBlockOn(world.getBlockState(blockPos.down()).getBlock());
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
}

