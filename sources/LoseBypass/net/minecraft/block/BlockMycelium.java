/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMycelium
extends Block {
    public static final PropertyBool SNOWY = PropertyBool.create("snowy");

    protected BlockMycelium() {
        super(Material.grass, MapColor.purpleColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        boolean bl;
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        if (block != Blocks.snow && block != Blocks.snow_layer) {
            bl = false;
            return state.withProperty(SNOWY, bl);
        }
        bl = true;
        return state.withProperty(SNOWY, bl);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.isRemote) return;
        if (worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getBlock().getLightOpacity() > 2) {
            worldIn.setBlockState(pos, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
            return;
        }
        if (worldIn.getLightFromNeighbors(pos.up()) < 9) return;
        int i = 0;
        while (i < 4) {
            BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            Block block = worldIn.getBlockState(blockpos.up()).getBlock();
            if (iblockstate.getBlock() == Blocks.dirt && iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(blockpos.up()) >= 4 && block.getLightOpacity() <= 2) {
                worldIn.setBlockState(blockpos, this.getDefaultState());
            }
            ++i;
        }
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.randomDisplayTick(worldIn, pos, state, rand);
        if (rand.nextInt(10) != 0) return;
        worldIn.spawnParticle(EnumParticleTypes.TOWN_AURA, (float)pos.getX() + rand.nextFloat(), (double)((float)pos.getY() + 1.1f), (double)((float)pos.getZ() + rand.nextFloat()), 0.0, 0.0, 0.0, new int[0]);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, SNOWY);
    }
}

