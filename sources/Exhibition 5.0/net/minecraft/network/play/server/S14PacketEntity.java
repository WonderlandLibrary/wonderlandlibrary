// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class S14PacketEntity implements Packet
{
    protected int field_149074_a;
    protected byte field_149072_b;
    protected byte field_149073_c;
    protected byte field_149070_d;
    protected byte field_149071_e;
    protected byte field_149068_f;
    protected boolean field_179743_g;
    protected boolean field_149069_g;
    private static final String __OBFID = "CL_00001312";
    
    public S14PacketEntity() {
    }
    
    public S14PacketEntity(final int p_i45206_1_) {
        this.field_149074_a = p_i45206_1_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149074_a = data.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149074_a);
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityMovement(this);
    }
    
    @Override
    public String toString() {
        return "Entity_" + super.toString();
    }
    
    public Entity func_149065_a(final World worldIn) {
        return worldIn.getEntityByID(this.field_149074_a);
    }
    
    public byte func_149062_c() {
        return this.field_149072_b;
    }
    
    public byte func_149061_d() {
        return this.field_149073_c;
    }
    
    public byte func_149064_e() {
        return this.field_149070_d;
    }
    
    public byte func_149066_f() {
        return this.field_149071_e;
    }
    
    public byte func_149063_g() {
        return this.field_149068_f;
    }
    
    public boolean func_149060_h() {
        return this.field_149069_g;
    }
    
    public boolean func_179742_g() {
        return this.field_179743_g;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
    
    public static class S15PacketEntityRelMove extends S14PacketEntity
    {
        private static final String __OBFID = "CL_00001313";
        
        public S15PacketEntityRelMove() {
        }
        
        public S15PacketEntityRelMove(final int p_i45974_1_, final byte p_i45974_2_, final byte p_i45974_3_, final byte p_i45974_4_, final boolean p_i45974_5_) {
            super(p_i45974_1_);
            this.field_149072_b = p_i45974_2_;
            this.field_149073_c = p_i45974_3_;
            this.field_149070_d = p_i45974_4_;
            this.field_179743_g = p_i45974_5_;
        }
        
        @Override
        public void readPacketData(final PacketBuffer data) throws IOException {
            super.readPacketData(data);
            this.field_149072_b = data.readByte();
            this.field_149073_c = data.readByte();
            this.field_149070_d = data.readByte();
            this.field_179743_g = data.readBoolean();
        }
        
        @Override
        public void writePacketData(final PacketBuffer data) throws IOException {
            super.writePacketData(data);
            data.writeByte(this.field_149072_b);
            data.writeByte(this.field_149073_c);
            data.writeByte(this.field_149070_d);
            data.writeBoolean(this.field_179743_g);
        }
        
        @Override
        public void processPacket(final INetHandler handler) {
            super.processPacket((INetHandlerPlayClient)handler);
        }
    }
    
    public static class S16PacketEntityLook extends S14PacketEntity
    {
        private static final String __OBFID = "CL_00001315";
        
        public S16PacketEntityLook() {
            this.field_149069_g = true;
        }
        
        public S16PacketEntityLook(final int p_i45972_1_, final byte p_i45972_2_, final byte p_i45972_3_, final boolean p_i45972_4_) {
            super(p_i45972_1_);
            this.field_149071_e = p_i45972_2_;
            this.field_149068_f = p_i45972_3_;
            this.field_149069_g = true;
            this.field_179743_g = p_i45972_4_;
        }
        
        @Override
        public void readPacketData(final PacketBuffer data) throws IOException {
            super.readPacketData(data);
            this.field_149071_e = data.readByte();
            this.field_149068_f = data.readByte();
            this.field_179743_g = data.readBoolean();
        }
        
        @Override
        public void writePacketData(final PacketBuffer data) throws IOException {
            super.writePacketData(data);
            data.writeByte(this.field_149071_e);
            data.writeByte(this.field_149068_f);
            data.writeBoolean(this.field_179743_g);
        }
        
        @Override
        public void processPacket(final INetHandler handler) {
            super.processPacket((INetHandlerPlayClient)handler);
        }
    }
    
    public static class S17PacketEntityLookMove extends S14PacketEntity
    {
        private static final String __OBFID = "CL_00001314";
        
        public S17PacketEntityLookMove() {
            this.field_149069_g = true;
        }
        
        public S17PacketEntityLookMove(final int p_i45973_1_, final byte p_i45973_2_, final byte p_i45973_3_, final byte p_i45973_4_, final byte p_i45973_5_, final byte p_i45973_6_, final boolean p_i45973_7_) {
            super(p_i45973_1_);
            this.field_149072_b = p_i45973_2_;
            this.field_149073_c = p_i45973_3_;
            this.field_149070_d = p_i45973_4_;
            this.field_149071_e = p_i45973_5_;
            this.field_149068_f = p_i45973_6_;
            this.field_179743_g = p_i45973_7_;
            this.field_149069_g = true;
        }
        
        @Override
        public void readPacketData(final PacketBuffer data) throws IOException {
            super.readPacketData(data);
            this.field_149072_b = data.readByte();
            this.field_149073_c = data.readByte();
            this.field_149070_d = data.readByte();
            this.field_149071_e = data.readByte();
            this.field_149068_f = data.readByte();
            this.field_179743_g = data.readBoolean();
        }
        
        @Override
        public void writePacketData(final PacketBuffer data) throws IOException {
            super.writePacketData(data);
            data.writeByte(this.field_149072_b);
            data.writeByte(this.field_149073_c);
            data.writeByte(this.field_149070_d);
            data.writeByte(this.field_149071_e);
            data.writeByte(this.field_149068_f);
            data.writeBoolean(this.field_179743_g);
        }
        
        @Override
        public void processPacket(final INetHandler handler) {
            super.processPacket((INetHandlerPlayClient)handler);
        }
    }
}
