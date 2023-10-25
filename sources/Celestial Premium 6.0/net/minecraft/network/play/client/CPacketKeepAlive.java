/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketKeepAlive
implements Packet<INetHandlerPlayServer> {
    public long key;

    public CPacketKeepAlive() {
    }

    public CPacketKeepAlive(long idIn) {
        this.key = idIn;
    }

    @Override
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processKeepAlive(this);
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.key = buf.readLong();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeLong(this.key);
    }

    public long getKey() {
        return this.key;
    }
}

