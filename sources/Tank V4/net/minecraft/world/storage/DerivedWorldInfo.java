package net.minecraft.world.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class DerivedWorldInfo extends WorldInfo {
   private final WorldInfo theWorldInfo;

   public boolean isHardcoreModeEnabled() {
      return this.theWorldInfo.isHardcoreModeEnabled();
   }

   public boolean isMapFeaturesEnabled() {
      return this.theWorldInfo.isMapFeaturesEnabled();
   }

   public EnumDifficulty getDifficulty() {
      return this.theWorldInfo.getDifficulty();
   }

   public void setDifficultyLocked(boolean var1) {
   }

   public int getSpawnY() {
      return this.theWorldInfo.getSpawnY();
   }

   public long getWorldTotalTime() {
      return this.theWorldInfo.getWorldTotalTime();
   }

   public void setSaveVersion(int var1) {
   }

   public int getThunderTime() {
      return this.theWorldInfo.getThunderTime();
   }

   public void setSpawnY(int var1) {
   }

   public WorldType getTerrainType() {
      return this.theWorldInfo.getTerrainType();
   }

   public void setSpawn(BlockPos var1) {
   }

   public void setSpawnX(int var1) {
   }

   public long getSizeOnDisk() {
      return this.theWorldInfo.getSizeOnDisk();
   }

   public void setThunderTime(int var1) {
   }

   public GameRules getGameRulesInstance() {
      return this.theWorldInfo.getGameRulesInstance();
   }

   public NBTTagCompound cloneNBTCompound(NBTTagCompound var1) {
      return this.theWorldInfo.cloneNBTCompound(var1);
   }

   public boolean isRaining() {
      return this.theWorldInfo.isRaining();
   }

   public void setDifficulty(EnumDifficulty var1) {
   }

   public void setServerInitialized(boolean var1) {
   }

   public NBTTagCompound getPlayerNBTTagCompound() {
      return this.theWorldInfo.getPlayerNBTTagCompound();
   }

   public String getWorldName() {
      return this.theWorldInfo.getWorldName();
   }

   public void setThundering(boolean var1) {
   }

   public void setWorldTime(long var1) {
   }

   public void setSpawnZ(int var1) {
   }

   public WorldSettings.GameType getGameType() {
      return this.theWorldInfo.getGameType();
   }

   public long getSeed() {
      return this.theWorldInfo.getSeed();
   }

   public boolean areCommandsAllowed() {
      return this.theWorldInfo.areCommandsAllowed();
   }

   public int getSpawnX() {
      return this.theWorldInfo.getSpawnX();
   }

   public void setAllowCommands(boolean var1) {
   }

   public void setRainTime(int var1) {
   }

   public DerivedWorldInfo(WorldInfo var1) {
      this.theWorldInfo = var1;
   }

   public int getSpawnZ() {
      return this.theWorldInfo.getSpawnZ();
   }

   public int getSaveVersion() {
      return this.theWorldInfo.getSaveVersion();
   }

   public void setRaining(boolean var1) {
   }

   public void setWorldTotalTime(long var1) {
   }

   public boolean isThundering() {
      return this.theWorldInfo.isThundering();
   }

   public boolean isInitialized() {
      return this.theWorldInfo.isInitialized();
   }

   public int getRainTime() {
      return this.theWorldInfo.getRainTime();
   }

   public void setWorldName(String var1) {
   }

   public boolean isDifficultyLocked() {
      return this.theWorldInfo.isDifficultyLocked();
   }

   public void setTerrainType(WorldType var1) {
   }

   public long getLastTimePlayed() {
      return this.theWorldInfo.getLastTimePlayed();
   }

   public NBTTagCompound getNBTTagCompound() {
      return this.theWorldInfo.getNBTTagCompound();
   }

   public long getWorldTime() {
      return this.theWorldInfo.getWorldTime();
   }
}
