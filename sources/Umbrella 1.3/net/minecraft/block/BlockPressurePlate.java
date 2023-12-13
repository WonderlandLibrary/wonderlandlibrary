/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPressurePlate
extends BlockBasePressurePlate {
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    private final Sensitivity sensitivity;
    private static final String __OBFID = "CL_00000289";

    protected BlockPressurePlate(Material p_i45693_1_, Sensitivity p_i45693_2_) {
        super(p_i45693_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false)));
        this.sensitivity = p_i45693_2_;
    }

    @Override
    protected int getRedstoneStrength(IBlockState p_176576_1_) {
        return (Boolean)p_176576_1_.getValue(POWERED) != false ? 15 : 0;
    }

    @Override
    protected IBlockState setRedstoneStrength(IBlockState p_176575_1_, int p_176575_2_) {
        return p_176575_1_.withProperty(POWERED, Boolean.valueOf(p_176575_2_ > 0));
    }

    @Override
    protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
        List var4;
        AxisAlignedBB var3 = this.getSensitiveAABB(pos);
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
            for (Entity var6 : var4) {
                if (var6.doesEntityNotTriggerPressurePlate()) continue;
                return 15;
            }
        }
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWERED, Boolean.valueOf(meta == 1));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (Boolean)state.getValue(POWERED) != false ? 1 : 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, POWERED);
    }

    public static enum Sensitivity {
        EVERYTHING("EVERYTHING", 0),
        MOBS("MOBS", 1);

        private static final Sensitivity[] $VALUES;
        private static final String __OBFID = "CL_00000290";

        static {
            $VALUES = new Sensitivity[]{EVERYTHING, MOBS};
        }

        private Sensitivity(String p_i45417_1_, int p_i45417_2_) {
        }
    }

    static final class SwitchSensitivity {
        static final int[] SENSITIVITY_ARRAY = new int[Sensitivity.values().length];
        private static final String __OBFID = "CL_00002078";

        static {
            try {
                SwitchSensitivity.SENSITIVITY_ARRAY[Sensitivity.EVERYTHING.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchSensitivity.SENSITIVITY_ARRAY[Sensitivity.MOBS.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchSensitivity() {
        }
    }
}

