/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S00PacketKeepAlive
implements Packet<INetHandlerPlayClient> {
    private int id;

    public int func_149134_c() {
        return this.id;
    }

    public S00PacketKeepAlive(int n) {
        this.id = n;
    }

    public S00PacketKeepAlive() {
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleKeepAlive(this);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.id);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.id = packetBuffer.readVarIntFromBuffer();
    }
}

