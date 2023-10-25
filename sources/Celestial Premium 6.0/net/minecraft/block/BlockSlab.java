/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockSlab
extends Block {
    public static final PropertyEnum<EnumBlockHalf> HALF = PropertyEnum.create("half", EnumBlockHalf.class);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
    protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);

    public BlockSlab(Material materialIn) {
        this(materialIn, materialIn.getMaterialMapColor());
    }

    public BlockSlab(Material p_i47249_1_, MapColor p_i47249_2_) {
        super(p_i47249_1_, p_i47249_2_);
        this.fullBlock = this.isDouble();
        this.setLightOpacity(255);
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (this.isDouble()) {
            return FULL_BLOCK_AABB;
        }
        return state.getValue(HALF) == EnumBlockHalf.TOP ? AABB_TOP_HALF : AABB_BOTTOM_HALF;
    }

    @Override
    public boolean isFullyOpaque(IBlockState state) {
        return ((BlockSlab)state.getBlock()).isDouble() || state.getValue(HALF) == EnumBlockHalf.TOP;
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        if (((BlockSlab)p_193383_2_.getBlock()).isDouble()) {
            return BlockFaceShape.SOLID;
        }
        if (p_193383_4_ == EnumFacing.UP && p_193383_2_.getValue(HALF) == EnumBlockHalf.TOP) {
            return BlockFaceShape.SOLID;
        }
        return p_193383_4_ == EnumFacing.DOWN && p_193383_2_.getValue(HALF) == EnumBlockHalf.BOTTOM ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return this.isDouble();
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(HALF, EnumBlockHalf.BOTTOM);
        if (this.isDouble()) {
            return iblockstate;
        }
        return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5) ? iblockstate : iblockstate.withProperty(HALF, EnumBlockHalf.TOP);
    }

    @Override
    public int quantityDropped(Random random) {
        return this.isDouble() ? 2 : 1;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return this.isDouble();
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        boolean flag1;
        if (this.isDouble()) {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
        if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(blockState, blockAccess, pos, side)) {
            return false;
        }
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        boolean flag = BlockSlab.isHalfSlab(iblockstate) && iblockstate.getValue(HALF) == EnumBlockHalf.TOP;
        boolean bl = flag1 = BlockSlab.isHalfSlab(blockState) && blockState.getValue(HALF) == EnumBlockHalf.TOP;
        if (flag1) {
            if (side == EnumFacing.DOWN) {
                return true;
            }
            if (side == EnumFacing.UP && super.shouldSideBeRendered(blockState, blockAccess, pos, side)) {
                return true;
            }
            return !BlockSlab.isHalfSlab(iblockstate) || !flag;
        }
        if (side == EnumFacing.UP) {
            return true;
        }
        if (side == EnumFacing.DOWN && super.shouldSideBeRendered(blockState, blockAccess, pos, side)) {
            return true;
        }
        return !BlockSlab.isHalfSlab(iblockstate) || flag;
    }

    protected static boolean isHalfSlab(IBlockState state) {
        Block block = state.getBlock();
        return block == Blocks.STONE_SLAB || block == Blocks.WOODEN_SLAB || block == Blocks.STONE_SLAB2 || block == Blocks.PURPUR_SLAB;
    }

    public abstract String getUnlocalizedName(int var1);

    public abstract boolean isDouble();

    public abstract IProperty<?> getVariantProperty();

    public abstract Comparable<?> getTypeForItem(ItemStack var1);

    public static enum EnumBlockHalf implements IStringSerializable
    {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        private EnumBlockHalf(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

