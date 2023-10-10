/*
 * Decompiled with CFR 0.152.
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
    private static Map<World, List<Toggle>> toggles = Maps.newHashMap();
    private final boolean isOn;

    private boolean isBurnedOut(World worldIn, BlockPos pos, boolean turnOff) {
        if (!toggles.containsKey(worldIn)) {
            toggles.put(worldIn, Lists.newArrayList());
        }
        List<Toggle> list = toggles.get(worldIn);
        if (turnOff) {
            list.add(new Toggle(pos, worldIn.getTotalWorldTime()));
        }
        int i = 0;
        int j = 0;
        while (j < list.size()) {
            Toggle blockredstonetorch$toggle = list.get(j);
            if (blockredstonetorch$toggle.pos.equals(pos) && ++i >= 8) {
                return true;
            }
            ++j;
        }
        return false;
    }

    protected BlockRedstoneTorch(boolean isOn) {
        this.isOn = isOn;
        this.setTickRandomly(true);
        this.setCreativeTab(null);
    }

    @Override
    public int tickRate(World worldIn) {
        return 2;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.isOn) return;
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumfacing = enumFacingArray[n2];
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            ++n2;
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.isOn) return;
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumfacing = enumFacingArray[n2];
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            ++n2;
        }
    }

    @Override
    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (!this.isOn) return 0;
        if (state.getValue(FACING) == side) return 0;
        return 15;
    }

    private boolean shouldBeOff(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = state.getValue(FACING).getOpposite();
        return worldIn.isSidePowered(pos.offset(enumfacing), enumfacing);
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean flag = this.shouldBeOff(worldIn, pos, state);
        List<Toggle> list = toggles.get(worldIn);
        while (list != null && !list.isEmpty() && worldIn.getTotalWorldTime() - list.get((int)0).time > 60L) {
            list.remove(0);
        }
        if (!this.isOn) {
            if (flag) return;
            if (this.isBurnedOut(worldIn, pos, false)) return;
            worldIn.setBlockState(pos, Blocks.redstone_torch.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
            return;
        }
        if (!flag) return;
        worldIn.setBlockState(pos, Blocks.unlit_redstone_torch.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
        if (!this.isBurnedOut(worldIn, pos, true)) return;
        worldIn.playSoundEffect((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f, "random.fizz", 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
        int i = 0;
        while (true) {
            if (i >= 5) {
                worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 160);
                return;
            }
            double d0 = (double)pos.getX() + rand.nextDouble() * 0.6 + 0.2;
            double d1 = (double)pos.getY() + rand.nextDouble() * 0.6 + 0.2;
            double d2 = (double)pos.getZ() + rand.nextDouble() * 0.6 + 0.2;
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0, 0.0, 0.0, new int[0]);
            ++i;
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (this.onNeighborChangeInternal(worldIn, pos, state)) return;
        if (this.isOn != this.shouldBeOff(worldIn, pos, state)) return;
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    @Override
    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (side != EnumFacing.DOWN) return 0;
        int n = this.getWeakPower(worldIn, pos, state, side);
        return n;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!this.isOn) return;
        double d0 = (double)pos.getX() + 0.5 + (rand.nextDouble() - 0.5) * 0.2;
        double d1 = (double)pos.getY() + 0.7 + (rand.nextDouble() - 0.5) * 0.2;
        double d2 = (double)pos.getZ() + 0.5 + (rand.nextDouble() - 0.5) * 0.2;
        EnumFacing enumfacing = state.getValue(FACING);
        if (enumfacing.getAxis().isHorizontal()) {
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            double d3 = 0.27;
            d0 += 0.27 * (double)enumfacing1.getFrontOffsetX();
            d1 += 0.22;
            d2 += 0.27 * (double)enumfacing1.getFrontOffsetZ();
        }
        worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, 0.0, 0.0, 0.0, new int[0]);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }

    @Override
    public boolean isAssociatedBlock(Block other) {
        if (other == Blocks.unlit_redstone_torch) return true;
        if (other == Blocks.redstone_torch) return true;
        return false;
    }

    static class Toggle {
        BlockPos pos;
        long time;

        public Toggle(BlockPos pos, long time) {
            this.pos = pos;
            this.time = time;
        }
    }
}

