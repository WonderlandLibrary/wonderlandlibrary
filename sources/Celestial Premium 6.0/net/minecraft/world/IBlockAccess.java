/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

public interface IBlockAccess {
    @Nullable
    public TileEntity getTileEntity(BlockPos var1);

    public int getCombinedLight(BlockPos var1, int var2);

    public IBlockState getBlockState(BlockPos var1);

    public boolean isAirBlock(BlockPos var1);

    public Biome getBiome(BlockPos var1);

    public int getStrongPower(BlockPos var1, EnumFacing var2);

    public WorldType getWorldType();
}

