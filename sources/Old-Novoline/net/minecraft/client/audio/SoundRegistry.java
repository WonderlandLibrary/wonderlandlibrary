package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import net.minecraft.util.RegistrySimple;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class SoundRegistry extends RegistrySimple<ResourceLocation, SoundEventAccessorComposite> {

    private Map<ResourceLocation, SoundEventAccessorComposite> soundRegistry;

    protected Map<ResourceLocation, SoundEventAccessorComposite> createUnderlyingMap() {
        this.soundRegistry = Maps.<ResourceLocation, SoundEventAccessorComposite>newHashMap();
        return this.soundRegistry;
    }

    public void registerSound(SoundEventAccessorComposite accessorComposite) {
        this.putObject(accessorComposite.getSoundEventLocation(), accessorComposite);
    }

    /**
     * Reset the underlying sound map (Called on resource manager reload)
     */
    public void clearMap() {
        this.soundRegistry.clear();
    }

}
