/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S0BPacketAnimation
implements Packet<INetHandlerPlayClient> {
    private int type;
    private int entityId;

    public int getAnimationType() {
        return this.type;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readVarIntFromBuffer();
        this.type = packetBuffer.readUnsignedByte();
    }

    public S0BPacketAnimation(Entity entity, int n) {
        this.entityId = entity.getEntityId();
        this.type = n;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleAnimation(this);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityId);
        packetBuffer.writeByte(this.type);
    }

    public S0BPacketAnimation() {
    }

    public int getEntityID() {
        return this.entityId;
    }
}

