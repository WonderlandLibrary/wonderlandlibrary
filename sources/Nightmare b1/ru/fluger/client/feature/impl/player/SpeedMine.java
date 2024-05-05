// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.player;

import ru.fluger.client.event.events.impl.player.EventBlockInteract;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.feature.Feature;

public class SpeedMine extends Feature
{
    public ListSetting mode;
    
    public SpeedMine() {
        super("SpeedMine", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u044b\u0441\u0442\u0440\u043e \u043b\u043e\u043c\u0430\u0442\u044c \u0431\u043b\u043e\u043a\u0438", Type.Player);
        this.mode = new ListSetting("SpeedMine Mode", "Packet", () -> true, new String[] { "Packet", "Potion" });
        this.addSettings(this.mode);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix(this.mode.currentMode);
    }
    
    @EventTarget
    public void onBlockInteract(final EventBlockInteract event) {
        final String currentMode = this.mode.currentMode;
        switch (currentMode) {
            case "Potion": {
                assert vb.c != null;
                SpeedMine.mc.h.c(new va(vb.c, 817, 1));
                break;
            }
            case "Packet": {
                SpeedMine.mc.h.a(ub.a);
                SpeedMine.mc.h.d.a(new lp(lp.a.a, event.getPos(), event.getFace()));
                SpeedMine.mc.h.d.a(new lp(lp.a.c, event.getPos(), event.getFace()));
                event.setCancelled(true);
                break;
            }
        }
    }
    
    @Override
    public void onDisable() {
        assert vb.c != null;
        SpeedMine.mc.h.d(vb.c);
        super.onDisable();
    }
}
