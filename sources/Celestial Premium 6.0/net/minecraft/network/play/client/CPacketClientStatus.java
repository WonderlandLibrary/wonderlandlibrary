/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketClientStatus
implements Packet<INetHandlerPlayServer> {
    private State status;

    public CPacketClientStatus() {
    }

    public CPacketClientStatus(State p_i46886_1_) {
        this.status = p_i46886_1_;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.status = buf.readEnumValue(State.class);
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.status);
    }

    @Override
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processClientStatus(this);
    }

    public State getStatus() {
        return this.status;
    }

    public static enum State {
        PERFORM_RESPAWN,
        REQUEST_STATS;

    }
}

