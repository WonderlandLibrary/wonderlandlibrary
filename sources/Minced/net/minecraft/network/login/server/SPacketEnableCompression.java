// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.login.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.Packet;

public class SPacketEnableCompression implements Packet<INetHandlerLoginClient>
{
    private int compressionThreshold;
    
    public SPacketEnableCompression() {
    }
    
    public SPacketEnableCompression(final int thresholdIn) {
        this.compressionThreshold = thresholdIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.compressionThreshold = buf.readVarInt();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.compressionThreshold);
    }
    
    @Override
    public void processPacket(final INetHandlerLoginClient handler) {
        handler.handleEnableCompression(this);
    }
    
    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }
}
