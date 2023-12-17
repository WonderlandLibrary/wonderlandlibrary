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

public class SPacketEntityVelocity implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private int motionX;
    private int motionY;
    private int motionZ;
    
    public SPacketEntityVelocity() {
    }
    
    public SPacketEntityVelocity(final Entity entityIn) {
        this(entityIn.getEntityId(), entityIn.motionX, entityIn.motionY, entityIn.motionZ);
    }
    
    public SPacketEntityVelocity(final int entityIdIn, double motionXIn, double motionYIn, double motionZIn) {
        this.entityID = entityIdIn;
        final double d0 = 3.9;
        if (motionXIn < -3.9) {
            motionXIn = -3.9;
        }
        if (motionYIn < -3.9) {
            motionYIn = -3.9;
        }
        if (motionZIn < -3.9) {
            motionZIn = -3.9;
        }
        if (motionXIn > 3.9) {
            motionXIn = 3.9;
        }
        if (motionYIn > 3.9) {
            motionYIn = 3.9;
        }
        if (motionZIn > 3.9) {
            motionZIn = 3.9;
        }
        this.motionX = (int)(motionXIn * 8000.0);
        this.motionY = (int)(motionYIn * 8000.0);
        this.motionZ = (int)(motionZIn * 8000.0);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityID = buf.readVarInt();
        this.motionX = buf.readShort();
        this.motionY = buf.readShort();
        this.motionZ = buf.readShort();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.entityID);
        buf.writeShort(this.motionX);
        buf.writeShort(this.motionY);
        buf.writeShort(this.motionZ);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityVelocity(this);
    }
    
    public int getEntityID() {
        return this.entityID;
    }
    
    public int getMotionX() {
        return this.motionX;
    }
    
    public int getMotionY() {
        return this.motionY;
    }
    
    public int getMotionZ() {
        return this.motionZ;
    }
}
