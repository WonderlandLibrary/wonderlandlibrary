/*
 * Decompiled with CFR 0.152.
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

    @Override
    public NBTTagCompound getNBTTagCompound() {
        return this.theWorldInfo.getNBTTagCompound();
    }

    @Override
    public String getWorldName() {
        return this.theWorldInfo.getWorldName();
    }

    @Override
    public boolean isDifficultyLocked() {
        return this.theWorldInfo.isDifficultyLocked();
    }

    @Override
    public void setWorldTime(long l) {
    }

    @Override
    public long getWorldTime() {
        return this.theWorldInfo.getWorldTime();
    }

    @Override
    public boolean areCommandsAllowed() {
        return this.theWorldInfo.areCommandsAllowed();
    }

    @Override
    public void setRainTime(int n) {
    }

    @Override
    public void setSpawnY(int n) {
    }

    @Override
    public NBTTagCompound cloneNBTCompound(NBTTagCompound nBTTagCompound) {
        return this.theWorldInfo.cloneNBTCompound(nBTTagCompound);
    }

    @Override
    public void setDifficulty(EnumDifficulty enumDifficulty) {
    }

    @Override
    public long getLastTimePlayed() {
        return this.theWorldInfo.getLastTimePlayed();
    }

    @Override
    public GameRules getGameRulesInstance() {
        return this.theWorldInfo.getGameRulesInstance();
    }

    @Override
    public void setWorldName(String string) {
    }

    @Override
    public boolean isRaining() {
        return this.theWorldInfo.isRaining();
    }

    @Override
    public WorldSettings.GameType getGameType() {
        return this.theWorldInfo.getGameType();
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
    public void setSpawnX(int n) {
    }

    @Override
    public boolean isHardcoreModeEnabled() {
        return this.theWorldInfo.isHardcoreModeEnabled();
    }

    @Override
    public void setSpawnZ(int n) {
    }

    @Override
    public int getSaveVersion() {
        return this.theWorldInfo.getSaveVersion();
    }

    @Override
    public NBTTagCompound getPlayerNBTTagCompound() {
        return this.theWorldInfo.getPlayerNBTTagCompound();
    }

    @Override
    public int getSpawnZ() {
        return this.theWorldInfo.getSpawnZ();
    }

    @Override
    public int getRainTime() {
        return this.theWorldInfo.getRainTime();
    }

    @Override
    public EnumDifficulty getDifficulty() {
        return this.theWorldInfo.getDifficulty();
    }

    @Override
    public void setThundering(boolean bl) {
    }

    @Override
    public void setSpawn(BlockPos blockPos) {
    }

    @Override
    public boolean isMapFeaturesEnabled() {
        return this.theWorldInfo.isMapFeaturesEnabled();
    }

    @Override
    public void setThunderTime(int n) {
    }

    @Override
    public WorldType getTerrainType() {
        return this.theWorldInfo.getTerrainType();
    }

    @Override
    public long getWorldTotalTime() {
        return this.theWorldInfo.getWorldTotalTime();
    }

    @Override
    public void setSaveVersion(int n) {
    }

    @Override
    public void setTerrainType(WorldType worldType) {
    }

    @Override
    public void setRaining(boolean bl) {
    }

    @Override
    public long getSeed() {
        return this.theWorldInfo.getSeed();
    }

    @Override
    public void setWorldTotalTime(long l) {
    }

    @Override
    public boolean isThundering() {
        return this.theWorldInfo.isThundering();
    }

    @Override
    public void setAllowCommands(boolean bl) {
    }

    @Override
    public void setDifficultyLocked(boolean bl) {
    }

    @Override
    public int getThunderTime() {
        return this.theWorldInfo.getThunderTime();
    }

    @Override
    public boolean isInitialized() {
        return this.theWorldInfo.isInitialized();
    }

    @Override
    public long getSizeOnDisk() {
        return this.theWorldInfo.getSizeOnDisk();
    }

    @Override
    public void setServerInitialized(boolean bl) {
    }

    public DerivedWorldInfo(WorldInfo worldInfo) {
        this.theWorldInfo = worldInfo;
    }
}

