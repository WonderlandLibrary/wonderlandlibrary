// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import java.util.Map;

public class BlockRedstoneTorch extends BlockTorch
{
    private static Map field_150112_b;
    private final boolean field_150113_a;
    private static final String __OBFID = "CL_00000298";
    
    private boolean func_176598_a(final World worldIn, final BlockPos p_176598_2_, final boolean p_176598_3_) {
        if (!BlockRedstoneTorch.field_150112_b.containsKey(worldIn)) {
            BlockRedstoneTorch.field_150112_b.put(worldIn, Lists.newArrayList());
        }
        final List var4 = BlockRedstoneTorch.field_150112_b.get(worldIn);
        if (p_176598_3_) {
            var4.add(new Toggle(p_176598_2_, worldIn.getTotalWorldTime()));
        }
        int var5 = 0;
        for (int var6 = 0; var6 < var4.size(); ++var6) {
            final Toggle var7 = var4.get(var6);
            if (var7.field_180111_a.equals(p_176598_2_) && ++var5 >= 8) {
                return true;
            }
        }
        return false;
    }
    
    protected BlockRedstoneTorch(final boolean p_i45423_1_) {
        this.field_150113_a = p_i45423_1_;
        this.setTickRandomly(true);
        this.setCreativeTab(null);
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 2;
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.field_150113_a) {
            for (final EnumFacing var7 : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.field_150113_a) {
            for (final EnumFacing var7 : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }
        }
    }
    
    @Override
    public int isProvidingWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return (this.field_150113_a && state.getValue(BlockTorch.FACING_PROP) != side) ? 15 : 0;
    }
    
    private boolean func_176597_g(final World worldIn, final BlockPos p_176597_2_, final IBlockState p_176597_3_) {
        final EnumFacing var4 = ((EnumFacing)p_176597_3_.getValue(BlockTorch.FACING_PROP)).getOpposite();
        return worldIn.func_175709_b(p_176597_2_.offset(var4), var4);
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final boolean var5 = this.func_176597_g(worldIn, pos, state);
        final List var6 = BlockRedstoneTorch.field_150112_b.get(worldIn);
        while (var6 != null && !var6.isEmpty() && worldIn.getTotalWorldTime() - var6.get(0).field_150844_d > 60L) {
            var6.remove(0);
        }
        if (this.field_150113_a) {
            if (var5) {
                worldIn.setBlockState(pos, Blocks.unlit_redstone_torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, state.getValue(BlockTorch.FACING_PROP)), 3);
                if (this.func_176598_a(worldIn, pos, true)) {
                    worldIn.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, "random.fizz", 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
                    for (int var7 = 0; var7 < 5; ++var7) {
                        final double var8 = pos.getX() + rand.nextDouble() * 0.6 + 0.2;
                        final double var9 = pos.getY() + rand.nextDouble() * 0.6 + 0.2;
                        final double var10 = pos.getZ() + rand.nextDouble() * 0.6 + 0.2;
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var8, var9, var10, 0.0, 0.0, 0.0, new int[0]);
                    }
                    worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 160);
                }
            }
        }
        else if (!var5 && !this.func_176598_a(worldIn, pos, false)) {
            worldIn.setBlockState(pos, Blocks.redstone_torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, state.getValue(BlockTorch.FACING_PROP)), 3);
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!this.func_176592_e(worldIn, pos, state) && this.field_150113_a == this.func_176597_g(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }
    
    @Override
    public int isProvidingStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return (side == EnumFacing.DOWN) ? this.isProvidingWeakPower(worldIn, pos, state, side) : 0;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this.field_150113_a) {
            double var5 = pos.getX() + 0.5f + (rand.nextFloat() - 0.5f) * 0.2;
            double var6 = pos.getY() + 0.7f + (rand.nextFloat() - 0.5f) * 0.2;
            double var7 = pos.getZ() + 0.5f + (rand.nextFloat() - 0.5f) * 0.2;
            final EnumFacing var8 = (EnumFacing)state.getValue(BlockTorch.FACING_PROP);
            if (var8.getAxis().isHorizontal()) {
                final EnumFacing var9 = var8.getOpposite();
                final double var10 = 0.27000001072883606;
                var5 += 0.27000001072883606 * var9.getFrontOffsetX();
                var6 += 0.2199999988079071;
                var7 += 0.27000001072883606 * var9.getFrontOffsetZ();
            }
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var5, var6, var7, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }
    
    @Override
    public boolean isAssociatedBlock(final Block other) {
        return other == Blocks.unlit_redstone_torch || other == Blocks.redstone_torch;
    }
    
    static {
        BlockRedstoneTorch.field_150112_b = Maps.newHashMap();
    }
    
    static class Toggle
    {
        BlockPos field_180111_a;
        long field_150844_d;
        private static final String __OBFID = "CL_00000299";
        
        public Toggle(final BlockPos p_i45688_1_, final long p_i45688_2_) {
            this.field_180111_a = p_i45688_1_;
            this.field_150844_d = p_i45688_2_;
        }
    }
}
