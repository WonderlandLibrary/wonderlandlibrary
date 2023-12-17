// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketEntity implements Packet<INetHandlerPlayClient>
{
    protected int entityId;
    protected int posX;
    protected int posY;
    protected int posZ;
    protected byte yaw;
    protected byte pitch;
    protected boolean onGround;
    protected boolean rotating;
    
    public SPacketEntity() {
    }
    
    public SPacketEntity(final int entityIdIn) {
        this.entityId = entityIdIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarInt();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.entityId);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityMovement(this);
    }
    
    @Override
    public String toString() {
        return "Entity_" + super.toString();
    }
    
    public Entity getEntity(final World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }
    
    public int getX() {
        return this.posX;
    }
    
    public int getY() {
        return this.posY;
    }
    
    public int getZ() {
        return this.posZ;
    }
    
    public byte getYaw() {
        return this.yaw;
    }
    
    public byte getPitch() {
        return this.pitch;
    }
    
    public boolean isRotating() {
        return this.rotating;
    }
    
    public boolean getOnGround() {
        return this.onGround;
    }
    
    public static class S15PacketEntityRelMove extends SPacketEntity
    {
        public S15PacketEntityRelMove() {
        }
        
        public S15PacketEntityRelMove(final int entityIdIn, final long xIn, final long yIn, final long zIn, final boolean onGroundIn) {
            super(entityIdIn);
            this.posX = (int)xIn;
            this.posY = (int)yIn;
            this.posZ = (int)zIn;
            this.onGround = onGroundIn;
        }
        
        @Override
        public void readPacketData(final PacketBuffer buf) throws IOException {
            super.readPacketData(buf);
            this.posX = buf.readShort();
            this.posY = buf.readShort();
            this.posZ = buf.readShort();
            this.onGround = buf.readBoolean();
        }
        
        @Override
        public void writePacketData(final PacketBuffer buf) throws IOException {
            super.writePacketData(buf);
            buf.writeShort(this.posX);
            buf.writeShort(this.posY);
            buf.writeShort(this.posZ);
            buf.writeBoolean(this.onGround);
        }
    }
    
    public static class S16PacketEntityLook extends SPacketEntity
    {
        public S16PacketEntityLook() {
            this.rotating = true;
        }
        
        public S16PacketEntityLook(final int entityIdIn, final byte yawIn, final byte pitchIn, final boolean onGroundIn) {
            super(entityIdIn);
            this.yaw = yawIn;
            this.pitch = pitchIn;
            this.rotating = true;
            this.onGround = onGroundIn;
        }
        
        @Override
        public void readPacketData(final PacketBuffer buf) throws IOException {
            super.readPacketData(buf);
            this.yaw = buf.readByte();
            this.pitch = buf.readByte();
            this.onGround = buf.readBoolean();
        }
        
        @Override
        public void writePacketData(final PacketBuffer buf) throws IOException {
            super.writePacketData(buf);
            buf.writeByte(this.yaw);
            buf.writeByte(this.pitch);
            buf.writeBoolean(this.onGround);
        }
    }
    
    public static class S17PacketEntityLookMove extends SPacketEntity
    {
        public S17PacketEntityLookMove() {
            this.rotating = true;
        }
        
        public S17PacketEntityLookMove(final int entityIdIn, final long xIn, final long yIn, final long zIn, final byte yawIn, final byte pitchIn, final boolean onGroundIn) {
            super(entityIdIn);
            this.posX = (int)xIn;
            this.posY = (int)yIn;
            this.posZ = (int)zIn;
            this.yaw = yawIn;
            this.pitch = pitchIn;
            this.onGround = onGroundIn;
            this.rotating = true;
        }
        
        @Override
        public void readPacketData(final PacketBuffer buf) throws IOException {
            super.readPacketData(buf);
            this.posX = buf.readShort();
            this.posY = buf.readShort();
            this.posZ = buf.readShort();
            this.yaw = buf.readByte();
            this.pitch = buf.readByte();
            this.onGround = buf.readBoolean();
        }
        
        @Override
        public void writePacketData(final PacketBuffer buf) throws IOException {
            super.writePacketData(buf);
            buf.writeShort(this.posX);
            buf.writeShort(this.posY);
            buf.writeShort(this.posZ);
            buf.writeByte(this.yaw);
            buf.writeByte(this.pitch);
            buf.writeBoolean(this.onGround);
        }
    }
}
