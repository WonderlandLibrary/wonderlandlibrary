package astronaut.modules.world;

import astronaut.events.EventReceivedPacket;
import astronaut.modules.Category;
import astronaut.modules.Module;
import eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import org.lwjgl.input.Keyboard;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.awt.*;

import static astronaut.events.EventReceivedPacket.packet;

public class PikaDisabler extends Module {
    public PikaDisabler() {
        super("PikaDisabler", Type.World, 0, Category.WORLD, Color.orange, "Disables the PikaNetwork AntiCheat");
    }

    @Override
    public void onEnable() {
    }

    public void onDisable() {
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    }

    @EventTarget
    public void onPacket(EventReceivedPacket event) {
        Packet<?> packet = event.getPacket();

        if (packet instanceof C03PacketPlayer) {
            event.setCancelled(true);
        }

        if (packet instanceof S08PacketPlayerPosLook) {

            event.setCancelled(true);
        }

        if (packet instanceof S18PacketEntityTeleport){
            S18PacketEntityTeleport packet2 = (S18PacketEntityTeleport) packet;
            if (packet2.getEntityId() == mc.thePlayer.getEntityId());
            event.setCancelled(true);
        }
    }
}
