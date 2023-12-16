// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import net.minecraft.client.gui.ScaledResolution;
import moonsense.event.SCEvent;

public class SCRenderEvent extends SCEvent
{
    public final ScaledResolution resolution;
    
    public SCRenderEvent(final ScaledResolution sr) {
        this.resolution = sr;
    }
    
    public int getWidth() {
        return this.resolution.getScaledWidth();
    }
    
    public int getHeight() {
        return this.resolution.getScaledHeight();
    }
}
