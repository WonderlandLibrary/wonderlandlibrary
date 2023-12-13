/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCake
extends Block {
    public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);
    private static final String __OBFID = "CL_00000211";

    protected BlockCake() {
        super(Material.cake);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BITES, Integer.valueOf(0)));
        this.setTickRandomly(true);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        float var3 = 0.0625f;
        float var4 = (float)(1 + (Integer)access.getBlockState(pos).getValue(BITES) * 2) / 16.0f;
        float var5 = 0.5f;
        this.setBlockBounds(var4, 0.0f, var3, 1.0f - var3, var5, 1.0f - var3);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float var1 = 0.0625f;
        float var2 = 0.5f;
        this.setBlockBounds(var1, 0.0f, var1, 1.0f - var1, var2, 1.0f - var1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        float var4 = 0.0625f;
        float var5 = (float)(1 + (Integer)state.getValue(BITES) * 2) / 16.0f;
        float var6 = 0.5f;
        return new AxisAlignedBB((float)pos.getX() + var5, pos.getY(), (float)pos.getZ() + var4, (float)(pos.getX() + 1) - var4, (float)pos.getY() + var6, (float)(pos.getZ() + 1) - var4);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        this.eatCake(worldIn, pos, state, playerIn);
        return true;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        this.eatCake(worldIn, pos, worldIn.getBlockState(pos), playerIn);
    }

    private void eatCake(World worldIn, BlockPos p_180682_2_, IBlockState p_180682_3_, EntityPlayer p_180682_4_) {
        if (p_180682_4_.canEat(false)) {
            p_180682_4_.getFoodStats().addStats(2, 0.1f);
            int var5 = (Integer)p_180682_3_.getValue(BITES);
            if (var5 < 6) {
                worldIn.setBlockState(p_180682_2_, p_180682_3_.withProperty(BITES, Integer.valueOf(var5 + 1)), 3);
            } else {
                worldIn.setBlockToAir(p_180682_2_);
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!this.canBlockStay(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos p_176588_2_) {
        return worldIn.getBlockState(p_176588_2_.offsetDown()).getBlock().getMaterial().isSolid();
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.cake;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BITES, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue(BITES);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, BITES);
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return (7 - (Integer)worldIn.getBlockState(pos).getValue(BITES)) * 2;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
}

