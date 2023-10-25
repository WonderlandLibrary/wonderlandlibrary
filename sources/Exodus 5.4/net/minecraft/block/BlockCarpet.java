/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCarpet
extends Block {
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(n));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    private boolean canBlockStay(World world, BlockPos blockPos) {
        return !world.isAirBlock(blockPos.down());
    }

    protected void setBlockBoundsFromMeta(int n) {
        int n2 = 0;
        float f = (float)(1 * (1 + n2)) / 16.0f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, f, 1.0f);
    }

    protected BlockCarpet() {
        super(Material.carpet);
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBoundsFromMeta(0);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        this.setBlockBoundsFromMeta(0);
    }

    private boolean checkForDrop(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (!this.canBlockStay(world, blockPos)) {
            this.dropBlockAsItem(world, blockPos, iBlockState, 0);
            world.setBlockToAir(blockPos);
            return false;
        }
        return true;
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBoundsFromMeta(0);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, COLOR);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return super.canPlaceBlockAt(world, blockPos) && this.canBlockStay(world, blockPos);
    }

    @Override
    public MapColor getMapColor(IBlockState iBlockState) {
        return iBlockState.getValue(COLOR).getMapColor();
    }

    @Override
    public int damageDropped(IBlockState iBlockState) {
        return iBlockState.getValue(COLOR).getMetadata();
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        int n = 0;
        while (n < 16) {
            list.add(new ItemStack(item, 1, n));
            ++n;
        }
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(COLOR).getMetadata();
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return enumFacing == EnumFacing.UP ? true : super.shouldSideBeRendered(iBlockAccess, blockPos, enumFacing);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        this.checkForDrop(world, blockPos, iBlockState);
    }
}

