/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S3FPacketCustomPayload
implements Packet {
    private String channel;
    private PacketBuffer data;
    private static final String __OBFID = "CL_00001297";

    public S3FPacketCustomPayload() {
    }

    public S3FPacketCustomPayload(String channelName, PacketBuffer dataIn) {
        this.channel = channelName;
        this.data = dataIn;
        if (dataIn.writerIndex() > 0x100000) {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.channel = data.readStringFromBuffer(20);
        int var2 = data.readableBytes();
        if (var2 < 0 || var2 > 0x100000) {
            throw new IOException("Payload may not be larger than 1048576 bytes");
        }
        this.data = new PacketBuffer(data.readBytes(var2));
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeString(this.channel);
        data.writeBytes(this.data);
    }

    public void process(INetHandlerPlayClient p_180734_1_) {
        p_180734_1_.handleCustomPayload(this);
    }

    public String getChannelName() {
        return this.channel;
    }

    public PacketBuffer getBufferData() {
        return this.data;
    }

    public void processPacket(INetHandler handler) {
        this.process((INetHandlerPlayClient)handler);
    }
}

