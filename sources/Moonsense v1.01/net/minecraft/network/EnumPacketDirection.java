// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

public enum EnumPacketDirection
{
    SERVERBOUND("SERVERBOUND", 0, "SERVERBOUND", 0), 
    CLIENTBOUND("CLIENTBOUND", 1, "CLIENTBOUND", 1);
    
    private static final EnumPacketDirection[] $VALUES;
    private static final String __OBFID = "CL_00002307";
    
    static {
        $VALUES = new EnumPacketDirection[] { EnumPacketDirection.SERVERBOUND, EnumPacketDirection.CLIENTBOUND };
    }
    
    private EnumPacketDirection(final String name, final int ordinal, final String p_i45995_1_, final int p_i45995_2_) {
    }
}
