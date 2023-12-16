// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import net.minecraft.entity.Entity;
import moonsense.event.SCEvent;

public class SCAttackEntityEvent extends SCEvent
{
    public final Entity attacker;
    public final Entity victim;
    
    public SCAttackEntityEvent(final Entity attacker, final Entity victim) {
        this.attacker = attacker;
        this.victim = victim;
    }
}
