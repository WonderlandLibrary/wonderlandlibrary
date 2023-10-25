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
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.0f);
        this.dimensionId = 1;
        this.hasNoSky = true;
    }

    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
    }

    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0.0f;
    }

    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
        return null;
    }

    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        int i = 0xA080A0;
        float f = MathHelper.cos((float)(p_76562_1_ * (float)Math.PI * 2.0f)) * 2.0f + 0.5f;
        f = MathHelper.clamp_float((float)f, (float)0.0f, (float)1.0f);
        float f1 = (float)(i >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(i >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(i & 0xFF) / 255.0f;
        return new Vec3((double)(f1 *= f * 0.0f + 0.15f), (double)(f2 *= f * 0.0f + 0.15f), (double)(f3 *= f * 0.0f + 0.15f));
    }

    public boolean isSkyColored() {
        return false;
    }

    public boolean canRespawnHere() {
        return false;
    }

    public boolean isSurfaceWorld() {
        return false;
    }

    public float getCloudHeight() {
        return 8.0f;
    }

    public boolean canCoordinateBeSpawn(int x, int z) {
        return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
    }

    public BlockPos getSpawnCoordinate() {
        return new BlockPos(100, 50, 0);
    }

    public int getAverageGroundLevel() {
        return 50;
    }

    public boolean doesXZShowFog(int x, int z) {
        return true;
    }

    public String getDimensionName() {
        return "The End";
    }

    public String getInternalNameSuffix() {
        return "_end";
    }
}
