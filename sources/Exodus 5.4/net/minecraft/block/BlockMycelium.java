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

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (!world.isRemote) {
            if (world.getLightFromNeighbors(blockPos.up()) < 4 && world.getBlockState(blockPos.up()).getBlock().getLightOpacity() > 2) {
                world.setBlockState(blockPos, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
            } else if (world.getLightFromNeighbors(blockPos.up()) >= 9) {
                int n = 0;
                while (n < 4) {
                    BlockPos blockPos2 = blockPos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    IBlockState iBlockState2 = world.getBlockState(blockPos2);
                    Block block = world.getBlockState(blockPos2.up()).getBlock();
                    if (iBlockState2.getBlock() == Blocks.dirt && iBlockState2.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && world.getLightFromNeighbors(blockPos2.up()) >= 4 && block.getLightOpacity() <= 2) {
                        world.setBlockState(blockPos2, this.getDefaultState());
                    }
                    ++n;
                }
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), random, n);
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        super.randomDisplayTick(world, blockPos, iBlockState, random);
        if (random.nextInt(10) == 0) {
            world.spawnParticle(EnumParticleTypes.TOWN_AURA, (float)blockPos.getX() + random.nextFloat(), (double)((float)blockPos.getY() + 1.1f), (double)((float)blockPos.getZ() + random.nextFloat()), 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        Block block = iBlockAccess.getBlockState(blockPos.up()).getBlock();
        return iBlockState.withProperty(SNOWY, block == Blocks.snow || block == Blocks.snow_layer);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, SNOWY);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return 0;
    }

    protected BlockMycelium() {
        super(Material.grass, MapColor.purpleColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
}

