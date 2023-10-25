/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class PhaseBase
implements IPhase {
    protected final EntityDragon dragon;

    public PhaseBase(EntityDragon dragonIn) {
        this.dragon = dragonIn;
    }

    @Override
    public boolean getIsStationary() {
        return false;
    }

    @Override
    public void doClientRenderEffects() {
    }

    @Override
    public void doLocalUpdate() {
    }

    @Override
    public void onCrystalDestroyed(EntityEnderCrystal crystal, BlockPos pos, DamageSource dmgSrc, @Nullable EntityPlayer plyr) {
    }

    @Override
    public void initPhase() {
    }

    @Override
    public void removeAreaEffect() {
    }

    @Override
    public float getMaxRiseOrFall() {
        return 0.6f;
    }

    @Override
    @Nullable
    public Vec3d getTargetLocation() {
        return null;
    }

    @Override
    public float getAdjustedDamage(MultiPartEntityPart pt, DamageSource src, float damage) {
        return damage;
    }

    @Override
    public float getYawFactor() {
        float f = MathHelper.sqrt(this.dragon.motionX * this.dragon.motionX + this.dragon.motionZ * this.dragon.motionZ) + 1.0f;
        float f1 = Math.min(f, 40.0f);
        return 0.7f / f1 / f;
    }
}

