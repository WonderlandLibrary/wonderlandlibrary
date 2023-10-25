/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public class BlockGrass
extends Block
implements IGrowable {
    public static final PropertyBool SNOWY = PropertyBool.create("snowy");

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), random, n);
    }

    protected BlockGrass() {
        super(Material.grass);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return 0;
    }

    @Override
    public int getRenderColor(IBlockState iBlockState) {
        return this.getBlockColor();
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (!world.isRemote) {
            if (world.getLightFromNeighbors(blockPos.up()) < 4 && world.getBlockState(blockPos.up()).getBlock().getLightOpacity() > 2) {
                world.setBlockState(blockPos, Blocks.dirt.getDefaultState());
            } else if (world.getLightFromNeighbors(blockPos.up()) >= 9) {
                int n = 0;
                while (n < 4) {
                    BlockPos blockPos2 = blockPos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    Block block = world.getBlockState(blockPos2.up()).getBlock();
                    IBlockState iBlockState2 = world.getBlockState(blockPos2);
                    if (iBlockState2.getBlock() == Blocks.dirt && iBlockState2.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && world.getLightFromNeighbors(blockPos2.up()) >= 4 && block.getLightOpacity() <= 2) {
                        world.setBlockState(blockPos2, Blocks.grass.getDefaultState());
                    }
                    ++n;
                }
            }
        }
    }

    @Override
    public boolean canGrow(World world, BlockPos blockPos, IBlockState iBlockState, boolean bl) {
        return true;
    }

    @Override
    public void grow(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        BlockPos blockPos2 = blockPos.up();
        int n = 0;
        while (n < 128) {
            BlockPos blockPos3 = blockPos2;
            int n2 = 0;
            while (true) {
                if (n2 >= n / 16) {
                    Object object;
                    if (world.getBlockState((BlockPos)blockPos3).getBlock().blockMaterial != Material.air) break;
                    if (random.nextInt(8) == 0) {
                        IBlockState iBlockState2;
                        object = world.getBiomeGenForCoords(blockPos3).pickRandomFlower(random, blockPos3);
                        BlockFlower blockFlower = ((BlockFlower.EnumFlowerType)object).getBlockType().getBlock();
                        if (!blockFlower.canBlockStay(world, blockPos3, iBlockState2 = blockFlower.getDefaultState().withProperty(blockFlower.getTypeProperty(), object))) break;
                        world.setBlockState(blockPos3, iBlockState2, 3);
                        break;
                    }
                    object = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
                    if (!Blocks.tallgrass.canBlockStay(world, blockPos3, (IBlockState)object)) break;
                    world.setBlockState(blockPos3, (IBlockState)object, 3);
                    break;
                }
                if (world.getBlockState((blockPos3 = blockPos3.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1)).down()).getBlock() != Blocks.grass || world.getBlockState(blockPos3).getBlock().isNormalCube()) break;
                ++n2;
            }
            ++n;
        }
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        return true;
    }

    @Override
    public int colorMultiplier(IBlockAccess iBlockAccess, BlockPos blockPos, int n) {
        return BiomeColorHelper.getGrassColorAtPos(iBlockAccess, blockPos);
    }

    @Override
    public int getBlockColor() {
        return ColorizerGrass.getGrassColor(0.5, 1.0);
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
}

