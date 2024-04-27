/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.SoundHandler
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.audio.ISoundHandler;
import net.ccbluex.liquidbounce.injection.backend.SoundHandlerImpl;
import net.minecraft.client.audio.SoundHandler;

public final class SoundHandlerImplKt {
    public static final ISoundHandler wrap(SoundHandler soundHandler) {
        boolean bl = false;
        return new SoundHandlerImpl(soundHandler);
    }

    public static final SoundHandler unwrap(ISoundHandler iSoundHandler) {
        boolean bl = false;
        return ((SoundHandlerImpl)iSoundHandler).getWrapped();
    }
}

