/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.particle.ParticleManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumParticleTypes
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.IParticleManager;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;

public final class ParticleManagerImpl
implements IParticleManager {
    private final ParticleManager wrapped;

    @Override
    public void spawnEffectParticle(int n, double d, double d2, double d3, double d4, double d5, double d6, int n2) {
        this.wrapped.func_178927_a(n, d, d2, d3, d4, d5, d6, new int[]{n2});
    }

    @Override
    public void emitParticleAtEntity(IEntity iEntity, EnumParticleTypes enumParticleTypes) {
        IEntity iEntity2 = iEntity;
        ParticleManager particleManager = this.wrapped;
        boolean bl = false;
        Entity entity = ((EntityImpl)iEntity2).getWrapped();
        particleManager.func_178926_a(entity, enumParticleTypes);
    }

    public final ParticleManager getWrapped() {
        return this.wrapped;
    }

    public ParticleManagerImpl(ParticleManager particleManager) {
        this.wrapped = particleManager;
    }
}

