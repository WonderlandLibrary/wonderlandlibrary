// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public class DerivedWorldInfo extends WorldInfo
{
    private final WorldInfo delegate;
    
    public DerivedWorldInfo(final WorldInfo worldInfoIn) {
        this.delegate = worldInfoIn;
    }
    
    @Override
    public NBTTagCompound cloneNBTCompound(@Nullable final NBTTagCompound nbt) {
        return this.delegate.cloneNBTCompound(nbt);
    }
    
    @Override
    public long getSeed() {
        return this.delegate.getSeed();
    }
    
    @Override
    public int getSpawnX() {
        return this.delegate.getSpawnX();
    }
    
    @Override
    public int getSpawnY() {
        return this.delegate.getSpawnY();
    }
    
    @Override
    public int getSpawnZ() {
        return this.delegate.getSpawnZ();
    }
    
    @Override
    public long getWorldTotalTime() {
        return this.delegate.getWorldTotalTime();
    }
    
    @Override
    public long getWorldTime() {
        return this.delegate.getWorldTime();
    }
    
    @Override
    public long getSizeOnDisk() {
        return this.delegate.getSizeOnDisk();
    }
    
    @Override
    public NBTTagCompound getPlayerNBTTagCompound() {
        return this.delegate.getPlayerNBTTagCompound();
    }
    
    @Override
    public String getWorldName() {
        return this.delegate.getWorldName();
    }
    
    @Override
    public int getSaveVersion() {
        return this.delegate.getSaveVersion();
    }
    
    @Override
    public long getLastTimePlayed() {
        return this.delegate.getLastTimePlayed();
    }
    
    @Override
    public boolean isThundering() {
        return this.delegate.isThundering();
    }
    
    @Override
    public int getThunderTime() {
        return this.delegate.getThunderTime();
    }
    
    @Override
    public boolean isRaining() {
        return this.delegate.isRaining();
    }
    
    @Override
    public int getRainTime() {
        return this.delegate.getRainTime();
    }
    
    @Override
    public GameType getGameType() {
        return this.delegate.getGameType();
    }
    
    @Override
    public void setSpawnX(final int x) {
    }
    
    @Override
    public void setSpawnY(final int y) {
    }
    
    @Override
    public void setSpawnZ(final int z) {
    }
    
    @Override
    public void setWorldTotalTime(final long time) {
    }
    
    @Override
    public void setWorldTime(final long time) {
    }
    
    @Override
    public void setSpawn(final BlockPos spawnPoint) {
    }
    
    @Override
    public void setWorldName(final String worldName) {
    }
    
    @Override
    public void setSaveVersion(final int version) {
    }
    
    @Override
    public void setThundering(final boolean thunderingIn) {
    }
    
    @Override
    public void setThunderTime(final int time) {
    }
    
    @Override
    public void setRaining(final boolean isRaining) {
    }
    
    @Override
    public void setRainTime(final int time) {
    }
    
    @Override
    public boolean isMapFeaturesEnabled() {
        return this.delegate.isMapFeaturesEnabled();
    }
    
    @Override
    public boolean isHardcoreModeEnabled() {
        return this.delegate.isHardcoreModeEnabled();
    }
    
    @Override
    public WorldType getTerrainType() {
        return this.delegate.getTerrainType();
    }
    
    @Override
    public void setTerrainType(final WorldType type) {
    }
    
    @Override
    public boolean areCommandsAllowed() {
        return this.delegate.areCommandsAllowed();
    }
    
    @Override
    public void setAllowCommands(final boolean allow) {
    }
    
    @Override
    public boolean isInitialized() {
        return this.delegate.isInitialized();
    }
    
    @Override
    public void setServerInitialized(final boolean initializedIn) {
    }
    
    @Override
    public GameRules getGameRulesInstance() {
        return this.delegate.getGameRulesInstance();
    }
    
    @Override
    public EnumDifficulty getDifficulty() {
        return this.delegate.getDifficulty();
    }
    
    @Override
    public void setDifficulty(final EnumDifficulty newDifficulty) {
    }
    
    @Override
    public boolean isDifficultyLocked() {
        return this.delegate.isDifficultyLocked();
    }
    
    @Override
    public void setDifficultyLocked(final boolean locked) {
    }
    
    @Override
    public void setDimensionData(final DimensionType dimensionIn, final NBTTagCompound compound) {
        this.delegate.setDimensionData(dimensionIn, compound);
    }
    
    @Override
    public NBTTagCompound getDimensionData(final DimensionType dimensionIn) {
        return this.delegate.getDimensionData(dimensionIn);
    }
}
