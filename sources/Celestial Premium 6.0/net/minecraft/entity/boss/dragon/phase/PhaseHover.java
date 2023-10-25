/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseBase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.util.math.Vec3d;

public class PhaseHover
extends PhaseBase {
    private Vec3d targetLocation;

    public PhaseHover(EntityDragon dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doLocalUpdate() {
        if (this.targetLocation == null) {
            this.targetLocation = new Vec3d(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
        }
    }

    @Override
    public boolean getIsStationary() {
        return true;
    }

    @Override
    public void initPhase() {
        this.targetLocation = null;
    }

    @Override
    public float getMaxRiseOrFall() {
        return 1.0f;
    }

    @Override
    @Nullable
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }

    public PhaseList<PhaseHover> getPhaseList() {
        return PhaseList.HOVER;
    }
}

