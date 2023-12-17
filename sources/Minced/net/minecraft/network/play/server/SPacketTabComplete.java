// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketTabComplete implements Packet<INetHandlerPlayClient>
{
    private String[] matches;
    
    public SPacketTabComplete() {
    }
    
    public SPacketTabComplete(final String[] matchesIn) {
        this.matches = matchesIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.matches = new String[buf.readVarInt()];
        for (int i = 0; i < this.matches.length; ++i) {
            this.matches[i] = buf.readString(32767);
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.matches.length);
        for (final String s : this.matches) {
            buf.writeString(s);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleTabComplete(this);
    }
    
    public String[] getMatches() {
        return this.matches;
    }
}
