// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.render;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.event.Event;

public class Render2DEvent extends Event
{
    public ScaledResolution sr;
    private float width;
    private float height;
    
    public Render2DEvent(final float width, final float height) {
        this.width = width;
        this.height = height;
    }
    
    public Render2DEvent(final ScaledResolution sr) {
        this.sr = sr;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public float getHeight() {
        return this.height;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public float getWidth() {
        return this.width;
    }
    
    public void setWidth(final float width) {
        this.width = width;
    }
}
