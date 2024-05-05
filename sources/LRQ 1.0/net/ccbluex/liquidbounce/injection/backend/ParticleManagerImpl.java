/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.particle.ParticleManager
 *  net.minecraft.util.EnumParticleTypes
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.IParticleManager;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.EnumParticleTypes;

public final class ParticleManagerImpl
implements IParticleManager {
    private final ParticleManager wrapped;

    /*
     * WARNING - void declaration
     */
    @Override
    public void emitParticleAtEntity(IEntity entity, EnumParticleTypes buffer) {
        void $this$unwrap$iv;
        IEntity iEntity = entity;
        ParticleManager particleManager = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t = ((EntityImpl)$this$unwrap$iv).getWrapped();
        particleManager.func_178926_a(t, buffer);
    }

    @Override
    public void spawnEffectParticle(int particleID, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, int StateId) {
        this.wrapped.func_178927_a(particleID, posX, posY, posZ, motionX, motionY, motionZ, new int[]{StateId});
    }

    public final ParticleManager getWrapped() {
        return this.wrapped;
    }

    public ParticleManagerImpl(ParticleManager wrapped) {
        this.wrapped = wrapped;
    }
}

