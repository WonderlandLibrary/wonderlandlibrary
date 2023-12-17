// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.init.SoundEvents;
import net.minecraft.entity.boss.EntityDragon;

public class PhaseSittingAttacking extends PhaseSittingBase
{
    private int attackingTicks;
    
    public PhaseSittingAttacking(final EntityDragon dragonIn) {
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
    
    @Override
    public PhaseList<PhaseSittingAttacking> getType() {
        return PhaseList.SITTING_ATTACKING;
    }
}
