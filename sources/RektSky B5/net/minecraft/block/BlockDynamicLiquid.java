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

    protected BlockDynamicLiquid(Material materialIn) {
        super(materialIn);
    }

    private void placeStaticBlock(World worldIn, BlockPos pos, IBlockState currentState) {
        worldIn.setBlockState(pos, BlockDynamicLiquid.getStaticBlock(this.blockMaterial).getDefaultState().withProperty(LEVEL, currentState.getValue(LEVEL)), 2);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int i2 = state.getValue(LEVEL);
        int j2 = 1;
        if (this.blockMaterial == Material.lava && !worldIn.provider.doesWaterVaporize()) {
            j2 = 2;
        }
        int k2 = this.tickRate(worldIn);
        if (i2 > 0) {
            int l2 = -100;
            this.adjacentSourceBlocks = 0;
            for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL) {
                EnumFacing enumfacing = (EnumFacing)enumfacing0;
                l2 = this.checkAdjacentBlock(worldIn, pos.offset(enumfacing), l2);
            }
            int i1 = l2 + j2;
            if (i1 >= 8 || l2 < 0) {
                i1 = -1;
            }
            if (this.getLevel(worldIn, pos.up()) >= 0) {
                int j1 = this.getLevel(worldIn, pos.up());
                i1 = j1 >= 8 ? j1 : j1 + 8;
            }
            if (this.adjacentSourceBlocks >= 2 && this.blockMaterial == Material.water) {
                IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
                if (iblockstate1.getBlock().getMaterial().isSolid()) {
                    i1 = 0;
                } else if (iblockstate1.getBlock().getMaterial() == this.blockMaterial && iblockstate1.getValue(LEVEL) == 0) {
                    i1 = 0;
                }
            }
            if (this.blockMaterial == Material.lava && i2 < 8 && i1 < 8 && i1 > i2 && rand.nextInt(4) != 0) {
                k2 *= 4;
            }
            if (i1 == i2) {
                this.placeStaticBlock(worldIn, pos, state);
            } else {
                i2 = i1;
                if (i1 < 0) {
                    worldIn.setBlockToAir(pos);
                } else {
                    state = state.withProperty(LEVEL, i1);
                    worldIn.setBlockState(pos, state, 2);
                    worldIn.scheduleUpdate(pos, this, k2);
                    worldIn.notifyNeighborsOfStateChange(pos, this);
                }
            }
        } else {
            this.placeStaticBlock(worldIn, pos, state);
        }
        IBlockState iblockstate = worldIn.getBlockState(pos.down());
        if (this.canFlowInto(worldIn, pos.down(), iblockstate)) {
            if (this.blockMaterial == Material.lava && worldIn.getBlockState(pos.down()).getBlock().getMaterial() == Material.water) {
                worldIn.setBlockState(pos.down(), Blocks.stone.getDefaultState());
                this.triggerMixEffects(worldIn, pos.down());
                return;
            }
            if (i2 >= 8) {
                this.tryFlowInto(worldIn, pos.down(), iblockstate, i2);
            } else {
                this.tryFlowInto(worldIn, pos.down(), iblockstate, i2 + 8);
            }
        } else if (i2 >= 0 && (i2 == 0 || this.isBlocked(worldIn, pos.down(), iblockstate))) {
            Set<EnumFacing> set = this.getPossibleFlowDirections(worldIn, pos);
            int k1 = i2 + j2;
            if (i2 >= 8) {
                k1 = 1;
            }
            if (k1 >= 8) {
                return;
            }
            for (EnumFacing enumfacing1 : set) {
                this.tryFlowInto(worldIn, pos.offset(enumfacing1), worldIn.getBlockState(pos.offset(enumfacing1)), k1);
            }
        }
    }

    private void tryFlowInto(World worldIn, BlockPos pos, IBlockState state, int level) {
        if (this.canFlowInto(worldIn, pos, state)) {
            if (state.getBlock() != Blocks.air) {
                if (this.blockMaterial == Material.lava) {
                    this.triggerMixEffects(worldIn, pos);
                } else {
                    state.getBlock().dropBlockAsItem(worldIn, pos, state, 0);
                }
            }
            worldIn.setBlockState(pos, this.getDefaultState().withProperty(LEVEL, level), 3);
        }
    }

    private int func_176374_a(World worldIn, BlockPos pos, int distance, EnumFacing calculateFlowCost) {
        int i2 = 1000;
        for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL) {
            int j2;
            IBlockState iblockstate;
            BlockPos blockpos;
            EnumFacing enumfacing = (EnumFacing)enumfacing0;
            if (enumfacing == calculateFlowCost || this.isBlocked(worldIn, blockpos = pos.offset(enumfacing), iblockstate = worldIn.getBlockState(blockpos)) || iblockstate.getBlock().getMaterial() == this.blockMaterial && iblockstate.getValue(LEVEL) <= 0) continue;
            if (!this.isBlocked(worldIn, blockpos.down(), iblockstate)) {
                return distance;
            }
            if (distance >= 4 || (j2 = this.func_176374_a(worldIn, blockpos, distance + 1, enumfacing.getOpposite())) >= i2) continue;
            i2 = j2;
        }
        return i2;
    }

    private Set<EnumFacing> getPossibleFlowDirections(World worldIn, BlockPos pos) {
        int i2 = 1000;
        EnumSet<EnumFacing> set = EnumSet.noneOf(EnumFacing.class);
        for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL) {
            IBlockState iblockstate;
            EnumFacing enumfacing = (EnumFacing)enumfacing0;
            BlockPos blockpos = pos.offset(enumfacing);
            if (this.isBlocked(worldIn, blockpos, iblockstate = worldIn.getBlockState(blockpos)) || iblockstate.getBlock().getMaterial() == this.blockMaterial && iblockstate.getValue(LEVEL) <= 0) continue;
            int j2 = this.isBlocked(worldIn, blockpos.down(), worldIn.getBlockState(blockpos.down())) ? this.func_176374_a(worldIn, blockpos, 1, enumfacing.getOpposite()) : 0;
            if (j2 < i2) {
                set.clear();
            }
            if (j2 > i2) continue;
            set.add(enumfacing);
            i2 = j2;
        }
        return set;
    }

    private boolean isBlocked(World worldIn, BlockPos pos, IBlockState state) {
        Block block = worldIn.getBlockState(pos).getBlock();
        return !(block instanceof BlockDoor) && block != Blocks.standing_sign && block != Blocks.ladder && block != Blocks.reeds ? (block.blockMaterial == Material.portal ? true : block.blockMaterial.blocksMovement()) : true;
    }

    protected int checkAdjacentBlock(World worldIn, BlockPos pos, int currentMinLevel) {
        int i2 = this.getLevel(worldIn, pos);
        if (i2 < 0) {
            return currentMinLevel;
        }
        if (i2 == 0) {
            ++this.adjacentSourceBlocks;
        }
        if (i2 >= 8) {
            i2 = 0;
        }
        return currentMinLevel >= 0 && i2 >= currentMinLevel ? currentMinLevel : i2;
    }

    private boolean canFlowInto(World worldIn, BlockPos pos, IBlockState state) {
        Material material = state.getBlock().getMaterial();
        return material != this.blockMaterial && material != Material.lava && !this.isBlocked(worldIn, pos, state);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.checkForMixing(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }
}

