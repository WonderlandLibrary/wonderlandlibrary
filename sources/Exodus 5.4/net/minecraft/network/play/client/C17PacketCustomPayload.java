/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C17PacketCustomPayload
implements Packet<INetHandlerPlayServer> {
    private String channel;
    private PacketBuffer data;

    public String getChannelName() {
        return this.channel;
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
        if (n < 0 || n > Short.MAX_VALUE) {
            throw new IOException("Payload may not be larger than 32767 bytes");
        }
        this.data = new PacketBuffer(packetBuffer.readBytes(n));
    }

    public C17PacketCustomPayload() {
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processVanilla250Packet(this);
    }

    public C17PacketCustomPayload(String string, PacketBuffer packetBuffer) {
        this.channel = string;
        this.data = packetBuffer;
        if (packetBuffer.writerIndex() > Short.MAX_VALUE) {
            throw new IllegalArgumentException("Payload may not be larger than 32767 bytes");
        }
    }

    public PacketBuffer getBufferData() {
        return this.data;
    }
}

