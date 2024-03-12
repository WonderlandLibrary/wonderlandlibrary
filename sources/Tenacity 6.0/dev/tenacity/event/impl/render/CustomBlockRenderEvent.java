// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.render;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import java.util.function.BiConsumer;
import dev.tenacity.event.Event;

public class CustomBlockRenderEvent extends Event
{
    private final BiConsumer<Float, Float> transformFirstPersonItem;
    private final Runnable doBlockTransformations;
    private final float swingProgress;
    private final float equipProgress;
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public float getSwingProgress() {
        return this.swingProgress;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public float getEquipProgress() {
        return this.equipProgress;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void transformFirstPersonItem(final float equipProgress, final float swingProgress) {
        this.transformFirstPersonItem.accept(equipProgress, swingProgress);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void doBlockTransformations() {
        this.doBlockTransformations.run();
    }
    
    public CustomBlockRenderEvent(final BiConsumer<Float, Float> transformFirstPersonItem, final Runnable doBlockTransformations, final float swingProgress, final float equipProgress) {
        this.transformFirstPersonItem = transformFirstPersonItem;
        this.doBlockTransformations = doBlockTransformations;
        this.swingProgress = swingProgress;
        this.equipProgress = equipProgress;
    }
}
