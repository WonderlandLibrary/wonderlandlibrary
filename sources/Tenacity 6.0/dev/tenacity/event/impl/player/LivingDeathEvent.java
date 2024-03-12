// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import net.minecraft.util.DamageSource;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.event.Event;

public class LivingDeathEvent extends Event
{
    private final EntityLivingBase entity;
    private final DamageSource source;
    
    public EntityLivingBase getEntity() {
        return this.entity;
    }
    
    public DamageSource getSource() {
        return this.source;
    }
    
    public LivingDeathEvent(final EntityLivingBase entity, final DamageSource source) {
        this.entity = entity;
        this.source = source;
    }
}
