// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.event.impl.SCAttackEntityEvent;
import moonsense.enums.ModuleCategory;
import moonsense.event.SubscribeEvent;
import moonsense.event.impl.SCUpdateEvent;
import moonsense.features.SCDefaultRenderModule;

public class ReachDisplayModule extends SCDefaultRenderModule
{
    private int distToEntity;
    private int ticksLasted;
    
    public ReachDisplayModule() {
        super("Reach Display", "Show how far you attacked an entity from on the HUD.");
        this.distToEntity = 0;
        this.ticksLasted = 0;
    }
    
    @Override
    public Object getValue() {
        return String.valueOf(this.distToEntity) + " blocks";
    }
    
    @SubscribeEvent
    public void onUpdate(final SCUpdateEvent event) {
        ++this.ticksLasted;
        if (this.ticksLasted > 40) {
            this.ticksLasted = 0;
            this.distToEntity = 0;
        }
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @SubscribeEvent
    public void onEntityAttack(final SCAttackEntityEvent event) {
        this.distToEntity = (int)event.attacker.getDistanceToEntity(event.victim);
        this.ticksLasted = 0;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.BOTTOM_RIGHT;
    }
}
