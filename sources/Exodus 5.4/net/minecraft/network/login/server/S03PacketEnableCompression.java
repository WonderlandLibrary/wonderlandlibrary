/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.login.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class S03PacketEnableCompression
implements Packet<INetHandlerLoginClient> {
    private int compressionTreshold;

    @Override
    public void processPacket(INetHandlerLoginClient iNetHandlerLoginClient) {
        iNetHandlerLoginClient.handleEnableCompression(this);
    }

    public S03PacketEnableCompression(int n) {
        this.compressionTreshold = n;
    }

    public S03PacketEnableCompression() {
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.compressionTreshold);
    }

    public int getCompressionTreshold() {
        return this.compressionTreshold;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.compressionTreshold = packetBuffer.readVarIntFromBuffer();
    }
}

