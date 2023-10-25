/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.login.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.IChatComponent;

public class S00PacketDisconnect
implements Packet<INetHandlerLoginClient> {
    private IChatComponent reason;

    public S00PacketDisconnect() {
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.reason = packetBuffer.readChatComponent();
    }

    public IChatComponent func_149603_c() {
        return this.reason;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeChatComponent(this.reason);
    }

    public S00PacketDisconnect(IChatComponent iChatComponent) {
        this.reason = iChatComponent;
    }

    @Override
    public void processPacket(INetHandlerLoginClient iNetHandlerLoginClient) {
        iNetHandlerLoginClient.handleDisconnect(this);
    }
}

