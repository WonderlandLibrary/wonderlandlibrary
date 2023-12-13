/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
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
    public static final PropertyInteger AGE_PROP = PropertyInteger.create("age", 0, 7);
    public static final PropertyDirection FACING_PROP = PropertyDirection.create("facing", new Predicate(){
        private static final String __OBFID = "CL_00002059";

        public boolean apply(EnumFacing p_177218_1_) {
            return p_177218_1_ != EnumFacing.DOWN;
        }

        public boolean apply(Object p_apply_1_) {
            return this.apply((EnumFacing)p_apply_1_);
        }
    });
    private final Block cropBlock;
    private static final String __OBFID = "CL_00000316";

    protected BlockStem(Block p_i45430_1_) {
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE_PROP, Integer.valueOf(0)).withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.UP)));
        this.cropBlock = p_i45430_1_;
        this.setTickRandomly(true);
        float var2 = 0.125f;
        this.setBlockBounds(0.5f - var2, 0.0f, 0.5f - var2, 0.5f + var2, 0.25f, 0.5f + var2);
        this.setCreativeTab(null);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        state = state.withProperty(FACING_PROP, (Comparable)((Object)EnumFacing.UP));
        for (EnumFacing var5 : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset(var5)).getBlock() != this.cropBlock) continue;
            state = state.withProperty(FACING_PROP, (Comparable)((Object)var5));
            break;
        }
        return state;
    }

    @Override
    protected boolean canPlaceBlockOn(Block ground) {
        return ground == Blocks.farmland;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        float var5;
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.getLightFromNeighbors(pos.offsetUp()) >= 9 && rand.nextInt((int)(25.0f / (var5 = BlockCrops.getGrowthChance(this, worldIn, pos))) + 1) == 0) {
            int var6 = (Integer)state.getValue(AGE_PROP);
            if (var6 < 7) {
                state = state.withProperty(AGE_PROP, Integer.valueOf(var6 + 1));
                worldIn.setBlockState(pos, state, 2);
            } else {
                for (EnumFacing var8 : EnumFacing.Plane.HORIZONTAL) {
                    if (worldIn.getBlockState(pos.offset(var8)).getBlock() != this.cropBlock) continue;
                    return;
                }
                pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));
                Block var9 = worldIn.getBlockState(pos.offsetDown()).getBlock();
                if (worldIn.getBlockState((BlockPos)pos).getBlock().blockMaterial == Material.air && (var9 == Blocks.farmland || var9 == Blocks.dirt || var9 == Blocks.grass)) {
                    worldIn.setBlockState(pos, this.cropBlock.getDefaultState());
                }
            }
        }
    }

    public void growStem(World worldIn, BlockPos p_176482_2_, IBlockState p_176482_3_) {
        int var4 = (Integer)p_176482_3_.getValue(AGE_PROP) + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
        worldIn.setBlockState(p_176482_2_, p_176482_3_.withProperty(AGE_PROP, Integer.valueOf(Math.min(7, var4))), 2);
    }

    @Override
    public int getRenderColor(IBlockState state) {
        if (state.getBlock() != this) {
            return super.getRenderColor(state);
        }
        int var2 = (Integer)state.getValue(AGE_PROP);
        int var3 = var2 * 32;
        int var4 = 255 - var2 * 8;
        int var5 = var2 * 4;
        return var3 << 16 | var4 << 8 | var5;
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return this.getRenderColor(worldIn.getBlockState(pos));
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float var1 = 0.125f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, 0.25f, 0.5f + var1);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        this.maxY = (float)((Integer)access.getBlockState(pos).getValue(AGE_PROP) * 2 + 2) / 16.0f;
        float var3 = 0.125f;
        this.setBlockBounds(0.5f - var3, 0.0f, 0.5f - var3, 0.5f + var3, (float)this.maxY, 0.5f + var3);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        Item var6;
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        if (!worldIn.isRemote && (var6 = this.getSeedItem()) != null) {
            int var7 = (Integer)state.getValue(AGE_PROP);
            for (int var8 = 0; var8 < 3; ++var8) {
                if (worldIn.rand.nextInt(15) > var7) continue;
                BlockStem.spawnAsEntity(worldIn, pos, new ItemStack(var6));
            }
        }
    }

    protected Item getSeedItem() {
        return this.cropBlock == Blocks.pumpkin ? Items.pumpkin_seeds : (this.cropBlock == Blocks.melon_block ? Items.melon_seeds : null);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        Item var3 = this.getSeedItem();
        return var3 != null ? var3 : null;
    }

    @Override
    public boolean isStillGrowing(World worldIn, BlockPos p_176473_2_, IBlockState p_176473_3_, boolean p_176473_4_) {
        return (Integer)p_176473_3_.getValue(AGE_PROP) != 7;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random p_180670_2_, BlockPos p_180670_3_, IBlockState p_180670_4_) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_) {
        this.growStem(worldIn, p_176474_3_, p_176474_4_);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE_PROP, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue(AGE_PROP);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AGE_PROP, FACING_PROP);
    }
}

