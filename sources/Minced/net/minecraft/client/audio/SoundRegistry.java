// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistrySimple;

public class SoundRegistry extends RegistrySimple<ResourceLocation, SoundEventAccessor>
{
    private Map<ResourceLocation, SoundEventAccessor> soundRegistry;
    
    @Override
    protected Map<ResourceLocation, SoundEventAccessor> createUnderlyingMap() {
        return this.soundRegistry = (Map<ResourceLocation, SoundEventAccessor>)Maps.newHashMap();
    }
    
    public void add(final SoundEventAccessor accessor) {
        this.putObject(accessor.getLocation(), accessor);
    }
    
    public void clearMap() {
        this.soundRegistry.clear();
    }
}
