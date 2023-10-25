/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.IChunkGenerator;

public abstract class WorldProvider {
    public static final float[] MOON_PHASE_FACTORS = new float[]{1.0f, 0.75f, 0.5f, 0.25f, 0.0f, 0.25f, 0.5f, 0.75f};
    protected World worldObj;
    private WorldType terrainType;
    private String generatorSettings;
    protected BiomeProvider biomeProvider;
    public boolean isHellWorld;
    protected boolean hasNoSky;
    protected boolean field_191067_f;
    protected final float[] lightBrightnessTable = new float[16];
    private final float[] colorsSunriseSunset = new float[4];

    public final void registerWorld(World worldIn) {
        this.worldObj = worldIn;
        this.terrainType = worldIn.getWorldInfo().getTerrainType();
        this.generatorSettings = worldIn.getWorldInfo().getGeneratorOptions();
        this.createBiomeProvider();
        this.generateLightBrightnessTable();
    }

    protected void generateLightBrightnessTable() {
        float f = 0.0f;
        for (int i = 0; i <= 15; ++i) {
            float f1 = 1.0f - (float)i / 15.0f;
            this.lightBrightnessTable[i] = (1.0f - f1) / (f1 * 3.0f + 1.0f) * 1.0f + 0.0f;
        }
    }

    protected void createBiomeProvider() {
        this.field_191067_f = true;
        WorldType worldtype = this.worldObj.getWorldInfo().getTerrainType();
        if (worldtype == WorldType.FLAT) {
            FlatGeneratorInfo flatgeneratorinfo = FlatGeneratorInfo.createFlatGeneratorFromString(this.worldObj.getWorldInfo().getGeneratorOptions());
            this.biomeProvider = new BiomeProviderSingle(Biome.getBiome(flatgeneratorinfo.getBiome(), Biomes.DEFAULT));
        } else {
            this.biomeProvider = worldtype == WorldType.DEBUG_WORLD ? new BiomeProviderSingle(Biomes.PLAINS) : new BiomeProvider(this.worldObj.getWorldInfo());
        }
    }

    public IChunkGenerator createChunkGenerator() {
        if (this.terrainType == WorldType.FLAT) {
            return new ChunkGeneratorFlat(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings);
        }
        if (this.terrainType == WorldType.DEBUG_WORLD) {
            return new ChunkGeneratorDebug(this.worldObj);
        }
        return this.terrainType == WorldType.CUSTOMIZED ? new ChunkGeneratorOverworld(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : new ChunkGeneratorOverworld(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings);
    }

    public boolean canCoordinateBeSpawn(int x, int z) {
        BlockPos blockpos = new BlockPos(x, 0, z);
        if (this.worldObj.getBiome(blockpos).ignorePlayerSpawnSuitability()) {
            return true;
        }
        return this.worldObj.getGroundAboveSeaLevel(blockpos).getBlock() == Blocks.GRASS;
    }

    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        int i = (int)(worldTime % 24000L);
        float f = ((float)i + partialTicks) / 24000.0f - 0.25f;
        if (f < 0.0f) {
            f += 1.0f;
        }
        if (f > 1.0f) {
            f -= 1.0f;
        }
        float f1 = 1.0f - (float)((Math.cos((double)f * Math.PI) + 1.0) / 2.0);
        f += (f1 - f) / 3.0f;
        return f;
    }

    public int getMoonPhase(long worldTime) {
        return (int)(worldTime / 24000L % 8L + 8L) % 8;
    }

    public boolean isSurfaceWorld() {
        return true;
    }

    @Nullable
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
        float f = 0.4f;
        float f1 = MathHelper.cos(celestialAngle * ((float)Math.PI * 2)) - 0.0f;
        float f2 = -0.0f;
        if (f1 >= -0.4f && f1 <= 0.4f) {
            float f3 = (f1 - -0.0f) / 0.4f * 0.5f + 0.5f;
            float f4 = 1.0f - (1.0f - MathHelper.sin(f3 * (float)Math.PI)) * 0.99f;
            f4 *= f4;
            this.colorsSunriseSunset[0] = f3 * 0.3f + 0.7f;
            this.colorsSunriseSunset[1] = f3 * f3 * 0.7f + 0.2f;
            this.colorsSunriseSunset[2] = f3 * f3 * 0.0f + 0.2f;
            this.colorsSunriseSunset[3] = f4;
            return this.colorsSunriseSunset;
        }
        return null;
    }

    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        float f = MathHelper.cos(p_76562_1_ * ((float)Math.PI * 2)) * 2.0f + 0.5f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        float f1 = 0.7529412f;
        float f2 = 0.84705883f;
        float f3 = 1.0f;
        return new Vec3d(f1 *= f * 0.94f + 0.06f, f2 *= f * 0.94f + 0.06f, f3 *= f * 0.91f + 0.09f);
    }

    public boolean canRespawnHere() {
        return true;
    }

    public float getCloudHeight() {
        return 128.0f;
    }

    public boolean isSkyColored() {
        return true;
    }

    @Nullable
    public BlockPos getSpawnCoordinate() {
        return null;
    }

    public int getAverageGroundLevel() {
        return this.terrainType == WorldType.FLAT ? 4 : this.worldObj.getSeaLevel() + 1;
    }

    public double getVoidFogYFactor() {
        return this.terrainType == WorldType.FLAT ? 1.0 : 0.03125;
    }

    public boolean doesXZShowFog(int x, int z) {
        return false;
    }

    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }

    public boolean doesWaterVaporize() {
        return this.isHellWorld;
    }

    public boolean func_191066_m() {
        return this.field_191067_f;
    }

    public boolean getHasNoSky() {
        return this.hasNoSky;
    }

    public float[] getLightBrightnessTable() {
        return this.lightBrightnessTable;
    }

    public WorldBorder createWorldBorder() {
        return new WorldBorder();
    }

    public void onPlayerAdded(EntityPlayerMP player) {
    }

    public void onPlayerRemoved(EntityPlayerMP player) {
    }

    public abstract DimensionType getDimensionType();

    public void onWorldSave() {
    }

    public void onWorldUpdateEntities() {
    }

    public boolean canDropChunk(int x, int z) {
        return true;
    }

    public boolean isNether() {
        return this.isHellWorld;
    }
}

