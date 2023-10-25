/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S3FPacketCustomPayload
implements Packet<INetHandlerPlayClient> {
    private PacketBuffer data;
    private String channel;

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleCustomPayload(this);
    }

    public S3FPacketCustomPayload() {
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeString(this.channel);
        packetBuffer.writeBytes(this.data);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.channel = packetBuffer.readStringFromBuffer(20);
        int n = packetBuffer.readableBytes();
        if (n < 0 || n > 0x100000) {
            throw new IOException("Payload may not be larger than 1048576 bytes");
        }
        this.data = new PacketBuffer(packetBuffer.readBytes(n));
    }

    public PacketBuffer getBufferData() {
        return this.data;
    }

    public S3FPacketCustomPayload(String string, PacketBuffer packetBuffer) {
        this.channel = string;
        this.data = packetBuffer;
        if (packetBuffer.writerIndex() > 0x100000) {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }

    public String getChannelName() {
        return this.channel;
    }
}

