/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.world.WorldServer;

public class C18PacketSpectate
implements Packet<INetHandlerPlayServer> {
    private UUID id;

    public C18PacketSpectate() {
    }

    public C18PacketSpectate(UUID uUID) {
        this.id = uUID;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeUuid(this.id);
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.handleSpectate(this);
    }

    public Entity getEntity(WorldServer worldServer) {
        return worldServer.getEntityFromUuid(this.id);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.id = packetBuffer.readUuid();
    }
}

