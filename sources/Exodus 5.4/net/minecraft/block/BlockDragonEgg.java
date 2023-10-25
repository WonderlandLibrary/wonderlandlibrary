/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDragonEgg
extends Block {
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        this.checkFall(world, blockPos);
    }

    @Override
    public int tickRate(World world) {
        return 5;
    }

    @Override
    public void onBlockClicked(World world, BlockPos blockPos, EntityPlayer entityPlayer) {
        this.teleport(world, blockPos);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return true;
    }

    private void teleport(World world, BlockPos blockPos) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        if (iBlockState.getBlock() == this) {
            int n = 0;
            while (n < 1000) {
                BlockPos blockPos2 = blockPos.add(world.rand.nextInt(16) - world.rand.nextInt(16), world.rand.nextInt(8) - world.rand.nextInt(8), world.rand.nextInt(16) - world.rand.nextInt(16));
                if (world.getBlockState((BlockPos)blockPos2).getBlock().blockMaterial == Material.air) {
                    if (world.isRemote) {
                        int n2 = 0;
                        while (n2 < 128) {
                            double d = world.rand.nextDouble();
                            float f = (world.rand.nextFloat() - 0.5f) * 0.2f;
                            float f2 = (world.rand.nextFloat() - 0.5f) * 0.2f;
                            float f3 = (world.rand.nextFloat() - 0.5f) * 0.2f;
                            double d2 = (double)blockPos2.getX() + (double)(blockPos.getX() - blockPos2.getX()) * d + (world.rand.nextDouble() - 0.5) * 1.0 + 0.5;
                            double d3 = (double)blockPos2.getY() + (double)(blockPos.getY() - blockPos2.getY()) * d + world.rand.nextDouble() * 1.0 - 0.5;
                            double d4 = (double)blockPos2.getZ() + (double)(blockPos.getZ() - blockPos2.getZ()) * d + (world.rand.nextDouble() - 0.5) * 1.0 + 0.5;
                            world.spawnParticle(EnumParticleTypes.PORTAL, d2, d3, d4, (double)f, (double)f2, (double)f3, new int[0]);
                            ++n2;
                        }
                    } else {
                        world.setBlockState(blockPos2, iBlockState, 2);
                        world.setBlockToAir(blockPos);
                    }
                    return;
                }
                ++n;
            }
        }
    }

    private void checkFall(World world, BlockPos blockPos) {
        if (BlockFalling.canFallInto(world, blockPos.down()) && blockPos.getY() >= 0) {
            int n = 32;
            if (!BlockFalling.fallInstantly && world.isAreaLoaded(blockPos.add(-n, -n, -n), blockPos.add(n, n, n))) {
                world.spawnEntityInWorld(new EntityFallingBlock(world, (float)blockPos.getX() + 0.5f, blockPos.getY(), (float)blockPos.getZ() + 0.5f, this.getDefaultState()));
            } else {
                world.setBlockToAir(blockPos);
                BlockPos blockPos2 = blockPos;
                while (BlockFalling.canFallInto(world, blockPos2) && blockPos2.getY() > 0) {
                    blockPos2 = blockPos2.down();
                }
                if (blockPos2.getY() > 0) {
                    world.setBlockState(blockPos2, this.getDefaultState(), 2);
                }
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        world.scheduleUpdate(blockPos, this, this.tickRate(world));
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return null;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    public BlockDragonEgg() {
        super(Material.dragonEgg, MapColor.blackColor);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        this.teleport(world, blockPos);
        return true;
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        world.scheduleUpdate(blockPos, this, this.tickRate(world));
    }
}

