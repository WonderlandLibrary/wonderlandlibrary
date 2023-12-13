// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.movement;

import exhibition.event.impl.EventMotion;
import exhibition.event.impl.EventMove;
import exhibition.event.RegisterEvent;
import exhibition.event.impl.EventBlockBounds;
import exhibition.event.impl.EventPushBlock;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class TurnIntoPlugin extends Module
{
    public TurnIntoPlugin(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventBlockBounds.class, EventMove.class, EventPushBlock.class, EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventPushBlock) {
            event.setCancelled(true);
        }
        if (event instanceof EventBlockBounds) {
            final EventBlockBounds ebb = (EventBlockBounds)event;
            if (ebb.getBounds() != null && !TurnIntoPlugin.mc.thePlayer.isOnLadder() && TurnIntoPlugin.mc.thePlayer.isSneaking()) {
                ebb.setCancelled(true);
            }
        }
    }
}
