// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.handshake.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;

public class C00Handshake implements Packet
{
    private int protocolVersion;
    private String ip;
    private int port;
    private EnumConnectionState requestedState;
    private static final String __OBFID = "CL_00001372";
    
    public C00Handshake() {
    }
    
    public C00Handshake(final int p_i45266_1_, final String p_i45266_2_, final int p_i45266_3_, final EnumConnectionState p_i45266_4_) {
        this.protocolVersion = p_i45266_1_;
        this.ip = p_i45266_2_;
        this.port = p_i45266_3_;
        this.requestedState = p_i45266_4_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.protocolVersion = data.readVarIntFromBuffer();
        this.ip = data.readStringFromBuffer(255);
        this.port = data.readUnsignedShort();
        this.requestedState = EnumConnectionState.getById(data.readVarIntFromBuffer());
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.protocolVersion);
        data.writeString(this.ip);
        data.writeShort(this.port);
        data.writeVarIntToBuffer(this.requestedState.getId());
    }
    
    public void func_180770_a(final INetHandlerHandshakeServer p_180770_1_) {
        p_180770_1_.processHandshake(this);
    }
    
    public EnumConnectionState getRequestedState() {
        return this.requestedState;
    }
    
    public int getProtocolVersion() {
        return this.protocolVersion;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180770_a((INetHandlerHandshakeServer)handler);
    }
}
