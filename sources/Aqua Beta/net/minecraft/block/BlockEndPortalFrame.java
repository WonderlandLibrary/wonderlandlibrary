package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
    public static final PropertyDirection FACING = PropertyDirection.create((String)"facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool EYE = PropertyBool.create((String)"eye");

    public BlockEndPortalFrame() {
        super(Material.rock, MapColor.greenColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)EYE, (Comparable)Boolean.valueOf((boolean)false)));
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.8125f, 1.0f);
    }

    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.8125f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)EYE)).booleanValue()) {
            this.setBlockBounds(0.3125f, 0.8125f, 0.3125f, 0.6875f, 1.0f, 0.6875f);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        this.setBlockBoundsForItemRender();
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing().getOpposite()).withProperty((IProperty)EYE, (Comparable)Boolean.valueOf((boolean)false));
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return (Boolean)worldIn.getBlockState(pos).getValue((IProperty)EYE) != false ? 15 : 0;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)EYE, (Comparable)Boolean.valueOf(((meta & 4) != 0 ? 1 : 0) != 0)).withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal((int)(meta & 3)));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
        if (((Boolean)state.getValue((IProperty)EYE)).booleanValue()) {
            i |= 4;
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{FACING, EYE});
    }
}
