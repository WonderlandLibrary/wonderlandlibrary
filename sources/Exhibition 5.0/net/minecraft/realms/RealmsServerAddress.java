// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.realms;

import net.minecraft.client.multiplayer.ServerAddress;

public class RealmsServerAddress
{
    private final String host;
    private final int port;
    private static final String __OBFID = "CL_00001864";
    
    protected RealmsServerAddress(final String p_i1121_1_, final int p_i1121_2_) {
        this.host = p_i1121_1_;
        this.port = p_i1121_2_;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public static RealmsServerAddress parseString(final String p_parseString_0_) {
        final ServerAddress var1 = ServerAddress.func_78860_a(p_parseString_0_);
        return new RealmsServerAddress(var1.getIP(), var1.getPort());
    }
}
