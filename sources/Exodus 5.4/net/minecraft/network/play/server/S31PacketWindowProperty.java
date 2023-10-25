/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S31PacketWindowProperty
implements Packet<INetHandlerPlayClient> {
    private int windowId;
    private int varIndex;
    private int varValue;

    public S31PacketWindowProperty() {
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getVarValue() {
        return this.varValue;
    }

    public int getVarIndex() {
        return this.varIndex;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.windowId = packetBuffer.readUnsignedByte();
        this.varIndex = packetBuffer.readShort();
        this.varValue = packetBuffer.readShort();
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleWindowProperty(this);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeByte(this.windowId);
        packetBuffer.writeShort(this.varIndex);
        packetBuffer.writeShort(this.varValue);
    }

    public S31PacketWindowProperty(int n, int n2, int n3) {
        this.windowId = n;
        this.varIndex = n2;
        this.varValue = n3;
    }
}

