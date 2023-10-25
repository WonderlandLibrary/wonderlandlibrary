/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S40PacketDisconnect
implements Packet<INetHandlerPlayClient> {
    private IChatComponent reason;

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleDisconnect(this);
    }

    public S40PacketDisconnect() {
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeChatComponent(this.reason);
    }

    public S40PacketDisconnect(IChatComponent iChatComponent) {
        this.reason = iChatComponent;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.reason = packetBuffer.readChatComponent();
    }

    public IChatComponent getReason() {
        return this.reason;
    }
}

