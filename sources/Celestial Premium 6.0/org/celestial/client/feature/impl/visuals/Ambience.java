/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class Ambience
extends Feature {
    private final NumberSetting time;
    private final ListSetting ambienceMode = new ListSetting("Ambience Mode", "Night", () -> true, "Day", "Night", "Morning", "Sunset", "Spin");
    private long spin = 0L;

    public Ambience() {
        super("Ambience", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043c\u0435\u043d\u044f\u0442\u044c \u0432\u0440\u0435\u043c\u044f \u0441\u0443\u0442\u043e\u043a", Type.Visuals);
        this.time = new NumberSetting("TimeSpin Speed", 2.0f, 1.0f, 10.0f, 1.0f, () -> this.ambienceMode.currentMode.equals("Spin"));
        this.addSettings(this.ambienceMode, this.time);
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = this.ambienceMode.getCurrentMode();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Spin")) {
            Ambience.mc.world.setWorldTime(this.spin);
            this.spin = (long)((float)this.spin + this.time.getCurrentValue() * 100.0f);
        } else if (mode.equalsIgnoreCase("Day")) {
            Ambience.mc.world.setWorldTime(5000L);
        } else if (mode.equalsIgnoreCase("Night")) {
            Ambience.mc.world.setWorldTime(17000L);
        } else if (mode.equalsIgnoreCase("Morning")) {
            Ambience.mc.world.setWorldTime(0L);
        } else if (mode.equalsIgnoreCase("Sunset")) {
            Ambience.mc.world.setWorldTime(13000L);
        }
    }
}

