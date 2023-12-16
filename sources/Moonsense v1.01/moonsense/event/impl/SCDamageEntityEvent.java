// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import net.minecraft.entity.Entity;
import moonsense.event.SCEvent;

public class SCDamageEntityEvent extends SCEvent
{
    public Entity entity;
    
    public SCDamageEntityEvent(final Entity entity) {
        this.entity = entity;
    }
}
