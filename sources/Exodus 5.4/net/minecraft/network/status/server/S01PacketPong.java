/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.status.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusClient;

public class S01PacketPong
implements Packet<INetHandlerStatusClient> {
    private long clientTime;

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeLong(this.clientTime);
    }

    public S01PacketPong() {
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.clientTime = packetBuffer.readLong();
    }

    public S01PacketPong(long l) {
        this.clientTime = l;
    }

    @Override
    public void processPacket(INetHandlerStatusClient iNetHandlerStatusClient) {
        iNetHandlerStatusClient.handlePong(this);
    }
}

