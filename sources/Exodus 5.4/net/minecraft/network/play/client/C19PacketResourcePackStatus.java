/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C19PacketResourcePackStatus
implements Packet<INetHandlerPlayServer> {
    private String hash;
    private Action status;

    public C19PacketResourcePackStatus() {
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.hash = packetBuffer.readStringFromBuffer(40);
        this.status = packetBuffer.readEnumValue(Action.class);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeString(this.hash);
        packetBuffer.writeEnumValue(this.status);
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.handleResourcePackStatus(this);
    }

    public C19PacketResourcePackStatus(String string, Action action) {
        if (string.length() > 40) {
            string = string.substring(0, 40);
        }
        this.hash = string;
        this.status = action;
    }

    public static enum Action {
        SUCCESSFULLY_LOADED,
        DECLINED,
        FAILED_DOWNLOAD,
        ACCEPTED;

    }
}

