/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneTorch
extends BlockTorch {
    private final boolean isOn;
    private static Map<World, List<Toggle>> toggles = Maps.newHashMap();

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (this.isOn) {
            EnumFacing[] enumFacingArray = EnumFacing.values();
            int n = enumFacingArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumFacing enumFacing = enumFacingArray[n2];
                world.notifyNeighborsOfStateChange(blockPos.offset(enumFacing), this);
                ++n2;
            }
        }
    }

    @Override
    public void randomTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }

    private boolean shouldBeOff(World world, BlockPos blockPos, IBlockState iBlockState) {
        EnumFacing enumFacing = iBlockState.getValue(FACING).getOpposite();
        return world.isSidePowered(blockPos.offset(enumFacing), enumFacing);
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }

    protected BlockRedstoneTorch(boolean bl) {
        this.isOn = bl;
        this.setTickRandomly(true);
        this.setCreativeTab(null);
    }

    private boolean isBurnedOut(World world, BlockPos blockPos, boolean bl) {
        if (!toggles.containsKey(world)) {
            toggles.put(world, Lists.newArrayList());
        }
        List<Toggle> list = toggles.get(world);
        if (bl) {
            list.add(new Toggle(blockPos, world.getTotalWorldTime()));
        }
        int n = 0;
        int n2 = 0;
        while (n2 < list.size()) {
            Toggle toggle = list.get(n2);
            if (toggle.pos.equals(blockPos) && ++n >= 8) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (this.isOn) {
            EnumFacing[] enumFacingArray = EnumFacing.values();
            int n = enumFacingArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumFacing enumFacing = enumFacingArray[n2];
                world.notifyNeighborsOfStateChange(blockPos.offset(enumFacing), this);
                ++n2;
            }
        }
    }

    @Override
    public int getStrongPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        return enumFacing == EnumFacing.DOWN ? this.getWeakPower(iBlockAccess, blockPos, iBlockState, enumFacing) : 0;
    }

    @Override
    public int tickRate(World world) {
        return 2;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        boolean bl = this.shouldBeOff(world, blockPos, iBlockState);
        List<Toggle> list = toggles.get(world);
        while (list != null && !list.isEmpty() && world.getTotalWorldTime() - list.get((int)0).time > 60L) {
            list.remove(0);
        }
        if (this.isOn) {
            if (bl) {
                world.setBlockState(blockPos, Blocks.unlit_redstone_torch.getDefaultState().withProperty(FACING, iBlockState.getValue(FACING)), 3);
                if (this.isBurnedOut(world, blockPos, true)) {
                    world.playSoundEffect((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f, "random.fizz", 0.5f, 2.6f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8f);
                    int n = 0;
                    while (n < 5) {
                        double d = (double)blockPos.getX() + random.nextDouble() * 0.6 + 0.2;
                        double d2 = (double)blockPos.getY() + random.nextDouble() * 0.6 + 0.2;
                        double d3 = (double)blockPos.getZ() + random.nextDouble() * 0.6 + 0.2;
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d, d2, d3, 0.0, 0.0, 0.0, new int[0]);
                        ++n;
                    }
                    world.scheduleUpdate(blockPos, world.getBlockState(blockPos).getBlock(), 160);
                }
            }
        } else if (!bl && !this.isBurnedOut(world, blockPos, false)) {
            world.setBlockState(blockPos, Blocks.redstone_torch.getDefaultState().withProperty(FACING, iBlockState.getValue(FACING)), 3);
        }
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int getWeakPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        return this.isOn && iBlockState.getValue(FACING) != enumFacing ? 15 : 0;
    }

    @Override
    public boolean isAssociatedBlock(Block block) {
        return block == Blocks.unlit_redstone_torch || block == Blocks.redstone_torch;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (!this.onNeighborChangeInternal(world, blockPos, iBlockState) && this.isOn == this.shouldBeOff(world, blockPos, iBlockState)) {
            world.scheduleUpdate(blockPos, this, this.tickRate(world));
        }
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (this.isOn) {
            double d = (double)blockPos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            double d2 = (double)blockPos.getY() + 0.7 + (random.nextDouble() - 0.5) * 0.2;
            double d3 = (double)blockPos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            EnumFacing enumFacing = iBlockState.getValue(FACING);
            if (enumFacing.getAxis().isHorizontal()) {
                EnumFacing enumFacing2 = enumFacing.getOpposite();
                double d4 = 0.27;
                d += 0.27 * (double)enumFacing2.getFrontOffsetX();
                d2 += 0.22;
                d3 += 0.27 * (double)enumFacing2.getFrontOffsetZ();
            }
            world.spawnParticle(EnumParticleTypes.REDSTONE, d, d2, d3, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    static class Toggle {
        long time;
        BlockPos pos;

        public Toggle(BlockPos blockPos, long l) {
            this.pos = blockPos;
            this.time = l;
        }
    }
}

