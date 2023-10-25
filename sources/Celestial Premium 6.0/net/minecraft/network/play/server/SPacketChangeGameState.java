/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketChangeGameState
implements Packet<INetHandlerPlayClient> {
    public static final String[] MESSAGE_NAMES = new String[]{"tile.bed.notValid"};
    private int state;
    private float value;

    public SPacketChangeGameState() {
    }

    public SPacketChangeGameState(int stateIn, float valueIn) {
        this.state = stateIn;
        this.value = valueIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.state = buf.readUnsignedByte();
        this.value = buf.readFloat();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeByte(this.state);
        buf.writeFloat(this.value);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleChangeGameState(this);
    }

    public int getGameState() {
        return this.state;
    }

    public float getValue() {
        return this.value;
    }
}

