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
    public static final SoundHandler unwrap(ISoundHandler $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((SoundHandlerImpl)$this$unwrap).getWrapped();
    }

    public static final ISoundHandler wrap(SoundHandler $this$wrap) {
        int $i$f$wrap = 0;
        return new SoundHandlerImpl($this$wrap);
    }
}

