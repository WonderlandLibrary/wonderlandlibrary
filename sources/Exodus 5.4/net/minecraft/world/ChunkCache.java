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
    protected World worldObj;
    protected boolean hasExtendedLevels;
    protected int chunkX;
    protected Chunk[][] chunkArray;
    protected int chunkZ;

    @Override
    public boolean isAirBlock(BlockPos blockPos) {
        return this.getBlockState(blockPos).getBlock().getMaterial() == Material.air;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(BlockPos blockPos) {
        return this.worldObj.getBiomeGenForCoords(blockPos);
    }

    @Override
    public WorldType getWorldType() {
        return this.worldObj.getWorldType();
    }

    @Override
    public int getStrongPower(BlockPos blockPos, EnumFacing enumFacing) {
        IBlockState iBlockState = this.getBlockState(blockPos);
        return iBlockState.getBlock().getStrongPower(this, blockPos, iBlockState, enumFacing);
    }

    private int getLightForExt(EnumSkyBlock enumSkyBlock, BlockPos blockPos) {
        if (enumSkyBlock == EnumSkyBlock.SKY && this.worldObj.provider.getHasNoSky()) {
            return 0;
        }
        if (blockPos.getY() >= 0 && blockPos.getY() < 256) {
            if (this.getBlockState(blockPos).getBlock().getUseNeighborBrightness()) {
                int n = 0;
                EnumFacing[] enumFacingArray = EnumFacing.values();
                int n2 = enumFacingArray.length;
                int n3 = 0;
                while (n3 < n2) {
                    EnumFacing enumFacing = enumFacingArray[n3];
                    int n4 = this.getLightFor(enumSkyBlock, blockPos.offset(enumFacing));
                    if (n4 > n) {
                        n = n4;
                    }
                    if (n >= 15) {
                        return n;
                    }
                    ++n3;
                }
                return n;
            }
            int n = (blockPos.getX() >> 4) - this.chunkX;
            int n5 = (blockPos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[n][n5].getLightFor(enumSkyBlock, blockPos);
        }
        return enumSkyBlock.defaultLightValue;
    }

    @Override
    public TileEntity getTileEntity(BlockPos blockPos) {
        int n = (blockPos.getX() >> 4) - this.chunkX;
        int n2 = (blockPos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[n][n2].getTileEntity(blockPos, Chunk.EnumCreateEntityType.IMMEDIATE);
    }

    @Override
    public IBlockState getBlockState(BlockPos blockPos) {
        if (blockPos.getY() >= 0 && blockPos.getY() < 256) {
            Chunk chunk;
            int n = (blockPos.getX() >> 4) - this.chunkX;
            int n2 = (blockPos.getZ() >> 4) - this.chunkZ;
            if (n >= 0 && n < this.chunkArray.length && n2 >= 0 && n2 < this.chunkArray[n].length && (chunk = this.chunkArray[n][n2]) != null) {
                return chunk.getBlockState(blockPos);
            }
        }
        return Blocks.air.getDefaultState();
    }

    @Override
    public int getCombinedLight(BlockPos blockPos, int n) {
        int n2 = this.getLightForExt(EnumSkyBlock.SKY, blockPos);
        int n3 = this.getLightForExt(EnumSkyBlock.BLOCK, blockPos);
        if (n3 < n) {
            n3 = n;
        }
        return n2 << 20 | n3 << 4;
    }

    public ChunkCache(World world, BlockPos blockPos, BlockPos blockPos2, int n) {
        int n2;
        this.worldObj = world;
        this.chunkX = blockPos.getX() - n >> 4;
        this.chunkZ = blockPos.getZ() - n >> 4;
        int n3 = blockPos2.getX() + n >> 4;
        int n4 = blockPos2.getZ() + n >> 4;
        this.chunkArray = new Chunk[n3 - this.chunkX + 1][n4 - this.chunkZ + 1];
        this.hasExtendedLevels = true;
        int n5 = this.chunkX;
        while (n5 <= n3) {
            n2 = this.chunkZ;
            while (n2 <= n4) {
                this.chunkArray[n5 - this.chunkX][n2 - this.chunkZ] = world.getChunkFromChunkCoords(n5, n2);
                ++n2;
            }
            ++n5;
        }
        n5 = blockPos.getX() >> 4;
        while (n5 <= blockPos2.getX() >> 4) {
            n2 = blockPos.getZ() >> 4;
            while (n2 <= blockPos2.getZ() >> 4) {
                Chunk chunk = this.chunkArray[n5 - this.chunkX][n2 - this.chunkZ];
                if (chunk != null && !chunk.getAreLevelsEmpty(blockPos.getY(), blockPos2.getY())) {
                    this.hasExtendedLevels = false;
                }
                ++n2;
            }
            ++n5;
        }
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        return this.hasExtendedLevels;
    }

    public int getLightFor(EnumSkyBlock enumSkyBlock, BlockPos blockPos) {
        if (blockPos.getY() >= 0 && blockPos.getY() < 256) {
            int n = (blockPos.getX() >> 4) - this.chunkX;
            int n2 = (blockPos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[n][n2].getLightFor(enumSkyBlock, blockPos);
        }
        return enumSkyBlock.defaultLightValue;
    }
}

