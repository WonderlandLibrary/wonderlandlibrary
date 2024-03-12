// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S46PacketSetCompressionLevel implements Packet<INetHandlerPlayClient>
{
    private int threshold;
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.threshold = buf.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.threshold);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSetCompressionLevel(this);
    }
    
    public int getThreshold() {
        return this.threshold;
    }
}
