// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.util.math.Vec3d;
import javax.annotation.Nullable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.biome.Biome;
import net.minecraft.init.Biomes;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.biome.BiomeProvider;

public abstract class WorldProvider
{
    public static final float[] MOON_PHASE_FACTORS;
    protected World world;
    private WorldType terrainType;
    private String generatorSettings;
    protected BiomeProvider biomeProvider;
    protected boolean doesWaterVaporize;
    protected boolean nether;
    protected boolean hasSkyLight;
    protected final float[] lightBrightnessTable;
    private final float[] colorsSunriseSunset;
    
    public WorldProvider() {
        this.lightBrightnessTable = new float[16];
        this.colorsSunriseSunset = new float[4];
    }
    
    public final void setWorld(final World worldIn) {
        this.world = worldIn;
        this.terrainType = worldIn.getWorldInfo().getTerrainType();
        this.generatorSettings = worldIn.getWorldInfo().getGeneratorOptions();
        this.init();
        this.generateLightBrightnessTable();
    }
    
    protected void generateLightBrightnessTable() {
        final float f = 0.0f;
        for (int i = 0; i <= 15; ++i) {
            final float f2 = 1.0f - i / 15.0f;
            this.lightBrightnessTable[i] = (1.0f - f2) / (f2 * 3.0f + 1.0f) * 1.0f + 0.0f;
        }
    }
    
    protected void init() {
        this.hasSkyLight = true;
        final WorldType worldtype = this.world.getWorldInfo().getTerrainType();
        if (worldtype == WorldType.FLAT) {
            final FlatGeneratorInfo flatgeneratorinfo = FlatGeneratorInfo.createFlatGeneratorFromString(this.world.getWorldInfo().getGeneratorOptions());
            this.biomeProvider = new BiomeProviderSingle(Biome.getBiome(flatgeneratorinfo.getBiome(), Biomes.DEFAULT));
        }
        else if (worldtype == WorldType.DEBUG_ALL_BLOCK_STATES) {
            this.biomeProvider = new BiomeProviderSingle(Biomes.PLAINS);
        }
        else {
            this.biomeProvider = new BiomeProvider(this.world.getWorldInfo());
        }
    }
    
    public IChunkGenerator createChunkGenerator() {
        if (this.terrainType == WorldType.FLAT) {
            return new ChunkGeneratorFlat(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings);
        }
        if (this.terrainType == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return new ChunkGeneratorDebug(this.world);
        }
        return (this.terrainType == WorldType.CUSTOMIZED) ? new ChunkGeneratorOverworld(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : new ChunkGeneratorOverworld(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings);
    }
    
    public boolean canCoordinateBeSpawn(final int x, final int z) {
        final BlockPos blockpos = new BlockPos(x, 0, z);
        return this.world.getBiome(blockpos).ignorePlayerSpawnSuitability() || this.world.getGroundAboveSeaLevel(blockpos).getBlock() == Blocks.GRASS;
    }
    
    public float calculateCelestialAngle(final long worldTime, final float partialTicks) {
        final int i = (int)(worldTime % 24000L);
        float f = (i + partialTicks) / 24000.0f - 0.25f;
        if (f < 0.0f) {
            ++f;
        }
        if (f > 1.0f) {
            --f;
        }
        final float f2 = 1.0f - (float)((Math.cos(f * 3.141592653589793) + 1.0) / 2.0);
        f += (f2 - f) / 3.0f;
        return f;
    }
    
    public int getMoonPhase(final long worldTime) {
        return (int)(worldTime / 24000L % 8L + 8L) % 8;
    }
    
    public boolean isSurfaceWorld() {
        return true;
    }
    
    @Nullable
    public float[] calcSunriseSunsetColors(final float celestialAngle, final float partialTicks) {
        final float f = 0.4f;
        final float f2 = MathHelper.cos(celestialAngle * 6.2831855f) - 0.0f;
        final float f3 = -0.0f;
        if (f2 >= -0.4f && f2 <= 0.4f) {
            final float f4 = (f2 + 0.0f) / 0.4f * 0.5f + 0.5f;
            float f5 = 1.0f - (1.0f - MathHelper.sin(f4 * 3.1415927f)) * 0.99f;
            f5 *= f5;
            this.colorsSunriseSunset[0] = f4 * 0.3f + 0.7f;
            this.colorsSunriseSunset[1] = f4 * f4 * 0.7f + 0.2f;
            this.colorsSunriseSunset[2] = f4 * f4 * 0.0f + 0.2f;
            this.colorsSunriseSunset[3] = f5;
            return this.colorsSunriseSunset;
        }
        return null;
    }
    
    public Vec3d getFogColor(final float p_76562_1_, final float p_76562_2_) {
        float f = MathHelper.cos(p_76562_1_ * 6.2831855f) * 2.0f + 0.5f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        float f2 = 0.7529412f;
        float f3 = 0.84705883f;
        float f4 = 1.0f;
        f2 *= f * 0.94f + 0.06f;
        f3 *= f * 0.94f + 0.06f;
        f4 *= f * 0.91f + 0.09f;
        return new Vec3d(f2, f3, f4);
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
        return (this.terrainType == WorldType.FLAT) ? 4 : (this.world.getSeaLevel() + 1);
    }
    
    public double getVoidFogYFactor() {
        return (this.terrainType == WorldType.FLAT) ? 1.0 : 0.03125;
    }
    
    public boolean doesXZShowFog(final int x, final int z) {
        return false;
    }
    
    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public boolean doesWaterVaporize() {
        return this.doesWaterVaporize;
    }
    
    public boolean hasSkyLight() {
        return this.hasSkyLight;
    }
    
    public boolean isNether() {
        return this.nether;
    }
    
    public float[] getLightBrightnessTable() {
        return this.lightBrightnessTable;
    }
    
    public WorldBorder createWorldBorder() {
        return new WorldBorder();
    }
    
    public void onPlayerAdded(final EntityPlayerMP player) {
    }
    
    public void onPlayerRemoved(final EntityPlayerMP player) {
    }
    
    public abstract DimensionType getDimensionType();
    
    public void onWorldSave() {
    }
    
    public void onWorldUpdateEntities() {
    }
    
    public boolean canDropChunk(final int x, final int z) {
        return true;
    }
    
    static {
        MOON_PHASE_FACTORS = new float[] { 1.0f, 0.75f, 0.5f, 0.25f, 0.0f, 0.25f, 0.5f, 0.75f };
    }
}
