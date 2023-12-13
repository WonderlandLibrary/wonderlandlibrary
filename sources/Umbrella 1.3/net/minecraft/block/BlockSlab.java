/*
 * Decompiled with CFR 0.150.
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
    public static final PropertyEnum HALF_PROP = PropertyEnum.create("half", EnumBlockHalf.class);
    private static final String __OBFID = "CL_00000253";

    public BlockSlab(Material p_i45714_1_) {
        super(p_i45714_1_);
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
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        if (this.isDouble()) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        } else {
            IBlockState var3 = access.getBlockState(pos);
            if (var3.getBlock() == this) {
                if (var3.getValue(HALF_PROP) == EnumBlockHalf.TOP) {
                    this.setBlockBounds(0.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f);
                } else {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
                }
            }
        }
    }

    @Override
    public void setBlockBoundsForItemRender() {
        if (this.isDouble()) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        } else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    @Override
    public boolean isOpaqueCube() {
        return this.isDouble();
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState var9 = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(HALF_PROP, (Comparable)((Object)EnumBlockHalf.BOTTOM));
        return this.isDouble() ? var9 : (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5) ? var9 : var9.withProperty(HALF_PROP, (Comparable)((Object)EnumBlockHalf.TOP)));
    }

    @Override
    public int quantityDropped(Random random) {
        return this.isDouble() ? 2 : 1;
    }

    @Override
    public boolean isFullCube() {
        return this.isDouble();
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        boolean var8;
        if (this.isDouble()) {
            return super.shouldSideBeRendered(worldIn, pos, side);
        }
        if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(worldIn, pos, side)) {
            return false;
        }
        BlockPos var4 = pos.offset(side.getOpposite());
        IBlockState var5 = worldIn.getBlockState(pos);
        IBlockState var6 = worldIn.getBlockState(var4);
        boolean var7 = BlockSlab.func_150003_a(var5.getBlock()) && var5.getValue(HALF_PROP) == EnumBlockHalf.TOP;
        boolean bl = var8 = BlockSlab.func_150003_a(var6.getBlock()) && var6.getValue(HALF_PROP) == EnumBlockHalf.TOP;
        return var8 ? (side == EnumFacing.DOWN ? true : (side == EnumFacing.UP && super.shouldSideBeRendered(worldIn, pos, side) ? true : !BlockSlab.func_150003_a(var5.getBlock()) || !var7)) : (side == EnumFacing.UP ? true : (side == EnumFacing.DOWN && super.shouldSideBeRendered(worldIn, pos, side) ? true : !BlockSlab.func_150003_a(var5.getBlock()) || var7));
    }

    protected static boolean func_150003_a(Block p_150003_0_) {
        return p_150003_0_ == Blocks.stone_slab || p_150003_0_ == Blocks.wooden_slab || p_150003_0_ == Blocks.stone_slab2;
    }

    public abstract String getFullSlabName(int var1);

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        return super.getDamageValue(worldIn, pos) & 7;
    }

    public abstract boolean isDouble();

    public abstract IProperty func_176551_l();

    public abstract Object func_176553_a(ItemStack var1);

    public static enum EnumBlockHalf implements IStringSerializable
    {
        TOP("TOP", 0, "top"),
        BOTTOM("BOTTOM", 1, "bottom");

        private final String halfName;
        private static final EnumBlockHalf[] $VALUES;
        private static final String __OBFID = "CL_00002109";

        static {
            $VALUES = new EnumBlockHalf[]{TOP, BOTTOM};
        }

        private EnumBlockHalf(String p_i45713_1_, int p_i45713_2_, String p_i45713_3_) {
            this.halfName = p_i45713_3_;
        }

        public String toString() {
            return this.halfName;
        }

        @Override
        public String getName() {
            return this.halfName;
        }
    }
}

