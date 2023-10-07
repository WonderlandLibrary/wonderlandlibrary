// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;

public class S1BPacketEntityAttach implements Packet
{
    private int field_149408_a;
    private int field_149406_b;
    private int field_149407_c;
    private static final String __OBFID = "CL_00001327";
    
    public S1BPacketEntityAttach() {
    }
    
    public S1BPacketEntityAttach(final int p_i45218_1_, final Entity p_i45218_2_, final Entity p_i45218_3_) {
        this.field_149408_a = p_i45218_1_;
        this.field_149406_b = p_i45218_2_.getEntityId();
        this.field_149407_c = ((p_i45218_3_ != null) ? p_i45218_3_.getEntityId() : -1);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149406_b = data.readInt();
        this.field_149407_c = data.readInt();
        this.field_149408_a = data.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeInt(this.field_149406_b);
        data.writeInt(this.field_149407_c);
        data.writeByte(this.field_149408_a);
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityAttach(this);
    }
    
    public int func_149404_c() {
        return this.field_149408_a;
    }
    
    public int func_149403_d() {
        return this.field_149406_b;
    }
    
    public int func_149402_e() {
        return this.field_149407_c;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
