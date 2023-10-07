// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;

public class S18PacketEntityTeleport implements Packet
{
    private int field_149458_a;
    private int field_149456_b;
    private int field_149457_c;
    private int field_149454_d;
    private byte field_149455_e;
    private byte field_149453_f;
    private boolean field_179698_g;
    private static final String __OBFID = "CL_00001340";
    
    public S18PacketEntityTeleport() {
    }
    
    public S18PacketEntityTeleport(final Entity p_i45233_1_) {
        this.field_149458_a = p_i45233_1_.getEntityId();
        this.field_149456_b = MathHelper.floor_double(p_i45233_1_.posX * 32.0);
        this.field_149457_c = MathHelper.floor_double(p_i45233_1_.posY * 32.0);
        this.field_149454_d = MathHelper.floor_double(p_i45233_1_.posZ * 32.0);
        this.field_149455_e = (byte)(p_i45233_1_.rotationYaw * 256.0f / 360.0f);
        this.field_149453_f = (byte)(p_i45233_1_.rotationPitch * 256.0f / 360.0f);
        this.field_179698_g = p_i45233_1_.onGround;
    }
    
    public S18PacketEntityTeleport(final int p_i45949_1_, final int p_i45949_2_, final int p_i45949_3_, final int p_i45949_4_, final byte p_i45949_5_, final byte p_i45949_6_, final boolean p_i45949_7_) {
        this.field_149458_a = p_i45949_1_;
        this.field_149456_b = p_i45949_2_;
        this.field_149457_c = p_i45949_3_;
        this.field_149454_d = p_i45949_4_;
        this.field_149455_e = p_i45949_5_;
        this.field_149453_f = p_i45949_6_;
        this.field_179698_g = p_i45949_7_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149458_a = data.readVarIntFromBuffer();
        this.field_149456_b = data.readInt();
        this.field_149457_c = data.readInt();
        this.field_149454_d = data.readInt();
        this.field_149455_e = data.readByte();
        this.field_149453_f = data.readByte();
        this.field_179698_g = data.readBoolean();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149458_a);
        data.writeInt(this.field_149456_b);
        data.writeInt(this.field_149457_c);
        data.writeInt(this.field_149454_d);
        data.writeByte(this.field_149455_e);
        data.writeByte(this.field_149453_f);
        data.writeBoolean(this.field_179698_g);
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityTeleport(this);
    }
    
    public int func_149451_c() {
        return this.field_149458_a;
    }
    
    public int func_149449_d() {
        return this.field_149456_b;
    }
    
    public int func_149448_e() {
        return this.field_149457_c;
    }
    
    public int func_149446_f() {
        return this.field_149454_d;
    }
    
    public byte func_149450_g() {
        return this.field_149455_e;
    }
    
    public byte func_149447_h() {
        return this.field_149453_f;
    }
    
    public boolean func_179697_g() {
        return this.field_179698_g;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
