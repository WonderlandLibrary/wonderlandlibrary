package intent.AquaDev.aqua.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtils {
    static Minecraft mc = Minecraft.getMinecraft();

    public static void sendPacket(Packet<?> packet, boolean silent) {
        if (PacketUtils.mc.thePlayer != null) {
            mc.getNetHandler().getNetworkManager().sendPacket(packet);
        }
    }

    public static void sendPacketNoEvent(Packet packet) {
        PacketUtils.sendPacket(packet, true);
    }

    public static void sendPacket(Packet packet) {
        PacketUtils.sendPacket(packet, false);
    }
}
