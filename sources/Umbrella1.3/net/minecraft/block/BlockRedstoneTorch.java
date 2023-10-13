/*
 * Decompiled with CFR 0.150.
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
    private static Map field_150112_b = Maps.newHashMap();
    private final boolean field_150113_a;
    private static final String __OBFID = "CL_00000298";

    private boolean func_176598_a(World worldIn, BlockPos p_176598_2_, boolean p_176598_3_) {
        if (!field_150112_b.containsKey(worldIn)) {
            field_150112_b.put(worldIn, Lists.newArrayList());
        }
        List var4 = (List)field_150112_b.get(worldIn);
        if (p_176598_3_) {
            var4.add(new Toggle(p_176598_2_, worldIn.getTotalWorldTime()));
        }
        int var5 = 0;
        for (int var6 = 0; var6 < var4.size(); ++var6) {
            Toggle var7 = (Toggle)var4.get(var6);
            if (!var7.field_180111_a.equals(p_176598_2_) || ++var5 < 8) continue;
            return true;
        }
        return false;
    }

    protected BlockRedstoneTorch(boolean p_i45423_1_) {
        this.field_150113_a = p_i45423_1_;
        this.setTickRandomly(true);
        this.setCreativeTab(null);
    }

    @Override
    public int tickRate(World worldIn) {
        return 2;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (this.field_150113_a) {
            for (EnumFacing var7 : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (this.field_150113_a) {
            for (EnumFacing var7 : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }
        }
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return this.field_150113_a && state.getValue(FACING_PROP) != side ? 15 : 0;
    }

    private boolean func_176597_g(World worldIn, BlockPos p_176597_2_, IBlockState p_176597_3_) {
        EnumFacing var4 = ((EnumFacing)((Object)p_176597_3_.getValue(FACING_PROP))).getOpposite();
        return worldIn.func_175709_b(p_176597_2_.offset(var4), var4);
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean var5 = this.func_176597_g(worldIn, pos, state);
        List var6 = (List)field_150112_b.get(worldIn);
        while (var6 != null && !var6.isEmpty() && worldIn.getTotalWorldTime() - ((Toggle)var6.get((int)0)).field_150844_d > 60L) {
            var6.remove(0);
        }
        if (this.field_150113_a) {
            if (var5) {
                worldIn.setBlockState(pos, Blocks.unlit_redstone_torch.getDefaultState().withProperty(FACING_PROP, state.getValue(FACING_PROP)), 3);
                if (this.func_176598_a(worldIn, pos, true)) {
                    worldIn.playSoundEffect((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f, "random.fizz", 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
                    for (int var7 = 0; var7 < 5; ++var7) {
                        double var8 = (double)pos.getX() + rand.nextDouble() * 0.6 + 0.2;
                        double var10 = (double)pos.getY() + rand.nextDouble() * 0.6 + 0.2;
                        double var12 = (double)pos.getZ() + rand.nextDouble() * 0.6 + 0.2;
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var8, var10, var12, 0.0, 0.0, 0.0, new int[0]);
                    }
                    worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 160);
                }
            }
        } else if (!var5 && !this.func_176598_a(worldIn, pos, false)) {
            worldIn.setBlockState(pos, Blocks.redstone_torch.getDefaultState().withProperty(FACING_PROP, state.getValue(FACING_PROP)), 3);
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!this.func_176592_e(worldIn, pos, state) && this.field_150113_a == this.func_176597_g(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return side == EnumFacing.DOWN ? this.isProvidingWeakPower(worldIn, pos, state, side) : 0;
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
        if (this.field_150113_a) {
            double var5 = (double)((float)pos.getX() + 0.5f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            double var7 = (double)((float)pos.getY() + 0.7f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            double var9 = (double)((float)pos.getZ() + 0.5f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            EnumFacing var11 = (EnumFacing)((Object)state.getValue(FACING_PROP));
            if (var11.getAxis().isHorizontal()) {
                EnumFacing var12 = var11.getOpposite();
                double var13 = 0.27f;
                var5 += (double)0.27f * (double)var12.getFrontOffsetX();
                var7 += (double)0.22f;
                var9 += (double)0.27f * (double)var12.getFrontOffsetZ();
            }
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var5, var7, var9, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }

    @Override
    public boolean isAssociatedBlock(Block other) {
        return other == Blocks.unlit_redstone_torch || other == Blocks.redstone_torch;
    }

    static class Toggle {
        BlockPos field_180111_a;
        long field_150844_d;
        private static final String __OBFID = "CL_00000299";

        public Toggle(BlockPos p_i45688_1_, long p_i45688_2_) {
            this.field_180111_a = p_i45688_1_;
            this.field_150844_d = p_i45688_2_;
        }
    }
}

