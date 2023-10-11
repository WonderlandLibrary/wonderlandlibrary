package io.github.raze.utilities.collection.packet;

import io.github.raze.utilities.system.Methods;
import net.minecraft.network.Packet;

public class PacketUtil implements Methods {

    public static void sendPacket(Packet<?> packet) {
        mc.getNetHandler().addToSendQueue(packet);
    }

}
