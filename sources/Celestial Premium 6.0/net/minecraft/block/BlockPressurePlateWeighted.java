/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockPressurePlateWeighted
extends BlockBasePressurePlate {
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    private final int maxWeight;

    protected BlockPressurePlateWeighted(Material materialIn, int p_i46379_2_) {
        this(materialIn, p_i46379_2_, materialIn.getMaterialMapColor());
    }

    protected BlockPressurePlateWeighted(Material materialIn, int p_i46380_2_, MapColor color) {
        super(materialIn, color);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, 0));
        this.maxWeight = p_i46380_2_;
    }

    @Override
    protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
        int i = Math.min(worldIn.getEntitiesWithinAABB(Entity.class, PRESSURE_AABB.offset(pos)).size(), this.maxWeight);
        if (i > 0) {
            float f = (float)Math.min(this.maxWeight, i) / (float)this.maxWeight;
            return MathHelper.ceil(f * 15.0f);
        }
        return 0;
    }

    @Override
    protected void playClickOnSound(World worldIn, BlockPos color) {
        worldIn.playSound(null, color, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3f, 0.90000004f);
    }

    @Override
    protected void playClickOffSound(World worldIn, BlockPos pos) {
        worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3f, 0.75f);
    }

    @Override
    protected int getRedstoneStrength(IBlockState state) {
        return state.getValue(POWER);
    }

    @Override
    protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
        return state.withProperty(POWER, strength);
    }

    @Override
    public int tickRate(World worldIn) {
        return 10;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWER, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWER);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, POWER);
    }
}

