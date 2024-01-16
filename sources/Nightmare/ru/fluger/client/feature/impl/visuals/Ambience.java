// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.packet.EventReceivePacket;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class Ambience extends Feature
{
    private final NumberSetting time;
    private final ListSetting ambienceMode;
    private long spin;
    
    public Ambience() {
        super("Ambience", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043c\u0435\u043d\u044f\u0442\u044c \u0432\u0440\u0435\u043c\u044f \u0441\u0443\u0442\u043e\u043a", Type.Visuals);
        this.ambienceMode = new ListSetting("Ambience Mode", "Night", () -> true, new String[] { "Day", "Night", "Morning", "Sunset", "Spin" });
        this.spin = 0L;
        this.time = new NumberSetting("TimeSpin Speed", 2.0f, 1.0f, 10.0f, 1.0f, () -> this.ambienceMode.currentMode.equals("Spin"));
        this.addSettings(this.ambienceMode, this.time);
    }
    
    @EventTarget
    public void onPacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof ko) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final String mode = this.ambienceMode.getCurrentMode();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Spin")) {
            Ambience.mc.f.b(this.spin);
            this.spin += (long)(this.time.getCurrentValue() * 100.0f);
        }
        else if (mode.equalsIgnoreCase("Day")) {
            Ambience.mc.f.b(5000L);
        }
        else if (mode.equalsIgnoreCase("Night")) {
            Ambience.mc.f.b(17000L);
        }
        else if (mode.equalsIgnoreCase("Morning")) {
            Ambience.mc.f.b(0L);
        }
        else if (mode.equalsIgnoreCase("Sunset")) {
            Ambience.mc.f.b(13000L);
        }
    }
}
