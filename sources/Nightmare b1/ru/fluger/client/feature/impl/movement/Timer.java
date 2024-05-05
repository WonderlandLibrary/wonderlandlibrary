// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class Timer extends Feature
{
    private final NumberSetting timerSpeed;
    
    public Timer() {
        super("Timer", "\u0423\u0432\u0435\u043b\u0438\u0447\u0438\u0432\u0430\u0435\u0442 \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0438\u0433\u0440\u044b", Type.Movement);
        this.timerSpeed = new NumberSetting("Timer Amount", 2.0f, 0.1f, 10.0f, 0.1f, () -> true);
        this.addSettings(this.timerSpeed);
    }
    
    @EventTarget
    public void onPreUpdate(final EventPreMotion preMotion) {
        Timer.mc.Y.timerSpeed = this.timerSpeed.getCurrentValue();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Timer.mc.Y.timerSpeed = 1.0f;
    }
}
