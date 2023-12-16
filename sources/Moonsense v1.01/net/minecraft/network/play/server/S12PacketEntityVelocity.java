// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;

public class S12PacketEntityVelocity implements Packet
{
    private int field_149417_a;
    private int field_149415_b;
    private int field_149416_c;
    private int field_149414_d;
    private static final String __OBFID = "CL_00001328";
    
    public S12PacketEntityVelocity() {
    }
    
    public S12PacketEntityVelocity(final Entity p_i45219_1_) {
        this(p_i45219_1_.getEntityId(), p_i45219_1_.motionX, p_i45219_1_.motionY, p_i45219_1_.motionZ);
    }
    
    public S12PacketEntityVelocity(final int p_i45220_1_, double p_i45220_2_, double p_i45220_4_, double p_i45220_6_) {
        this.field_149417_a = p_i45220_1_;
        final double var8 = 3.9;
        if (p_i45220_2_ < -var8) {
            p_i45220_2_ = -var8;
        }
        if (p_i45220_4_ < -var8) {
            p_i45220_4_ = -var8;
        }
        if (p_i45220_6_ < -var8) {
            p_i45220_6_ = -var8;
        }
        if (p_i45220_2_ > var8) {
            p_i45220_2_ = var8;
        }
        if (p_i45220_4_ > var8) {
            p_i45220_4_ = var8;
        }
        if (p_i45220_6_ > var8) {
            p_i45220_6_ = var8;
        }
        this.field_149415_b = (int)(p_i45220_2_ * 8000.0);
        this.field_149416_c = (int)(p_i45220_4_ * 8000.0);
        this.field_149414_d = (int)(p_i45220_6_ * 8000.0);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149417_a = data.readVarIntFromBuffer();
        this.field_149415_b = data.readShort();
        this.field_149416_c = data.readShort();
        this.field_149414_d = data.readShort();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149417_a);
        data.writeShort(this.field_149415_b);
        data.writeShort(this.field_149416_c);
        data.writeShort(this.field_149414_d);
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityVelocity(this);
    }
    
    public int func_149412_c() {
        return this.field_149417_a;
    }
    
    public int func_149411_d() {
        return this.field_149415_b;
    }
    
    public int func_149410_e() {
        return this.field_149416_c;
    }
    
    public int func_149409_f() {
        return this.field_149414_d;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
