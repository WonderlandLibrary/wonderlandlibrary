/*
 * Decompiled with CFR 0.152.
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

    protected BlockPressurePlate(Material material, Sensitivity sensitivity) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false));
        this.sensitivity = sensitivity;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(POWERED, n == 1);
    }

    @Override
    protected IBlockState setRedstoneStrength(IBlockState iBlockState, int n) {
        return iBlockState.withProperty(POWERED, n > 0);
    }

    @Override
    protected int computeRedstoneStrength(World world, BlockPos blockPos) {
        List<Entity> list;
        AxisAlignedBB axisAlignedBB = this.getSensitiveAABB(blockPos);
        switch (this.sensitivity) {
            case EVERYTHING: {
                list = world.getEntitiesWithinAABBExcludingEntity(null, axisAlignedBB);
                break;
            }
            case MOBS: {
                list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisAlignedBB);
                break;
            }
            default: {
                return 0;
            }
        }
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity.doesEntityNotTriggerPressurePlate()) continue;
                return 15;
            }
        }
        return 0;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(POWERED) != false ? 1 : 0;
    }

    @Override
    protected int getRedstoneStrength(IBlockState iBlockState) {
        return iBlockState.getValue(POWERED) != false ? 15 : 0;
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

