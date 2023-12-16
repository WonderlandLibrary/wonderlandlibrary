// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.Validate;
import net.minecraft.network.Packet;

public class S29PacketSoundEffect implements Packet
{
    private String field_149219_a;
    private int field_149217_b;
    private int field_149218_c;
    private int field_149215_d;
    private float field_149216_e;
    private int field_149214_f;
    private static final String __OBFID = "CL_00001309";
    
    public S29PacketSoundEffect() {
        this.field_149218_c = Integer.MAX_VALUE;
    }
    
    public S29PacketSoundEffect(final String p_i45200_1_, final double p_i45200_2_, final double p_i45200_4_, final double p_i45200_6_, final float p_i45200_8_, float p_i45200_9_) {
        this.field_149218_c = Integer.MAX_VALUE;
        Validate.notNull((Object)p_i45200_1_, "name", new Object[0]);
        this.field_149219_a = p_i45200_1_;
        this.field_149217_b = (int)(p_i45200_2_ * 8.0);
        this.field_149218_c = (int)(p_i45200_4_ * 8.0);
        this.field_149215_d = (int)(p_i45200_6_ * 8.0);
        this.field_149216_e = p_i45200_8_;
        this.field_149214_f = (int)(p_i45200_9_ * 63.0f);
        p_i45200_9_ = MathHelper.clamp_float(p_i45200_9_, 0.0f, 255.0f);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149219_a = data.readStringFromBuffer(256);
        this.field_149217_b = data.readInt();
        this.field_149218_c = data.readInt();
        this.field_149215_d = data.readInt();
        this.field_149216_e = data.readFloat();
        this.field_149214_f = data.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeString(this.field_149219_a);
        data.writeInt(this.field_149217_b);
        data.writeInt(this.field_149218_c);
        data.writeInt(this.field_149215_d);
        data.writeFloat(this.field_149216_e);
        data.writeByte(this.field_149214_f);
    }
    
    public String func_149212_c() {
        return this.field_149219_a;
    }
    
    public double func_149207_d() {
        return this.field_149217_b / 8.0f;
    }
    
    public double func_149211_e() {
        return this.field_149218_c / 8.0f;
    }
    
    public double func_149210_f() {
        return this.field_149215_d / 8.0f;
    }
    
    public float func_149208_g() {
        return this.field_149216_e;
    }
    
    public float func_149209_h() {
        return this.field_149214_f / 63.0f;
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSoundEffect(this);
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
