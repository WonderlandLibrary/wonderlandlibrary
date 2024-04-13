package com.canon.majik.impl.modules.impl.movement;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.PacketEvent;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {
    public Velocity(String name, Category category) {
        super(name, category);
    }

    @EventListener
    public void onPacket(PacketEvent.Receive event){
        if(nullCheck()) return;
        if(event.getPacket() instanceof SPacketExplosion){
            event.setCancelled(true);
        }
        if(event.getPacket() instanceof SPacketEntityVelocity){
            event.setCancelled(true);
        }
    }
}
