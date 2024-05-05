package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C16PacketClientStatus;

public class C16PacketClientStatus
implements Packet<INetHandlerPlayServer> {
    private EnumState status;

    public C16PacketClientStatus() {
    }

    public C16PacketClientStatus(EnumState statusIn) {
        this.status = statusIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.status = (EnumState)buf.readEnumValue(EnumState.class);
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeEnumValue((Enum)this.status);
    }

    public void processPacket(INetHandlerPlayServer handler) {
        handler.processClientStatus(this);
    }

    public EnumState getStatus() {
        return this.status;
    }
}
