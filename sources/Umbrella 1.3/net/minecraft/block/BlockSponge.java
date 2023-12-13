/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;

public class BlockSponge
extends Block {
    public static final PropertyBool WET_PROP = PropertyBool.create("wet");
    private static final String __OBFID = "CL_00000311";

    protected BlockSponge() {
        super(Material.sponge);
        this.setDefaultState(this.blockState.getBaseState().withProperty(WET_PROP, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return (Boolean)state.getValue(WET_PROP) != false ? 1 : 0;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.setWet(worldIn, pos, state);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.setWet(worldIn, pos, state);
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    protected void setWet(World worldIn, BlockPos p_176311_2_, IBlockState p_176311_3_) {
        if (!((Boolean)p_176311_3_.getValue(WET_PROP)).booleanValue() && this.absorbWater(worldIn, p_176311_2_)) {
            worldIn.setBlockState(p_176311_2_, p_176311_3_.withProperty(WET_PROP, Boolean.valueOf(true)), 2);
            worldIn.playAuxSFX(2001, p_176311_2_, Block.getIdFromBlock(Blocks.water));
        }
    }

    private boolean absorbWater(World worldIn, BlockPos p_176312_2_) {
        LinkedList var3 = Lists.newLinkedList();
        ArrayList var4 = Lists.newArrayList();
        var3.add(new Tuple(p_176312_2_, 0));
        int var5 = 0;
        while (!var3.isEmpty()) {
            Tuple var6 = (Tuple)var3.poll();
            BlockPos var7 = (BlockPos)var6.getFirst();
            int var8 = (Integer)var6.getSecond();
            for (EnumFacing var12 : EnumFacing.values()) {
                BlockPos var13 = var7.offset(var12);
                if (worldIn.getBlockState(var13).getBlock().getMaterial() != Material.water) continue;
                worldIn.setBlockState(var13, Blocks.air.getDefaultState(), 2);
                var4.add(var13);
                ++var5;
                if (var8 >= 6) continue;
                var3.add(new Tuple(var13, var8 + 1));
            }
            if (var5 > 64) break;
        }
        for (BlockPos var7 : var4) {
            worldIn.notifyNeighborsOfStateChange(var7, Blocks.air);
        }
        return var5 > 0;
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(WET_PROP, Boolean.valueOf((meta & 1) == 1));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (Boolean)state.getValue(WET_PROP) != false ? 1 : 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, WET_PROP);
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        EnumFacing var5;
        if (((Boolean)state.getValue(WET_PROP)).booleanValue() && (var5 = EnumFacing.random(rand)) != EnumFacing.UP && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offset(var5))) {
            double var6 = pos.getX();
            double var8 = pos.getY();
            double var10 = pos.getZ();
            if (var5 == EnumFacing.DOWN) {
                var8 -= 0.05;
                var6 += rand.nextDouble();
                var10 += rand.nextDouble();
            } else {
                var8 += rand.nextDouble() * 0.8;
                if (var5.getAxis() == EnumFacing.Axis.X) {
                    var10 += rand.nextDouble();
                    var6 = var5 == EnumFacing.EAST ? (var6 += 1.0) : (var6 += 0.05);
                } else {
                    var6 += rand.nextDouble();
                    var10 = var5 == EnumFacing.SOUTH ? (var10 += 1.0) : (var10 += 0.05);
                }
            }
            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, var6, var8, var10, 0.0, 0.0, 0.0, new int[0]);
        }
    }
}

