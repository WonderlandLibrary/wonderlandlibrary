// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.render;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.event.Event;

public class ShaderEvent extends Event
{
    private final boolean bloom;
    private final MultipleBoolSetting bloomOptions;
    
    public ShaderEvent(final boolean bloom, final MultipleBoolSetting bloomOptions) {
        this.bloom = bloom;
        this.bloomOptions = bloomOptions;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public boolean isBloom() {
        return this.bloom;
    }
    
    public MultipleBoolSetting getBloomOptions() {
        return this.bloomOptions;
    }
}
