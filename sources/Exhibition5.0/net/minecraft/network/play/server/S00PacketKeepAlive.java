// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S00PacketKeepAlive implements Packet
{
    private int field_149136_a;
    private static final String __OBFID = "CL_00001303";
    
    public S00PacketKeepAlive() {
    }
    
    public S00PacketKeepAlive(final int p_i45195_1_) {
        this.field_149136_a = p_i45195_1_;
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleKeepAlive(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149136_a = data.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149136_a);
    }
    
    public int func_149134_c() {
        return this.field_149136_a;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
