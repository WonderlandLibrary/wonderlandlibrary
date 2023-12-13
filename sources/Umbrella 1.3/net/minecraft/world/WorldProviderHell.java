/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000387";

    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 0.0f);
        this.isHellWorld = true;
        this.hasNoSky = true;
        this.dimensionId = -1;
    }

    @Override
    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        return new Vec3(0.2f, 0.03f, 0.03f);
    }

    @Override
    protected void generateLightBrightnessTable() {
        float var1 = 0.1f;
        for (int var2 = 0; var2 <= 15; ++var2) {
            float var3 = 1.0f - (float)var2 / 15.0f;
            this.lightBrightnessTable[var2] = (1.0f - var3) / (var3 * 3.0f + 1.0f) * (1.0f - var1) + var1;
        }
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderHell(this.worldObj, this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getSeed());
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    public boolean canCoordinateBeSpawn(int x, int z) {
        return false;
    }

    @Override
    public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
        return 0.5f;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
        return true;
    }

    @Override
    public String getDimensionName() {
        return "Nether";
    }

    @Override
    public String getInternalNameSuffix() {
        return "_nether";
    }

    @Override
    public WorldBorder getWorldBorder() {
        return new WorldBorder(){
            private static final String __OBFID = "CL_00002008";

            @Override
            public double getCenterX() {
                return super.getCenterX() / 8.0;
            }

            @Override
            public double getCenterZ() {
                return super.getCenterZ() / 8.0;
            }
        };
    }
}

