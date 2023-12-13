// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.realms;

public class RealmsServerPing
{
    public volatile String nrOfPlayers;
    public volatile long lastPingSnapshot;
    private static final String __OBFID = "CL_00002328";
    
    public RealmsServerPing() {
        this.nrOfPlayers = "0";
        this.lastPingSnapshot = 0L;
    }
}
