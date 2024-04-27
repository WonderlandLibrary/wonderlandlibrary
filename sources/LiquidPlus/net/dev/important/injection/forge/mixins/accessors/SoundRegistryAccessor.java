/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.SoundEventAccessorComposite
 *  net.minecraft.client.audio.SoundRegistry
 *  net.minecraft.util.ResourceLocation
 */
package net.dev.important.injection.forge.mixins.accessors;

import java.util.Map;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SoundRegistry.class})
public interface SoundRegistryAccessor {
    @Accessor
    public Map<ResourceLocation, SoundEventAccessorComposite> getSoundRegistry();
}

