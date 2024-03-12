// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public final class Timer extends Module
{
    private final NumberSetting amount;
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        Timer.mc.timer.timerSpeed = this.amount.getValue().floatValue();
    }
    
    @Override
    public void onDisable() {
        Timer.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    public Timer() {
        super("Timer", "Timer", Category.PLAYER, "changes game speed");
        this.amount = new NumberSetting("Amount", 1.0, 10.0, 0.1, 0.1);
        this.addSettings(this.amount);
    }
}
