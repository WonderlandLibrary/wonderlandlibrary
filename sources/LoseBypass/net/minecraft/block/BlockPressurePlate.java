/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Iterator;
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

    protected BlockPressurePlate(Material materialIn, Sensitivity sensitivityIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false));
        this.sensitivity = sensitivityIn;
    }

    @Override
    protected int getRedstoneStrength(IBlockState state) {
        if (state.getValue(POWERED) == false) return 0;
        return 15;
    }

    @Override
    protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
        boolean bl;
        if (strength > 0) {
            bl = true;
            return state.withProperty(POWERED, bl);
        }
        bl = false;
        return state.withProperty(POWERED, bl);
    }

    @Override
    protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
        Entity entity;
        List<Entity> list;
        AxisAlignedBB axisalignedbb = this.getSensitiveAABB(pos);
        switch (1.$SwitchMap$net$minecraft$block$BlockPressurePlate$Sensitivity[this.sensitivity.ordinal()]) {
            case 1: {
                list = worldIn.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
                break;
            }
            case 2: {
                list = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
                break;
            }
            default: {
                return 0;
            }
        }
        if (list.isEmpty()) return 0;
        Iterator<Entity> iterator = list.iterator();
        do {
            if (!iterator.hasNext()) return 0;
        } while ((entity = iterator.next()).doesEntityNotTriggerPressurePlate());
        return 15;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean bl;
        IBlockState iBlockState = this.getDefaultState();
        if (meta == 1) {
            bl = true;
            return iBlockState.withProperty(POWERED, bl);
        }
        bl = false;
        return iBlockState.withProperty(POWERED, bl);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(POWERED) == false) return 0;
        return 1;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, POWERED);
    }

    public static enum Sensitivity {
        EVERYTHING,
        MOBS;

    }
}

