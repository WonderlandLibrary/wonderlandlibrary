// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketTimeUpdate implements Packet<INetHandlerPlayClient>
{
    private long totalWorldTime;
    private long worldTime;
    
    public SPacketTimeUpdate() {
    }
    
    public SPacketTimeUpdate(final long totalWorldTimeIn, final long worldTimeIn, final boolean doDaylightCycle) {
        this.totalWorldTime = totalWorldTimeIn;
        this.worldTime = worldTimeIn;
        if (!doDaylightCycle) {
            this.worldTime = -this.worldTime;
            if (this.worldTime == 0L) {
                this.worldTime = -1L;
            }
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.totalWorldTime = buf.readLong();
        this.worldTime = buf.readLong();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeLong(this.totalWorldTime);
        buf.writeLong(this.worldTime);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleTimeUpdate(this);
    }
    
    public long getTotalWorldTime() {
        return this.totalWorldTime;
    }
    
    public long getWorldTime() {
        return this.worldTime;
    }
}
