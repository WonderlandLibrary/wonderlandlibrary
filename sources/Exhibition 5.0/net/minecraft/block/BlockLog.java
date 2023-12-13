// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public abstract class BlockLog extends BlockRotatedPillar
{
    public static final PropertyEnum AXIS_PROP;
    private static final String __OBFID = "CL_00000266";
    
    public BlockLog() {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(2.0f);
        this.setStepSound(Block.soundTypeWood);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final byte var4 = 4;
        final int var5 = var4 + 1;
        if (worldIn.isAreaLoaded(pos.add(-var5, -var5, -var5), pos.add(var5, var5, var5))) {
            for (final BlockPos var7 : BlockPos.getAllInBox(pos.add(-var4, -var4, -var4), pos.add(var4, var4, var4))) {
                final IBlockState var8 = worldIn.getBlockState(var7);
                if (var8.getBlock().getMaterial() == Material.leaves && !(boolean)var8.getValue(BlockLeaves.field_176236_b)) {
                    worldIn.setBlockState(var7, var8.withProperty(BlockLeaves.field_176236_b, true), 4);
                }
            }
        }
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BlockLog.AXIS_PROP, EnumAxis.func_176870_a(facing.getAxis()));
    }
    
    static {
        AXIS_PROP = PropertyEnum.create("axis", EnumAxis.class);
    }
    
    public enum EnumAxis implements IStringSerializable
    {
        X("X", 0, "x"), 
        Y("Y", 1, "y"), 
        Z("Z", 2, "z"), 
        NONE("NONE", 3, "none");
        
        private final String field_176874_e;
        private static final EnumAxis[] $VALUES;
        private static final String __OBFID = "CL_00002100";
        
        private EnumAxis(final String p_i45708_1_, final int p_i45708_2_, final String p_i45708_3_) {
            this.field_176874_e = p_i45708_3_;
        }
        
        @Override
        public String toString() {
            return this.field_176874_e;
        }
        
        public static EnumAxis func_176870_a(final EnumFacing.Axis p_176870_0_) {
            switch (SwitchAxis.field_180167_a[p_176870_0_.ordinal()]) {
                case 1: {
                    return EnumAxis.X;
                }
                case 2: {
                    return EnumAxis.Y;
                }
                case 3: {
                    return EnumAxis.Z;
                }
                default: {
                    return EnumAxis.NONE;
                }
            }
        }
        
        @Override
        public String getName() {
            return this.field_176874_e;
        }
        
        static {
            $VALUES = new EnumAxis[] { EnumAxis.X, EnumAxis.Y, EnumAxis.Z, EnumAxis.NONE };
        }
    }
    
    static final class SwitchAxis
    {
        static final int[] field_180167_a;
        private static final String __OBFID = "CL_00002101";
        
        static {
            field_180167_a = new int[EnumFacing.Axis.values().length];
            try {
                SwitchAxis.field_180167_a[EnumFacing.Axis.X.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchAxis.field_180167_a[EnumFacing.Axis.Y.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchAxis.field_180167_a[EnumFacing.Axis.Z.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
        }
    }
}
