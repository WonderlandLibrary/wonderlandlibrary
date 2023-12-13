/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000386";

    public final void registerWorld(World worldIn) {
        this.worldObj = worldIn;
        this.terrainType = worldIn.getWorldInfo().getTerrainType();
        this.generatorSettings = worldIn.getWorldInfo().getGeneratorOptions();
        this.registerWorldChunkManager();
        this.generateLightBrightnessTable();
    }

    protected void generateLightBrightnessTable() {
        float var1 = 0.0f;
        for (int var2 = 0; var2 <= 15; ++var2) {
            float var3 = 1.0f - (float)var2 / 15.0f;
            this.lightBrightnessTable[var2] = (1.0f - var3) / (var3 * 3.0f + 1.0f) * (1.0f - var1) + var1;
        }
    }

    protected void registerWorldChunkManager() {
        WorldType var1 = this.worldObj.getWorldInfo().getTerrainType();
        if (var1 == WorldType.FLAT) {
            FlatGeneratorInfo var2 = FlatGeneratorInfo.createFlatGeneratorFromString(this.worldObj.getWorldInfo().getGeneratorOptions());
            this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.getBiomeFromBiomeList(var2.getBiome(), BiomeGenBase.field_180279_ad), 0.5f);
        } else {
            this.worldChunkMgr = var1 == WorldType.DEBUG_WORLD ? new WorldChunkManagerHell(BiomeGenBase.plains, 0.0f) : new WorldChunkManager(this.worldObj);
        }
    }

    public IChunkProvider createChunkGenerator() {
        return this.terrainType == WorldType.FLAT ? new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : (this.terrainType == WorldType.DEBUG_WORLD ? new ChunkProviderDebug(this.worldObj) : (this.terrainType == WorldType.CUSTOMIZED ? new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings)));
    }

    public boolean canCoordinateBeSpawn(int x, int z) {
        return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)) == Blocks.grass;
    }

    public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
        int var4 = (int)(p_76563_1_ % 24000L);
        float var5 = ((float)var4 + p_76563_3_) / 24000.0f - 0.25f;
        if (var5 < 0.0f) {
            var5 += 1.0f;
        }
        if (var5 > 1.0f) {
            var5 -= 1.0f;
        }
        float var6 = var5;
        var5 = 1.0f - (float)((Math.cos((double)var5 * Math.PI) + 1.0) / 2.0);
        var5 = var6 + (var5 - var6) / 3.0f;
        return var5;
    }

    public int getMoonPhase(long p_76559_1_) {
        return (int)(p_76559_1_ / 24000L % 8L + 8L) % 8;
    }

    public boolean isSurfaceWorld() {
        return true;
    }

    public float[] calcSunriseSunsetColors(float p_76560_1_, float p_76560_2_) {
        float var5;
        float var3 = 0.4f;
        float var4 = MathHelper.cos(p_76560_1_ * (float)Math.PI * 2.0f) - 0.0f;
        if (var4 >= (var5 = -0.0f) - var3 && var4 <= var5 + var3) {
            float var6 = (var4 - var5) / var3 * 0.5f + 0.5f;
            float var7 = 1.0f - (1.0f - MathHelper.sin(var6 * (float)Math.PI)) * 0.99f;
            var7 *= var7;
            this.colorsSunriseSunset[0] = var6 * 0.3f + 0.7f;
            this.colorsSunriseSunset[1] = var6 * var6 * 0.7f + 0.2f;
            this.colorsSunriseSunset[2] = var6 * var6 * 0.0f + 0.2f;
            this.colorsSunriseSunset[3] = var7;
            return this.colorsSunriseSunset;
        }
        return null;
    }

    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        float var3 = MathHelper.cos(p_76562_1_ * (float)Math.PI * 2.0f) * 2.0f + 0.5f;
        var3 = MathHelper.clamp_float(var3, 0.0f, 1.0f);
        float var4 = 0.7529412f;
        float var5 = 0.84705883f;
        float var6 = 1.0f;
        return new Vec3(var4 *= var3 * 0.94f + 0.06f, var5 *= var3 * 0.94f + 0.06f, var6 *= var3 * 0.91f + 0.09f);
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

    public BlockPos func_177496_h() {
        return null;
    }

    public int getAverageGroundLevel() {
        return this.terrainType == WorldType.FLAT ? 4 : 64;
    }

    public double getVoidFogYFactor() {
        return this.terrainType == WorldType.FLAT ? 1.0 : 0.03125;
    }

    public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
        return false;
    }

    public abstract String getDimensionName();

    public abstract String getInternalNameSuffix();

    public WorldChunkManager getWorldChunkManager() {
        return this.worldChunkMgr;
    }

    public boolean func_177500_n() {
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

