package com.initial.modules.impl.player;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import com.initial.events.*;

public class TimerNigger extends Module
{
    public DoubleSet timerSpeed;
    
    public TimerNigger() {
        super("Timer", 0, Category.PLAYER);
        this.timerSpeed = new DoubleSet("Delay", 1.0, 0.1, 20.0, 0.1);
        this.addSettings(this.timerSpeed);
    }
    
    @EventTarget
    @Override
    public void onEvent(final EventNigger e) {
        if (e instanceof UpdateEvent) {
            this.mc.timer.timerSpeed = (float)this.timerSpeed.getValue();
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.timer.timerSpeed = 1.0f;
    }
}
