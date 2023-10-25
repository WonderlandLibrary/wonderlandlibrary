/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S13PacketDestroyEntities
implements Packet<INetHandlerPlayClient> {
    private int[] entityIDs;

    public S13PacketDestroyEntities(int ... nArray) {
        this.entityIDs = nArray;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityIDs.length);
        int n = 0;
        while (n < this.entityIDs.length) {
            packetBuffer.writeVarIntToBuffer(this.entityIDs[n]);
            ++n;
        }
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityIDs = new int[packetBuffer.readVarIntFromBuffer()];
        int n = 0;
        while (n < this.entityIDs.length) {
            this.entityIDs[n] = packetBuffer.readVarIntFromBuffer();
            ++n;
        }
    }

    public int[] getEntityIDs() {
        return this.entityIDs;
    }

    public S13PacketDestroyEntities() {
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleDestroyEntities(this);
    }
}

