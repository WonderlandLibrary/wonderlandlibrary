/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.particle.ParticleManager
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.IParticleManager;
import net.ccbluex.liquidbounce.injection.backend.ParticleManagerImpl;
import net.minecraft.client.particle.ParticleManager;

public final class ParticleManagerImplKt {
    public static final ParticleManager unwrap(IParticleManager $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((ParticleManagerImpl)$this$unwrap).getWrapped();
    }

    public static final IParticleManager wrap(ParticleManager $this$wrap) {
        int $i$f$wrap = 0;
        return new ParticleManagerImpl($this$wrap);
    }
}

