// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import javax.annotation.Nullable;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.init.Biomes;
import net.minecraft.world.end.DragonFightManager;

public class WorldProviderEnd extends WorldProvider
{
    private DragonFightManager dragonFightManager;
    
    public void init() {
        this.biomeProvider = new BiomeProviderSingle(Biomes.SKY);
        final NBTTagCompound nbttagcompound = this.world.getWorldInfo().getDimensionData(DimensionType.THE_END);
        this.dragonFightManager = ((this.world instanceof WorldServer) ? new DragonFightManager((WorldServer)this.world, nbttagcompound.getCompoundTag("DragonFight")) : null);
    }
    
    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorEnd(this.world, this.world.getWorldInfo().isMapFeaturesEnabled(), this.world.getSeed(), this.getSpawnCoordinate());
    }
    
    @Override
    public float calculateCelestialAngle(final long worldTime, final float partialTicks) {
        return 0.0f;
    }
    
    @Nullable
    @Override
    public float[] calcSunriseSunsetColors(final float celestialAngle, final float partialTicks) {
        return null;
    }
    
    @Override
    public Vec3d getFogColor(final float p_76562_1_, final float p_76562_2_) {
        final int i = 10518688;
        float f = MathHelper.cos(p_76562_1_ * 6.2831855f) * 2.0f + 0.5f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        float f2 = 0.627451f;
        float f3 = 0.5019608f;
        float f4 = 0.627451f;
        f2 *= f * 0.0f + 0.15f;
        f3 *= f * 0.0f + 0.15f;
        f4 *= f * 0.0f + 0.15f;
        return new Vec3d(f2, f3, f4);
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
        return this.world.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
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
    public boolean doesXZShowFog(final int x, final int z) {
        return false;
    }
    
    @Override
    public DimensionType getDimensionType() {
        return DimensionType.THE_END;
    }
    
    @Override
    public void onWorldSave() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        if (this.dragonFightManager != null) {
            nbttagcompound.setTag("DragonFight", this.dragonFightManager.getCompound());
        }
        this.world.getWorldInfo().setDimensionData(DimensionType.THE_END, nbttagcompound);
    }
    
    @Override
    public void onWorldUpdateEntities() {
        if (this.dragonFightManager != null) {
            this.dragonFightManager.tick();
        }
    }
    
    @Nullable
    public DragonFightManager getDragonFightManager() {
        return this.dragonFightManager;
    }
}
