// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.player;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class NoDelay extends Feature
{
    public BooleanSetting rightClickDelay;
    public BooleanSetting jumpDelay;
    
    public NoDelay() {
        super("NoDelay", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0437\u0430\u0434\u0435\u0440\u0436\u043a\u0443", Type.Player);
        this.rightClickDelay = new BooleanSetting("Fast Place", false, () -> true);
        this.jumpDelay = new BooleanSetting("NoJumpDelay", true, () -> true);
        this.addSettings(this.rightClickDelay, this.jumpDelay);
    }
    
    @Override
    public void onDisable() {
        NoDelay.mc.as = 4;
        super.onDisable();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (!this.getState()) {
            return;
        }
        if (this.rightClickDelay.getCurrentValue()) {
            NoDelay.mc.as = 0;
        }
        if (this.jumpDelay.getCurrentValue()) {
            NoDelay.mc.h.bD = 0;
        }
    }
}
