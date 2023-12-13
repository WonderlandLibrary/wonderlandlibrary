/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.callables.EventCancellable;
import net.minecraft.entity.EntityLivingBase;

public class EventNameTag
extends EventCancellable {
    private final EntityLivingBase entity;
    private String renderedName;

    public EventNameTag(EntityLivingBase entity, String renderedName) {
        this.entity = entity;
        this.renderedName = renderedName;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }

    public String getRenderedName() {
        return this.renderedName;
    }

    public void setRenderedName(String renderedName) {
        this.renderedName = renderedName;
    }
}

