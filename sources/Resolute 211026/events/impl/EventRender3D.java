// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import shadersmod.client.Shaders;
import vip.Resolute.events.Event;

public class EventRender3D extends Event<EventRender3D>
{
    private float ticks;
    private boolean isUsingShaders;
    
    public EventRender3D() {
        this.isUsingShaders = (Shaders.getShaderPackName() != null);
    }
    
    public EventRender3D(final float ticks) {
        this.ticks = ticks;
        this.isUsingShaders = (Shaders.getShaderPackName() != null);
    }
    
    public float getPartialTicks() {
        return this.ticks;
    }
    
    public boolean isUsingShaders() {
        return this.isUsingShaders;
    }
}
