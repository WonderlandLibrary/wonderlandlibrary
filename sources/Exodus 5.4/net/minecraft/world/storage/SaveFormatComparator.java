/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.storage;

import net.minecraft.world.WorldSettings;

public class SaveFormatComparator
implements Comparable<SaveFormatComparator> {
    private final boolean requiresConversion;
    private final boolean hardcore;
    private final String displayName;
    private final String fileName;
    private final boolean cheatsEnabled;
    private final long sizeOnDisk;
    private final WorldSettings.GameType theEnumGameType;
    private final long lastTimePlayed;

    public String getDisplayName() {
        return this.displayName;
    }

    public boolean getCheatsEnabled() {
        return this.cheatsEnabled;
    }

    public long getSizeOnDisk() {
        return this.sizeOnDisk;
    }

    public SaveFormatComparator(String string, String string2, long l, long l2, WorldSettings.GameType gameType, boolean bl, boolean bl2, boolean bl3) {
        this.fileName = string;
        this.displayName = string2;
        this.lastTimePlayed = l;
        this.sizeOnDisk = l2;
        this.theEnumGameType = gameType;
        this.requiresConversion = bl;
        this.hardcore = bl2;
        this.cheatsEnabled = bl3;
    }

    public boolean requiresConversion() {
        return this.requiresConversion;
    }

    public String getFileName() {
        return this.fileName;
    }

    public WorldSettings.GameType getEnumGameType() {
        return this.theEnumGameType;
    }

    public long getLastTimePlayed() {
        return this.lastTimePlayed;
    }

    public boolean isHardcoreModeEnabled() {
        return this.hardcore;
    }

    @Override
    public int compareTo(SaveFormatComparator saveFormatComparator) {
        return this.lastTimePlayed < saveFormatComparator.lastTimePlayed ? 1 : (this.lastTimePlayed > saveFormatComparator.lastTimePlayed ? -1 : this.fileName.compareTo(saveFormatComparator.fileName));
    }
}

