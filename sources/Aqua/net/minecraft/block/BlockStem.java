package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStem
extends BlockBush
implements IGrowable {
    public static final PropertyInteger AGE = PropertyInteger.create((String)"age", (int)0, (int)7);
    public static final PropertyDirection FACING = PropertyDirection.create((String)"facing", (Predicate)new /* Unavailable Anonymous Inner Class!! */);
    private final Block crop;

    protected BlockStem(Block crop) {
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AGE, (Comparable)Integer.valueOf((int)0)).withProperty((IProperty)FACING, (Comparable)EnumFacing.UP));
        this.crop = crop;
        this.setTickRandomly(true);
        float f = 0.125f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.25f, 0.5f + f);
        this.setCreativeTab(null);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        state = state.withProperty((IProperty)FACING, (Comparable)EnumFacing.UP);
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this.crop) continue;
            state = state.withProperty((IProperty)FACING, (Comparable)enumfacing);
            break;
        }
        return state;
    }

    protected boolean canPlaceBlockOn(Block ground) {
        return ground == Blocks.farmland;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        float f;
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt((int)(25.0f / (f = BlockCrops.getGrowthChance((Block)this, (World)worldIn, (BlockPos)pos))) + 1) == 0) {
            int i = (Integer)state.getValue((IProperty)AGE);
            if (i < 7) {
                state = state.withProperty((IProperty)AGE, (Comparable)Integer.valueOf((int)(i + 1)));
                worldIn.setBlockState(pos, state, 2);
            } else {
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                    if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this.crop) continue;
                    return;
                }
                pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));
                Block block = worldIn.getBlockState(pos.down()).getBlock();
                if (worldIn.getBlockState((BlockPos)pos).getBlock().blockMaterial == Material.air && (block == Blocks.farmland || block == Blocks.dirt || block == Blocks.grass)) {
                    worldIn.setBlockState(pos, this.crop.getDefaultState());
                }
            }
        }
    }

    public void growStem(World worldIn, BlockPos pos, IBlockState state) {
        int i = (Integer)state.getValue((IProperty)AGE) + MathHelper.getRandomIntegerInRange((Random)worldIn.rand, (int)2, (int)5);
        worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, (Comparable)Integer.valueOf((int)Math.min((int)7, (int)i))), 2);
    }

    public int getRenderColor(IBlockState state) {
        if (state.getBlock() != this) {
            return super.getRenderColor(state);
        }
        int i = (Integer)state.getValue((IProperty)AGE);
        int j = i * 32;
        int k = 255 - i * 8;
        int l = i * 4;
        return j << 16 | k << 8 | l;
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return this.getRenderColor(worldIn.getBlockState(pos));
    }

    public void setBlockBoundsForItemRender() {
        float f = 0.125f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.25f, 0.5f + f);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.maxY = (float)((Integer)worldIn.getBlockState(pos).getValue((IProperty)AGE) * 2 + 2) / 16.0f;
        float f = 0.125f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, (float)this.maxY, 0.5f + f);
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        Item item;
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        if (!worldIn.isRemote && (item = this.getSeedItem()) != null) {
            int i = (Integer)state.getValue((IProperty)AGE);
            for (int j = 0; j < 3; ++j) {
                if (worldIn.rand.nextInt(15) > i) continue;
                BlockStem.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack(item));
            }
        }
    }

    protected Item getSeedItem() {
        return this.crop == Blocks.pumpkin ? Items.pumpkin_seeds : (this.crop == Blocks.melon_block ? Items.melon_seeds : null);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        Item item = this.getSeedItem();
        return item != null ? item : null;
    }

    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return (Integer)state.getValue((IProperty)AGE) != 7;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        this.growStem(worldIn, pos, state);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)AGE, (Comparable)Integer.valueOf((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue((IProperty)AGE);
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{AGE, FACING});
    }
}
