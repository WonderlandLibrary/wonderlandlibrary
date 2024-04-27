/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.particle.ParticleManager
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.IParticleManager;
import net.ccbluex.liquidbounce.injection.backend.ParticleManagerImpl;
import net.minecraft.client.particle.ParticleManager;

public final class ParticleManagerImplKt {
    public static final ParticleManager unwrap(IParticleManager iParticleManager) {
        boolean bl = false;
        return ((ParticleManagerImpl)iParticleManager).getWrapped();
    }

    public static final IParticleManager wrap(ParticleManager particleManager) {
        boolean bl = false;
        return new ParticleManagerImpl(particleManager);
    }
}

