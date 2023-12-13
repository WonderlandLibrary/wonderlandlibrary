// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.biome.BiomeGenBase;

public class WorldProviderEnd extends WorldProvider
{
    private static final String __OBFID = "CL_00000389";
    
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
    public float calculateCelestialAngle(final long p_76563_1_, final float p_76563_3_) {
        return 0.0f;
    }
    
    @Override
    public float[] calcSunriseSunsetColors(final float p_76560_1_, final float p_76560_2_) {
        return null;
    }
    
    @Override
    public Vec3 getFogColor(final float p_76562_1_, final float p_76562_2_) {
        final int var3 = 10518688;
        float var4 = MathHelper.cos(p_76562_1_ * 3.1415927f * 2.0f) * 2.0f + 0.5f;
        var4 = MathHelper.clamp_float(var4, 0.0f, 1.0f);
        float var5 = (var3 >> 16 & 0xFF) / 255.0f;
        float var6 = (var3 >> 8 & 0xFF) / 255.0f;
        float var7 = (var3 & 0xFF) / 255.0f;
        var5 *= var4 * 0.0f + 0.15f;
        var6 *= var4 * 0.0f + 0.15f;
        var7 *= var4 * 0.0f + 0.15f;
        return new Vec3(var5, var6, var7);
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
    public boolean canCoordinateBeSpawn(final int x, final int z) {
        return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
    }
    
    @Override
    public BlockPos func_177496_h() {
        return new BlockPos(100, 50, 0);
    }
    
    @Override
    public int getAverageGroundLevel() {
        return 50;
    }
    
    @Override
    public boolean doesXZShowFog(final int p_76568_1_, final int p_76568_2_) {
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
