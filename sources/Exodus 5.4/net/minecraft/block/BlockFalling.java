/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockFalling
extends Block {
    public static boolean fallInstantly;

    public static boolean canFallInto(World world, BlockPos blockPos) {
        Block block = world.getBlockState(blockPos).getBlock();
        Material material = block.blockMaterial;
        return block == Blocks.fire || material == Material.air || material == Material.water || material == Material.lava;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (!world.isRemote) {
            this.checkFallable(world, blockPos);
        }
    }

    @Override
    public int tickRate(World world) {
        return 2;
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        world.scheduleUpdate(blockPos, this, this.tickRate(world));
    }

    public BlockFalling(Material material) {
        super(material);
    }

    public BlockFalling() {
        super(Material.sand);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    private void checkFallable(World world, BlockPos blockPos) {
        if (BlockFalling.canFallInto(world, blockPos.down()) && blockPos.getY() >= 0) {
            int n = 32;
            if (!fallInstantly && world.isAreaLoaded(blockPos.add(-n, -n, -n), blockPos.add(n, n, n))) {
                if (!world.isRemote) {
                    EntityFallingBlock entityFallingBlock = new EntityFallingBlock(world, (double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5, world.getBlockState(blockPos));
                    this.onStartFalling(entityFallingBlock);
                    world.spawnEntityInWorld(entityFallingBlock);
                }
            } else {
                world.setBlockToAir(blockPos);
                BlockPos blockPos2 = blockPos.down();
                while (BlockFalling.canFallInto(world, blockPos2) && blockPos2.getY() > 0) {
                    blockPos2 = blockPos2.down();
                }
                if (blockPos2.getY() > 0) {
                    world.setBlockState(blockPos2.up(), this.getDefaultState());
                }
            }
        }
    }

    public void onEndFalling(World world, BlockPos blockPos) {
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        world.scheduleUpdate(blockPos, this, this.tickRate(world));
    }

    protected void onStartFalling(EntityFallingBlock entityFallingBlock) {
    }
}

