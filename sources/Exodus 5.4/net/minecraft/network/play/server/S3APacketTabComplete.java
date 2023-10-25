/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S3APacketTabComplete
implements Packet<INetHandlerPlayClient> {
    private String[] matches;

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.matches = new String[packetBuffer.readVarIntFromBuffer()];
        int n = 0;
        while (n < this.matches.length) {
            this.matches[n] = packetBuffer.readStringFromBuffer(Short.MAX_VALUE);
            ++n;
        }
    }

    public S3APacketTabComplete(String[] stringArray) {
        this.matches = stringArray;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.matches.length);
        String[] stringArray = this.matches;
        int n = this.matches.length;
        int n2 = 0;
        while (n2 < n) {
            String string = stringArray[n2];
            packetBuffer.writeString(string);
            ++n2;
        }
    }

    public String[] func_149630_c() {
        return this.matches;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleTabComplete(this);
    }

    public S3APacketTabComplete() {
    }
}

