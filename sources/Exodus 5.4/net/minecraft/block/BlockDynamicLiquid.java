/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockDynamicLiquid
extends BlockLiquid {
    int adjacentSourceBlocks;

    private void tryFlowInto(World world, BlockPos blockPos, IBlockState iBlockState, int n) {
        if (this.canFlowInto(world, blockPos, iBlockState)) {
            if (iBlockState.getBlock() != Blocks.air) {
                if (this.blockMaterial == Material.lava) {
                    this.triggerMixEffects(world, blockPos);
                } else {
                    iBlockState.getBlock().dropBlockAsItem(world, blockPos, iBlockState, 0);
                }
            }
            world.setBlockState(blockPos, this.getDefaultState().withProperty(LEVEL, n), 3);
        }
    }

    private int func_176374_a(World world, BlockPos blockPos, int n, EnumFacing enumFacing) {
        int n2 = 1000;
        for (EnumFacing enumFacing2 : EnumFacing.Plane.HORIZONTAL) {
            int n3;
            IBlockState iBlockState;
            BlockPos blockPos2;
            if (enumFacing2 == enumFacing || this.isBlocked(world, blockPos2 = blockPos.offset(enumFacing2), iBlockState = world.getBlockState(blockPos2)) || iBlockState.getBlock().getMaterial() == this.blockMaterial && iBlockState.getValue(LEVEL) <= 0) continue;
            if (!this.isBlocked(world, blockPos2.down(), iBlockState)) {
                return n;
            }
            if (n >= 4 || (n3 = this.func_176374_a(world, blockPos2, n + 1, enumFacing2.getOpposite())) >= n2) continue;
            n2 = n3;
        }
        return n2;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        int n = iBlockState.getValue(LEVEL);
        int n2 = 1;
        if (this.blockMaterial == Material.lava && !world.provider.doesWaterVaporize()) {
            n2 = 2;
        }
        int n3 = this.tickRate(world);
        if (n > 0) {
            int n4 = -100;
            this.adjacentSourceBlocks = 0;
            for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
                n4 = this.checkAdjacentBlock(world, blockPos.offset(enumFacing), n4);
            }
            int n5 = n4 + n2;
            if (n5 >= 8 || n4 < 0) {
                n5 = -1;
            }
            if (this.getLevel(world, blockPos.up()) >= 0) {
                int n6 = this.getLevel(world, blockPos.up());
                n5 = n6 >= 8 ? n6 : n6 + 8;
            }
            if (this.adjacentSourceBlocks >= 2 && this.blockMaterial == Material.water) {
                IBlockState iBlockState2 = world.getBlockState(blockPos.down());
                if (iBlockState2.getBlock().getMaterial().isSolid()) {
                    n5 = 0;
                } else if (iBlockState2.getBlock().getMaterial() == this.blockMaterial && iBlockState2.getValue(LEVEL) == 0) {
                    n5 = 0;
                }
            }
            if (this.blockMaterial == Material.lava && n < 8 && n5 < 8 && n5 > n && random.nextInt(4) != 0) {
                n3 *= 4;
            }
            if (n5 == n) {
                this.placeStaticBlock(world, blockPos, iBlockState);
            } else {
                n = n5;
                if (n5 < 0) {
                    world.setBlockToAir(blockPos);
                } else {
                    iBlockState = iBlockState.withProperty(LEVEL, n5);
                    world.setBlockState(blockPos, iBlockState, 2);
                    world.scheduleUpdate(blockPos, this, n3);
                    world.notifyNeighborsOfStateChange(blockPos, this);
                }
            }
        } else {
            this.placeStaticBlock(world, blockPos, iBlockState);
        }
        IBlockState iBlockState3 = world.getBlockState(blockPos.down());
        if (this.canFlowInto(world, blockPos.down(), iBlockState3)) {
            if (this.blockMaterial == Material.lava && world.getBlockState(blockPos.down()).getBlock().getMaterial() == Material.water) {
                world.setBlockState(blockPos.down(), Blocks.stone.getDefaultState());
                this.triggerMixEffects(world, blockPos.down());
                return;
            }
            if (n >= 8) {
                this.tryFlowInto(world, blockPos.down(), iBlockState3, n);
            } else {
                this.tryFlowInto(world, blockPos.down(), iBlockState3, n + 8);
            }
        } else if (n >= 0 && (n == 0 || this.isBlocked(world, blockPos.down(), iBlockState3))) {
            Set<EnumFacing> set = this.getPossibleFlowDirections(world, blockPos);
            int n7 = n + n2;
            if (n >= 8) {
                n7 = 1;
            }
            if (n7 >= 8) {
                return;
            }
            for (EnumFacing enumFacing : set) {
                this.tryFlowInto(world, blockPos.offset(enumFacing), world.getBlockState(blockPos.offset(enumFacing)), n7);
            }
        }
    }

    protected int checkAdjacentBlock(World world, BlockPos blockPos, int n) {
        int n2 = this.getLevel(world, blockPos);
        if (n2 < 0) {
            return n;
        }
        if (n2 == 0) {
            ++this.adjacentSourceBlocks;
        }
        if (n2 >= 8) {
            n2 = 0;
        }
        return n >= 0 && n2 >= n ? n : n2;
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (!this.checkForMixing(world, blockPos, iBlockState)) {
            world.scheduleUpdate(blockPos, this, this.tickRate(world));
        }
    }

    private boolean isBlocked(World world, BlockPos blockPos, IBlockState iBlockState) {
        Block block = world.getBlockState(blockPos).getBlock();
        return !(block instanceof BlockDoor) && block != Blocks.standing_sign && block != Blocks.ladder && block != Blocks.reeds ? (block.blockMaterial == Material.portal ? true : block.blockMaterial.blocksMovement()) : true;
    }

    private boolean canFlowInto(World world, BlockPos blockPos, IBlockState iBlockState) {
        Material material = iBlockState.getBlock().getMaterial();
        return material != this.blockMaterial && material != Material.lava && !this.isBlocked(world, blockPos, iBlockState);
    }

    protected BlockDynamicLiquid(Material material) {
        super(material);
    }

    private Set<EnumFacing> getPossibleFlowDirections(World world, BlockPos blockPos) {
        int n = 1000;
        EnumSet<EnumFacing> enumSet = EnumSet.noneOf(EnumFacing.class);
        for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
            IBlockState iBlockState;
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            if (this.isBlocked(world, blockPos2, iBlockState = world.getBlockState(blockPos2)) || iBlockState.getBlock().getMaterial() == this.blockMaterial && iBlockState.getValue(LEVEL) <= 0) continue;
            int n2 = this.isBlocked(world, blockPos2.down(), world.getBlockState(blockPos2.down())) ? this.func_176374_a(world, blockPos2, 1, enumFacing.getOpposite()) : 0;
            if (n2 < n) {
                enumSet.clear();
            }
            if (n2 > n) continue;
            enumSet.add(enumFacing);
            n = n2;
        }
        return enumSet;
    }

    private void placeStaticBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        world.setBlockState(blockPos, BlockDynamicLiquid.getStaticBlock(this.blockMaterial).getDefaultState().withProperty(LEVEL, iBlockState.getValue(LEVEL)), 2);
    }
}

