/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockRedstoneOre
extends Block {
    private final boolean isOn;

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
        super.dropBlockAsItemWithChance(world, blockPos, iBlockState, f, n);
        if (this.getItemDropped(iBlockState, world.rand, n) != Item.getItemFromBlock(this)) {
            int n2 = 1 + world.rand.nextInt(5);
            this.dropXpOnBlockBreak(world, blockPos, n2);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, Entity entity) {
        this.activate(world, blockPos);
        super.onEntityCollidedWithBlock(world, blockPos, entity);
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState iBlockState) {
        return new ItemStack(Blocks.redstone_ore);
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (this == Blocks.lit_redstone_ore) {
            world.setBlockState(blockPos, Blocks.redstone_ore.getDefaultState());
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        this.activate(world, blockPos);
        return super.onBlockActivated(world, blockPos, iBlockState, entityPlayer, enumFacing, f, f2, f3);
    }

    private void activate(World world, BlockPos blockPos) {
        this.spawnParticles(world, blockPos);
        if (this == Blocks.redstone_ore) {
            world.setBlockState(blockPos, Blocks.lit_redstone_ore.getDefaultState());
        }
    }

    public BlockRedstoneOre(boolean bl) {
        super(Material.rock);
        if (bl) {
            this.setTickRandomly(true);
        }
        this.isOn = bl;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (this.isOn) {
            this.spawnParticles(world, blockPos);
        }
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Items.redstone;
    }

    @Override
    public int tickRate(World world) {
        return 30;
    }

    @Override
    public void onBlockClicked(World world, BlockPos blockPos, EntityPlayer entityPlayer) {
        this.activate(world, blockPos);
        super.onBlockClicked(world, blockPos, entityPlayer);
    }

    @Override
    public int quantityDropped(Random random) {
        return 4 + random.nextInt(2);
    }

    private void spawnParticles(World world, BlockPos blockPos) {
        Random random = world.rand;
        double d = 0.0625;
        int n = 0;
        while (n < 6) {
            double d2 = (float)blockPos.getX() + random.nextFloat();
            double d3 = (float)blockPos.getY() + random.nextFloat();
            double d4 = (float)blockPos.getZ() + random.nextFloat();
            if (n == 0 && !world.getBlockState(blockPos.up()).getBlock().isOpaqueCube()) {
                d3 = (double)blockPos.getY() + d + 1.0;
            }
            if (n == 1 && !world.getBlockState(blockPos.down()).getBlock().isOpaqueCube()) {
                d3 = (double)blockPos.getY() - d;
            }
            if (n == 2 && !world.getBlockState(blockPos.south()).getBlock().isOpaqueCube()) {
                d4 = (double)blockPos.getZ() + d + 1.0;
            }
            if (n == 3 && !world.getBlockState(blockPos.north()).getBlock().isOpaqueCube()) {
                d4 = (double)blockPos.getZ() - d;
            }
            if (n == 4 && !world.getBlockState(blockPos.east()).getBlock().isOpaqueCube()) {
                d2 = (double)blockPos.getX() + d + 1.0;
            }
            if (n == 5 && !world.getBlockState(blockPos.west()).getBlock().isOpaqueCube()) {
                d2 = (double)blockPos.getX() - d;
            }
            if (d2 < (double)blockPos.getX() || d2 > (double)(blockPos.getX() + 1) || d3 < 0.0 || d3 > (double)(blockPos.getY() + 1) || d4 < (double)blockPos.getZ() || d4 > (double)(blockPos.getZ() + 1)) {
                world.spawnParticle(EnumParticleTypes.REDSTONE, d2, d3, d4, 0.0, 0.0, 0.0, new int[0]);
            }
            ++n;
        }
    }

    @Override
    public int quantityDroppedWithBonus(int n, Random random) {
        return this.quantityDropped(random) + random.nextInt(n + 1);
    }
}

