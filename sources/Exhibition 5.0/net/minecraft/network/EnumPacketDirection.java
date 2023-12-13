// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network;

public enum EnumPacketDirection
{
    SERVERBOUND("SERVERBOUND", 0), 
    CLIENTBOUND("CLIENTBOUND", 1);
    
    private static final EnumPacketDirection[] $VALUES;
    private static final String __OBFID = "CL_00002307";
    
    private EnumPacketDirection(final String p_i45995_1_, final int p_i45995_2_) {
    }
    
    static {
        $VALUES = new EnumPacketDirection[] { EnumPacketDirection.SERVERBOUND, EnumPacketDirection.CLIENTBOUND };
    }
}
