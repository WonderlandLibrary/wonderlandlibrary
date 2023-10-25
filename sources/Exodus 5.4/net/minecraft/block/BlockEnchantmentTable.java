/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockEnchantmentTable
extends BlockContainer {
    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        super.randomDisplayTick(world, blockPos, iBlockState, random);
        int n = -2;
        while (n <= 2) {
            int n2 = -2;
            while (n2 <= 2) {
                if (n > -2 && n < 2 && n2 == -1) {
                    n2 = 2;
                }
                if (random.nextInt(16) == 0) {
                    int n3 = 0;
                    while (n3 <= 1) {
                        BlockPos blockPos2 = blockPos.add(n, n3, n2);
                        if (world.getBlockState(blockPos2).getBlock() == Blocks.bookshelf) {
                            if (!world.isAirBlock(blockPos.add(n / 2, 0, n2 / 2))) break;
                            world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 2.0, (double)blockPos.getZ() + 0.5, (double)((float)n + random.nextFloat()) - 0.5, (double)((float)n3 - random.nextFloat() - 1.0f), (double)((float)n2 + random.nextFloat()) - 0.5, new int[0]);
                        }
                        ++n3;
                    }
                }
                ++n2;
            }
            ++n;
        }
    }

    protected BlockEnchantmentTable() {
        super(Material.rock, MapColor.redColor);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState iBlockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        TileEntity tileEntity;
        super.onBlockPlacedBy(world, blockPos, iBlockState, entityLivingBase, itemStack);
        if (itemStack.hasDisplayName() && (tileEntity = world.getTileEntity(blockPos)) instanceof TileEntityEnchantmentTable) {
            ((TileEntityEnchantmentTable)tileEntity).setCustomName(itemStack.getDisplayName());
        }
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
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (world.isRemote) {
            return true;
        }
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityEnchantmentTable) {
            entityPlayer.displayGui((TileEntityEnchantmentTable)tileEntity);
        }
        return true;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityEnchantmentTable();
    }
}

