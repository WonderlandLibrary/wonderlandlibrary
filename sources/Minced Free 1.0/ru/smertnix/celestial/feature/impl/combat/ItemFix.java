package ru.smertnix.celestial.feature.impl.combat;

import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventReceivePacket;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;

public class ItemFix extends Feature {
    public ItemFix() {
        super("Items Fix", "Фиксит сортировку предметов на Matrix", FeatureCategory.Combat);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
    	if (e.getPacket() instanceof SPacketHeldItemChange) {
    		e.setCancelled(true);
    	}
    	if (e.getPacket() instanceof SPacketCloseWindow) {
    		e.setCancelled(true);
    	}
    }
}
