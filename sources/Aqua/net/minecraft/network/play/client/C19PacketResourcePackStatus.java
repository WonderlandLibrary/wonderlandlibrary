package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;

public class C19PacketResourcePackStatus
implements Packet<INetHandlerPlayServer> {
    private String hash;
    private Action status;

    public C19PacketResourcePackStatus() {
    }

    public C19PacketResourcePackStatus(String hashIn, Action statusIn) {
        if (hashIn.length() > 40) {
            hashIn = hashIn.substring(0, 40);
        }
        this.hash = hashIn;
        this.status = statusIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.hash = buf.readStringFromBuffer(40);
        this.status = (Action)buf.readEnumValue(Action.class);
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeString(this.hash);
        buf.writeEnumValue((Enum)this.status);
    }

    public void processPacket(INetHandlerPlayServer handler) {
        handler.handleResourcePackStatus(this);
    }
}
