/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketDisconnect
implements Packet<INetHandlerPlayClient> {
    private ITextComponent reason;

    public SPacketDisconnect() {
    }

    public SPacketDisconnect(ITextComponent messageIn) {
        this.reason = messageIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.reason = buf.readTextComponent();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeTextComponent(this.reason);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleDisconnect(this);
    }

    public ITextComponent getReason() {
        return this.reason;
    }
}

