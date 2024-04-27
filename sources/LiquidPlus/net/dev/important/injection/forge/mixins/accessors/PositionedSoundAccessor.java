/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.PositionedSound
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.client.audio.PositionedSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={PositionedSound.class})
public interface PositionedSoundAccessor {
    @Accessor
    public void setVolume(float var1);
}

