/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketEnchantItem
implements Packet<INetHandlerPlayServer> {
    private int windowId;
    private int button;

    public CPacketEnchantItem() {
    }

    public CPacketEnchantItem(int windowIdIn, int buttonIn) {
        this.windowId = windowIdIn;
        this.button = buttonIn;
    }

    @Override
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processEnchantItem(this);
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.windowId = buf.readByte();
        this.button = buf.readByte();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeByte(this.button);
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getButton() {
        return this.button;
    }
}

