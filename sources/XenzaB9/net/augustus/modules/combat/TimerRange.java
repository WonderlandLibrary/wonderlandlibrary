// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.combat;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventUpdate;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class TimerRange extends Module
{
    private int last;
    private boolean pls_do_the_timer_momento;
    private boolean newFunny;
    private boolean reset;
    private final DoubleValue ticks;
    private final DoubleValue lowTimer;
    private final DoubleValue highTimer;
    
    public TimerRange() {
        super("TickBase", new Color(23, 233, 123), Categorys.COMBAT);
        this.ticks = new DoubleValue(2385, "Ticks", this, 10.0, 0.0, 40.0, 0);
        this.lowTimer = new DoubleValue(624, "FirstTimer", this, 0.3, 0.1, 20.0, 2);
        this.highTimer = new DoubleValue(73, "NextTimer", this, 3.0, 0.1, 20.0, 2);
    }
    
    @Override
    public void onEnable() {
        try {
            this.last = TimerRange.mc.thePlayer.ticksExisted;
        }
        catch (final NullPointerException exception) {
            this.last = 0;
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (TimerRange.mc.thePlayer.ticksExisted == 5) {
            this.reset = true;
            this.newFunny = false;
            this.pls_do_the_timer_momento = false;
            this.last = 0;
        }
        if (this.reset) {
            TimerRange.mc.timer.timerSpeed = 1.0f;
            this.reset = false;
        }
        if (this.newFunny) {
            TimerRange.mc.timer.timerSpeed = (float)this.highTimer.getValue();
            this.newFunny = false;
            this.reset = true;
        }
        if (this.pls_do_the_timer_momento) {
            TimerRange.mc.timer.timerSpeed = (float)this.lowTimer.getValue();
            this.pls_do_the_timer_momento = false;
            this.newFunny = true;
        }
    }
    
    public void attack() {
        if (TimerRange.mc.thePlayer.ticksExisted - this.last >= (int)this.ticks.getValue()) {
            this.pls_do_the_timer_momento = true;
            this.last = TimerRange.mc.thePlayer.ticksExisted;
        }
    }
}
