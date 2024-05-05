package astronaut.modules.world;

import astronaut.events.EventReceivedPacket;
import astronaut.events.EventUpdate;
import astronaut.modules.Category;
import astronaut.modules.Module;
import eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class IntaveDisabler extends Module {
    public IntaveDisabler() {
        super("Intave Disabler", Type.World, 0, Category.WORLD, Color.orange, "Disables the Intave anticheat");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @EventTarget
    public void onPacket(EventReceivedPacket event) {
        Packet<?> packet = event.getPacket();

        if (packet instanceof S3FPacketCustomPayload) {
            S3FPacketCustomPayload customPayloadPacket = (S3FPacketCustomPayload) packet;

            if (customPayloadPacket.getChannelName().equals("MC|Brand")) {
                event.setCancelled(true);
            }
        }
    }
}
