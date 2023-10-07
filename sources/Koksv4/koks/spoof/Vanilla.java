package koks.spoof;

import io.netty.buffer.Unpooled;
import koks.api.registry.spoof.Spoof;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class Vanilla implements Spoof {

    @Override
    public Type type() {
        return Type.VANILLA;
    }

    @Override
    public void handleSpoof() {
        sendPacket(new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));
    }
}
