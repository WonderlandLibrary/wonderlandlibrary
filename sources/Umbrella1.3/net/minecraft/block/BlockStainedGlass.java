/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockStainedGlass
extends BlockBreakable {
    public static final PropertyEnum field_176547_a = PropertyEnum.create("color", EnumDyeColor.class);
    private static final String __OBFID = "CL_00000312";

    public BlockStainedGlass(Material p_i45427_1_) {
        super(p_i45427_1_, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176547_a, (Comparable)((Object)EnumDyeColor.WHITE)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumDyeColor)((Object)state.getValue(field_176547_a))).func_176765_a();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (EnumDyeColor var7 : EnumDyeColor.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176765_a()));
        }
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return ((EnumDyeColor)((Object)state.getValue(field_176547_a))).func_176768_e();
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176547_a, (Comparable)((Object)EnumDyeColor.func_176764_b(meta)));
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.func_176450_d(worldIn, pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.func_176450_d(worldIn, pos);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumDyeColor)((Object)state.getValue(field_176547_a))).func_176765_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176547_a);
    }
}

