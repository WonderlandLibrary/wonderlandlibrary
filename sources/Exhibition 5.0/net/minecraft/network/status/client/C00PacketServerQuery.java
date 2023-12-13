// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.status.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.status.INetHandlerStatusServer;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class C00PacketServerQuery implements Packet
{
    private static final String __OBFID = "CL_00001393";
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
    }
    
    public void func_180775_a(final INetHandlerStatusServer p_180775_1_) {
        p_180775_1_.processServerQuery(this);
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180775_a((INetHandlerStatusServer)handler);
    }
}
