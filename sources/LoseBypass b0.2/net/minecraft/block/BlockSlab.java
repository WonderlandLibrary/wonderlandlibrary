/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockSlab
extends Block {
    public static final PropertyEnum<EnumBlockHalf> HALF = PropertyEnum.create("half", EnumBlockHalf.class);

    public BlockSlab(Material materialIn) {
        super(materialIn);
        if (this.isDouble()) {
            this.fullBlock = true;
        } else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
        this.setLightOpacity(255);
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        if (this.isDouble()) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            return;
        }
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() != this) return;
        if (iblockstate.getValue(HALF) == EnumBlockHalf.TOP) {
            this.setBlockBounds(0.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f);
            return;
        }
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        if (this.isDouble()) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            return;
        }
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    @Override
    public boolean isOpaqueCube() {
        return this.isDouble();
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iBlockState;
        IBlockState iblockstate = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(HALF, EnumBlockHalf.BOTTOM);
        if (this.isDouble()) {
            iBlockState = iblockstate;
            return iBlockState;
        }
        if (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5)) {
            iBlockState = iblockstate;
            return iBlockState;
        }
        iBlockState = iblockstate.withProperty(HALF, EnumBlockHalf.TOP);
        return iBlockState;
    }

    @Override
    public int quantityDropped(Random random) {
        if (!this.isDouble()) return 1;
        return 2;
    }

    @Override
    public boolean isFullCube() {
        return this.isDouble();
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        boolean flag1;
        if (this.isDouble()) {
            return super.shouldSideBeRendered(worldIn, pos, side);
        }
        if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(worldIn, pos, side)) {
            return false;
        }
        BlockPos blockpos = pos.offset(side.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(pos);
        IBlockState iblockstate1 = worldIn.getBlockState(blockpos);
        boolean flag = BlockSlab.isSlab(iblockstate.getBlock()) && iblockstate.getValue(HALF) == EnumBlockHalf.TOP;
        boolean bl = flag1 = BlockSlab.isSlab(iblockstate1.getBlock()) && iblockstate1.getValue(HALF) == EnumBlockHalf.TOP;
        if (flag1) {
            if (side == EnumFacing.DOWN) {
                return true;
            }
            if (side == EnumFacing.UP && super.shouldSideBeRendered(worldIn, pos, side)) {
                return true;
            }
            if (!BlockSlab.isSlab(iblockstate.getBlock())) return true;
            if (!flag) return true;
            return false;
        }
        if (side == EnumFacing.UP) {
            return true;
        }
        if (side == EnumFacing.DOWN && super.shouldSideBeRendered(worldIn, pos, side)) {
            return true;
        }
        if (!BlockSlab.isSlab(iblockstate.getBlock())) return true;
        if (flag) return true;
        return false;
    }

    protected static boolean isSlab(Block blockIn) {
        if (blockIn == Blocks.stone_slab) return true;
        if (blockIn == Blocks.wooden_slab) return true;
        if (blockIn == Blocks.stone_slab2) return true;
        return false;
    }

    public abstract String getUnlocalizedName(int var1);

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        return super.getDamageValue(worldIn, pos) & 7;
    }

    public abstract boolean isDouble();

    public abstract IProperty<?> getVariantProperty();

    public abstract Object getVariant(ItemStack var1);

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

