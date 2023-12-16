// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import java.io.IOException;

public interface Packet
{
    void readPacketData(final PacketBuffer p0) throws IOException;
    
    void writePacketData(final PacketBuffer p0) throws IOException;
    
    void processPacket(final INetHandler p0);
}
