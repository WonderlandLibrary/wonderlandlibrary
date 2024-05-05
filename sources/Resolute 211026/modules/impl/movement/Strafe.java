// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.modules.Module;

public class Strafe extends Module
{
    public Strafe() {
        super("Strafe", 0, "Allows in air strafing", Category.MOVEMENT);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion && e.isPre() && MovementUtils.isMoving()) {
            MovementUtils.strafe();
        }
    }
}
