// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketPlayer implements Packet<INetHandlerPlayServer>
{
    protected double x;
    protected double y;
    protected double z;
    protected float yaw;
    protected float pitch;
    protected boolean onGround;
    protected boolean moving;
    protected boolean rotating;
    
    public CPacketPlayer() {
    }
    
    public CPacketPlayer(final boolean onGroundIn) {
        this.onGround = onGroundIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processPlayer(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.onGround = (buf.readUnsignedByte() != 0);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.onGround ? 1 : 0);
    }
    
    public double getX(final double defaultValue) {
        return this.moving ? this.x : defaultValue;
    }
    
    public double getY(final double defaultValue) {
        return this.moving ? this.y : defaultValue;
    }
    
    public double getZ(final double defaultValue) {
        return this.moving ? this.z : defaultValue;
    }
    
    public float getYaw(final float defaultValue) {
        return this.rotating ? this.yaw : defaultValue;
    }
    
    public float getPitch(final float defaultValue) {
        return this.rotating ? this.pitch : defaultValue;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public static class Position extends CPacketPlayer
    {
        public Position() {
            this.moving = true;
        }
        
        public Position(final double xIn, final double yIn, final double zIn, final boolean onGroundIn) {
            this.x = xIn;
            this.y = yIn;
            this.z = zIn;
            this.onGround = onGroundIn;
            this.moving = true;
        }
        
        @Override
        public void readPacketData(final PacketBuffer buf) throws IOException {
            this.x = buf.readDouble();
            this.y = buf.readDouble();
            this.z = buf.readDouble();
            super.readPacketData(buf);
        }
        
        @Override
        public void writePacketData(final PacketBuffer buf) throws IOException {
            buf.writeDouble(this.x);
            buf.writeDouble(this.y);
            buf.writeDouble(this.z);
            super.writePacketData(buf);
        }
    }
    
    public static class PositionRotation extends CPacketPlayer
    {
        public PositionRotation() {
            this.moving = true;
            this.rotating = true;
        }
        
        public PositionRotation(final double xIn, final double yIn, final double zIn, final float yawIn, final float pitchIn, final boolean onGroundIn) {
            this.x = xIn;
            this.y = yIn;
            this.z = zIn;
            this.yaw = yawIn;
            this.pitch = pitchIn;
            this.onGround = onGroundIn;
            this.rotating = true;
            this.moving = true;
        }
        
        @Override
        public void readPacketData(final PacketBuffer buf) throws IOException {
            this.x = buf.readDouble();
            this.y = buf.readDouble();
            this.z = buf.readDouble();
            this.yaw = buf.readFloat();
            this.pitch = buf.readFloat();
            super.readPacketData(buf);
        }
        
        @Override
        public void writePacketData(final PacketBuffer buf) throws IOException {
            buf.writeDouble(this.x);
            buf.writeDouble(this.y);
            buf.writeDouble(this.z);
            buf.writeFloat(this.yaw);
            buf.writeFloat(this.pitch);
            super.writePacketData(buf);
        }
    }
    
    public static class Rotation extends CPacketPlayer
    {
        public Rotation() {
            this.rotating = true;
        }
        
        public Rotation(final float yawIn, final float pitchIn, final boolean onGroundIn) {
            this.yaw = yawIn;
            this.pitch = pitchIn;
            this.onGround = onGroundIn;
            this.rotating = true;
        }
        
        @Override
        public void readPacketData(final PacketBuffer buf) throws IOException {
            this.yaw = buf.readFloat();
            this.pitch = buf.readFloat();
            super.readPacketData(buf);
        }
        
        @Override
        public void writePacketData(final PacketBuffer buf) throws IOException {
            buf.writeFloat(this.yaw);
            buf.writeFloat(this.pitch);
            super.writePacketData(buf);
        }
    }
}
