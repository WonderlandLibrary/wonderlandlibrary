/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C11PacketEnchantItem
implements Packet<INetHandlerPlayServer> {
    private int button;
    private int windowId;

    public int getWindowId() {
        return this.windowId;
    }

    public int getButton() {
        return this.button;
    }

    public C11PacketEnchantItem() {
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeByte(this.windowId);
        packetBuffer.writeByte(this.button);
    }

    public C11PacketEnchantItem(int n, int n2) {
        this.windowId = n;
        this.button = n2;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.windowId = packetBuffer.readByte();
        this.button = packetBuffer.readByte();
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processEnchantItem(this);
    }
}

