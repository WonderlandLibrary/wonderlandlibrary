/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.SoundHandler
 *  net.minecraft.client.audio.SoundRegistry
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SoundHandler.class})
public interface SoundHandlerAccessor {
    @Accessor
    public SoundRegistry getSndRegistry();
}

