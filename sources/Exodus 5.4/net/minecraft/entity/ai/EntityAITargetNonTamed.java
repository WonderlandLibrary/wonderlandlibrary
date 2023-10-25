/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetNonTamed<T extends EntityLivingBase>
extends EntityAINearestAttackableTarget {
    private EntityTameable theTameable;

    public EntityAITargetNonTamed(EntityTameable entityTameable, Class<T> clazz, boolean bl, Predicate<? super T> predicate) {
        super(entityTameable, clazz, 10, bl, false, predicate);
        this.theTameable = entityTameable;
    }

    @Override
    public boolean shouldExecute() {
        return !this.theTameable.isTamed() && super.shouldExecute();
    }
}

