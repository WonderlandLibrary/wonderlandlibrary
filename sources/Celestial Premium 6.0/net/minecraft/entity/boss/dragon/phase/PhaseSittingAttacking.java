/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.boss.dragon.phase.PhaseSittingBase;
import net.minecraft.init.SoundEvents;

public class PhaseSittingAttacking
extends PhaseSittingBase {
    private int attackingTicks;

    public PhaseSittingAttacking(EntityDragon dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doClientRenderEffects() {
        this.dragon.world.playSound(this.dragon.posX, this.dragon.posY, this.dragon.posZ, SoundEvents.ENTITY_ENDERDRAGON_GROWL, this.dragon.getSoundCategory(), 2.5f, 0.8f + this.dragon.getRNG().nextFloat() * 0.3f, false);
    }

    @Override
    public void doLocalUpdate() {
        if (this.attackingTicks++ >= 40) {
            this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_FLAMING);
        }
    }

    @Override
    public void initPhase() {
        this.attackingTicks = 0;
    }

    public PhaseList<PhaseSittingAttacking> getPhaseList() {
        return PhaseList.SITTING_ATTACKING;
    }
}

