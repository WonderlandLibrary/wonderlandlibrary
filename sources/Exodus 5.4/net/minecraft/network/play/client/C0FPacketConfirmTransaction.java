/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0FPacketConfirmTransaction
implements Packet<INetHandlerPlayServer> {
    private boolean accepted;
    private int windowId;
    private short uid;

    public C0FPacketConfirmTransaction() {
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.windowId = packetBuffer.readByte();
        this.uid = packetBuffer.readShort();
        this.accepted = packetBuffer.readByte() != 0;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeByte(this.windowId);
        packetBuffer.writeShort(this.uid);
        packetBuffer.writeByte(this.accepted ? 1 : 0);
    }

    public short getUid() {
        return this.uid;
    }

    public C0FPacketConfirmTransaction(int n, short s, boolean bl) {
        this.windowId = n;
        this.uid = s;
        this.accepted = bl;
    }

    public int getWindowId() {
        return this.windowId;
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processConfirmTransaction(this);
    }
}

