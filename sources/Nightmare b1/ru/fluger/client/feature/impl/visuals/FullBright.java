// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.feature.Feature;

public class FullBright extends Feature
{
    private final ListSetting brightMode;
    
    public FullBright() {
        super("FullBright", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0442\u0435\u043c\u043d\u043e\u0442\u0443 \u0432 \u0438\u0433\u0440\u0435", Type.Visuals);
        this.brightMode = new ListSetting("FullBright Mode", "Gamma", () -> true, new String[] { "Gamma", "Potion" });
        this.addSettings(this.brightMode);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (this.getState()) {
            final String mode = this.brightMode.getOptions();
            if (mode.equalsIgnoreCase("Gamma")) {
                FullBright.mc.t.aE = 10.0f;
            }
            if (mode.equalsIgnoreCase("Potion")) {
                FullBright.mc.h.c(new va(uz.a(16), 817, 1));
            }
            else {
                FullBright.mc.h.d(uz.a(16));
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        FullBright.mc.t.aE = 0.1f;
        FullBright.mc.h.d(uz.a(16));
    }
}
