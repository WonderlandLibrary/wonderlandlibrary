/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network;

public enum EnumPacketDirection {
    SERVERBOUND("SERVERBOUND", 0),
    CLIENTBOUND("CLIENTBOUND", 1);

    private static final EnumPacketDirection[] $VALUES;
    private static final String __OBFID = "CL_00002307";

    static {
        $VALUES = new EnumPacketDirection[]{SERVERBOUND, CLIENTBOUND};
    }

    private EnumPacketDirection(String p_i45995_1_, int p_i45995_2_) {
    }
}

