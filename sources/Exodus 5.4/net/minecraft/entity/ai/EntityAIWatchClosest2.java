/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public class EntityAIWatchClosest2
extends EntityAIWatchClosest {
    public EntityAIWatchClosest2(EntityLiving entityLiving, Class<? extends Entity> clazz, float f, float f2) {
        super(entityLiving, clazz, f, f2);
        this.setMutexBits(3);
    }
}

