/*
 * Decompiled with CFR 0.150.
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
    public static final PropertyEnum AXIS_PROP = PropertyEnum.create("axis", EnumAxis.class);
    private static final String __OBFID = "CL_00000266";

    public BlockLog() {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(2.0f);
        this.setStepSound(soundTypeWood);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        int var4 = 4;
        int var5 = var4 + 1;
        if (worldIn.isAreaLoaded(pos.add(-var5, -var5, -var5), pos.add(var5, var5, var5))) {
            for (BlockPos var7 : BlockPos.getAllInBox(pos.add(-var4, -var4, -var4), pos.add(var4, var4, var4))) {
                IBlockState var8 = worldIn.getBlockState(var7);
                if (var8.getBlock().getMaterial() != Material.leaves || ((Boolean)var8.getValue(BlockLeaves.field_176236_b)).booleanValue()) continue;
                worldIn.setBlockState(var7, var8.withProperty(BlockLeaves.field_176236_b, Boolean.valueOf(true)), 4);
            }
        }
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(AXIS_PROP, (Comparable)((Object)EnumAxis.func_176870_a(facing.getAxis())));
    }

    public static enum EnumAxis implements IStringSerializable
    {
        X("X", 0, "x"),
        Y("Y", 1, "y"),
        Z("Z", 2, "z"),
        NONE("NONE", 3, "none");

        private final String field_176874_e;
        private static final EnumAxis[] $VALUES;
        private static final String __OBFID = "CL_00002100";

        static {
            $VALUES = new EnumAxis[]{X, Y, Z, NONE};
        }

        private EnumAxis(String p_i45708_1_, int p_i45708_2_, String p_i45708_3_) {
            this.field_176874_e = p_i45708_3_;
        }

        public String toString() {
            return this.field_176874_e;
        }

        public static EnumAxis func_176870_a(EnumFacing.Axis p_176870_0_) {
            switch (p_176870_0_) {
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
            return this.field_176874_e;
        }
    }
}

