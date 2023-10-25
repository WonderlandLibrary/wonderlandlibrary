/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import net.minecraft.world.storage.SaveFormatComparator;

public class RealmsLevelSummary
implements Comparable<RealmsLevelSummary> {
    private SaveFormatComparator levelSummary;

    public RealmsLevelSummary(SaveFormatComparator saveFormatComparator) {
        this.levelSummary = saveFormatComparator;
    }

    public long getLastPlayed() {
        return this.levelSummary.getLastTimePlayed();
    }

    @Override
    public int compareTo(RealmsLevelSummary realmsLevelSummary) {
        return this.levelSummary.getLastTimePlayed() < realmsLevelSummary.getLastPlayed() ? 1 : (this.levelSummary.getLastTimePlayed() > realmsLevelSummary.getLastPlayed() ? -1 : this.levelSummary.getFileName().compareTo(realmsLevelSummary.getLevelId()));
    }

    public String getLevelId() {
        return this.levelSummary.getFileName();
    }

    public int getGameMode() {
        return this.levelSummary.getEnumGameType().getID();
    }

    public boolean isRequiresConversion() {
        return this.levelSummary.requiresConversion();
    }

    @Override
    public int compareTo(SaveFormatComparator saveFormatComparator) {
        return this.levelSummary.compareTo(saveFormatComparator);
    }

    public long getSizeOnDisk() {
        return this.levelSummary.getSizeOnDisk();
    }

    public String getLevelName() {
        return this.levelSummary.getDisplayName();
    }

    public boolean isHardcore() {
        return this.levelSummary.isHardcoreModeEnabled();
    }

    public boolean hasCheats() {
        return this.levelSummary.getCheatsEnabled();
    }
}

