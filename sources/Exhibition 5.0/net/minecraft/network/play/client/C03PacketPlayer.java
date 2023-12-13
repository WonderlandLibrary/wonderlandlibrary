// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C03PacketPlayer implements Packet
{
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean onGround;
    protected boolean moving;
    protected boolean rotating;
    private static final String __OBFID = "CL_00001360";
    
    public C03PacketPlayer() {
    }
    
    public C03PacketPlayer(final boolean p_i45256_1_) {
        this.onGround = p_i45256_1_;
    }
    
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processPlayer(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.onGround = (data.readUnsignedByte() != 0);
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeByte(this.onGround ? 1 : 0);
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
    
    public void setPositionY(final double y) {
        this.y = y;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public boolean isMoving() {
        return this.moving;
    }
    
    public boolean getRotating() {
        return this.rotating;
    }
    
    public void setMoving(final boolean moving) {
        this.moving = moving;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayServer)handler);
    }
    
    public static class C04PacketPlayerPosition extends C03PacketPlayer
    {
        private static final String __OBFID = "CL_00001361";
        
        public C04PacketPlayerPosition() {
            this.moving = true;
        }
        
        public C04PacketPlayerPosition(final double x, final double y, final double z, final boolean g) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.onGround = g;
            this.moving = true;
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
            this.onGround = p_i45255_3_;
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
            this.moving = true;
            this.rotating = true;
        }
        
        public C06PacketPlayerPosLook(final double p_i45941_1_, final double p_i45941_3_, final double p_i45941_5_, final float p_i45941_7_, final float p_i45941_8_, final boolean p_i45941_9_) {
            this.x = p_i45941_1_;
            this.y = p_i45941_3_;
            this.z = p_i45941_5_;
            this.yaw = p_i45941_7_;
            this.pitch = p_i45941_8_;
            this.onGround = p_i45941_9_;
            this.rotating = true;
            this.moving = true;
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
