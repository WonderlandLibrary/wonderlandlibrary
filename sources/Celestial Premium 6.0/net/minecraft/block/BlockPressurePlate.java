/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
        return state.getValue(POWERED) != false ? 15 : 0;
    }

    @Override
    protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
        return state.withProperty(POWERED, strength > 0);
    }

    @Override
    protected void playClickOnSound(World worldIn, BlockPos color) {
        if (this.blockMaterial == Material.WOOD) {
            worldIn.playSound(null, color, SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3f, 0.8f);
        } else {
            worldIn.playSound(null, color, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3f, 0.6f);
        }
    }

    @Override
    protected void playClickOffSound(World worldIn, BlockPos pos) {
        if (this.blockMaterial == Material.WOOD) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3f, 0.7f);
        } else {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3f, 0.5f);
        }
    }

    @Override
    protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
        List<Entity> list;
        AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(pos);
        switch (this.sensitivity) {
            case EVERYTHING: {
                list = worldIn.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
                break;
            }
            case MOBS: {
                list = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
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
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWERED, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWERED) != false ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, POWERED);
    }

    public static enum Sensitivity {
        EVERYTHING,
        MOBS;

    }
}

