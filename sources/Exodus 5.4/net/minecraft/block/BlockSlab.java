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

    public abstract IProperty<?> getVariantProperty();

    public abstract Object getVariant(ItemStack var1);

    @Override
    public void setBlockBoundsForItemRender() {
        if (this.isDouble()) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        } else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    protected static boolean isSlab(Block block) {
        return block == Blocks.stone_slab || block == Blocks.wooden_slab || block == Blocks.stone_slab2;
    }

    public abstract boolean isDouble();

    public abstract String getUnlocalizedName(int var1);

    @Override
    public int quantityDropped(Random random) {
        return this.isDouble() ? 2 : 1;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        IBlockState iBlockState = super.onBlockPlaced(world, blockPos, enumFacing, f, f2, f3, n, entityLivingBase).withProperty(HALF, EnumBlockHalf.BOTTOM);
        return this.isDouble() ? iBlockState : (enumFacing != EnumFacing.DOWN && (enumFacing == EnumFacing.UP || (double)f2 <= 0.5) ? iBlockState : iBlockState.withProperty(HALF, EnumBlockHalf.TOP));
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        boolean bl;
        if (this.isDouble()) {
            return super.shouldSideBeRendered(iBlockAccess, blockPos, enumFacing);
        }
        if (enumFacing != EnumFacing.UP && enumFacing != EnumFacing.DOWN && !super.shouldSideBeRendered(iBlockAccess, blockPos, enumFacing)) {
            return false;
        }
        BlockPos blockPos2 = blockPos.offset(enumFacing.getOpposite());
        IBlockState iBlockState = iBlockAccess.getBlockState(blockPos);
        IBlockState iBlockState2 = iBlockAccess.getBlockState(blockPos2);
        boolean bl2 = BlockSlab.isSlab(iBlockState.getBlock()) && iBlockState.getValue(HALF) == EnumBlockHalf.TOP;
        boolean bl3 = bl = BlockSlab.isSlab(iBlockState2.getBlock()) && iBlockState2.getValue(HALF) == EnumBlockHalf.TOP;
        return bl ? (enumFacing == EnumFacing.DOWN ? true : (enumFacing == EnumFacing.UP && super.shouldSideBeRendered(iBlockAccess, blockPos, enumFacing) ? true : !BlockSlab.isSlab(iBlockState.getBlock()) || !bl2)) : (enumFacing == EnumFacing.UP ? true : (enumFacing == EnumFacing.DOWN && super.shouldSideBeRendered(iBlockAccess, blockPos, enumFacing) ? true : !BlockSlab.isSlab(iBlockState.getBlock()) || bl2));
    }

    @Override
    public boolean isFullCube() {
        return this.isDouble();
    }

    @Override
    public boolean isOpaqueCube() {
        return this.isDouble();
    }

    public BlockSlab(Material material) {
        super(material);
        if (this.isDouble()) {
            this.fullBlock = true;
        } else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
        this.setLightOpacity(255);
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos blockPos, IBlockState iBlockState, AxisAlignedBB axisAlignedBB, List<AxisAlignedBB> list, Entity entity) {
        this.setBlockBoundsBasedOnState(world, blockPos);
        super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
    }

    @Override
    public int getDamageValue(World world, BlockPos blockPos) {
        return super.getDamageValue(world, blockPos) & 7;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        if (this.isDouble()) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        } else {
            IBlockState iBlockState = iBlockAccess.getBlockState(blockPos);
            if (iBlockState.getBlock() == this) {
                if (iBlockState.getValue(HALF) == EnumBlockHalf.TOP) {
                    this.setBlockBounds(0.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f);
                } else {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
                }
            }
        }
    }

    public static enum EnumBlockHalf implements IStringSerializable
    {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        private EnumBlockHalf(String string2) {
            this.name = string2;
        }
    }
}

