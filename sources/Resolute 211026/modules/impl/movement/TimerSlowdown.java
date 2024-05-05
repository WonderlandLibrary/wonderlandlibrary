// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class TimerSlowdown extends Module
{
    public NumberSetting start;
    public NumberSetting end;
    public NumberSetting speed;
    public NumberSetting delay;
    int ticks;
    float currentTimer;
    
    public TimerSlowdown() {
        super("TimerSlow", 0, "Slows timer over time", Category.MOVEMENT);
        this.start = new NumberSetting("Start Time", 1.8, 0.1, 10.0, 0.1);
        this.end = new NumberSetting("End Time", 1.0, 0.1, 10.0, 0.1);
        this.speed = new NumberSetting("Speed", 1.0, 1.0, 20.0, 1.0);
        this.delay = new NumberSetting("Delay", 3.0, 1.0, 20.0, 1.0);
        this.ticks = 0;
        this.currentTimer = 1.0f;
        this.addSettings(this.start, this.end, this.speed, this.delay);
    }
    
    @Override
    public void onEnable() {
        this.currentTimer = (float)this.start.getValue();
        this.ticks = 0;
    }
    
    @Override
    public void onDisable() {
        TimerSlowdown.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventUpdate) {
            ++this.ticks;
            TimerSlowdown.mc.timer.timerSpeed = this.currentTimer;
            if (this.currentTimer > this.end.getValue() && this.ticks == this.delay.getValue()) {
                this.currentTimer -= (float)(0.05 * this.speed.getValue());
                this.ticks = 0;
            }
        }
    }
}
