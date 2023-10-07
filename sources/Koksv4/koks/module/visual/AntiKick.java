package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.PacketEvent;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S40PacketDisconnect;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "AntiKick", description = "You don't disconnect when you got kicked", category = Module.Category.VISUAL)
public class AntiKick extends Module {

    @Override
    @Event.Info(priority = Event.Priority.EXTREME)
    public void onEvent(Event event) {
        if(event instanceof final PacketEvent packetEvent) {
            final Packet<? extends INetHandler> packet = packetEvent.getPacket();
            if(packetEvent.getType() == PacketEvent.Type.RECEIVE) {
                if(packet instanceof S40PacketDisconnect) {
                    event.setCanceled(true);
                    sendMessage("§cYou got kicked! \n\n§eReason: §7" + ((S40PacketDisconnect) packet).getReason().getFormattedText());
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
