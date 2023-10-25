/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S02PacketChat
implements Packet<INetHandlerPlayClient> {
    private IChatComponent chatComponent;
    private byte type;

    public S02PacketChat() {
    }

    public IChatComponent getChatComponent() {
        return this.chatComponent;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleChat(this);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeChatComponent(this.chatComponent);
        packetBuffer.writeByte(this.type);
    }

    public S02PacketChat(IChatComponent iChatComponent, byte by) {
        this.chatComponent = iChatComponent;
        this.type = by;
    }

    public boolean isChat() {
        return this.type == 1 || this.type == 2;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.chatComponent = packetBuffer.readChatComponent();
        this.type = packetBuffer.readByte();
    }

    public S02PacketChat(IChatComponent iChatComponent) {
        this(iChatComponent, 1);
    }

    public byte getType() {
        return this.type;
    }
}

