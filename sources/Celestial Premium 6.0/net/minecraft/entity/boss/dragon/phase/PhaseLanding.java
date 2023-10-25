/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseBase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseLanding
extends PhaseBase {
    private Vec3d targetLocation;

    public PhaseLanding(EntityDragon dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doClientRenderEffects() {
        Vec3d vec3d = this.dragon.getHeadLookVec(1.0f).normalize();
        vec3d.rotateYaw(-0.7853982f);
        double d0 = this.dragon.dragonPartHead.posX;
        double d1 = this.dragon.dragonPartHead.posY + (double)(this.dragon.dragonPartHead.height / 2.0f);
        double d2 = this.dragon.dragonPartHead.posZ;
        for (int i = 0; i < 8; ++i) {
            double d3 = d0 + this.dragon.getRNG().nextGaussian() / 2.0;
            double d4 = d1 + this.dragon.getRNG().nextGaussian() / 2.0;
            double d5 = d2 + this.dragon.getRNG().nextGaussian() / 2.0;
            this.dragon.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, d3, d4, d5, -vec3d.x * (double)0.08f + this.dragon.motionX, -vec3d.y * (double)0.3f + this.dragon.motionY, -vec3d.z * (double)0.08f + this.dragon.motionZ, new int[0]);
            vec3d.rotateYaw(0.19634955f);
        }
    }

    @Override
    public void doLocalUpdate() {
        if (this.targetLocation == null) {
            this.targetLocation = new Vec3d(this.dragon.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION));
        }
        if (this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ) < 1.0) {
            this.dragon.getPhaseManager().getPhase(PhaseList.SITTING_FLAMING).resetFlameCount();
            this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
        }
    }

    @Override
    public float getMaxRiseOrFall() {
        return 1.5f;
    }

    @Override
    public float getYawFactor() {
        float f = MathHelper.sqrt(this.dragon.motionX * this.dragon.motionX + this.dragon.motionZ * this.dragon.motionZ) + 1.0f;
        float f1 = Math.min(f, 40.0f);
        return f1 / f;
    }

    @Override
    public void initPhase() {
        this.targetLocation = null;
    }

    @Override
    @Nullable
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }

    public PhaseList<PhaseLanding> getPhaseList() {
        return PhaseList.LANDING;
    }
}

