// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.status.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.status.INetHandlerStatusServer;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class C01PacketPing implements Packet
{
    private long clientTime;
    private static final String __OBFID = "CL_00001392";
    
    public C01PacketPing() {
    }
    
    public C01PacketPing(final long p_i45276_1_) {
        this.clientTime = p_i45276_1_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.clientTime = data.readLong();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeLong(this.clientTime);
    }
    
    public void func_180774_a(final INetHandlerStatusServer p_180774_1_) {
        p_180774_1_.processPing(this);
    }
    
    public long getClientTime() {
        return this.clientTime;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180774_a((INetHandlerStatusServer)handler);
    }
}
