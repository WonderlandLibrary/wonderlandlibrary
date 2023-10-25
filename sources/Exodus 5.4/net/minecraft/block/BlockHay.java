/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockHay
extends BlockRotatedPillar {
    @Override
    public IBlockState getStateFromMeta(int n) {
        EnumFacing.Axis axis = EnumFacing.Axis.Y;
        int n2 = n & 0xC;
        if (n2 == 4) {
            axis = EnumFacing.Axis.X;
        } else if (n2 == 8) {
            axis = EnumFacing.Axis.Z;
        }
        return this.getDefaultState().withProperty(AXIS, axis);
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return super.onBlockPlaced(world, blockPos, enumFacing, f, f2, f3, n, entityLivingBase).withProperty(AXIS, enumFacing.getAxis());
    }

    public BlockHay() {
        super(Material.grass, MapColor.yellowColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState iBlockState) {
        return new ItemStack(Item.getItemFromBlock(this), 1, 0);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AXIS);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        EnumFacing.Axis axis = (EnumFacing.Axis)iBlockState.getValue(AXIS);
        if (axis == EnumFacing.Axis.X) {
            n |= 4;
        } else if (axis == EnumFacing.Axis.Z) {
            n |= 8;
        }
        return n;
    }
}

