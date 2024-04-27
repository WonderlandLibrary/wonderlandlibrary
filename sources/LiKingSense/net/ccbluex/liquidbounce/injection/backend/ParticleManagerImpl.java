/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.particle.ParticleManager
 *  net.minecraft.util.EnumParticleTypes
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.IParticleManager;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.EnumParticleTypes;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016JH\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u000fH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0018"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/ParticleManagerImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/IParticleManager;", "wrapped", "Lnet/minecraft/client/particle/ParticleManager;", "(Lnet/minecraft/client/particle/ParticleManager;)V", "getWrapped", "()Lnet/minecraft/client/particle/ParticleManager;", "emitParticleAtEntity", "", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "buffer", "Lnet/minecraft/util/EnumParticleTypes;", "spawnEffectParticle", "particleID", "", "posX", "", "posY", "posZ", "motionX", "motionY", "motionZ", "StateId", "LiKingSense"})
public final class ParticleManagerImpl
implements IParticleManager {
    @NotNull
    private final ParticleManager wrapped;

    /*
     * WARNING - void declaration
     */
    @Override
    public void emitParticleAtEntity(@NotNull IEntity entity, @NotNull EnumParticleTypes buffer) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
        Intrinsics.checkParameterIsNotNull((Object)buffer, (String)"buffer");
        IEntity iEntity = entity;
        ParticleManager particleManager = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t2 = ((EntityImpl)$this$unwrap$iv).getWrapped();
        particleManager.func_178926_a(t2, buffer);
    }

    @Override
    public void spawnEffectParticle(int particleID, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, int StateId) {
        this.wrapped.func_178927_a(particleID, posX, posY, posZ, motionX, motionY, motionZ, new int[]{StateId});
    }

    @NotNull
    public final ParticleManager getWrapped() {
        return this.wrapped;
    }

    public ParticleManagerImpl(@NotNull ParticleManager wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

