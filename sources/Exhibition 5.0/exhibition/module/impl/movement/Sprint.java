// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.movement;

import exhibition.util.PlayerUtil;
import exhibition.event.impl.EventRenderGui;
import exhibition.event.RegisterEvent;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Sprint extends Module
{
    public Sprint(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventRenderGui.class })
    @Override
    public void onEvent(final Event event) {
        if (this.canSprint()) {
            Sprint.mc.thePlayer.setSprinting(true);
        }
    }
    
    private boolean canSprint() {
        return PlayerUtil.isMoving() && Sprint.mc.thePlayer.getFoodStats().getFoodLevel() >= 6;
    }
}
