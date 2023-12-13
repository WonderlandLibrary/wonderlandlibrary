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

    public BlockLog() {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(2.0f);
        this.setStepSound(soundTypeWood);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        int i2 = 4;
        int j2 = i2 + 1;
        if (worldIn.isAreaLoaded(pos.add(-j2, -j2, -j2), pos.add(j2, j2, j2))) {
            for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-i2, -i2, -i2), pos.add(i2, i2, i2))) {
                IBlockState iblockstate = worldIn.getBlockState(blockpos);
                if (iblockstate.getBlock().getMaterial() != Material.leaves || iblockstate.getValue(BlockLeaves.CHECK_DECAY).booleanValue()) continue;
                worldIn.setBlockState(blockpos, iblockstate.withProperty(BlockLeaves.CHECK_DECAY, true), 4);
            }
        }
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(LOG_AXIS, EnumAxis.fromFacingAxis(facing.getAxis()));
    }

    public static enum EnumAxis implements IStringSerializable
    {
        X("x"),
        Y("y"),
        Z("z"),
        NONE("none");

        private final String name;

        private EnumAxis(String name) {
            this.name = name;
        }

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
    }
}

