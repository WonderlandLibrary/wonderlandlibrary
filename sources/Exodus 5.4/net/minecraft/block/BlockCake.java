/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCake
extends Block {
    public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        float f = 0.0625f;
        float f2 = (float)(1 + iBlockState.getValue(BITES) * 2) / 16.0f;
        float f3 = 0.5f;
        return new AxisAlignedBB((float)blockPos.getX() + f2, blockPos.getY(), (float)blockPos.getZ() + f, (float)(blockPos.getX() + 1) - f, (float)blockPos.getY() + f3, (float)(blockPos.getZ() + 1) - f);
    }

    @Override
    public void onBlockClicked(World world, BlockPos blockPos, EntityPlayer entityPlayer) {
        this.eatCake(world, blockPos, world.getBlockState(blockPos), entityPlayer);
    }

    private boolean canBlockStay(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos.down()).getBlock().getMaterial().isSolid();
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (!this.canBlockStay(world, blockPos)) {
            world.setBlockToAir(blockPos);
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(BITES);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, BITES);
    }

    private void eatCake(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer) {
        if (entityPlayer.canEat(false)) {
            entityPlayer.triggerAchievement(StatList.field_181724_H);
            entityPlayer.getFoodStats().addStats(2, 0.1f);
            int n = iBlockState.getValue(BITES);
            if (n < 6) {
                world.setBlockState(blockPos, iBlockState.withProperty(BITES, n + 1), 3);
            } else {
                world.setBlockToAir(blockPos);
            }
        }
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float f = 0.0625f;
        float f2 = 0.5f;
        this.setBlockBounds(f, 0.0f, f, 1.0f - f, f2, 1.0f - f);
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(BITES, n);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return super.canPlaceBlockAt(world, blockPos) ? this.canBlockStay(world, blockPos) : false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        float f = 0.0625f;
        float f2 = (float)(1 + iBlockAccess.getBlockState(blockPos).getValue(BITES) * 2) / 16.0f;
        float f3 = 0.5f;
        this.setBlockBounds(f2, 0.0f, f, 1.0f - f, f3, 1.0f - f);
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos) {
        return (7 - world.getBlockState(blockPos).getValue(BITES)) * 2;
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return null;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos blockPos) {
        return this.getCollisionBoundingBox(world, blockPos, world.getBlockState(blockPos));
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Items.cake;
    }

    protected BlockCake() {
        super(Material.cake);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BITES, 0));
        this.setTickRandomly(true);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        this.eatCake(world, blockPos, iBlockState, entityPlayer);
        return true;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
}

