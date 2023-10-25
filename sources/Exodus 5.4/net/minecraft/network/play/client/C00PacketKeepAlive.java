/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C00PacketKeepAlive
implements Packet<INetHandlerPlayServer> {
    private int key;

    public C00PacketKeepAlive() {
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.key = packetBuffer.readVarIntFromBuffer();
    }

    public int getKey() {
        return this.key;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.key);
    }

    public C00PacketKeepAlive(int n) {
        this.key = n;
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processKeepAlive(this);
    }
}

