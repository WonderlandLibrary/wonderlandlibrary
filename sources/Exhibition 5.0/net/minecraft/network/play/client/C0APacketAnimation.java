// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayServer;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class C0APacketAnimation implements Packet
{
    private static final String __OBFID = "CL_00001345";
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
    }
    
    public void func_179721_a(final INetHandlerPlayServer p_179721_1_) {
        p_179721_1_.func_175087_a(this);
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_179721_a((INetHandlerPlayServer)handler);
    }
}
