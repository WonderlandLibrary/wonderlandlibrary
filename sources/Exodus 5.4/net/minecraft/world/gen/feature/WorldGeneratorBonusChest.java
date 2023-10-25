/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGeneratorBonusChest
extends WorldGenerator {
    private final int itemsToGenerateInBonusChest;
    private final List<WeightedRandomChestContent> chestItems;

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        Block block;
        while (((block = world.getBlockState(blockPos).getBlock()).getMaterial() == Material.air || block.getMaterial() == Material.leaves) && blockPos.getY() > 1) {
            blockPos = blockPos.down();
        }
        if (blockPos.getY() < 1) {
            return false;
        }
        blockPos = blockPos.up();
        int n = 0;
        while (n < 4) {
            BlockPos blockPos2 = blockPos.add(random.nextInt(4) - random.nextInt(4), random.nextInt(3) - random.nextInt(3), random.nextInt(4) - random.nextInt(4));
            if (world.isAirBlock(blockPos2) && World.doesBlockHaveSolidTopSurface(world, blockPos2.down())) {
                world.setBlockState(blockPos2, Blocks.chest.getDefaultState(), 2);
                TileEntity tileEntity = world.getTileEntity(blockPos2);
                if (tileEntity instanceof TileEntityChest) {
                    WeightedRandomChestContent.generateChestContents(random, this.chestItems, (TileEntityChest)tileEntity, this.itemsToGenerateInBonusChest);
                }
                BlockPos blockPos3 = blockPos2.east();
                BlockPos blockPos4 = blockPos2.west();
                BlockPos blockPos5 = blockPos2.north();
                BlockPos blockPos6 = blockPos2.south();
                if (world.isAirBlock(blockPos4) && World.doesBlockHaveSolidTopSurface(world, blockPos4.down())) {
                    world.setBlockState(blockPos4, Blocks.torch.getDefaultState(), 2);
                }
                if (world.isAirBlock(blockPos3) && World.doesBlockHaveSolidTopSurface(world, blockPos3.down())) {
                    world.setBlockState(blockPos3, Blocks.torch.getDefaultState(), 2);
                }
                if (world.isAirBlock(blockPos5) && World.doesBlockHaveSolidTopSurface(world, blockPos5.down())) {
                    world.setBlockState(blockPos5, Blocks.torch.getDefaultState(), 2);
                }
                if (world.isAirBlock(blockPos6) && World.doesBlockHaveSolidTopSurface(world, blockPos6.down())) {
                    world.setBlockState(blockPos6, Blocks.torch.getDefaultState(), 2);
                }
                return true;
            }
            ++n;
        }
        return false;
    }

    public WorldGeneratorBonusChest(List<WeightedRandomChestContent> list, int n) {
        this.chestItems = list;
        this.itemsToGenerateInBonusChest = n;
    }
}

