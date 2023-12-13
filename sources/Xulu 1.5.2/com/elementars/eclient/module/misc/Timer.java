// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Timer extends Module
{
    private final Value<Float> tickSpeed;
    
    public Timer() {
        super("Timer", "Modifies the game speed", 0, Category.MISC, true);
        this.tickSpeed = this.register(new Value<Float>("Speed", this, 4.0f, 0.0f, 10.0f));
    }
    
    @Override
    public void onDisable() {
        Timer.mc.timer.tickLength = 50.0f;
    }
    
    @Override
    public void onUpdate() {
        Timer.mc.timer.tickLength = 50.0f / ((this.tickSpeed.getValue() == 0.0f) ? 0.1f : this.tickSpeed.getValue());
    }
}
