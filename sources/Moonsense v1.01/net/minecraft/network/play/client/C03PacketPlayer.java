// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C03PacketPlayer implements Packet
{
    protected double x;
    protected double y;
    protected double z;
    protected float yaw;
    protected float pitch;
    protected boolean field_149474_g;
    protected boolean field_149480_h;
    protected boolean rotating;
    private static final String __OBFID = "CL_00001360";
    
    public C03PacketPlayer() {
    }
    
    public C03PacketPlayer(final boolean p_i45256_1_) {
        this.field_149474_g = p_i45256_1_;
    }
    
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processPlayer(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149474_g = (data.readUnsignedByte() != 0);
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeByte(this.field_149474_g ? 1 : 0);
    }
    
    public double getPositionX() {
        return this.x;
    }
    
    public double getPositionY() {
        return this.y;
    }
    
    public double getPositionZ() {
        return this.z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public boolean func_149465_i() {
        return this.field_149474_g;
    }
    
    public boolean func_149466_j() {
        return this.field_149480_h;
    }
    
    public boolean getRotating() {
        return this.rotating;
    }
    
    public void func_149469_a(final boolean p_149469_1_) {
        this.field_149480_h = p_149469_1_;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayServer)handler);
    }
    
    public static class C04PacketPlayerPosition extends C03PacketPlayer
    {
        private static final String __OBFID = "CL_00001361";
        
        public C04PacketPlayerPosition() {
            this.field_149480_h = true;
        }
        
        public C04PacketPlayerPosition(final double p_i45942_1_, final double p_i45942_3_, final double p_i45942_5_, final boolean p_i45942_7_) {
            this.x = p_i45942_1_;
            this.y = p_i45942_3_;
            this.z = p_i45942_5_;
            this.field_149474_g = p_i45942_7_;
            this.field_149480_h = true;
        }
        
        @Override
        public void readPacketData(final PacketBuffer data) throws IOException {
            this.x = data.readDouble();
            this.y = data.readDouble();
            this.z = data.readDouble();
            super.readPacketData(data);
        }
        
        @Override
        public void writePacketData(final PacketBuffer data) throws IOException {
            data.writeDouble(this.x);
            data.writeDouble(this.y);
            data.writeDouble(this.z);
            super.writePacketData(data);
        }
        
        @Override
        public void processPacket(final INetHandler handler) {
            super.processPacket((INetHandlerPlayServer)handler);
        }
    }
    
    public static class C05PacketPlayerLook extends C03PacketPlayer
    {
        private static final String __OBFID = "CL_00001363";
        
        public C05PacketPlayerLook() {
            this.rotating = true;
        }
        
        public C05PacketPlayerLook(final float p_i45255_1_, final float p_i45255_2_, final boolean p_i45255_3_) {
            this.yaw = p_i45255_1_;
            this.pitch = p_i45255_2_;
            this.field_149474_g = p_i45255_3_;
            this.rotating = true;
        }
        
        @Override
        public void readPacketData(final PacketBuffer data) throws IOException {
            this.yaw = data.readFloat();
            this.pitch = data.readFloat();
            super.readPacketData(data);
        }
        
        @Override
        public void writePacketData(final PacketBuffer data) throws IOException {
            data.writeFloat(this.yaw);
            data.writeFloat(this.pitch);
            super.writePacketData(data);
        }
        
        @Override
        public void processPacket(final INetHandler handler) {
            super.processPacket((INetHandlerPlayServer)handler);
        }
    }
    
    public static class C06PacketPlayerPosLook extends C03PacketPlayer
    {
        private static final String __OBFID = "CL_00001362";
        
        public C06PacketPlayerPosLook() {
            this.field_149480_h = true;
            this.rotating = true;
        }
        
        public C06PacketPlayerPosLook(final double p_i45941_1_, final double p_i45941_3_, final double p_i45941_5_, final float p_i45941_7_, final float p_i45941_8_, final boolean p_i45941_9_) {
            this.x = p_i45941_1_;
            this.y = p_i45941_3_;
            this.z = p_i45941_5_;
            this.yaw = p_i45941_7_;
            this.pitch = p_i45941_8_;
            this.field_149474_g = p_i45941_9_;
            this.rotating = true;
            this.field_149480_h = true;
        }
        
        @Override
        public void readPacketData(final PacketBuffer data) throws IOException {
            this.x = data.readDouble();
            this.y = data.readDouble();
            this.z = data.readDouble();
            this.yaw = data.readFloat();
            this.pitch = data.readFloat();
            super.readPacketData(data);
        }
        
        @Override
        public void writePacketData(final PacketBuffer data) throws IOException {
            data.writeDouble(this.x);
            data.writeDouble(this.y);
            data.writeDouble(this.z);
            data.writeFloat(this.yaw);
            data.writeFloat(this.pitch);
            super.writePacketData(data);
        }
        
        @Override
        public void processPacket(final INetHandler handler) {
            super.processPacket((INetHandlerPlayServer)handler);
        }
    }
}
