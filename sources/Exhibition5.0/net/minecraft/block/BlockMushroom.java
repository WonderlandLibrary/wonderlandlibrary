// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockMushroom extends BlockBush implements IGrowable
{
    private static final String __OBFID = "CL_00000272";
    
    protected BlockMushroom() {
        final float var1 = 0.2f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, var1 * 2.0f, 0.5f + var1);
        this.setTickRandomly(true);
    }
    
    @Override
    public void updateTick(final World worldIn, BlockPos pos, final IBlockState state, final Random rand) {
        if (rand.nextInt(25) == 0) {
            int var5 = 5;
            final boolean var6 = true;
            for (final BlockPos var8 : BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4))) {
                if (worldIn.getBlockState(var8).getBlock() == this && --var5 <= 0) {
                    return;
                }
            }
            BlockPos var9 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
            for (int var10 = 0; var10 < 4; ++var10) {
                if (worldIn.isAirBlock(var9) && this.canBlockStay(worldIn, var9, this.getDefaultState())) {
                    pos = var9;
                }
                var9 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
            }
            if (worldIn.isAirBlock(var9) && this.canBlockStay(worldIn, var9, this.getDefaultState())) {
                worldIn.setBlockState(var9, this.getDefaultState(), 2);
            }
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos, this.getDefaultState());
    }
    
    @Override
    protected boolean canPlaceBlockOn(final Block ground) {
        return ground.isFullBlock();
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos p_180671_2_, final IBlockState p_180671_3_) {
        if (p_180671_2_.getY() >= 0 && p_180671_2_.getY() < 256) {
            final IBlockState var4 = worldIn.getBlockState(p_180671_2_.offsetDown());
            return var4.getBlock() == Blocks.mycelium || (var4.getBlock() == Blocks.dirt && var4.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL) || (worldIn.getLight(p_180671_2_) < 13 && this.canPlaceBlockOn(var4.getBlock()));
        }
        return false;
    }
    
    public boolean func_176485_d(final World worldIn, final BlockPos p_176485_2_, final IBlockState p_176485_3_, final Random p_176485_4_) {
        worldIn.setBlockToAir(p_176485_2_);
        WorldGenBigMushroom var5 = null;
        if (this == Blocks.brown_mushroom) {
            var5 = new WorldGenBigMushroom(0);
        }
        else if (this == Blocks.red_mushroom) {
            var5 = new WorldGenBigMushroom(1);
        }
        if (var5 != null && var5.generate(worldIn, p_176485_4_, p_176485_2_)) {
            return true;
        }
        worldIn.setBlockState(p_176485_2_, p_176485_3_, 3);
        return false;
    }
    
    @Override
    public boolean isStillGrowing(final World worldIn, final BlockPos p_176473_2_, final IBlockState p_176473_3_, final boolean p_176473_4_) {
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random p_180670_2_, final BlockPos p_180670_3_, final IBlockState p_180670_4_) {
        return p_180670_2_.nextFloat() < 0.4;
    }
    
    @Override
    public void grow(final World worldIn, final Random p_176474_2_, final BlockPos p_176474_3_, final IBlockState p_176474_4_) {
        this.func_176485_d(worldIn, p_176474_3_, p_176474_4_, p_176474_2_);
    }
}
