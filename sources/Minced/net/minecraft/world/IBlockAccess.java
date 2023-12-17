// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.Biome;
import net.minecraft.block.state.IBlockState;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface IBlockAccess
{
    @Nullable
    TileEntity getTileEntity(final BlockPos p0);
    
    int getCombinedLight(final BlockPos p0, final int p1);
    
    IBlockState getBlockState(final BlockPos p0);
    
    boolean isAirBlock(final BlockPos p0);
    
    Biome getBiome(final BlockPos p0);
    
    int getStrongPower(final BlockPos p0, final EnumFacing p1);
    
    WorldType getWorldType();
}
