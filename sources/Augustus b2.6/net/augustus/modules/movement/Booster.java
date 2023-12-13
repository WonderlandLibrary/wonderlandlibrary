// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.augustus.events.EventEarlyTick;
import net.lenni0451.eventapi.reflection.EventTarget;
import org.lwjgl.input.Keyboard;
import net.augustus.events.EventRender3D;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class Booster extends Module
{
    public final DoubleValue boostTicks;
    public final DoubleValue timerSpeed;
    public final DoubleValue delay;
    private final TimeHelper timeHelper;
    private final TimeHelper timeHelper2;
    private int tickCounter;
    
    public Booster() {
        super("Booster", new Color(214, 84, 186), Categorys.MOVEMENT);
        this.boostTicks = new DoubleValue(1, "BoostTicks", this, 2.0, 0.0, 20.0, 0);
        this.timerSpeed = new DoubleValue(2, "TimerSpeed", this, 15.0, 1.0, 50.0, 1);
        this.delay = new DoubleValue(3, "Delay", this, 2000.0, 0.0, 10000.0, 0);
        this.timeHelper = new TimeHelper();
        this.timeHelper2 = new TimeHelper();
        this.tickCounter = 0;
        this.tickCounter = 0;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Booster.mc.getTimer().timerSpeed = 1.0f;
        this.tickCounter = 0;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        Booster.mc.getTimer().timerSpeed = 1.0f;
        this.tickCounter = 0;
        this.timeHelper.reset();
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
        this.setToggled(false);
        if (this.timeHelper.reached((long)this.delay.getValue()) && Keyboard.isKeyDown(29)) {
            if (Booster.mc.getTimer().timerSpeed == 1.0f) {
                this.tickCounter = 0;
            }
            if (this.tickCounter < this.boostTicks.getValue()) {
                Booster.mc.getTimer().timerSpeed = (float)this.timerSpeed.getValue();
            }
            else if (Booster.mc.getTimer().timerSpeed != 1.0f) {
                Booster.mc.getTimer().timerSpeed = 1.0f;
                this.timeHelper.reset();
            }
        }
    }
    
    @EventTarget
    public void onEventEarlyTick(final EventEarlyTick eventEarlyTick) {
        ++this.tickCounter;
    }
    
    public int getTickCounter() {
        return this.tickCounter;
    }
    
    public void setTickCounter(final int tickCounter) {
        this.tickCounter = tickCounter;
    }
}
