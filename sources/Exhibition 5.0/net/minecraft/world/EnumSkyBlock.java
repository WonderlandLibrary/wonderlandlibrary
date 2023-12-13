// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world;

public enum EnumSkyBlock
{
    SKY("SKY", 0, 15), 
    BLOCK("BLOCK", 1, 0);
    
    public final int defaultLightValue;
    private static final EnumSkyBlock[] $VALUES;
    private static final String __OBFID = "CL_00000151";
    
    private EnumSkyBlock(final String p_i1961_1_, final int p_i1961_2_, final int p_i1961_3_) {
        this.defaultLightValue = p_i1961_3_;
    }
    
    static {
        $VALUES = new EnumSkyBlock[] { EnumSkyBlock.SKY, EnumSkyBlock.BLOCK };
    }
}
