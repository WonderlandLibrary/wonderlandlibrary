// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class S09PacketHeldItemChange implements Packet
{
    private int field_149387_a;
    private static final String __OBFID = "CL_00001324";
    
    public S09PacketHeldItemChange() {
    }
    
    public S09PacketHeldItemChange(final int p_i45215_1_) {
        this.field_149387_a = p_i45215_1_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149387_a = data.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeByte(this.field_149387_a);
    }
    
    public void func_180746_a(final INetHandlerPlayClient p_180746_1_) {
        p_180746_1_.handleHeldItemChange(this);
    }
    
    public int func_149385_c() {
        return this.field_149387_a;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180746_a((INetHandlerPlayClient)handler);
    }
}
