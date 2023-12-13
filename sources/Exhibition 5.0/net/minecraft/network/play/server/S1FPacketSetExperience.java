// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class S1FPacketSetExperience implements Packet
{
    private float field_149401_a;
    private int field_149399_b;
    private int field_149400_c;
    private static final String __OBFID = "CL_00001331";
    
    public S1FPacketSetExperience() {
    }
    
    public S1FPacketSetExperience(final float p_i45222_1_, final int p_i45222_2_, final int p_i45222_3_) {
        this.field_149401_a = p_i45222_1_;
        this.field_149399_b = p_i45222_2_;
        this.field_149400_c = p_i45222_3_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149401_a = data.readFloat();
        this.field_149400_c = data.readVarIntFromBuffer();
        this.field_149399_b = data.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeFloat(this.field_149401_a);
        data.writeVarIntToBuffer(this.field_149400_c);
        data.writeVarIntToBuffer(this.field_149399_b);
    }
    
    public void func_180749_a(final INetHandlerPlayClient p_180749_1_) {
        p_180749_1_.handleSetExperience(this);
    }
    
    public float func_149397_c() {
        return this.field_149401_a;
    }
    
    public int func_149396_d() {
        return this.field_149399_b;
    }
    
    public int func_149395_e() {
        return this.field_149400_c;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180749_a((INetHandlerPlayClient)handler);
    }
}
