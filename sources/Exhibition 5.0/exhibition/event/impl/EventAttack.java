// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import net.minecraft.entity.Entity;
import exhibition.event.Event;

public class EventAttack extends Event
{
    private Entity entity;
    private boolean preAttack;
    
    public void fire(final Entity targetEntity, final boolean preAttack) {
        this.entity = targetEntity;
        this.preAttack = preAttack;
        super.fire();
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public boolean isPreAttack() {
        return this.preAttack;
    }
    
    public boolean isPostAttack() {
        return !this.preAttack;
    }
}
