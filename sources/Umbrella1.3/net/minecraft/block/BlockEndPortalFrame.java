/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockEndPortalFrame
extends Block {
    public static final PropertyDirection field_176508_a = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool field_176507_b = PropertyBool.create("eye");
    private static final String __OBFID = "CL_00000237";

    public BlockEndPortalFrame() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176508_a, (Comparable)((Object)EnumFacing.NORTH)).withProperty(field_176507_b, Boolean.valueOf(false)));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.8125f, 1.0f);
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.8125f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        if (((Boolean)worldIn.getBlockState(pos).getValue(field_176507_b)).booleanValue()) {
            this.setBlockBounds(0.3125f, 0.8125f, 0.3125f, 0.6875f, 1.0f, 0.6875f);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        this.setBlockBoundsForItemRender();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(field_176508_a, (Comparable)((Object)placer.func_174811_aO().getOpposite())).withProperty(field_176507_b, Boolean.valueOf(false));
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return (Boolean)worldIn.getBlockState(pos).getValue(field_176507_b) != false ? 15 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176507_b, Boolean.valueOf((meta & 4) != 0)).withProperty(field_176508_a, (Comparable)((Object)EnumFacing.getHorizontal(meta & 3)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((EnumFacing)((Object)state.getValue(field_176508_a))).getHorizontalIndex();
        if (((Boolean)state.getValue(field_176507_b)).booleanValue()) {
            var3 |= 4;
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176508_a, field_176507_b);
    }
}

