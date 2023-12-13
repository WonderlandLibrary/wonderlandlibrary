// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.world.World;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;

public class S19PacketEntityHeadLook implements Packet
{
    private int field_149384_a;
    private byte field_149383_b;
    private static final String __OBFID = "CL_00001323";
    
    public S19PacketEntityHeadLook() {
    }
    
    public S19PacketEntityHeadLook(final Entity p_i45214_1_, final byte p_i45214_2_) {
        this.field_149384_a = p_i45214_1_.getEntityId();
        this.field_149383_b = p_i45214_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149384_a = data.readVarIntFromBuffer();
        this.field_149383_b = data.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149384_a);
        data.writeByte(this.field_149383_b);
    }
    
    public void func_180745_a(final INetHandlerPlayClient p_180745_1_) {
        p_180745_1_.handleEntityHeadLook(this);
    }
    
    public Entity func_149381_a(final World worldIn) {
        return worldIn.getEntityByID(this.field_149384_a);
    }
    
    public byte func_149380_c() {
        return this.field_149383_b;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180745_a((INetHandlerPlayClient)handler);
    }
}
