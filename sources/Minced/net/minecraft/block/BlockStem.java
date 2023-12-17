// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import javax.annotation.Nullable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.material.Material;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import java.util.Iterator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;

public class BlockStem extends BlockBush implements IGrowable
{
    public static final PropertyInteger AGE;
    public static final PropertyDirection FACING;
    private final Block crop;
    protected static final AxisAlignedBB[] STEM_AABB;
    
    protected BlockStem(final Block crop) {
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockStem.AGE, 0).withProperty((IProperty<Comparable>)BlockStem.FACING, EnumFacing.UP));
        this.crop = crop;
        this.setTickRandomly(true);
        this.setCreativeTab(null);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockStem.STEM_AABB[state.getValue((IProperty<Integer>)BlockStem.AGE)];
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final int i = state.getValue((IProperty<Integer>)BlockStem.AGE);
        state = state.withProperty((IProperty<Comparable>)BlockStem.FACING, EnumFacing.UP);
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.crop && i == 7) {
                state = state.withProperty((IProperty<Comparable>)BlockStem.FACING, enumfacing);
                break;
            }
        }
        return state;
    }
    
    @Override
    protected boolean canSustainBush(final IBlockState state) {
        return state.getBlock() == Blocks.FARMLAND;
    }
    
    @Override
    public void updateTick(final World worldIn, BlockPos pos, IBlockState state, final Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
            final float f = BlockCrops.getGrowthChance(this, worldIn, pos);
            if (rand.nextInt((int)(25.0f / f) + 1) == 0) {
                final int i = state.getValue((IProperty<Integer>)BlockStem.AGE);
                if (i < 7) {
                    state = state.withProperty((IProperty<Comparable>)BlockStem.AGE, i + 1);
                    worldIn.setBlockState(pos, state, 2);
                }
                else {
                    for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                        if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.crop) {
                            return;
                        }
                    }
                    pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));
                    final Block block = worldIn.getBlockState(pos.down()).getBlock();
                    if (worldIn.getBlockState(pos).getBlock().material == Material.AIR && (block == Blocks.FARMLAND || block == Blocks.DIRT || block == Blocks.GRASS)) {
                        worldIn.setBlockState(pos, this.crop.getDefaultState());
                    }
                }
            }
        }
    }
    
    public void growStem(final World worldIn, final BlockPos pos, final IBlockState state) {
        final int i = state.getValue((IProperty<Integer>)BlockStem.AGE) + MathHelper.getInt(worldIn.rand, 2, 5);
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockStem.AGE, Math.min(7, i)), 2);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        if (!worldIn.isRemote) {
            final Item item = this.getSeedItem();
            if (item != null) {
                final int i = state.getValue((IProperty<Integer>)BlockStem.AGE);
                for (int j = 0; j < 3; ++j) {
                    if (worldIn.rand.nextInt(15) <= i) {
                        Block.spawnAsEntity(worldIn, pos, new ItemStack(item));
                    }
                }
            }
        }
    }
    
    @Nullable
    protected Item getSeedItem() {
        if (this.crop == Blocks.PUMPKIN) {
            return Items.PUMPKIN_SEEDS;
        }
        return (this.crop == Blocks.MELON_BLOCK) ? Items.MELON_SEEDS : null;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.AIR;
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        final Item item = this.getSeedItem();
        return (item == null) ? ItemStack.EMPTY : new ItemStack(item);
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return state.getValue((IProperty<Integer>)BlockStem.AGE) != 7;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        this.growStem(worldIn, pos, state);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockStem.AGE, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockStem.AGE);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockStem.AGE, BlockStem.FACING });
    }
    
    static {
        AGE = PropertyInteger.create("age", 0, 7);
        FACING = BlockTorch.FACING;
        STEM_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.125, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.25, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.375, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.5, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.625, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.75, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.875, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625) };
    }
}
