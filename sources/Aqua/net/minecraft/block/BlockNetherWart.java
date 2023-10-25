package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
    public static final PropertyInteger AGE = PropertyInteger.create((String)"age", (int)0, (int)3);

    protected BlockNetherWart() {
        super(Material.plants, MapColor.redColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AGE, (Comparable)Integer.valueOf((int)0)));
        this.setTickRandomly(true);
        float f = 0.5f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.25f, 0.5f + f);
        this.setCreativeTab(null);
    }

    protected boolean canPlaceBlockOn(Block ground) {
        return ground == Blocks.soul_sand;
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        return this.canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int i = (Integer)state.getValue((IProperty)AGE);
        if (i < 3 && rand.nextInt(10) == 0) {
            state = state.withProperty((IProperty)AGE, (Comparable)Integer.valueOf((int)(i + 1)));
            worldIn.setBlockState(pos, state, 2);
        }
        super.updateTick(worldIn, pos, state, rand);
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote) {
            int i = 1;
            if ((Integer)state.getValue((IProperty)AGE) >= 3) {
                i = 2 + worldIn.rand.nextInt(3);
                if (fortune > 0) {
                    i += worldIn.rand.nextInt(fortune + 1);
                }
            }
            for (int j = 0; j < i; ++j) {
                BlockNetherWart.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack(Items.nether_wart));
            }
        }
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Items.nether_wart;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)AGE, (Comparable)Integer.valueOf((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue((IProperty)AGE);
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{AGE});
    }
}
