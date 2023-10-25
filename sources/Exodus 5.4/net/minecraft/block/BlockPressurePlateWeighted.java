/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPressurePlateWeighted
extends BlockBasePressurePlate {
    private final int field_150068_a;
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(POWER);
    }

    @Override
    protected IBlockState setRedstoneStrength(IBlockState iBlockState, int n) {
        return iBlockState.withProperty(POWER, n);
    }

    protected BlockPressurePlateWeighted(Material material, int n) {
        this(material, n, material.getMaterialMapColor());
    }

    @Override
    public int tickRate(World world) {
        return 10;
    }

    @Override
    protected int computeRedstoneStrength(World world, BlockPos blockPos) {
        int n = Math.min(world.getEntitiesWithinAABB(Entity.class, this.getSensitiveAABB(blockPos)).size(), this.field_150068_a);
        if (n > 0) {
            float f = (float)Math.min(this.field_150068_a, n) / (float)this.field_150068_a;
            return MathHelper.ceiling_float_int(f * 15.0f);
        }
        return 0;
    }

    @Override
    protected int getRedstoneStrength(IBlockState iBlockState) {
        return iBlockState.getValue(POWER);
    }

    protected BlockPressurePlateWeighted(Material material, int n, MapColor mapColor) {
        super(material, mapColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, 0));
        this.field_150068_a = n;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(POWER, n);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, POWER);
    }
}

