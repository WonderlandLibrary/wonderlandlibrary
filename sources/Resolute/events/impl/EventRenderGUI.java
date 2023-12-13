// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.events.Event;

public class EventRenderGUI extends Event<EventRenderGUI>
{
    public ScaledResolution sr;
    
    public EventRenderGUI(final ScaledResolution sr) {
        this.sr = sr;
    }
}
