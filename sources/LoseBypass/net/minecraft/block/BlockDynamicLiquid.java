/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.EnumSet;
import java.util.Iterator;
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
        int i = state.getValue(LEVEL);
        int j = 1;
        if (this.blockMaterial == Material.lava && !worldIn.provider.doesWaterVaporize()) {
            j = 2;
        }
        int k = this.tickRate(worldIn);
        if (i > 0) {
            int l = -100;
            this.adjacentSourceBlocks = 0;
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                l = this.checkAdjacentBlock(worldIn, pos.offset(enumfacing), l);
            }
            int i1 = l + j;
            if (i1 >= 8 || l < 0) {
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
            if (this.blockMaterial == Material.lava && i < 8 && i1 < 8 && i1 > i && rand.nextInt(4) != 0) {
                k *= 4;
            }
            if (i1 == i) {
                this.placeStaticBlock(worldIn, pos, state);
            } else {
                i = i1;
                if (i1 < 0) {
                    worldIn.setBlockToAir(pos);
                } else {
                    state = state.withProperty(LEVEL, i1);
                    worldIn.setBlockState(pos, state, 2);
                    worldIn.scheduleUpdate(pos, this, k);
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
            if (i >= 8) {
                this.tryFlowInto(worldIn, pos.down(), iblockstate, i);
                return;
            }
            this.tryFlowInto(worldIn, pos.down(), iblockstate, i + 8);
            return;
        }
        if (i < 0) return;
        if (i != 0) {
            if (!this.isBlocked(worldIn, pos.down(), iblockstate)) return;
        }
        Set<EnumFacing> set = this.getPossibleFlowDirections(worldIn, pos);
        int k1 = i + j;
        if (i >= 8) {
            k1 = 1;
        }
        if (k1 >= 8) {
            return;
        }
        Iterator<EnumFacing> iterator = set.iterator();
        while (iterator.hasNext()) {
            EnumFacing enumfacing1 = iterator.next();
            this.tryFlowInto(worldIn, pos.offset(enumfacing1), worldIn.getBlockState(pos.offset(enumfacing1)), k1);
        }
    }

    private void tryFlowInto(World worldIn, BlockPos pos, IBlockState state, int level) {
        if (!this.canFlowInto(worldIn, pos, state)) return;
        if (state.getBlock() != Blocks.air) {
            if (this.blockMaterial == Material.lava) {
                this.triggerMixEffects(worldIn, pos);
            } else {
                state.getBlock().dropBlockAsItem(worldIn, pos, state, 0);
            }
        }
        worldIn.setBlockState(pos, this.getDefaultState().withProperty(LEVEL, level), 3);
    }

    private int func_176374_a(World worldIn, BlockPos pos, int distance, EnumFacing calculateFlowCost) {
        int i = 1000;
        Iterator<EnumFacing> iterator = EnumFacing.Plane.HORIZONTAL.iterator();
        while (iterator.hasNext()) {
            int j;
            IBlockState iblockstate;
            BlockPos blockpos;
            EnumFacing enumfacing = iterator.next();
            if (enumfacing == calculateFlowCost || this.isBlocked(worldIn, blockpos = pos.offset(enumfacing), iblockstate = worldIn.getBlockState(blockpos)) || iblockstate.getBlock().getMaterial() == this.blockMaterial && iblockstate.getValue(LEVEL) <= 0) continue;
            if (!this.isBlocked(worldIn, blockpos.down(), iblockstate)) {
                return distance;
            }
            if (distance >= 4 || (j = this.func_176374_a(worldIn, blockpos, distance + 1, enumfacing.getOpposite())) >= i) continue;
            i = j;
        }
        return i;
    }

    private Set<EnumFacing> getPossibleFlowDirections(World worldIn, BlockPos pos) {
        int i = 1000;
        EnumSet<EnumFacing> set = EnumSet.noneOf(EnumFacing.class);
        Iterator<EnumFacing> iterator = EnumFacing.Plane.HORIZONTAL.iterator();
        while (iterator.hasNext()) {
            IBlockState iblockstate;
            EnumFacing enumfacing = iterator.next();
            BlockPos blockpos = pos.offset(enumfacing);
            if (this.isBlocked(worldIn, blockpos, iblockstate = worldIn.getBlockState(blockpos)) || iblockstate.getBlock().getMaterial() == this.blockMaterial && iblockstate.getValue(LEVEL) <= 0) continue;
            int j = this.isBlocked(worldIn, blockpos.down(), worldIn.getBlockState(blockpos.down())) ? this.func_176374_a(worldIn, blockpos, 1, enumfacing.getOpposite()) : 0;
            if (j < i) {
                set.clear();
            }
            if (j > i) continue;
            set.add(enumfacing);
            i = j;
        }
        return set;
    }

    private boolean isBlocked(World worldIn, BlockPos pos, IBlockState state) {
        Block block = worldIn.getBlockState(pos).getBlock();
        if (block instanceof BlockDoor) return true;
        if (block == Blocks.standing_sign) return true;
        if (block == Blocks.ladder) return true;
        if (block == Blocks.reeds) return true;
        if (block.blockMaterial == Material.portal) {
            return true;
        }
        boolean bl = block.blockMaterial.blocksMovement();
        return bl;
    }

    protected int checkAdjacentBlock(World worldIn, BlockPos pos, int currentMinLevel) {
        int n;
        int i = this.getLevel(worldIn, pos);
        if (i < 0) {
            return currentMinLevel;
        }
        if (i == 0) {
            ++this.adjacentSourceBlocks;
        }
        if (i >= 8) {
            i = 0;
        }
        if (currentMinLevel >= 0 && i >= currentMinLevel) {
            n = currentMinLevel;
            return n;
        }
        n = i;
        return n;
    }

    private boolean canFlowInto(World worldIn, BlockPos pos, IBlockState state) {
        Material material = state.getBlock().getMaterial();
        if (material == this.blockMaterial) return false;
        if (material == Material.lava) return false;
        if (this.isBlocked(worldIn, pos, state)) return false;
        return true;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (this.checkForMixing(worldIn, pos, state)) return;
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
}

