// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class WaterSpeed extends Feature
{
    public static NumberSetting speed;
    private final BooleanSetting speedCheck;
    
    public WaterSpeed() {
        super("WaterSpeed", "\u0414\u0435\u043b\u0430\u0435\u0442 \u0432\u0430\u0441 \u0431\u044b\u0441\u0442\u0440\u0435\u0435 \u0432 \u0432\u043e\u0434\u0435", Type.Movement);
        WaterSpeed.speed = new NumberSetting("Speed Amount", 1.0f, 0.1f, 4.0f, 0.01f, () -> true);
        this.speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
        this.addSettings(this.speedCheck, WaterSpeed.speed);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (!WaterSpeed.mc.h.a(vb.a) && this.speedCheck.getCurrentValue()) {
            return;
        }
        if (WaterSpeed.mc.h.isInLiquid()) {
            MovementHelper.setSpeed(WaterSpeed.speed.getCurrentValue());
        }
    }
}
