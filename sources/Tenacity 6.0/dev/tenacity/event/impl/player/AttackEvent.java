// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.event.Event;

public class AttackEvent extends Event
{
    private final EntityLivingBase targetEntity;
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public EntityLivingBase getTargetEntity() {
        return this.targetEntity;
    }
    
    public AttackEvent(final EntityLivingBase targetEntity) {
        this.targetEntity = targetEntity;
    }
}
