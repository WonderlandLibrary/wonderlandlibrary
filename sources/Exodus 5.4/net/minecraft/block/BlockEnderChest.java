/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockEnderChest
extends BlockContainer {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(FACING).getIndex();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        InventoryEnderChest inventoryEnderChest = entityPlayer.getInventoryEnderChest();
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (inventoryEnderChest != null && tileEntity instanceof TileEntityEnderChest) {
            if (world.getBlockState(blockPos.up()).getBlock().isNormalCube()) {
                return true;
            }
            if (world.isRemote) {
                return true;
            }
            inventoryEnderChest.setChestTileEntity((TileEntityEnderChest)tileEntity);
            entityPlayer.displayGUIChest(inventoryEnderChest);
            entityPlayer.triggerAchievement(StatList.field_181738_V);
            return true;
        }
        return true;
    }

    @Override
    public int getRenderType() {
        return 2;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState iBlockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        world.setBlockState(blockPos, iBlockState.withProperty(FACING, entityLivingBase.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Item.getItemFromBlock(Blocks.obsidian);
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        EnumFacing enumFacing = EnumFacing.getFront(n);
        if (enumFacing.getAxis() == EnumFacing.Axis.Y) {
            enumFacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumFacing);
    }

    protected BlockEnderChest() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        int n = 0;
        while (n < 3) {
            int n2 = random.nextInt(2) * 2 - 1;
            int n3 = random.nextInt(2) * 2 - 1;
            double d = (double)blockPos.getX() + 0.5 + 0.25 * (double)n2;
            double d2 = (float)blockPos.getY() + random.nextFloat();
            double d3 = (double)blockPos.getZ() + 0.5 + 0.25 * (double)n3;
            double d4 = random.nextFloat() * (float)n2;
            double d5 = ((double)random.nextFloat() - 0.5) * 0.125;
            double d6 = random.nextFloat() * (float)n3;
            world.spawnParticle(EnumParticleTypes.PORTAL, d, d2, d3, d4, d5, d6, new int[0]);
            ++n;
        }
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return this.getDefaultState().withProperty(FACING, entityLivingBase.getHorizontalFacing().getOpposite());
    }

    @Override
    public int quantityDropped(Random random) {
        return 8;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityEnderChest();
    }
}

