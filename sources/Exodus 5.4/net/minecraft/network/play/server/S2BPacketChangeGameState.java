/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S2BPacketChangeGameState
implements Packet<INetHandlerPlayClient> {
    private int state;
    private float field_149141_c;
    public static final String[] MESSAGE_NAMES = new String[]{"tile.bed.notValid"};

    public int getGameState() {
        return this.state;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleChangeGameState(this);
    }

    public float func_149137_d() {
        return this.field_149141_c;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeByte(this.state);
        packetBuffer.writeFloat(this.field_149141_c);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.state = packetBuffer.readUnsignedByte();
        this.field_149141_c = packetBuffer.readFloat();
    }

    public S2BPacketChangeGameState(int n, float f) {
        this.state = n;
        this.field_149141_c = f;
    }

    public S2BPacketChangeGameState() {
    }
}

