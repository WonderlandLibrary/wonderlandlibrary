// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockPressurePlate extends BlockBasePressurePlate
{
    public static final PropertyBool POWERED;
    private final Sensitivity sensitivity;
    private static final String __OBFID = "CL_00000289";
    
    protected BlockPressurePlate(final Material p_i45693_1_, final Sensitivity p_i45693_2_) {
        super(p_i45693_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPressurePlate.POWERED, false));
        this.sensitivity = p_i45693_2_;
    }
    
    @Override
    protected int getRedstoneStrength(final IBlockState p_176576_1_) {
        return p_176576_1_.getValue(BlockPressurePlate.POWERED) ? 15 : 0;
    }
    
    @Override
    protected IBlockState setRedstoneStrength(final IBlockState p_176575_1_, final int p_176575_2_) {
        return p_176575_1_.withProperty(BlockPressurePlate.POWERED, p_176575_2_ > 0);
    }
    
    @Override
    protected int computeRedstoneStrength(final World worldIn, final BlockPos pos) {
        final AxisAlignedBB var3 = this.getSensitiveAABB(pos);
        List var4 = null;
        switch (SwitchSensitivity.SENSITIVITY_ARRAY[this.sensitivity.ordinal()]) {
            case 1: {
                var4 = worldIn.getEntitiesWithinAABBExcludingEntity(null, var3);
                break;
            }
            case 2: {
                var4 = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, var3);
                break;
            }
            default: {
                return 0;
            }
        }
        if (!var4.isEmpty()) {
            for (final Entity var6 : var4) {
                if (!var6.doesEntityNotTriggerPressurePlate()) {
                    return 15;
                }
            }
        }
        return 0;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPressurePlate.POWERED, meta == 1);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((boolean)state.getValue(BlockPressurePlate.POWERED)) ? 1 : 0;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPressurePlate.POWERED });
    }
    
    static {
        POWERED = PropertyBool.create("powered");
    }
    
    public enum Sensitivity
    {
        EVERYTHING("EVERYTHING", 0), 
        MOBS("MOBS", 1);
        
        private static final Sensitivity[] $VALUES;
        private static final String __OBFID = "CL_00000290";
        
        private Sensitivity(final String p_i45417_1_, final int p_i45417_2_) {
        }
        
        static {
            $VALUES = new Sensitivity[] { Sensitivity.EVERYTHING, Sensitivity.MOBS };
        }
    }
    
    static final class SwitchSensitivity
    {
        static final int[] SENSITIVITY_ARRAY;
        private static final String __OBFID = "CL_00002078";
        
        static {
            SENSITIVITY_ARRAY = new int[Sensitivity.values().length];
            try {
                SwitchSensitivity.SENSITIVITY_ARRAY[Sensitivity.EVERYTHING.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchSensitivity.SENSITIVITY_ARRAY[Sensitivity.MOBS.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
        }
    }
}
