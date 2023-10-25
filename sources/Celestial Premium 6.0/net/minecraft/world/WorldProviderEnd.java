/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderEnd
extends WorldProvider {
    private DragonFightManager dragonFightManager;

    @Override
    public void createBiomeProvider() {
        this.biomeProvider = new BiomeProviderSingle(Biomes.SKY);
        NBTTagCompound nbttagcompound = this.worldObj.getWorldInfo().getDimensionData(DimensionType.THE_END);
        this.dragonFightManager = this.worldObj instanceof WorldServer ? new DragonFightManager((WorldServer)this.worldObj, nbttagcompound.getCompoundTag("DragonFight")) : null;
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorEnd(this.worldObj, this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.worldObj.getSeed(), this.getSpawnCoordinate());
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0.0f;
    }

    @Override
    @Nullable
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
        return null;
    }

    @Override
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        int i = 0xA080A0;
        float f = MathHelper.cos(p_76562_1_ * ((float)Math.PI * 2)) * 2.0f + 0.5f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        float f1 = 0.627451f;
        float f2 = 0.5019608f;
        float f3 = 0.627451f;
        return new Vec3d(f1 *= f * 0.0f + 0.15f, f2 *= f * 0.0f + 0.15f, f3 *= f * 0.0f + 0.15f);
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
    public boolean canCoordinateBeSpawn(int x, int z) {
        return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
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
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }

    @Override
    public DimensionType getDimensionType() {
        return DimensionType.THE_END;
    }

    @Override
    public void onWorldSave() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        if (this.dragonFightManager != null) {
            nbttagcompound.setTag("DragonFight", this.dragonFightManager.getCompound());
        }
        this.worldObj.getWorldInfo().setDimensionData(DimensionType.THE_END, nbttagcompound);
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

