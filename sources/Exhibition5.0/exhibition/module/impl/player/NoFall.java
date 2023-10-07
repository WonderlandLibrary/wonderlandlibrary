// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import exhibition.event.RegisterEvent;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class NoFall extends Module
{
    public NoFall(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre() && NoFall.mc.thePlayer.fallDistance >= 2.0f) {
                em.setGround(true);
            }
        }
    }
}
