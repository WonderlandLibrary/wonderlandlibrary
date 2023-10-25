package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.Hand;

public class CAnimateHandPacket
implements Packet<INetHandlerPlayServer> {
    private Hand hand;

    public CAnimateHandPacket() {
    }

    public CAnimateHandPacket(Hand handIn) {
        this.hand = handIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.hand = (Hand)buf.readEnumValue(Hand.class);
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeEnumValue((Enum)this.hand);
    }

    public void processPacket(INetHandlerPlayServer handler) {
        handler.handleAnimation(this);
    }

    public Hand getHand() {
        return this.hand;
    }
}
