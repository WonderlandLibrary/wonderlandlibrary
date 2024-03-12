// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.render;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.event.Event;

public class RenderModelEvent extends StateEvent
{
    private final EntityLivingBase entity;
    private final Runnable modelRenderer;
    private final Runnable layerRenderer;
    
    public RenderModelEvent(final EntityLivingBase entity, final Runnable modelRenderer, final Runnable layerRenderer) {
        this.entity = entity;
        this.modelRenderer = modelRenderer;
        this.layerRenderer = layerRenderer;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public EntityLivingBase getEntity() {
        return this.entity;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void drawModel() {
        this.modelRenderer.run();
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void drawLayers() {
        this.layerRenderer.run();
    }
}
