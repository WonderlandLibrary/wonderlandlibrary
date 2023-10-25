/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockStaticLiquid
extends BlockLiquid {
    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (!this.checkForMixing(world, blockPos, iBlockState)) {
            this.updateLiquid(world, blockPos, iBlockState);
        }
    }

    private boolean getCanBlockBurn(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock().getMaterial().getCanBurn();
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        block9: {
            if (this.blockMaterial != Material.lava || !world.getGameRules().getBoolean("doFireTick")) break block9;
            int n = random.nextInt(3);
            if (n > 0) {
                BlockPos blockPos2 = blockPos;
                int n2 = 0;
                while (n2 < n) {
                    blockPos2 = blockPos2.add(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
                    Block block = world.getBlockState(blockPos2).getBlock();
                    if (block.blockMaterial == Material.air) {
                        if (this.isSurroundingBlockFlammable(world, blockPos2)) {
                            world.setBlockState(blockPos2, Blocks.fire.getDefaultState());
                            return;
                        }
                    } else if (block.blockMaterial.blocksMovement()) {
                        return;
                    }
                    ++n2;
                }
            } else {
                int n3 = 0;
                while (n3 < 3) {
                    BlockPos blockPos3 = blockPos.add(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
                    if (world.isAirBlock(blockPos3.up()) && this.getCanBlockBurn(world, blockPos3)) {
                        world.setBlockState(blockPos3.up(), Blocks.fire.getDefaultState());
                    }
                    ++n3;
                }
            }
        }
    }

    protected BlockStaticLiquid(Material material) {
        super(material);
        this.setTickRandomly(false);
        if (material == Material.lava) {
            this.setTickRandomly(true);
        }
    }

    protected boolean isSurroundingBlockFlammable(World world, BlockPos blockPos) {
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumFacing = enumFacingArray[n2];
            if (this.getCanBlockBurn(world, blockPos.offset(enumFacing))) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    private void updateLiquid(World world, BlockPos blockPos, IBlockState iBlockState) {
        BlockDynamicLiquid blockDynamicLiquid = BlockStaticLiquid.getFlowingBlock(this.blockMaterial);
        world.setBlockState(blockPos, blockDynamicLiquid.getDefaultState().withProperty(LEVEL, iBlockState.getValue(LEVEL)), 2);
        world.scheduleUpdate(blockPos, blockDynamicLiquid, this.tickRate(world));
    }
}

