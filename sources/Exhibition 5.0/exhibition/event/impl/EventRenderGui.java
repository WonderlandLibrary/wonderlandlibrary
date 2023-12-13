// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import net.minecraft.client.gui.ScaledResolution;
import exhibition.event.Event;

public class EventRenderGui extends Event
{
    private ScaledResolution resolution;
    
    public void fire(final ScaledResolution resolution) {
        this.resolution = resolution;
        super.fire();
    }
    
    public ScaledResolution getResolution() {
        return this.resolution;
    }
}
