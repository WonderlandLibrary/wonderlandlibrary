// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import net.minecraft.entity.Entity;
import vip.Resolute.events.Event;

public class EventAttack extends Event<EventAttack>
{
    public Entity target;
    
    public EventAttack(final Entity t) {
        this.target = t;
    }
    
    public Entity getTarget() {
        return this.target;
    }
}
