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
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 0.0f);
        this.isHellWorld = true;
        this.hasNoSky = true;
        this.dimensionId = -1;
    }

    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        return new Vec3((double)0.2f, (double)0.03f, (double)0.03f);
    }

    protected void generateLightBrightnessTable() {
        float f = 0.1f;
        for (int i = 0; i <= 15; ++i) {
            float f1 = 1.0f - (float)i / 15.0f;
            this.lightBrightnessTable[i] = (1.0f - f1) / (f1 * 3.0f + 1.0f) * (1.0f - f) + f;
        }
    }

    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderHell(this.worldObj, this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getSeed());
    }

    public boolean isSurfaceWorld() {
        return false;
    }

    public boolean canCoordinateBeSpawn(int x, int z) {
        return false;
    }

    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0.5f;
    }

    public boolean canRespawnHere() {
        return false;
    }

    public boolean doesXZShowFog(int x, int z) {
        return true;
    }

    public String getDimensionName() {
        return "Nether";
    }

    public String getInternalNameSuffix() {
        return "_nether";
    }

    public WorldBorder getWorldBorder() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }
}
