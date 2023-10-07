package koks.module.movement;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@Module.Info(name = "AntiHunger", description = "You doesn't become hunger for sprinting", category = Module.Category.MOVEMENT)
public class AntiHunger extends Module {

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final PacketEvent packetEvent) {
            final Packet<?> packet = packetEvent.getPacket();
            if (packet instanceof final C0BPacketEntityAction actionPacket) {
                final C0BPacketEntityAction.Action action = actionPacket.getAction();
                if(action == C0BPacketEntityAction.Action.STOP_SPRINTING || action == C0BPacketEntityAction.Action.START_SPRINTING) {
                    packetEvent.setCanceled(true);
                }

            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
