// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.boss.EntityDragon;
import org.apache.logging.log4j.Logger;

public class PhaseManager
{
    private static final Logger LOGGER;
    private final EntityDragon dragon;
    private final IPhase[] phases;
    private IPhase phase;
    
    public PhaseManager(final EntityDragon dragonIn) {
        this.phases = new IPhase[PhaseList.getTotalPhases()];
        this.dragon = dragonIn;
        this.setPhase(PhaseList.HOVER);
    }
    
    public void setPhase(final PhaseList<?> phaseIn) {
        if (this.phase == null || phaseIn != this.phase.getType()) {
            if (this.phase != null) {
                this.phase.removeAreaEffect();
            }
            this.phase = this.getPhase(phaseIn);
            if (!this.dragon.world.isRemote) {
                this.dragon.getDataManager().set(EntityDragon.PHASE, phaseIn.getId());
            }
            PhaseManager.LOGGER.debug("Dragon is now in phase {} on the {}", (Object)phaseIn, (Object)(this.dragon.world.isRemote ? "client" : "server"));
            this.phase.initPhase();
        }
    }
    
    public IPhase getCurrentPhase() {
        return this.phase;
    }
    
    public <T extends IPhase> T getPhase(final PhaseList<T> phaseIn) {
        final int i = phaseIn.getId();
        if (this.phases[i] == null) {
            this.phases[i] = phaseIn.createPhase(this.dragon);
        }
        return (T)this.phases[i];
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
