/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.status.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class C00PacketServerQuery
implements Packet<INetHandlerStatusServer> {
    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
    }

    @Override
    public void processPacket(INetHandlerStatusServer iNetHandlerStatusServer) {
        iNetHandlerStatusServer.processServerQuery(this);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
    }
}

