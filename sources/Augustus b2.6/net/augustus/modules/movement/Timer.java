// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.augustus.events.EventWorld;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventPreMotion;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class Timer extends Module
{
    public DoubleValue timerSpeed;
    
    public Timer() {
        super("Timer", new Color(180, 176, 119), Categorys.MOVEMENT);
        this.timerSpeed = new DoubleValue(1, "Time", this, 1.0, 0.1, 10.0, 2);
    }
    
    @EventTarget
    public void onEventPreMotion(final EventPreMotion eventPreMotion) {
        Timer.mc.getTimer().timerSpeed = (float)this.timerSpeed.getValue();
    }
    
    @Override
    public void onDisable() {
        Timer.mc.getTimer().timerSpeed = 1.0f;
    }
    
    @EventTarget
    public void onEventWord(final EventWorld eventWorld) {
        if (eventWorld.getWorldClient() != null) {
            return;
        }
        this.toggle();
    }
}
