// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.handshake.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.Packet;

public class C00Handshake implements Packet<INetHandlerHandshakeServer>
{
    private int protocolVersion;
    private String ip;
    private int port;
    private EnumConnectionState requestedState;
    
    public C00Handshake() {
    }
    
    public C00Handshake(final String p_i47613_1_, final int p_i47613_2_, final EnumConnectionState p_i47613_3_) {
        this.protocolVersion = 340;
        this.ip = p_i47613_1_;
        this.port = p_i47613_2_;
        this.requestedState = p_i47613_3_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.protocolVersion = buf.readVarInt();
        this.ip = buf.readString(255);
        this.port = buf.readUnsignedShort();
        this.requestedState = EnumConnectionState.getById(buf.readVarInt());
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.protocolVersion);
        buf.writeString(this.ip);
        buf.writeShort(this.port);
        buf.writeVarInt(this.requestedState.getId());
    }
    
    @Override
    public void processPacket(final INetHandlerHandshakeServer handler) {
        handler.processHandshake(this);
    }
    
    public EnumConnectionState getRequestedState() {
        return this.requestedState;
    }
    
    public int getProtocolVersion() {
        return this.protocolVersion;
    }
}
