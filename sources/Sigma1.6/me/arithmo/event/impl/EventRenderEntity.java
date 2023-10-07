/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.event.impl;

import me.arithmo.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderEntity
extends Event {
    private EntityLivingBase entity;
    private boolean pre;

    public void fire(EntityLivingBase entity, boolean pre) {
        this.entity = entity;
        this.pre = pre;
        super.fire();
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }

    public boolean isPre() {
        return this.pre;
    }

    public boolean isPost() {
        return !this.pre;
    }
}

