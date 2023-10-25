/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S03PacketTimeUpdate
implements Packet<INetHandlerPlayClient> {
    private long totalWorldTime;
    private long worldTime;

    public long getTotalWorldTime() {
        return this.totalWorldTime;
    }

    public S03PacketTimeUpdate(long l, long l2, boolean bl) {
        this.totalWorldTime = l;
        this.worldTime = l2;
        if (!bl) {
            this.worldTime = -this.worldTime;
            if (this.worldTime == 0L) {
                this.worldTime = -1L;
            }
        }
    }

    public S03PacketTimeUpdate() {
    }

    public long getWorldTime() {
        return this.worldTime;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.totalWorldTime = packetBuffer.readLong();
        this.worldTime = packetBuffer.readLong();
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleTimeUpdate(this);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeLong(this.totalWorldTime);
        packetBuffer.writeLong(this.worldTime);
    }
}

