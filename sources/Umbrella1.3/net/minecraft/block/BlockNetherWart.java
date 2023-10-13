/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockNetherWart
extends BlockBush {
    public static final PropertyInteger AGE_PROP = PropertyInteger.create("age", 0, 3);
    private static final String __OBFID = "CL_00000274";

    protected BlockNetherWart() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE_PROP, Integer.valueOf(0)));
        this.setTickRandomly(true);
        float var1 = 0.5f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, 0.25f, 0.5f + var1);
        this.setCreativeTab(null);
    }

    @Override
    protected boolean canPlaceBlockOn(Block ground) {
        return ground == Blocks.soul_sand;
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos p_180671_2_, IBlockState p_180671_3_) {
        return this.canPlaceBlockOn(worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock());
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int var5 = (Integer)state.getValue(AGE_PROP);
        if (var5 < 3 && rand.nextInt(10) == 0) {
            state = state.withProperty(AGE_PROP, Integer.valueOf(var5 + 1));
            worldIn.setBlockState(pos, state, 2);
        }
        super.updateTick(worldIn, pos, state, rand);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote) {
            int var6 = 1;
            if ((Integer)state.getValue(AGE_PROP) >= 3) {
                var6 = 2 + worldIn.rand.nextInt(3);
                if (fortune > 0) {
                    var6 += worldIn.rand.nextInt(fortune + 1);
                }
            }
            for (int var7 = 0; var7 < var6; ++var7) {
                BlockNetherWart.spawnAsEntity(worldIn, pos, new ItemStack(Items.nether_wart));
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.nether_wart;
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
        return new BlockState(this, AGE_PROP);
    }
}

