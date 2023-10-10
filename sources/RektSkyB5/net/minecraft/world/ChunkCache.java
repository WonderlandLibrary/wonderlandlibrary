/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class ChunkCache
implements IBlockAccess {
    protected int chunkX;
    protected int chunkZ;
    protected Chunk[][] chunkArray;
    protected boolean hasExtendedLevels;
    protected World worldObj;

    public ChunkCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn) {
        this.worldObj = worldIn;
        this.chunkX = posFromIn.getX() - subIn >> 4;
        this.chunkZ = posFromIn.getZ() - subIn >> 4;
        int i2 = posToIn.getX() + subIn >> 4;
        int j2 = posToIn.getZ() + subIn >> 4;
        this.chunkArray = new Chunk[i2 - this.chunkX + 1][j2 - this.chunkZ + 1];
        this.hasExtendedLevels = true;
        for (int k2 = this.chunkX; k2 <= i2; ++k2) {
            for (int l2 = this.chunkZ; l2 <= j2; ++l2) {
                this.chunkArray[k2 - this.chunkX][l2 - this.chunkZ] = worldIn.getChunkFromChunkCoords(k2, l2);
            }
        }
        for (int i1 = posFromIn.getX() >> 4; i1 <= posToIn.getX() >> 4; ++i1) {
            for (int j1 = posFromIn.getZ() >> 4; j1 <= posToIn.getZ() >> 4; ++j1) {
                Chunk chunk = this.chunkArray[i1 - this.chunkX][j1 - this.chunkZ];
                if (chunk == null || chunk.getAreLevelsEmpty(posFromIn.getY(), posToIn.getY())) continue;
                this.hasExtendedLevels = false;
            }
        }
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        return this.hasExtendedLevels;
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        int i2 = (pos.getX() >> 4) - this.chunkX;
        int j2 = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[i2][j2].getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        int i2 = this.getLightForExt(EnumSkyBlock.SKY, pos);
        int j2 = this.getLightForExt(EnumSkyBlock.BLOCK, pos);
        if (j2 < lightValue) {
            j2 = lightValue;
        }
        return i2 << 20 | j2 << 4;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            Chunk chunk;
            int i2 = (pos.getX() >> 4) - this.chunkX;
            int j2 = (pos.getZ() >> 4) - this.chunkZ;
            if (i2 >= 0 && i2 < this.chunkArray.length && j2 >= 0 && j2 < this.chunkArray[i2].length && (chunk = this.chunkArray[i2][j2]) != null) {
                return chunk.getBlockState(pos);
            }
        }
        return Blocks.air.getDefaultState();
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(BlockPos pos) {
        return this.worldObj.getBiomeGenForCoords(pos);
    }

    private int getLightForExt(EnumSkyBlock p_175629_1_, BlockPos pos) {
        if (p_175629_1_ == EnumSkyBlock.SKY && this.worldObj.provider.getHasNoSky()) {
            return 0;
        }
        if (pos.getY() >= 0 && pos.getY() < 256) {
            if (this.getBlockState(pos).getBlock().getUseNeighborBrightness()) {
                int l2 = 0;
                for (EnumFacing enumfacing : EnumFacing.values()) {
                    int k2 = this.getLightFor(p_175629_1_, pos.offset(enumfacing));
                    if (k2 > l2) {
                        l2 = k2;
                    }
                    if (l2 < 15) continue;
                    return l2;
                }
                return l2;
            }
            int i2 = (pos.getX() >> 4) - this.chunkX;
            int j2 = (pos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[i2][j2].getLightFor(p_175629_1_, pos);
        }
        return p_175629_1_.defaultLightValue;
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return this.getBlockState(pos).getBlock().getMaterial() == Material.air;
    }

    public int getLightFor(EnumSkyBlock p_175628_1_, BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            int i2 = (pos.getX() >> 4) - this.chunkX;
            int j2 = (pos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[i2][j2].getLightFor(p_175628_1_, pos);
        }
        return p_175628_1_.defaultLightValue;
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        IBlockState iblockstate = this.getBlockState(pos);
        return iblockstate.getBlock().getStrongPower(this, pos, iblockstate, direction);
    }

    @Override
    public WorldType getWorldType() {
        return this.worldObj.getWorldType();
    }
}

