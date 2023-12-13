package com.ohare.client.module.modules.combat;

import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

import java.awt.*;

public class AntiVelocity extends Module {

    public AntiVelocity() {
        super("AntiVelocity", Category.COMBAT, new Color(120, 120, 150, 255).getRGB());
        setDescription("Cancels velocity packets");
        setRenderlabel("Anti Velocity");

    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (!event.isSending()) {
            if ((event.getPacket() instanceof S12PacketEntityVelocity)) {
                event.setCanceled(true);
            }
            if (event.getPacket() instanceof S27PacketExplosion) {
                event.setCanceled(true);
            }
        }
    }
}
