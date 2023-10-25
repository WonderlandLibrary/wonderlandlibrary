/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConcretePowder
extends BlockFalling {
    public static final PropertyEnum<EnumDyeColor> field_192426_a = PropertyEnum.create("color", EnumDyeColor.class);

    public BlockConcretePowder() {
        super(Material.SAND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_192426_a, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {
        if (p_176502_4_.getMaterial().isLiquid()) {
            worldIn.setBlockState(pos, Blocks.field_192443_dR.getDefaultState().withProperty(BlockColored.COLOR, p_176502_3_.getValue(field_192426_a)), 3);
        }
    }

    protected boolean func_192425_e(World p_192425_1_, BlockPos p_192425_2_, IBlockState p_192425_3_) {
        boolean flag = false;
        for (EnumFacing enumfacing : EnumFacing.values()) {
            BlockPos blockpos;
            if (enumfacing == EnumFacing.DOWN || p_192425_1_.getBlockState(blockpos = p_192425_2_.offset(enumfacing)).getMaterial() != Material.WATER) continue;
            flag = true;
            break;
        }
        if (flag) {
            p_192425_1_.setBlockState(p_192425_2_, Blocks.field_192443_dR.getDefaultState().withProperty(BlockColored.COLOR, p_192425_3_.getValue(field_192426_a)), 3);
        }
        return flag;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        if (!this.func_192425_e(worldIn, pos, state)) {
            super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.func_192425_e(worldIn, pos, state)) {
            super.onBlockAdded(worldIn, pos, state);
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(field_192426_a).getMetadata();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
        for (EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
            tab.add(new ItemStack(this, 1, enumdyecolor.getMetadata()));
        }
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
        return MapColor.func_193558_a(state.getValue(field_192426_a));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_192426_a, EnumDyeColor.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(field_192426_a).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, field_192426_a);
    }
}

