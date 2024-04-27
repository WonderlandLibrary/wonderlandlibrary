/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.audio.SoundHandler
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundEvent
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.audio.ISoundHandler;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.Nullable;

public final class SoundHandlerImpl
implements ISoundHandler {
    private final SoundHandler wrapped;

    @Override
    public void playSound(String string, float f) {
        this.wrapped.func_147682_a((ISound)PositionedSoundRecord.func_194007_a((SoundEvent)new SoundEvent(new ResourceLocation(string)), (float)f, (float)1.0f));
    }

    public SoundHandlerImpl(SoundHandler soundHandler) {
        this.wrapped = soundHandler;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof SoundHandlerImpl && ((SoundHandlerImpl)object).wrapped.equals(this.wrapped);
    }

    public final SoundHandler getWrapped() {
        return this.wrapped;
    }
}

