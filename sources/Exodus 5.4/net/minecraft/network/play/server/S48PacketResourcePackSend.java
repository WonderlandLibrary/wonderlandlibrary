/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S48PacketResourcePackSend
implements Packet<INetHandlerPlayClient> {
    private String hash;
    private String url;

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleResourcePack(this);
    }

    public String getURL() {
        return this.url;
    }

    public S48PacketResourcePackSend() {
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeString(this.url);
        packetBuffer.writeString(this.hash);
    }

    public S48PacketResourcePackSend(String string, String string2) {
        this.url = string;
        this.hash = string2;
        if (string2.length() > 40) {
            throw new IllegalArgumentException("Hash is too long (max 40, was " + string2.length() + ")");
        }
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.url = packetBuffer.readStringFromBuffer(Short.MAX_VALUE);
        this.hash = packetBuffer.readStringFromBuffer(40);
    }

    public String getHash() {
        return this.hash;
    }
}

