// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.storage;

import net.minecraft.world.WorldSettings;

public class SaveFormatComparator implements Comparable
{
    private final String fileName;
    private final String displayName;
    private final long lastTimePlayed;
    private final long sizeOnDisk;
    private final boolean requiresConversion;
    private final WorldSettings.GameType theEnumGameType;
    private final boolean hardcore;
    private final boolean cheatsEnabled;
    private static final String __OBFID = "CL_00000601";
    
    public SaveFormatComparator(final String p_i2161_1_, final String p_i2161_2_, final long p_i2161_3_, final long p_i2161_5_, final WorldSettings.GameType p_i2161_7_, final boolean p_i2161_8_, final boolean p_i2161_9_, final boolean p_i2161_10_) {
        this.fileName = p_i2161_1_;
        this.displayName = p_i2161_2_;
        this.lastTimePlayed = p_i2161_3_;
        this.sizeOnDisk = p_i2161_5_;
        this.theEnumGameType = p_i2161_7_;
        this.requiresConversion = p_i2161_8_;
        this.hardcore = p_i2161_9_;
        this.cheatsEnabled = p_i2161_10_;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public long func_154336_c() {
        return this.sizeOnDisk;
    }
    
    public boolean requiresConversion() {
        return this.requiresConversion;
    }
    
    public long getLastTimePlayed() {
        return this.lastTimePlayed;
    }
    
    public int compareTo(final SaveFormatComparator p_compareTo_1_) {
        return (this.lastTimePlayed < p_compareTo_1_.lastTimePlayed) ? 1 : ((this.lastTimePlayed > p_compareTo_1_.lastTimePlayed) ? -1 : this.fileName.compareTo(p_compareTo_1_.fileName));
    }
    
    public WorldSettings.GameType getEnumGameType() {
        return this.theEnumGameType;
    }
    
    public boolean isHardcoreModeEnabled() {
        return this.hardcore;
    }
    
    public boolean getCheatsEnabled() {
        return this.cheatsEnabled;
    }
    
    @Override
    public int compareTo(final Object p_compareTo_1_) {
        return this.compareTo((SaveFormatComparator)p_compareTo_1_);
    }
}
