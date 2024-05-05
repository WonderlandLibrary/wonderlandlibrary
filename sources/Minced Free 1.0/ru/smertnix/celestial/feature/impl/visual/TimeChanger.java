package ru.smertnix.celestial.feature.impl.visual;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventReceivePacket;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;

public class TimeChanger extends Feature {

    private final ListSetting mode = new ListSetting("Mode", "Day", () -> true, "Night", "Day", "Morning", "Sunset");;

    public TimeChanger() {
        super("Time Changer", "Меняет время в мире", FeatureCategory.Render);
        addSettings(mode);
    }
    
    @EventTarget
	public void onPacket(EventReceivePacket event) {
		if (event.getPacket() instanceof SPacketTimeUpdate) {
			event.setCancelled(true);
		}
	}

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (isEnabled()) {
            String mode = this.mode.getOptions();
            if (mode.equalsIgnoreCase("Day")) {
    			mc.world.setWorldTime(5000);
    		} else if (mode.equalsIgnoreCase("Night")) {
    			mc.world.setWorldTime(17000);
    		} else if (mode.equalsIgnoreCase("Morning")) {
    			mc.world.setWorldTime(0);
    		} else if (mode.equalsIgnoreCase("Sunset")) {
    			mc.world.setWorldTime(13000);
    		}
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = 0.1f;
        mc.player.removePotionEffect(Potion.getPotionById(16));
    }
}
