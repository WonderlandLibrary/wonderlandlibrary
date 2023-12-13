// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.events.Event;
import vip.Resolute.modules.Module;

public class FastPlace extends Module
{
    public FastPlace() {
        super("FastPlace", 0, "Removes click delay", Category.PLAYER);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventUpdate && e.isPre()) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
    }
    
    @Override
    public void onDisable() {
        FastPlace.mc.rightClickDelayTimer = 6;
        super.onDisable();
    }
}
