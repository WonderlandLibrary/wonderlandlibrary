/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;

public class DerivedWorldInfo
extends WorldInfo {
    private final WorldInfo theWorldInfo;
    private static final String __OBFID = "CL_00000584";

    public DerivedWorldInfo(WorldInfo p_i2145_1_) {
        this.theWorldInfo = p_i2145_1_;
    }

    @Override
    public NBTTagCompound getNBTTagCompound() {
        return this.theWorldInfo.getNBTTagCompound();
    }

    @Override
    public NBTTagCompound cloneNBTCompound(NBTTagCompound nbt) {
        return this.theWorldInfo.cloneNBTCompound(nbt);
    }

    @Override
    public long getSeed() {
        return this.theWorldInfo.getSeed();
    }

    @Override
    public int getSpawnX() {
        return this.theWorldInfo.getSpawnX();
    }

    @Override
    public int getSpawnY() {
        return this.theWorldInfo.getSpawnY();
    }

    @Override
    public int getSpawnZ() {
        return this.theWorldInfo.getSpawnZ();
    }

    @Override
    public long getWorldTotalTime() {
        return this.theWorldInfo.getWorldTotalTime();
    }

    @Override
    public long getWorldTime() {
        return this.theWorldInfo.getWorldTime();
    }

    @Override
    public long getSizeOnDisk() {
        return this.theWorldInfo.getSizeOnDisk();
    }

    @Override
    public NBTTagCompound getPlayerNBTTagCompound() {
        return this.theWorldInfo.getPlayerNBTTagCompound();
    }

    @Override
    public String getWorldName() {
        return this.theWorldInfo.getWorldName();
    }

    @Override
    public int getSaveVersion() {
        return this.theWorldInfo.getSaveVersion();
    }

    @Override
    public long getLastTimePlayed() {
        return this.theWorldInfo.getLastTimePlayed();
    }

    @Override
    public boolean isThundering() {
        return this.theWorldInfo.isThundering();
    }

    @Override
    public int getThunderTime() {
        return this.theWorldInfo.getThunderTime();
    }

    @Override
    public boolean isRaining() {
        return this.theWorldInfo.isRaining();
    }

    @Override
    public int getRainTime() {
        return this.theWorldInfo.getRainTime();
    }

    @Override
    public WorldSettings.GameType getGameType() {
        return this.theWorldInfo.getGameType();
    }

    @Override
    public void setSpawnX(int p_76058_1_) {
    }

    @Override
    public void setSpawnY(int p_76056_1_) {
    }

    @Override
    public void setSpawnZ(int p_76087_1_) {
    }

    @Override
    public void incrementTotalWorldTime(long p_82572_1_) {
    }

    @Override
    public void setWorldTime(long p_76068_1_) {
    }

    @Override
    public void setSpawn(BlockPos spawnPoint) {
    }

    @Override
    public void setWorldName(String p_76062_1_) {
    }

    @Override
    public void setSaveVersion(int p_76078_1_) {
    }

    @Override
    public void setThundering(boolean p_76069_1_) {
    }

    @Override
    public void setThunderTime(int p_76090_1_) {
    }

    @Override
    public void setRaining(boolean p_76084_1_) {
    }

    @Override
    public void setRainTime(int p_76080_1_) {
    }

    @Override
    public boolean isMapFeaturesEnabled() {
        return this.theWorldInfo.isMapFeaturesEnabled();
    }

    @Override
    public boolean isHardcoreModeEnabled() {
        return this.theWorldInfo.isHardcoreModeEnabled();
    }

    @Override
    public WorldType getTerrainType() {
        return this.theWorldInfo.getTerrainType();
    }

    @Override
    public void setTerrainType(WorldType p_76085_1_) {
    }

    @Override
    public boolean areCommandsAllowed() {
        return this.theWorldInfo.areCommandsAllowed();
    }

    @Override
    public void setAllowCommands(boolean allow) {
    }

    @Override
    public boolean isInitialized() {
        return this.theWorldInfo.isInitialized();
    }

    @Override
    public void setServerInitialized(boolean initializedIn) {
    }

    @Override
    public GameRules getGameRulesInstance() {
        return this.theWorldInfo.getGameRulesInstance();
    }

    @Override
    public EnumDifficulty getDifficulty() {
        return this.theWorldInfo.getDifficulty();
    }

    @Override
    public void setDifficulty(EnumDifficulty newDifficulty) {
    }

    @Override
    public boolean isDifficultyLocked() {
        return this.theWorldInfo.isDifficultyLocked();
    }

    @Override
    public void setDifficultyLocked(boolean locked) {
    }
}

