/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;

public class WorldProviderEnd
extends WorldProvider {
    @Override
    public String getDimensionName() {
        return "The End";
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public float getCloudHeight() {
        return 8.0f;
    }

    @Override
    public boolean canCoordinateBeSpawn(int n, int n2) {
        return this.worldObj.getGroundAboveSeaLevel(new BlockPos(n, 0, n2)).getMaterial().blocksMovement();
    }

    @Override
    public BlockPos getSpawnCoordinate() {
        return new BlockPos(100, 50, 0);
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
    }

    @Override
    public boolean doesXZShowFog(int n, int n2) {
        return true;
    }

    @Override
    public float[] calcSunriseSunsetColors(float f, float f2) {
        return null;
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    public Vec3 getFogColor(float f, float f2) {
        int n = 0xA080A0;
        float f3 = MathHelper.cos(f * (float)Math.PI * 2.0f) * 2.0f + 0.5f;
        f3 = MathHelper.clamp_float(f3, 0.0f, 1.0f);
        float f4 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f5 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f6 = (float)(n & 0xFF) / 255.0f;
        return new Vec3(f4 *= f3 * 0.0f + 0.15f, f5 *= f3 * 0.0f + 0.15f, f6 *= f3 * 0.0f + 0.15f);
    }

    @Override
    public boolean isSkyColored() {
        return false;
    }

    @Override
    public int getAverageGroundLevel() {
        return 50;
    }

    @Override
    public String getInternalNameSuffix() {
        return "_end";
    }

    @Override
    public float calculateCelestialAngle(long l, float f) {
        return 0.0f;
    }

    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.0f);
        this.dimensionId = 1;
        this.hasNoSky = true;
    }
}

