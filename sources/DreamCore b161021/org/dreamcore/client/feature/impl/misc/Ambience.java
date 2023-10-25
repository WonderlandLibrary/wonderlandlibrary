package org.dreamcore.client.feature.impl.misc;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventReceivePacket;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class Ambience extends Feature {

    private final NumberSetting time;
    private final ListSetting modeAmbri;
    private long spin = 0;

    public Ambience() {
        super("Ambience", "Позволяет менять время суток", Type.Misc);
        modeAmbri = new ListSetting("Ambience Mode", "Night", () -> true, "Day", "Night", "Morning", "Sunset", "Spin");
        time = new NumberSetting("TimeSpin Speed", 2, 1, 10, 1, () -> true);
        addSettings(modeAmbri, time);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = modeAmbri.getOptions();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Spin")) {
            mc.world.setWorldTime(spin);
            this.spin = (long) (spin + time.getNumberValue() * 100);
        } else if (mode.equalsIgnoreCase("Day")) {
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
