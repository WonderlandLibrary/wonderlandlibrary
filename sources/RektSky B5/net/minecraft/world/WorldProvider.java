/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderDebug;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.FlatGeneratorInfo;

public abstract class WorldProvider {
    public static final float[] moonPhaseFactors = new float[]{1.0f, 0.75f, 0.5f, 0.25f, 0.0f, 0.25f, 0.5f, 0.75f};
    protected World worldObj;
    private WorldType terrainType;
    private String generatorSettings;
    protected WorldChunkManager worldChunkMgr;
    protected boolean isHellWorld;
    protected boolean hasNoSky;
    protected final float[] lightBrightnessTable = new float[16];
    protected int dimensionId;
    private final float[] colorsSunriseSunset = new float[4];

    public final void registerWorld(World worldIn) {
        this.worldObj = worldIn;
        this.terrainType = worldIn.getWorldInfo().getTerrainType();
        this.generatorSettings = worldIn.getWorldInfo().getGeneratorOptions();
        this.registerWorldChunkManager();
        this.generateLightBrightnessTable();
    }

    protected void generateLightBrightnessTable() {
        float f2 = 0.0f;
        for (int i2 = 0; i2 <= 15; ++i2) {
            float f1 = 1.0f - (float)i2 / 15.0f;
            this.lightBrightnessTable[i2] = (1.0f - f1) / (f1 * 3.0f + 1.0f) * (1.0f - f2) + f2;
        }
    }

    protected void registerWorldChunkManager() {
        WorldType worldtype = this.worldObj.getWorldInfo().getTerrainType();
        if (worldtype == WorldType.FLAT) {
            FlatGeneratorInfo flatgeneratorinfo = FlatGeneratorInfo.createFlatGeneratorFromString(this.worldObj.getWorldInfo().getGeneratorOptions());
            this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.getBiomeFromBiomeList(flatgeneratorinfo.getBiome(), BiomeGenBase.field_180279_ad), 0.5f);
        } else {
            this.worldChunkMgr = worldtype == WorldType.DEBUG_WORLD ? new WorldChunkManagerHell(BiomeGenBase.plains, 0.0f) : new WorldChunkManager(this.worldObj);
        }
    }

    public IChunkProvider createChunkGenerator() {
        return this.terrainType == WorldType.FLAT ? new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : (this.terrainType == WorldType.DEBUG_WORLD ? new ChunkProviderDebug(this.worldObj) : (this.terrainType == WorldType.CUSTOMIZED ? new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings)));
    }

    public boolean canCoordinateBeSpawn(int x2, int z2) {
        return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x2, 0, z2)) == Blocks.grass;
    }

    public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
        int i2 = (int)(p_76563_1_ % 24000L);
        float f2 = ((float)i2 + p_76563_3_) / 24000.0f - 0.25f;
        if (f2 < 0.0f) {
            f2 += 1.0f;
        }
        if (f2 > 1.0f) {
            f2 -= 1.0f;
        }
        f2 = 1.0f - (float)((Math.cos((double)f2 * Math.PI) + 1.0) / 2.0);
        f2 += (f2 - f2) / 3.0f;
        return f2;
    }

    public int getMoonPhase(long p_76559_1_) {
        return (int)(p_76559_1_ / 24000L % 8L + 8L) % 8;
    }

    public boolean isSurfaceWorld() {
        return true;
    }

    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
        float f2;
        float f3 = 0.4f;
        float f1 = MathHelper.cos(celestialAngle * (float)Math.PI * 2.0f) - 0.0f;
        if (f1 >= (f2 = -0.0f) - f3 && f1 <= f2 + f3) {
            float f32 = (f1 - f2) / f3 * 0.5f + 0.5f;
            float f4 = 1.0f - (1.0f - MathHelper.sin(f32 * (float)Math.PI)) * 0.99f;
            f4 *= f4;
            this.colorsSunriseSunset[0] = f32 * 0.3f + 0.7f;
            this.colorsSunriseSunset[1] = f32 * f32 * 0.7f + 0.2f;
            this.colorsSunriseSunset[2] = f32 * f32 * 0.0f + 0.2f;
            this.colorsSunriseSunset[3] = f4;
            return this.colorsSunriseSunset;
        }
        return null;
    }

    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        float f2 = MathHelper.cos(p_76562_1_ * (float)Math.PI * 2.0f) * 2.0f + 0.5f;
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        float f1 = 0.7529412f;
        float f22 = 0.84705883f;
        float f3 = 1.0f;
        return new Vec3(f1 *= f2 * 0.94f + 0.06f, f22 *= f2 * 0.94f + 0.06f, f3 *= f2 * 0.91f + 0.09f);
    }

    public boolean canRespawnHere() {
        return true;
    }

    public static WorldProvider getProviderForDimension(int dimension) {
        return dimension == -1 ? new WorldProviderHell() : (dimension == 0 ? new WorldProviderSurface() : (dimension == 1 ? new WorldProviderEnd() : null));
    }

    public float getCloudHeight() {
        return 128.0f;
    }

    public boolean isSkyColored() {
        return true;
    }

    public BlockPos getSpawnCoordinate() {
        return null;
    }

    public int getAverageGroundLevel() {
        return this.terrainType == WorldType.FLAT ? 4 : this.worldObj.func_181545_F() + 1;
    }

    public double getVoidFogYFactor() {
        return this.terrainType == WorldType.FLAT ? 1.0 : 0.03125;
    }

    public boolean doesXZShowFog(int x2, int z2) {
        return false;
    }

    public abstract String getDimensionName();

    public abstract String getInternalNameSuffix();

    public WorldChunkManager getWorldChunkManager() {
        return this.worldChunkMgr;
    }

    public boolean doesWaterVaporize() {
        return this.isHellWorld;
    }

    public boolean getHasNoSky() {
        return this.hasNoSky;
    }

    public float[] getLightBrightnessTable() {
        return this.lightBrightnessTable;
    }

    public int getDimensionId() {
        return this.dimensionId;
    }

    public WorldBorder getWorldBorder() {
        return new WorldBorder();
    }
}

