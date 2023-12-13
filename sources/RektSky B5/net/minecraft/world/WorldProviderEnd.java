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
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.0f);
        this.dimensionId = 1;
        this.hasNoSky = true;
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
    }

    @Override
    public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
        return 0.0f;
    }

    @Override
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
        return null;
    }

    @Override
    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        int i2 = 0xA080A0;
        float f2 = MathHelper.cos(p_76562_1_ * (float)Math.PI * 2.0f) * 2.0f + 0.5f;
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        float f1 = (float)(i2 >> 16 & 0xFF) / 255.0f;
        float f22 = (float)(i2 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(i2 & 0xFF) / 255.0f;
        return new Vec3(f1 *= f2 * 0.0f + 0.15f, f22 *= f2 * 0.0f + 0.15f, f3 *= f2 * 0.0f + 0.15f);
    }

    @Override
    public boolean isSkyColored() {
        return false;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    public float getCloudHeight() {
        return 8.0f;
    }

    @Override
    public boolean canCoordinateBeSpawn(int x2, int z2) {
        return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x2, 0, z2)).getMaterial().blocksMovement();
    }

    @Override
    public BlockPos getSpawnCoordinate() {
        return new BlockPos(100, 50, 0);
    }

    @Override
    public int getAverageGroundLevel() {
        return 50;
    }

    @Override
    public boolean doesXZShowFog(int x2, int z2) {
        return true;
    }

    @Override
    public String getDimensionName() {
        return "The End";
    }

    @Override
    public String getInternalNameSuffix() {
        return "_end";
    }
}

