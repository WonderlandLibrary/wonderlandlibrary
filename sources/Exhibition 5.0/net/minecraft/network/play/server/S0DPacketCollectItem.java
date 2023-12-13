// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class S0DPacketCollectItem implements Packet
{
    private int field_149357_a;
    private int field_149356_b;
    private static final String __OBFID = "CL_00001339";
    
    public S0DPacketCollectItem() {
    }
    
    public S0DPacketCollectItem(final int p_i45232_1_, final int p_i45232_2_) {
        this.field_149357_a = p_i45232_1_;
        this.field_149356_b = p_i45232_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149357_a = data.readVarIntFromBuffer();
        this.field_149356_b = data.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149357_a);
        data.writeVarIntToBuffer(this.field_149356_b);
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleCollectItem(this);
    }
    
    public int func_149354_c() {
        return this.field_149357_a;
    }
    
    public int func_149353_d() {
        return this.field_149356_b;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
