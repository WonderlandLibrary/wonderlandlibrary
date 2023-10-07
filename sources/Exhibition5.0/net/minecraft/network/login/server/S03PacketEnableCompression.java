// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.login.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.login.INetHandlerLoginClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class S03PacketEnableCompression implements Packet
{
    private int field_179733_a;
    private static final String __OBFID = "CL_00002279";
    
    public S03PacketEnableCompression() {
    }
    
    public S03PacketEnableCompression(final int p_i45929_1_) {
        this.field_179733_a = p_i45929_1_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_179733_a = data.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_179733_a);
    }
    
    public void func_179732_a(final INetHandlerLoginClient p_179732_1_) {
        p_179732_1_.func_180464_a(this);
    }
    
    public int func_179731_a() {
        return this.field_179733_a;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_179732_a((INetHandlerLoginClient)handler);
    }
}
