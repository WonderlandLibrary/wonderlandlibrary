// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.override;

import net.optifine.reflect.Reflector;
import net.minecraft.world.WorldType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.Arrays;
import net.optifine.DynamicLights;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.optifine.util.ArrayCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;

public class ChunkCacheOF implements IBlockAccess
{
    private final ChunkCache chunkCache;
    private final int posX;
    private final int posY;
    private final int posZ;
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private final int sizeXY;
    private int[] combinedLights;
    private IBlockState[] blockStates;
    private final int arraySize;
    private final boolean dynamicLights;
    private static final ArrayCache cacheCombinedLights;
    private static final ArrayCache cacheBlockStates;
    
    static {
        cacheCombinedLights = new ArrayCache(Integer.TYPE, 16);
        cacheBlockStates = new ArrayCache(IBlockState.class, 16);
    }
    
    public ChunkCacheOF(final ChunkCache chunkCache, final BlockPos posFromIn, final BlockPos posToIn, final int subIn) {
        this.dynamicLights = Config.isDynamicLights();
        this.chunkCache = chunkCache;
        final int i = posFromIn.getX() - subIn >> 4;
        final int j = posFromIn.getY() - subIn >> 4;
        final int k = posFromIn.getZ() - subIn >> 4;
        final int l = posToIn.getX() + subIn >> 4;
        final int i2 = posToIn.getY() + subIn >> 4;
        final int j2 = posToIn.getZ() + subIn >> 4;
        this.sizeX = l - i + 1 << 4;
        this.sizeY = i2 - j + 1 << 4;
        this.sizeZ = j2 - k + 1 << 4;
        this.sizeXY = this.sizeX * this.sizeY;
        this.arraySize = this.sizeX * this.sizeY * this.sizeZ;
        this.posX = i << 4;
        this.posY = j << 4;
        this.posZ = k << 4;
    }
    
    private int getPositionIndex(final BlockPos pos) {
        final int i = pos.getX() - this.posX;
        if (i < 0 || i >= this.sizeX) {
            return -1;
        }
        final int j = pos.getY() - this.posY;
        if (j >= 0 && j < this.sizeY) {
            final int k = pos.getZ() - this.posZ;
            return (k >= 0 && k < this.sizeZ) ? (k * this.sizeXY + j * this.sizeX + i) : -1;
        }
        return -1;
    }
    
    @Override
    public int getCombinedLight(final BlockPos pos, final int lightValue) {
        final int i = this.getPositionIndex(pos);
        if (i >= 0 && i < this.arraySize && this.combinedLights != null) {
            int j = this.combinedLights[i];
            if (j == -1) {
                j = this.getCombinedLightRaw(pos, lightValue);
                this.combinedLights[i] = j;
            }
            return j;
        }
        return this.getCombinedLightRaw(pos, lightValue);
    }
    
    private int getCombinedLightRaw(final BlockPos pos, final int lightValue) {
        int i = this.chunkCache.getCombinedLight(pos, lightValue);
        if (this.dynamicLights && !this.getBlockState(pos).getBlock().isOpaqueCube()) {
            i = DynamicLights.getCombinedLight(pos, i);
        }
        return i;
    }
    
    @Override
    public IBlockState getBlockState(final BlockPos pos) {
        final int i = this.getPositionIndex(pos);
        if (i >= 0 && i < this.arraySize && this.blockStates != null) {
            IBlockState iblockstate = this.blockStates[i];
            if (iblockstate == null) {
                iblockstate = this.chunkCache.getBlockState(pos);
                this.blockStates[i] = iblockstate;
            }
            return iblockstate;
        }
        return this.chunkCache.getBlockState(pos);
    }
    
    public void renderStart() {
        if (this.combinedLights == null) {
            this.combinedLights = (int[])ChunkCacheOF.cacheCombinedLights.allocate(this.arraySize);
        }
        Arrays.fill(this.combinedLights, -1);
        if (this.blockStates == null) {
            this.blockStates = (IBlockState[])ChunkCacheOF.cacheBlockStates.allocate(this.arraySize);
        }
        Arrays.fill(this.blockStates, null);
    }
    
    public void renderFinish() {
        ChunkCacheOF.cacheCombinedLights.free(this.combinedLights);
        this.combinedLights = null;
        ChunkCacheOF.cacheBlockStates.free(this.blockStates);
        this.blockStates = null;
    }
    
    @Override
    public boolean extendedLevelsInChunkCache() {
        return this.chunkCache.extendedLevelsInChunkCache();
    }
    
    @Override
    public BiomeGenBase getBiomeGenForCoords(final BlockPos pos) {
        return this.chunkCache.getBiomeGenForCoords(pos);
    }
    
    @Override
    public int getStrongPower(final BlockPos pos, final EnumFacing direction) {
        return this.chunkCache.getStrongPower(pos, direction);
    }
    
    @Override
    public TileEntity getTileEntity(final BlockPos pos) {
        return this.chunkCache.getTileEntity(pos);
    }
    
    @Override
    public WorldType getWorldType() {
        return this.chunkCache.getWorldType();
    }
    
    @Override
    public boolean isAirBlock(final BlockPos pos) {
        return this.chunkCache.isAirBlock(pos);
    }
    
    public boolean isSideSolid(final BlockPos pos, final EnumFacing side, final boolean _default) {
        return Reflector.callBoolean(this.chunkCache, Reflector.ForgeChunkCache_isSideSolid, pos, side, _default);
    }
}
