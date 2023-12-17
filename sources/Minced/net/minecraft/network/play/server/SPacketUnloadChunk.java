// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketUnloadChunk implements Packet<INetHandlerPlayClient>
{
    private int x;
    private int z;
    
    public SPacketUnloadChunk() {
    }
    
    public SPacketUnloadChunk(final int xIn, final int zIn) {
        this.x = xIn;
        this.z = zIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.x = buf.readInt();
        this.z = buf.readInt();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.x);
        buf.writeInt(this.z);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.processChunkUnload(this);
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getZ() {
        return this.z;
    }
}
