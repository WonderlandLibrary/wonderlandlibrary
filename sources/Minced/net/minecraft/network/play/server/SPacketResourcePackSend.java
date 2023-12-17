// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketResourcePackSend implements Packet<INetHandlerPlayClient>
{
    private String url;
    private String hash;
    
    public SPacketResourcePackSend() {
    }
    
    public SPacketResourcePackSend(final String urlIn, final String hashIn) {
        this.url = urlIn;
        this.hash = hashIn;
        if (hashIn.length() > 40) {
            throw new IllegalArgumentException("Hash is too long (max 40, was " + hashIn.length() + ")");
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.url = buf.readString(32767);
        this.hash = buf.readString(40);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(this.url);
        buf.writeString(this.hash);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleResourcePack(this);
    }
    
    public String getURL() {
        return this.url;
    }
    
    public String getHash() {
        return this.hash;
    }
}
