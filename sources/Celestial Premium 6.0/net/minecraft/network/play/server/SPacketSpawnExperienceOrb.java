/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnExperienceOrb
implements Packet<INetHandlerPlayClient> {
    private int entityID;
    private double posX;
    private double posY;
    private double posZ;
    private int xpValue;

    public SPacketSpawnExperienceOrb() {
    }

    public SPacketSpawnExperienceOrb(EntityXPOrb orb) {
        this.entityID = orb.getEntityId();
        this.posX = orb.posX;
        this.posY = orb.posY;
        this.posZ = orb.posZ;
        this.xpValue = orb.getXpValue();
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityID = buf.readVarIntFromBuffer();
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
        this.xpValue = buf.readShort();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);
        buf.writeShort(this.xpValue);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleSpawnExperienceOrb(this);
    }

    public int getEntityID() {
        return this.entityID;
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

    public int getXPValue() {
        return this.xpValue;
    }
}

