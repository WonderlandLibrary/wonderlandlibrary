/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseBase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseDying
extends PhaseBase {
    private Vec3d targetLocation;
    private int time;

    public PhaseDying(EntityDragon dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doClientRenderEffects() {
        if (this.time++ % 10 == 0) {
            float f = (this.dragon.getRNG().nextFloat() - 0.5f) * 8.0f;
            float f1 = (this.dragon.getRNG().nextFloat() - 0.5f) * 4.0f;
            float f2 = (this.dragon.getRNG().nextFloat() - 0.5f) * 8.0f;
            this.dragon.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.dragon.posX + (double)f, this.dragon.posY + 2.0 + (double)f1, this.dragon.posZ + (double)f2, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public void doLocalUpdate() {
        double d0;
        ++this.time;
        if (this.targetLocation == null) {
            BlockPos blockpos = this.dragon.world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION);
            this.targetLocation = new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }
        if ((d0 = this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ)) >= 100.0 && d0 <= 22500.0 && !this.dragon.isCollidedHorizontally && !this.dragon.isCollidedVertically) {
            this.dragon.setHealth(1.0f);
        } else {
            this.dragon.setHealth(0.0f);
        }
    }

    @Override
    public void initPhase() {
        this.targetLocation = null;
        this.time = 0;
    }

    @Override
    public float getMaxRiseOrFall() {
        return 3.0f;
    }

    @Override
    @Nullable
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }

    public PhaseList<PhaseDying> getPhaseList() {
        return PhaseList.DYING;
    }
}

