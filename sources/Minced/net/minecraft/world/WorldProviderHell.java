// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.init.Biomes;

public class WorldProviderHell extends WorldProvider
{
    public void init() {
        this.biomeProvider = new BiomeProviderSingle(Biomes.HELL);
        this.doesWaterVaporize = true;
        this.nether = true;
    }
    
    @Override
    public Vec3d getFogColor(final float p_76562_1_, final float p_76562_2_) {
        return new Vec3d(0.20000000298023224, 0.029999999329447746, 0.029999999329447746);
    }
    
    @Override
    protected void generateLightBrightnessTable() {
        final float f = 0.1f;
        for (int i = 0; i <= 15; ++i) {
            final float f2 = 1.0f - i / 15.0f;
            this.lightBrightnessTable[i] = (1.0f - f2) / (f2 * 3.0f + 1.0f) * 0.9f + 0.1f;
        }
    }
    
    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorHell(this.world, this.world.getWorldInfo().isMapFeaturesEnabled(), this.world.getSeed());
    }
    
    @Override
    public boolean isSurfaceWorld() {
        return false;
    }
    
    @Override
    public boolean canCoordinateBeSpawn(final int x, final int z) {
        return false;
    }
    
    @Override
    public float calculateCelestialAngle(final long worldTime, final float partialTicks) {
        return 0.5f;
    }
    
    @Override
    public boolean canRespawnHere() {
        return false;
    }
    
    @Override
    public boolean doesXZShowFog(final int x, final int z) {
        return true;
    }
    
    @Override
    public WorldBorder createWorldBorder() {
        return new WorldBorder() {
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
    
    @Override
    public DimensionType getDimensionType() {
        return DimensionType.NETHER;
    }
}
