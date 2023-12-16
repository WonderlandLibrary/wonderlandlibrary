// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import moonsense.event.impl.SCDamageEntityEvent;
import moonsense.event.impl.SCAttackEntityEvent;
import moonsense.event.SubscribeEvent;
import moonsense.event.impl.SCUpdateEvent;
import moonsense.features.SCDefaultRenderModule;

public class ComboCounterModule extends SCDefaultRenderModule
{
    private long hitTime;
    private int combo;
    private int possibleTarget;
    
    public ComboCounterModule() {
        super("Combo Counter", "Display the number of subsequent hits.");
        this.hitTime = -1L;
    }
    
    @Override
    public Object getValue() {
        if (this.combo == 0) {
            return null;
        }
        return String.valueOf(this.combo) + " hit" + ((this.combo > 1) ? "s" : "");
    }
    
    @Override
    public Object getDummyValue() {
        return "6 hits";
    }
    
    @SubscribeEvent
    public void onUpdate(final SCUpdateEvent event) {
        if (System.currentTimeMillis() - this.hitTime > 2000L) {
            this.combo = 0;
        }
    }
    
    @SubscribeEvent
    public void onEntityAttack(final SCAttackEntityEvent event) {
        this.possibleTarget = event.victim.getEntityId();
    }
    
    @SubscribeEvent
    public void onEntityDamage(final SCDamageEntityEvent event) {
        if (event.entity.getEntityId() == this.possibleTarget) {
            this.dealHit();
        }
        else if (event.entity == this.mc.thePlayer) {
            this.takeHit();
        }
    }
    
    public void dealHit() {
        ++this.combo;
        this.possibleTarget = -1;
        this.hitTime = System.currentTimeMillis();
    }
    
    public void takeHit() {
        this.combo = 0;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.BOTTOM_CENTER;
    }
}
