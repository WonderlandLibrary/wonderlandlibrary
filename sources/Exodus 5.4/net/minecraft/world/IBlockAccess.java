/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;

public interface IBlockAccess {
    public boolean isAirBlock(BlockPos var1);

    public WorldType getWorldType();

    public BiomeGenBase getBiomeGenForCoords(BlockPos var1);

    public int getCombinedLight(BlockPos var1, int var2);

    public TileEntity getTileEntity(BlockPos var1);

    public boolean extendedLevelsInChunkCache();

    public IBlockState getBlockState(BlockPos var1);

    public int getStrongPower(BlockPos var1, EnumFacing var2);
}

