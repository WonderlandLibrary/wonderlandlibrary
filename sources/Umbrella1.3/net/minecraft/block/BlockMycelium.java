/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
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
    public static final PropertyBool SNOWY_PROP = PropertyBool.create("snowy");
    private static final String __OBFID = "CL_00000273";

    protected BlockMycelium() {
        super(Material.grass);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY_PROP, Boolean.valueOf(false)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        Block var4 = worldIn.getBlockState(pos.offsetUp()).getBlock();
        return state.withProperty(SNOWY_PROP, Boolean.valueOf(var4 == Blocks.snow || var4 == Blocks.snow_layer));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (worldIn.getLightFromNeighbors(pos.offsetUp()) < 4 && worldIn.getBlockState(pos.offsetUp()).getBlock().getLightOpacity() > 2) {
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, (Comparable)((Object)BlockDirt.DirtType.DIRT)));
            } else if (worldIn.getLightFromNeighbors(pos.offsetUp()) >= 9) {
                for (int var5 = 0; var5 < 4; ++var5) {
                    BlockPos var6 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                    IBlockState var7 = worldIn.getBlockState(var6);
                    Block var8 = worldIn.getBlockState(var6.offsetUp()).getBlock();
                    if (var7.getBlock() != Blocks.dirt || var7.getValue(BlockDirt.VARIANT) != BlockDirt.DirtType.DIRT || worldIn.getLightFromNeighbors(var6.offsetUp()) < 4 || var8.getLightOpacity() > 2) continue;
                    worldIn.setBlockState(var6, this.getDefaultState());
                }
            }
        }
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.randomDisplayTick(worldIn, pos, state, rand);
        if (rand.nextInt(10) == 0) {
            worldIn.spawnParticle(EnumParticleTypes.TOWN_AURA, (float)pos.getX() + rand.nextFloat(), (double)((float)pos.getY() + 1.1f), (double)((float)pos.getZ() + rand.nextFloat()), 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, (Comparable)((Object)BlockDirt.DirtType.DIRT)), rand, fortune);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, SNOWY_PROP);
    }
}

