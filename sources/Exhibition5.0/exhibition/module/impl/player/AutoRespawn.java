// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import exhibition.event.impl.EventTick;
import exhibition.event.RegisterEvent;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class AutoRespawn extends Module
{
    public AutoRespawn(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventTick.class })
    @Override
    public void onEvent(final Event event) {
        if (AutoRespawn.mc.thePlayer.isDead) {
            AutoRespawn.mc.thePlayer.respawnPlayer();
        }
    }
}
