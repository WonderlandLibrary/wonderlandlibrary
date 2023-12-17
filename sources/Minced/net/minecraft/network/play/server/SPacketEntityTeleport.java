// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketEntityTeleport implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private double posX;
    private double posY;
    private double posZ;
    private byte yaw;
    private byte pitch;
    private boolean onGround;
    
    public SPacketEntityTeleport() {
    }
    
    public SPacketEntityTeleport(final Entity entityIn) {
        this.entityId = entityIn.getEntityId();
        this.posX = entityIn.posX;
        this.posY = entityIn.posY;
        this.posZ = entityIn.posZ;
        this.yaw = (byte)(entityIn.rotationYaw * 256.0f / 360.0f);
        this.pitch = (byte)(entityIn.rotationPitch * 256.0f / 360.0f);
        this.onGround = entityIn.onGround;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarInt();
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
        this.yaw = buf.readByte();
        this.pitch = buf.readByte();
        this.onGround = buf.readBoolean();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.entityId);
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);
        buf.writeByte(this.yaw);
        buf.writeByte(this.pitch);
        buf.writeBoolean(this.onGround);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityTeleport(this);
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public double getX() {
        return this.posX;
    }
    
    public double getY() {
        return this.posY;
    }
    
    public double getZ() {
        return this.posZ;
    }
    
    public byte getYaw() {
        return this.yaw;
    }
    
    public byte getPitch() {
        return this.pitch;
    }
    
    public boolean getOnGround() {
        return this.onGround;
    }
}
