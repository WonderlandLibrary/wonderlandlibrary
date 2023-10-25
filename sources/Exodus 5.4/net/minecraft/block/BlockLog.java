/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public abstract class BlockLog
extends BlockRotatedPillar {
    public static final PropertyEnum<EnumAxis> LOG_AXIS = PropertyEnum.create("axis", EnumAxis.class);

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        int n = 4;
        int n2 = n + 1;
        if (world.isAreaLoaded(blockPos.add(-n2, -n2, -n2), blockPos.add(n2, n2, n2))) {
            for (BlockPos blockPos2 : BlockPos.getAllInBox(blockPos.add(-n, -n, -n), blockPos.add(n, n, n))) {
                IBlockState iBlockState2 = world.getBlockState(blockPos2);
                if (iBlockState2.getBlock().getMaterial() != Material.leaves || iBlockState2.getValue(BlockLeaves.CHECK_DECAY).booleanValue()) continue;
                world.setBlockState(blockPos2, iBlockState2.withProperty(BlockLeaves.CHECK_DECAY, true), 4);
            }
        }
    }

    public BlockLog() {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(2.0f);
        this.setStepSound(soundTypeWood);
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return super.onBlockPlaced(world, blockPos, enumFacing, f, f2, f3, n, entityLivingBase).withProperty(LOG_AXIS, EnumAxis.fromFacingAxis(enumFacing.getAxis()));
    }

    public static enum EnumAxis implements IStringSerializable
    {
        X("x"),
        Y("y"),
        Z("z"),
        NONE("none");

        private final String name;

        public String toString() {
            return this.name;
        }

        public static EnumAxis fromFacingAxis(EnumFacing.Axis axis) {
            switch (axis) {
                case X: {
                    return X;
                }
                case Y: {
                    return Y;
                }
                case Z: {
                    return Z;
                }
            }
            return NONE;
        }

        @Override
        public String getName() {
            return this.name;
        }

        private EnumAxis(String string2) {
            this.name = string2;
        }
    }
}

