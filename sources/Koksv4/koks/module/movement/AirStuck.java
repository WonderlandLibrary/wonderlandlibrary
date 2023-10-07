package koks.module.movement;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.PacketEvent;
import koks.event.UpdateEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S40PacketDisconnect;

@Module.Info(name = "AirStuck", category = Module.Category.MOVEMENT, description = "You stuck in the Air")
public class AirStuck extends Module implements Module.Unsafe {

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final PacketEvent packetEvent) {
            final Packet<?> packet = packetEvent.getPacket();
            if(packet instanceof S40PacketDisconnect) {
                setToggled(false);
            }
        }

        if (event instanceof UpdateEvent) {
            getPlayer().isDead = true;
            setMotion(0);
            stopWalk();
            getPlayer().motionY = 0;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        getPlayer().isDead = false;
        resumeWalk();
    }
}
