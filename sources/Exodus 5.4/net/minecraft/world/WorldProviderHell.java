/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderHell;

public class WorldProviderHell
extends WorldProvider {
    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 0.0f);
        this.isHellWorld = true;
        this.hasNoSky = true;
        this.dimensionId = -1;
    }

    @Override
    protected void generateLightBrightnessTable() {
        float f = 0.1f;
        int n = 0;
        while (n <= 15) {
            float f2 = 1.0f - (float)n / 15.0f;
            this.lightBrightnessTable[n] = (1.0f - f2) / (f2 * 3.0f + 1.0f) * (1.0f - f) + f;
            ++n;
        }
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderHell(this.worldObj, this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getSeed());
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public String getDimensionName() {
        return "Nether";
    }

    @Override
    public boolean doesXZShowFog(int n, int n2) {
        return true;
    }

    @Override
    public Vec3 getFogColor(float f, float f2) {
        return new Vec3(0.2f, 0.03f, 0.03f);
    }

    @Override
    public String getInternalNameSuffix() {
        return "_nether";
    }

    @Override
    public float calculateCelestialAngle(long l, float f) {
        return 0.5f;
    }

    @Override
    public boolean canCoordinateBeSpawn(int n, int n2) {
        return false;
    }

    @Override
    public WorldBorder getWorldBorder() {
        return new WorldBorder(){

            @Override
            public double getCenterZ() {
                return super.getCenterZ() / 8.0;
            }

            @Override
            public double getCenterX() {
                return super.getCenterX() / 8.0;
            }
        };
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }
}

