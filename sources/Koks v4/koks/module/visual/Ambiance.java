package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.event.PacketEvent;
import koks.event.UpdateEvent;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Ambiance", description = "You can change the time", category = Module.Category.VISUAL)
public class Ambiance extends Module {

    @Value(name = "Time", minimum = 0, maximum = 20000)
    int time = 0;

    @Override
    @Event.Info(priority = Event.Priority.EXTREME)
    public void onEvent(Event event) {
        if(event instanceof final PacketEvent packetEvent) {
            if(packetEvent.getType() == PacketEvent.Type.RECEIVE) {
                Packet<? extends INetHandler> packet = packetEvent.getPacket();
                if(packet instanceof S03PacketTimeUpdate)
                    event.setCanceled(true);
            }
        }

        if(event instanceof UpdateEvent) {
            getWorld().setWorldTime(time);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
