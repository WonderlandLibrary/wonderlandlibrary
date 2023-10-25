/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C01PacketChatMessage
implements Packet<INetHandlerPlayServer> {
    private String message;

    public C01PacketChatMessage(String string) {
        if (string.length() > 100) {
            string = string.substring(0, 100);
        }
        this.message = string;
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processChatMessage(this);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.message = packetBuffer.readStringFromBuffer(100);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeString(this.message);
    }

    public String getMessage() {
        return this.message;
    }

    public C01PacketChatMessage() {
    }
}

