/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S09PacketHeldItemChange
implements Packet<INetHandlerPlayClient> {
    private int heldItemHotbarIndex;

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.heldItemHotbarIndex = packetBuffer.readByte();
    }

    public S09PacketHeldItemChange() {
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleHeldItemChange(this);
    }

    public S09PacketHeldItemChange(int n) {
        this.heldItemHotbarIndex = n;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeByte(this.heldItemHotbarIndex);
    }

    public int getHeldItemHotbarIndex() {
        return this.heldItemHotbarIndex;
    }
}

