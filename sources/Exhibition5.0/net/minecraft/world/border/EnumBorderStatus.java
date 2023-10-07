// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.border;

public enum EnumBorderStatus
{
    GROWING("GROWING", 0, 4259712), 
    SHRINKING("SHRINKING", 1, 16724016), 
    STATIONARY("STATIONARY", 2, 2138367);
    
    private final int id;
    private static final EnumBorderStatus[] $VALUES;
    private static final String __OBFID = "CL_00002013";
    
    private EnumBorderStatus(final String p_i45647_1_, final int p_i45647_2_, final int id) {
        this.id = id;
    }
    
    public int func_177766_a() {
        return this.id;
    }
    
    static {
        $VALUES = new EnumBorderStatus[] { EnumBorderStatus.GROWING, EnumBorderStatus.SHRINKING, EnumBorderStatus.STATIONARY };
    }
}
