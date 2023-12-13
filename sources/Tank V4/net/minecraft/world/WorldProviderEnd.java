package net.minecraft.world;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;

public class WorldProviderEnd extends WorldProvider {
   public String getInternalNameSuffix() {
      return "_end";
   }

   public boolean isSurfaceWorld() {
      return false;
   }

   public boolean isSkyColored() {
      return false;
   }

   public String getDimensionName() {
      return "The End";
   }

   public int getAverageGroundLevel() {
      return 50;
   }

   public float calculateCelestialAngle(long var1, float var3) {
      return 0.0F;
   }

   public boolean canRespawnHere() {
      return false;
   }

   public Vec3 getFogColor(float var1, float var2) {
      int var3 = 10518688;
      float var4 = MathHelper.cos(var1 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
      var4 = MathHelper.clamp_float(var4, 0.0F, 1.0F);
      float var5 = (float)(var3 >> 16 & 255) / 255.0F;
      float var6 = (float)(var3 >> 8 & 255) / 255.0F;
      float var7 = (float)(var3 & 255) / 255.0F;
      var5 *= var4 * 0.0F + 0.15F;
      var6 *= var4 * 0.0F + 0.15F;
      var7 *= var4 * 0.0F + 0.15F;
      return new Vec3((double)var5, (double)var6, (double)var7);
   }

   public BlockPos getSpawnCoordinate() {
      return new BlockPos(100, 50, 0);
   }

   public float[] calcSunriseSunsetColors(float var1, float var2) {
      return null;
   }

   public boolean doesXZShowFog(int var1, int var2) {
      return true;
   }

   public IChunkProvider createChunkGenerator() {
      return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
   }

   public boolean canCoordinateBeSpawn(int var1, int var2) {
      return this.worldObj.getGroundAboveSeaLevel(new BlockPos(var1, 0, var2)).getMaterial().blocksMovement();
   }

   public void registerWorldChunkManager() {
      this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.0F);
      this.dimensionId = 1;
      this.hasNoSky = true;
   }

   public float getCloudHeight() {
      return 8.0F;
   }
}
