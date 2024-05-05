// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.event.events;

import net.minecraft.client.gui.ScaledResolution;
import Lavish.event.Event;

public class EventRenderGUI extends Event<EventRenderGUI>
{
    public ScaledResolution sr;
    
    public EventRenderGUI(final ScaledResolution sr) {
        this.sr = sr;
    }
}
