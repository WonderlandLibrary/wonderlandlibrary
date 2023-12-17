// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.util.text.ITextComponent;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;

public class CombatEntry
{
    private final DamageSource damageSrc;
    private final int time;
    private final float damage;
    private final float health;
    private final String fallSuffix;
    private final float fallDistance;
    
    public CombatEntry(final DamageSource damageSrcIn, final int timeIn, final float healthAmount, final float damageAmount, final String fallSuffixIn, final float fallDistanceIn) {
        this.damageSrc = damageSrcIn;
        this.time = timeIn;
        this.damage = damageAmount;
        this.health = healthAmount;
        this.fallSuffix = fallSuffixIn;
        this.fallDistance = fallDistanceIn;
    }
    
    public DamageSource getDamageSrc() {
        return this.damageSrc;
    }
    
    public float getDamage() {
        return this.damage;
    }
    
    public boolean isLivingDamageSrc() {
        return this.damageSrc.getTrueSource() instanceof EntityLivingBase;
    }
    
    @Nullable
    public String getFallSuffix() {
        return this.fallSuffix;
    }
    
    @Nullable
    public ITextComponent getDamageSrcDisplayName() {
        return (this.getDamageSrc().getTrueSource() == null) ? null : this.getDamageSrc().getTrueSource().getDisplayName();
    }
    
    public float getDamageAmount() {
        return (this.damageSrc == DamageSource.OUT_OF_WORLD) ? Float.MAX_VALUE : this.fallDistance;
    }
}
