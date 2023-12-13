package dev.rise.util.player;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

@UtilityClass
public final class PacketUtil {
    private final Minecraft mc = Minecraft.getMinecraft();

    public void sendPacket(final Packet<?> packet) {
        mc.getNetHandler().addToSendQueue(packet);
    }

    public void sendPacketWithoutEvent(final Packet<?> packet) {
        mc.getNetHandler().addToSendQueueWithoutEvent(packet);
    }

    public void receivePacket(final Packet<?> packet) {
        mc.getNetHandler().addToReceiveQueue(packet);
    }

    public void receivePacketWithoutEvent(final Packet<?> packet) {
        mc.getNetHandler().addToReceiveQueueWithoutEvent(packet);
    }
}
