/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S11PacketSpawnExperienceOrb
implements Packet<INetHandlerPlayClient> {
    private int entityID;
    private int posZ;
    private int posX;
    private int posY;
    private int xpValue;

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityID = packetBuffer.readVarIntFromBuffer();
        this.posX = packetBuffer.readInt();
        this.posY = packetBuffer.readInt();
        this.posZ = packetBuffer.readInt();
        this.xpValue = packetBuffer.readShort();
    }

    public int getXPValue() {
        return this.xpValue;
    }

    public int getZ() {
        return this.posZ;
    }

    public int getY() {
        return this.posY;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityID);
        packetBuffer.writeInt(this.posX);
        packetBuffer.writeInt(this.posY);
        packetBuffer.writeInt(this.posZ);
        packetBuffer.writeShort(this.xpValue);
    }

    public S11PacketSpawnExperienceOrb() {
    }

    public S11PacketSpawnExperienceOrb(EntityXPOrb entityXPOrb) {
        this.entityID = entityXPOrb.getEntityId();
        this.posX = MathHelper.floor_double(entityXPOrb.posX * 32.0);
        this.posY = MathHelper.floor_double(entityXPOrb.posY * 32.0);
        this.posZ = MathHelper.floor_double(entityXPOrb.posZ * 32.0);
        this.xpValue = entityXPOrb.getXpValue();
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleSpawnExperienceOrb(this);
    }

    public int getX() {
        return this.posX;
    }

    public int getEntityID() {
        return this.entityID;
    }
}

