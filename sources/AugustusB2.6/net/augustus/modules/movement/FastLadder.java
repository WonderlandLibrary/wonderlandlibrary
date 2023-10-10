// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.augustus.events.EventUpdate;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventLadder;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class FastLadder extends Module
{
    public DoubleValue motionY;
    public BooleanValue stopOnEnd;
    private boolean wasOnLadder;
    
    public FastLadder() {
        super("FastLadder", new Color(166, 168, 50), Categorys.MOVEMENT);
        this.motionY = new DoubleValue(1, "MotionY", this, 0.4, 0.2, 2.0, 2);
        this.stopOnEnd = new BooleanValue(2, "StopOnEnd", this, true);
    }
    
    @EventTarget
    public void onEventLadder(final EventLadder eventLadder) {
        eventLadder.setMotionYSpeed(this.motionY.getValue());
    }
    
    @EventTarget
    public void onEventUpdate(final EventUpdate eventUpdate) {
        if (this.stopOnEnd.getBoolean() && !FastLadder.mc.thePlayer.isOnLadder() && this.wasOnLadder) {
            FastLadder.mc.thePlayer.motionY = 0.20000000298023224;
        }
        this.wasOnLadder = FastLadder.mc.thePlayer.isOnLadder();
    }
}
