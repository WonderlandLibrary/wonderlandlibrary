// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCrops extends BlockBush implements IGrowable
{
    public static final PropertyInteger AGE;
    private static final String __OBFID = "CL_00000222";
    
    protected BlockCrops() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockCrops.AGE, 0));
        this.setTickRandomly(true);
        final float var1 = 0.5f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, 0.25f, 0.5f + var1);
        this.setCreativeTab(null);
        this.setHardness(0.0f);
        this.setStepSound(Block.soundTypeGrass);
        this.disableStats();
    }
    
    @Override
    protected boolean canPlaceBlockOn(final Block ground) {
        return ground == Blocks.farmland;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.getLightFromNeighbors(pos.offsetUp()) >= 9) {
            final int var5 = (int)state.getValue(BlockCrops.AGE);
            if (var5 < 7) {
                final float var6 = getGrowthChance(this, worldIn, pos);
                if (rand.nextInt((int)(25.0f / var6) + 1) == 0) {
                    worldIn.setBlockState(pos, state.withProperty(BlockCrops.AGE, var5 + 1), 2);
                }
            }
        }
    }
    
    public void growCrops(final World worldIn, final BlockPos pos, final IBlockState state) {
        int var4 = (int)state.getValue(BlockCrops.AGE) + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
        if (var4 > 7) {
            var4 = 7;
        }
        worldIn.setBlockState(pos, state.withProperty(BlockCrops.AGE, var4), 2);
    }
    
    protected static float getGrowthChance(final Block p_180672_0_, final World worldIn, final BlockPos p_180672_2_) {
        float var3 = 1.0f;
        final BlockPos var4 = p_180672_2_.offsetDown();
        for (int var5 = -1; var5 <= 1; ++var5) {
            for (int var6 = -1; var6 <= 1; ++var6) {
                float var7 = 0.0f;
                final IBlockState var8 = worldIn.getBlockState(var4.add(var5, 0, var6));
                if (var8.getBlock() == Blocks.farmland) {
                    var7 = 1.0f;
                    if ((int)var8.getValue(BlockFarmland.field_176531_a) > 0) {
                        var7 = 3.0f;
                    }
                }
                if (var5 != 0 || var6 != 0) {
                    var7 /= 4.0f;
                }
                var3 += var7;
            }
        }
        final BlockPos var9 = p_180672_2_.offsetNorth();
        final BlockPos var10 = p_180672_2_.offsetSouth();
        final BlockPos var11 = p_180672_2_.offsetWest();
        final BlockPos var12 = p_180672_2_.offsetEast();
        final boolean var13 = p_180672_0_ == worldIn.getBlockState(var11).getBlock() || p_180672_0_ == worldIn.getBlockState(var12).getBlock();
        final boolean var14 = p_180672_0_ == worldIn.getBlockState(var9).getBlock() || p_180672_0_ == worldIn.getBlockState(var10).getBlock();
        if (var13 && var14) {
            var3 /= 2.0f;
        }
        else {
            final boolean var15 = p_180672_0_ == worldIn.getBlockState(var11.offsetNorth()).getBlock() || p_180672_0_ == worldIn.getBlockState(var12.offsetNorth()).getBlock() || p_180672_0_ == worldIn.getBlockState(var12.offsetSouth()).getBlock() || p_180672_0_ == worldIn.getBlockState(var11.offsetSouth()).getBlock();
            if (var15) {
                var3 /= 2.0f;
            }
        }
        return var3;
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos p_180671_2_, final IBlockState p_180671_3_) {
        return (worldIn.getLight(p_180671_2_) >= 8 || worldIn.isAgainstSky(p_180671_2_)) && this.canPlaceBlockOn(worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock());
    }
    
    protected Item getSeed() {
        return Items.wheat_seeds;
    }
    
    protected Item getCrop() {
        return Items.wheat;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        if (!worldIn.isRemote) {
            final int var6 = (int)state.getValue(BlockCrops.AGE);
            if (var6 >= 7) {
                for (int var7 = 3 + fortune, var8 = 0; var8 < var7; ++var8) {
                    if (worldIn.rand.nextInt(15) <= var6) {
                        Block.spawnAsEntity(worldIn, pos, new ItemStack(this.getSeed(), 1, 0));
                    }
                }
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return ((int)state.getValue(BlockCrops.AGE) == 7) ? this.getCrop() : this.getSeed();
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return this.getSeed();
    }
    
    @Override
    public boolean isStillGrowing(final World worldIn, final BlockPos pos, final IBlockState state, final boolean unused) {
        return (int)state.getValue(BlockCrops.AGE) < 7;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random p_180670_2_, final BlockPos p_180670_3_, final IBlockState p_180670_4_) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random p_176474_2_, final BlockPos p_176474_3_, final IBlockState p_176474_4_) {
        this.growCrops(worldIn, p_176474_3_, p_176474_4_);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockCrops.AGE, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockCrops.AGE);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCrops.AGE });
    }
    
    static {
        AGE = PropertyInteger.create("age", 0, 7);
    }
}
