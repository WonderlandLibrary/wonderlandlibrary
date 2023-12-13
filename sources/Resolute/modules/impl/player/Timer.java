// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class Timer extends Module
{
    public NumberSetting timer;
    
    public Timer() {
        super("Timer", 0, "Changes world timer", Category.PLAYER);
        this.timer = new NumberSetting("Timer", 1.0, 0.1, 5.0, 0.1);
        this.addSettings(this.timer);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Timer.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix("Speed: " + this.timer.getValue());
        if (e instanceof EventUpdate) {
            Timer.mc.timer.timerSpeed = (float)this.timer.getValue();
        }
    }
}
