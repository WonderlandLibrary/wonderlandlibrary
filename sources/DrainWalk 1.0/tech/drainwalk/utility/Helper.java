package tech.drainwalk.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.Packet;

public interface Helper {

    Minecraft mc = Minecraft.getMinecraft();
    ScaledResolution sr = new ScaledResolution(mc);

    default void sendPacket(Packet<?> packet) {
        mc.player.connection.sendPacket(packet);
    }

}
