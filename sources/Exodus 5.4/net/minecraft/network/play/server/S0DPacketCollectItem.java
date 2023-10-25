/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S0DPacketCollectItem
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private int collectedItemEntityId;

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleCollectItem(this);
    }

    public int getCollectedItemEntityID() {
        return this.collectedItemEntityId;
    }

    public int getEntityID() {
        return this.entityId;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.collectedItemEntityId = packetBuffer.readVarIntFromBuffer();
        this.entityId = packetBuffer.readVarIntFromBuffer();
    }

    public S0DPacketCollectItem() {
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.collectedItemEntityId);
        packetBuffer.writeVarIntToBuffer(this.entityId);
    }

    public S0DPacketCollectItem(int n, int n2) {
        this.collectedItemEntityId = n;
        this.entityId = n2;
    }
}

