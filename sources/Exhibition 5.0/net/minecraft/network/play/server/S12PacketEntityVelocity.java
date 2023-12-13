// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;

public class S12PacketEntityVelocity implements Packet
{
    private int entID;
    private int motX;
    private int motY;
    private int motZ;
    private static final String __OBFID = "CL_00001328";
    
    public S12PacketEntityVelocity() {
    }
    
    public S12PacketEntityVelocity(final Entity e) {
        this(e.getEntityId(), e.motionX, e.motionY, e.motionZ);
    }
    
    public S12PacketEntityVelocity(final int i, double x, double y, double z) {
        this.entID = i;
        final double var8 = 3.9;
        if (x < -var8) {
            x = -var8;
        }
        if (y < -var8) {
            y = -var8;
        }
        if (z < -var8) {
            z = -var8;
        }
        if (x > var8) {
            x = var8;
        }
        if (y > var8) {
            y = var8;
        }
        if (z > var8) {
            z = var8;
        }
        this.motX = (int)(x * 8000.0);
        this.motY = (int)(y * 8000.0);
        this.motZ = (int)(z * 8000.0);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.entID = data.readVarIntFromBuffer();
        this.motX = data.readShort();
        this.motY = data.readShort();
        this.motZ = data.readShort();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.entID);
        data.writeShort(this.motX);
        data.writeShort(this.motY);
        data.writeShort(this.motZ);
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityVelocity(this);
    }
    
    public int getEntityID() {
        return this.entID;
    }
    
    public int getX() {
        return this.motX;
    }
    
    public int getY() {
        return this.motY;
    }
    
    public int getZ() {
        return this.motZ;
    }
    
    public void setMotX(final int motX) {
        this.motX = motX;
    }
    
    public void setMotY(final int motY) {
        this.motY = motY;
    }
    
    public void setMotZ(final int motZ) {
        this.motZ = motZ;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
