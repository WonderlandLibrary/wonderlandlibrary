// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import exhibition.event.impl.EventMotion;
import exhibition.event.RegisterEvent;
import exhibition.event.impl.EventRenderGui;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class ESP extends Module
{
    public ESP(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventRenderGui.class, EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventRenderGui) {
            final EventRenderGui eventRenderGui = (EventRenderGui)event;
        }
    }
}
