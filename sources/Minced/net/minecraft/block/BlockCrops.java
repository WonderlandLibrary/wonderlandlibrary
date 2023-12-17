// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCrops extends BlockBush implements IGrowable
{
    public static final PropertyInteger AGE;
    private static final AxisAlignedBB[] CROPS_AABB;
    
    protected BlockCrops() {
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)this.getAgeProperty(), 0));
        this.setTickRandomly(true);
        this.setCreativeTab(null);
        this.setHardness(0.0f);
        this.setSoundType(SoundType.PLANT);
        this.disableStats();
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockCrops.CROPS_AABB[state.getValue((IProperty<Integer>)this.getAgeProperty())];
    }
    
    @Override
    protected boolean canSustainBush(final IBlockState state) {
        return state.getBlock() == Blocks.FARMLAND;
    }
    
    protected PropertyInteger getAgeProperty() {
        return BlockCrops.AGE;
    }
    
    public int getMaxAge() {
        return 7;
    }
    
    protected int getAge(final IBlockState state) {
        return state.getValue((IProperty<Integer>)this.getAgeProperty());
    }
    
    public IBlockState withAge(final int age) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)this.getAgeProperty(), age);
    }
    
    public boolean isMaxAge(final IBlockState state) {
        return state.getValue((IProperty<Integer>)this.getAgeProperty()) >= this.getMaxAge();
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
            final int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                final float f = getGrowthChance(this, worldIn, pos);
                if (rand.nextInt((int)(25.0f / f) + 1) == 0) {
                    worldIn.setBlockState(pos, this.withAge(i + 1), 2);
                }
            }
        }
    }
    
    public void grow(final World worldIn, final BlockPos pos, final IBlockState state) {
        int i = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
        final int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        worldIn.setBlockState(pos, this.withAge(i), 2);
    }
    
    protected int getBonemealAgeIncrease(final World worldIn) {
        return MathHelper.getInt(worldIn.rand, 2, 5);
    }
    
    protected static float getGrowthChance(final Block blockIn, final World worldIn, final BlockPos pos) {
        float f = 1.0f;
        final BlockPos blockpos = pos.down();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f2 = 0.0f;
                final IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));
                if (iblockstate.getBlock() == Blocks.FARMLAND) {
                    f2 = 1.0f;
                    if (iblockstate.getValue((IProperty<Integer>)BlockFarmland.MOISTURE) > 0) {
                        f2 = 3.0f;
                    }
                }
                if (i != 0 || j != 0) {
                    f2 /= 4.0f;
                }
                f += f2;
            }
        }
        final BlockPos blockpos2 = pos.north();
        final BlockPos blockpos3 = pos.south();
        final BlockPos blockpos4 = pos.west();
        final BlockPos blockpos5 = pos.east();
        final boolean flag = blockIn == worldIn.getBlockState(blockpos4).getBlock() || blockIn == worldIn.getBlockState(blockpos5).getBlock();
        final boolean flag2 = blockIn == worldIn.getBlockState(blockpos2).getBlock() || blockIn == worldIn.getBlockState(blockpos3).getBlock();
        if (flag && flag2) {
            f /= 2.0f;
        }
        else {
            final boolean flag3 = blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos5.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos5.south()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock();
            if (flag3) {
                f /= 2.0f;
            }
        }
        return f;
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos pos, final IBlockState state) {
        return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) && this.canSustainBush(worldIn.getBlockState(pos.down()));
    }
    
    protected Item getSeed() {
        return Items.WHEAT_SEEDS;
    }
    
    protected Item getCrop() {
        return Items.WHEAT;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        if (!worldIn.isRemote) {
            final int i = this.getAge(state);
            if (i >= this.getMaxAge()) {
                for (int j = 3 + fortune, k = 0; k < j; ++k) {
                    if (worldIn.rand.nextInt(2 * this.getMaxAge()) <= i) {
                        Block.spawnAsEntity(worldIn, pos, new ItemStack(this.getSeed()));
                    }
                }
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return this.isMaxAge(state) ? this.getCrop() : this.getSeed();
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(this.getSeed());
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return !this.isMaxAge(state);
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        this.grow(worldIn, pos, state);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.withAge(meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return this.getAge(state);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockCrops.AGE });
    }
    
    static {
        AGE = PropertyInteger.create("age", 0, 7);
        CROPS_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.375, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.625, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.75, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.875, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) };
    }
}
