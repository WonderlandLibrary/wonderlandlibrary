/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;

public class CombatEntry {
    private final DamageSource damageSrc;
    private final int time;
    private final float damage;
    private final float health;
    private final String fallSuffix;
    private final float fallDistance;

    public CombatEntry(DamageSource damageSrcIn, int p_i1564_2_, float healthAmount, float damageAmount, String fallSuffixIn, float fallDistanceIn) {
        this.damageSrc = damageSrcIn;
        this.time = p_i1564_2_;
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
        return this.damageSrc.getEntity() instanceof EntityLivingBase;
    }

    @Nullable
    public String getFallSuffix() {
        return this.fallSuffix;
    }

    @Nullable
    public ITextComponent getDamageSrcDisplayName() {
        return this.getDamageSrc().getEntity() == null ? null : this.getDamageSrc().getEntity().getDisplayName();
    }

    public float getDamageAmount() {
        return this.damageSrc == DamageSource.outOfWorld ? Float.MAX_VALUE : this.fallDistance;
    }
}

